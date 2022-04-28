package community.icon.cps.score.cpftreasury;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import score.*;
import score.annotation.EventLog;
import score.annotation.External;
import score.annotation.Optional;
import score.annotation.Payable;
import scorex.util.ArrayList;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import static community.icon.cps.score.cpftreasury.Constants.*;
import static community.icon.cps.score.cpftreasury.Validations.validateAdmins;
import static community.icon.cps.score.cpftreasury.Validations.validateCpsScore;

public class CPFTreasury extends SetterGetter {

    private final ArrayDB<String> proposalsKeys = Context.newArrayDB(PROPOSALS_KEYS, String.class);
    private final DictDB<String, BigInteger> proposalBudgets = Context.newDictDB(PROPOSAL_BUDGETS, BigInteger.class);
    private final VarDB<BigInteger> treasuryFund = Context.newVarDB(TREASURY_FUND, BigInteger.class);
    private final VarDB<BigInteger> treasuryFundbnUSD = Context.newVarDB(TREASURY_FUND_BNUSD, BigInteger.class);

    public static final VarDB<Address> cpsTreasuryScore = Context.newVarDB(CPS_TREASURY_SCORE, Address.class);
    public static final VarDB<Address> cpsScore = Context.newVarDB(CPS_SCORE, Address.class);
    public static final VarDB<Address> balancedDollar = Context.newVarDB(BALANCED_DOLLAR, Address.class);
    public static final VarDB<Address> dexScore = Context.newVarDB(DEX_SCORE, Address.class);
    public static final VarDB<Address> sICXScore = Context.newVarDB(SICX_SCORE, Address.class);
    public static final VarDB<Address> routerScore = Context.newVarDB(ROUTER_SCORE, Address.class);

    private final VarDB<Integer> swapState = Context.newVarDB(SWAP_STATE, Integer.class);
    private final VarDB<Integer> swapCount = Context.newVarDB(SWAP_COUNT, Integer.class);

    public CPFTreasury() {

    }

    private boolean proposalExists(String ipfsKey) {
        return proposalBudgets.get(ipfsKey) != null;
    }

    @External(readonly = true)
    public String name() {
        return TAG;
    }


    @External
    public void setMaximumTreasuryFundIcx(BigInteger _value) {
        validateAdmins();
        treasuryFund.set(_value);
    }

    /**
     * Set the maximum Treasury fund. Default 1M in bnUSD
     *
     * @param _value: value in loop
     */
    @External
    public void setMaximumTreasuryFundBnusd(BigInteger _value) {
        validateAdmins();
        treasuryFundbnUSD.set(_value);
    }


    private void burn(BigInteger amount) {
        Context.call(amount, SYSTEM_ADDRESS, "burn");
    }

    /**
     * Get total amount of fund on the SCORE
     *
     * @return map of ICX and bnUSD amount
     */
    @External(readonly = true)
    public Map<String, BigInteger> get_total_funds() {
        return Map.of(ICX, Context.getBalance(Context.getAddress()),
                bnUSD, getTotalFundBNUSD());
    }

    private BigInteger getTotalFundBNUSD() {
        return (BigInteger) Context.call(balancedDollar.get(), "balanceOf", Context.getAddress());
    }

    @External(readonly = true)
    public Map<String, BigInteger> get_remaining_swap_amount() {
        BigInteger maxCap = treasuryFundbnUSD.get();
        return Map.of("maxCap", maxCap,
                "remainingToSwap", maxCap.subtract(getTotalFundBNUSD()));
    }

    private void returnFundAmount(Address address, BigInteger value) {
        Context.require(value.compareTo(BigInteger.ZERO) > 0, TAG + ": Sponsor Bond Amount should be greater than 0");
        burnExtraFund();
        FundReturned(address, "Sponsor Bond amount " + value + " " + bnUSD + " Returned to CPF Treasury.");
    }

    @External
    public void transfer_proposal_fund_to_cps_treasury(String _ipfs_key, int _total_installment_count,
                                                       Address _sponsor_address, Address _contributor_address,
                                                       String token_flag, BigInteger _total_budget) {
        validateCpsScore();
        Context.require(!proposalExists(_ipfs_key), TAG + ": Project already exists. Invalid IPFS Hash");
        Context.require(token_flag.equals(bnUSD), TAG + ": " + token_flag + " is not supported. Only " + bnUSD + " token available.");
        BigInteger sponsorReward = _total_budget.multiply(BigInteger.TWO).divide(BigInteger.valueOf(100));
        BigInteger totalTransfer = _total_budget.add(sponsorReward);

        Address balancedDollar = CPFTreasury.balancedDollar.get();
        BigInteger bnUSDBalance = Context.call(BigInteger.class, balancedDollar, "balanceOf", Context.getAddress());
        Context.require(totalTransfer.compareTo(bnUSDBalance) < 0, TAG + ": Not enough fund " + bnUSDBalance + " token available");

        proposalsKeys.add(_ipfs_key);
        proposalBudgets.set(_ipfs_key, totalTransfer);

        JsonObject depositProposal = new JsonObject();
        depositProposal.add("method", "deposit_proposal_fund");
        JsonObject params = new JsonObject();
        params.add("ipfs_hash", _ipfs_key);
        params.add("project_duration", _total_installment_count);
        params.add("sponsor_address", _sponsor_address.toString());
        params.add("contributor_address", _contributor_address.toString());
        params.add("total_budget", _total_budget.toString(16));
        params.add("sponsor_reward", sponsorReward.toString(16));
        params.add("token", token_flag);
        depositProposal.add("params", params);

        Context.call(balancedDollar, "transfer", cpsTreasuryScore.get(), totalTransfer, depositProposal.toString().getBytes());
        ProposalFundTransferred(_ipfs_key, "Successfully transferred " + totalTransfer + " " + token_flag + " to CPS Treasury " + cpsTreasuryScore.get());
    }

    @External
    public void update_proposal_fund(String _ipfs_key, @Optional String _flag, @Optional BigInteger _added_budget,
                                     @Optional int _total_installment_count) {
        validateCpsScore();
        Context.require(proposalExists(_ipfs_key), TAG + ": IPFS hash does not exist.");
        Context.require(_flag != null && _flag.equals(bnUSD), TAG + ": Unsupported token. " + _flag);

        if (_added_budget == null) {
            _added_budget = BigInteger.ZERO;
        }


        BigInteger sponsorReward = _added_budget.multiply(BigInteger.TWO).divide(BigInteger.valueOf(100));
        BigInteger totalTransfer = _added_budget.add(sponsorReward);

        BigInteger proposalBudget = proposalBudgets.getOrDefault(_ipfs_key, BigInteger.ZERO);
        proposalBudgets.set(_ipfs_key, proposalBudget.add(totalTransfer));
        BigInteger bnUSDFund = get_total_funds().get(bnUSD);
        Context.require(totalTransfer.compareTo(bnUSDFund) <= 0, TAG + ": Not enough " + totalTransfer + " BNUSD on treasury");

        JsonObject budgetAdjustmentData = new JsonObject();
        budgetAdjustmentData.add("method", "budget_adjustment");
        JsonObject params = new JsonObject();
        params.add("_ipfs_key", _ipfs_key);
        params.add("_added_budget", _added_budget.toString(16));
        params.add("_added_sponsor_reward", sponsorReward.toString(16));
        params.add("_added_installment_count", _total_installment_count);
        budgetAdjustmentData.add("params", params);

        Context.call(balancedDollar.get(), "transfer", cpsTreasuryScore.get(), totalTransfer, budgetAdjustmentData.toString().getBytes());
        ProposalFundTransferred(_ipfs_key, "Successfully transferred " + totalTransfer + " " + bnUSD + " to CPS Treasury");
    }

    private void disqualifyProposalFund(String ipfsKey, BigInteger value) {
        Context.require(proposalExists(ipfsKey), TAG + ": IPFS key does not exist.");

        BigInteger budget = proposalBudgets.get(ipfsKey);
        proposalBudgets.set(ipfsKey, budget.subtract(value));

        burnExtraFund();
        ProposalDisqualified(ipfsKey, "Proposal disqualified. " + value + " " + bnUSD + " is returned back to Treasury.");
    }

    @External
    @Payable
    public void add_fund() {
        burnExtraFund();
        FundReceived(Context.getCaller(), "Treasury fund " + Context.getValue() + " " + ICX + " received.");
    }

    private void burnExtraFund() {
        Map<String, BigInteger> amounts = get_total_funds();
        BigInteger icxAmount = amounts.get(ICX);
        BigInteger bnUSDAmount = amounts.get(bnUSD);
        BigInteger extraAmountIcx = icxAmount.subtract(treasuryFund.get());
        BigInteger extraAmountBnUSD = bnUSDAmount.subtract(treasuryFundbnUSD.get());

        if (extraAmountIcx.compareTo(BigInteger.ZERO) > 0) {
            burn(extraAmountIcx);
        }

        if (extraAmountBnUSD.compareTo(BigInteger.ZERO) > 0) {
            swapTokens(balancedDollar.get(), sICXScore.get(), extraAmountBnUSD);
        }
    }

    private void swapTokens(Address _from, Address _to, BigInteger _amount) {
        JsonObject swapData = new JsonObject();
        swapData.add("method", "_swap");
        JsonObject params = new JsonObject();
        params.add("toToken", _to.toString());
        swapData.add("params", params);
        Context.call(_from, "transfer", dexScore.get(), _amount, swapData.toString().getBytes());
    }

    @External
    public void swapIcxBnusd(BigInteger amount) {
        Address[] path = new Address[]{sICXScore.get(), balancedDollar.get()};
        Object[] params = new Object[]{path};
        Context.call(amount, routerScore.get(), "route", params);
    }

    @External
    public void swap_tokens(int _count) {
        validateCpsScore();
        BigInteger sicxICXPrice = (BigInteger) Context.call(dexScore.get(), "getPrice", sICXICXPoolID);
        BigInteger sicxBnusdPrice = (BigInteger) Context.call(dexScore.get(), "getPrice", sICXBNUSDPoolID);
        BigInteger icxbnUSDPrice = sicxBnusdPrice.multiply(EXA).divide(sicxICXPrice);
        BigInteger bnUSDRemainingToSwap = get_remaining_swap_amount().get("remainingToSwap");
        if (bnUSDRemainingToSwap.compareTo(BigInteger.TEN.multiply(EXA)) < 0 || _count == 0) {
            swapState.set(SwapCompleted);
            swapCount.set(SwapReset);
        } else {
            int swapState = this.swapState.getOrDefault(0);
            if (swapState == SwapContinue) {
                int swapCountValue = swapCount.getOrDefault(0);
                int count = _count - swapCountValue;
                if (count == 0) {
                    this.swapState.set(SwapCompleted);
                    swapCount.set(SwapReset);
                } else {
                    BigInteger remainingICXToSwap = bnUSDRemainingToSwap.multiply(EXA).divide(icxbnUSDPrice.multiply(BigInteger.valueOf(count)));
                    BigInteger icxBalance = Context.getBalance(Context.getAddress());
                    if (remainingICXToSwap.compareTo(icxBalance) > 0) {
                        remainingICXToSwap = icxBalance;
                    }

                    if (remainingICXToSwap.compareTo(BigInteger.valueOf(5).multiply(EXA)) > 0) {
                        swapIcxBnusd(remainingICXToSwap);
                        swapCount.set(swapCountValue + 1);
                    }
                }
            }
        }
    }

    @External(readonly = true)
    public Map<String, Integer> get_swap_state_status() {
        return Map.of("state", swapState.getOrDefault(0), "count", swapCount.getOrDefault(0));
    }

    @External
    public void reset_swap_state() {
        Address cpsScoreAddress = cpsScore.get();
        Address caller = Context.getCaller();

        boolean checkCaller = caller.equals(cpsScoreAddress) || (Boolean) Context.call(cpsScoreAddress, "is_admin", caller);
        Context.require(checkCaller, TAG + ": Only admin can call this method.");
        swapState.set(SwapContinue);
        swapCount.set(SwapReset);
    }

    @External(readonly = true)
    public Map<String, Object> get_proposal_details(@Optional int _start_index, @Optional int _end_index) {
        if (_end_index == 0) {
            _end_index = 20;
        }
        List<Map<String, Object>> proposalsList = new ArrayList<>();
        if ((_end_index - _start_index) > 50) {
            Context.revert(TAG + ": Page Length cannot be greater than 50");
        }
        int count = proposalsKeys.size();
        if (_start_index > count) {
            Context.revert(TAG + ": Start index can't be higher than total count.");
        }

        if (_start_index < 0) {
            _start_index = 0;
        }

        if (_end_index > count) {
            _end_index = count;

        }

        for (int i = _start_index; i < _end_index; i++) {
            String proposalHash = proposalsKeys.get(i);
            Map<String, Object> proposalDetails = Map.of(TOTAL_BUDGET, proposalBudgets.getOrDefault(proposalHash, BigInteger.ZERO).toString(), IPFS_HASH, proposalHash);
            proposalsList.add(proposalDetails);
        }
        return Map.of("data", proposalsList, "count", count);
    }

    @External
    public void tokenFallback(Address _from, BigInteger _value, byte[] _data) {
        Address bnUSDScore = balancedDollar.get();
        Address sICX = sICXScore.get();
        Address caller = Context.getCaller();

        Context.require(caller.equals(bnUSDScore) || caller.equals(sICX), TAG +
                " Only " + bnUSDScore + " and " + sICX + " can send tokens to CPF Treasury.");
        if (caller.equals(sICX)) {
            if (_from.equals(dexScore.get())) {
                JsonObject swapICX = new JsonObject();
                swapICX.add("method", "_swap_icx");
                Context.call(caller, "transfer", dexScore.get(), _value, swapICX.toString().getBytes());
            } else {
                Context.revert(TAG + ": sICX can be approved only from Balanced DEX.");
            }
        } else {

            if (_data == null || new String(_data).equalsIgnoreCase("none")) {
                _data = "{}".getBytes();
            }
            String unpacked_data = new String(_data);
            JsonObject transferData = Json.parse(unpacked_data).asObject();

            if (_from.equals(cpsScore.get())) {
                if (transferData.get("method").asString().equals("return_fund_amount")) {
                    Address _sponsor_address = Address.fromString(transferData.get("params").asObject().get("sponsor_address").asString());
                    returnFundAmount(_sponsor_address, _value);
                } else if (transferData.get("method").asString().equals("burn_amount")) {
                    swapTokens(caller, sICX, _value);
                } else {
                    Context.revert(TAG + ": Not supported method " + transferData.get("method").asString());
                }
            } else if (_from.equals(cpsTreasuryScore.get())) {
                if (transferData.get("method").asString().equals("disqualify_project")) {
                    String ipfs_key = transferData.get("params").asObject().get("ipfs_key").asString();
                    disqualifyProposalFund(ipfs_key, _value);
                } else {
                    Context.revert(TAG + ": Not supported method " + transferData.get("method").asString());
                }
            } else {
                burnExtraFund();
            }
        }
    }

    @Payable
    public void fallback() {
        if (Context.getCaller().equals(dexScore.get())) {
            burn(Context.getValue());
        } else {
            Context.revert(TAG + ": Please send fund using add_fund().");
        }
    }


    //EventLogs
    @EventLog(indexed = 1)
    public void FundReturned(Address _sponsor_address, String note) {
    }

    @EventLog(indexed = 1)
    public void ProposalFundTransferred(String _ipfs_key, String note) {
    }

    @EventLog(indexed = 1)
    public void ProposalDisqualified(String _ipfs_key, String note) {
    }

    @EventLog(indexed = 1)
    public void FundReceived(Address _sponsor_address, String note) {
    }
}
