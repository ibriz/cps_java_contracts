package community.icon.cps.score.cpftreasury;

import com.eclipsesource.json.JsonObject;
import com.iconloop.score.test.Account;
import com.iconloop.score.test.Score;
import com.iconloop.score.test.ServiceManager;
import com.iconloop.score.test.TestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import score.Address;
import score.Context;
import score.DictDB;
import score.VarDB;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

public class CPFTTreasuryTest extends TestBase {
    private static final Address ZERO_ADDRESS = new Address(new byte[Address.LENGTH]);
    private static final Address treasury_score = new Address(new byte[Address.LENGTH]);
    private static final Address score_address = Address.fromString("cx0000000000000000000000000000000000000000");
    private static final Address sicxScore = Address.fromString("cx0000000000000000000000000000000000000001");
    private static final Address bnUSDScore = Address.fromString("cx0000000000000000000000000000000000000002");
    private static final Address dexScore = Address.fromString("cx0000000000000000000000000000000000000003");
    private static final Address cpsTreasuryScore = Address.fromString("cx0000000000000000000000000000000000000004");

    private static final String name = "CPF_TREASURY";
    private static final String symbol = "CPF_TREASURY";
    public static final String TAG = "CPF_TREASURY";
    CPFTreasury cpfTreasury;
    private static final BigInteger MULTIPLIER = new BigInteger("1000000000000000000");


    private static final ServiceManager sm = getServiceManager();
    private static final Account owner = sm.createAccount();
    private static final Account testing_account = sm.createAccount();
    private static final Account testing_account2 = sm.createAccount();

    private Score tokenScore;
    private final SecureRandom secureRandom = new SecureRandom();

    DictDB<String, BigInteger> proposalBudgets = Mockito.mock(DictDB.class);
    VarDB<Integer> swapState = Mockito.mock(VarDB.class);
    VarDB<Integer> swapCount = Mockito.mock(VarDB.class);

    @BeforeEach
    public void setup() throws Exception {
        tokenScore = sm.deploy(owner, CPFTreasury.class);
    }

    @Test
    void name() {
        assertEquals(name, tokenScore.call("name"));
    }


    public void setMaximumTreasuryFundICXExceptions(Account address) {
        try {
            tokenScore.invoke(address, "setMaximumTreasuryFundBnusd", BigInteger.valueOf(100));
        } catch (Exception e) {
            throw e;
        }
    }

    public void setMaximumTreasuryFundBNUSDExceptions(Account address) {
        try {
            tokenScore.invoke(address, "set_maximum_treasury_fund_bnusd", BigInteger.valueOf(100 * 10 ^ 18));
        } catch (Exception e) {
            throw e;
        }
    }

    @Test
    void setCPSScore() {
        VarDB<Address> cpsScore = mock(VarDB.class);
        try(MockedStatic<Context> theMock = Mockito.mockStatic(Context.class)){
            theMock.when(() -> Context.call(cpsScore.get(), "is_admin", Context.getCaller())).thenReturn(true);
            tokenScore.invoke(owner, "setCpsScore", score_address);
        }
        assertEquals(score_address, tokenScore.call("getCpsScore"));
    }

    @Test
    void setCPSTreasuryScore() {
        VarDB<Address> cpsScore = mock(VarDB.class);
        try(MockedStatic<Context> theMock = Mockito.mockStatic(Context.class)){
            theMock.when(() -> Context.call(cpsScore.get(), "is_admin", Context.getCaller())).thenReturn(true);
            tokenScore.invoke(owner, "setCpsTreasuryScore", score_address);
        }
        assertEquals(score_address, tokenScore.call("getCpsTreasuryScore"));
    }

    @Test
    void setBMUSDScore() {
        VarDB<Address> cpsScore = mock(VarDB.class);
        try(MockedStatic<Context> theMock = Mockito.mockStatic(Context.class)){
            theMock.when(() -> Context.call(cpsScore.get(), "is_admin", Context.getCaller())).thenReturn(true);
            tokenScore.invoke(owner, "setBnUSDScore", score_address);
        }
        assertEquals(score_address, tokenScore.call("getBnUSDScore"));
    }

    @Test
    void setSICXScore() {
        VarDB<Address> cpsScore = mock(VarDB.class);
        try(MockedStatic<Context> theMock = Mockito.mockStatic(Context.class)){
            theMock.when(() -> Context.call(cpsScore.get(), "is_admin", Context.getCaller())).thenReturn(true);
            tokenScore.invoke(owner, "setSicxScore", score_address);
        }
        assertEquals(score_address, tokenScore.call("getSicxScore"));
    }

    @Test
    void setDEXScore() {
        VarDB<Address> cpsScore = mock(VarDB.class);
        try(MockedStatic<Context> theMock = Mockito.mockStatic(Context.class)){
            theMock.when(() -> Context.call(cpsScore.get(), "is_admin", Context.getCaller())).thenReturn(true);
            tokenScore.invoke(owner, "setDexScore", score_address);
        }
        assertEquals(score_address, tokenScore.call("getDexScore"));
    }

    @Test
    void setRouterScore() {
        VarDB<Address> cpsScore = mock(VarDB.class);
        try(MockedStatic<Context> theMock = Mockito.mockStatic(Context.class)){
            theMock.when(() -> Context.call(cpsScore.get(), "is_admin", Context.getCaller())).thenReturn(true);
            tokenScore.invoke(owner, "setRouterScore", score_address);
        }
        assertEquals(score_address, tokenScore.call("getRouterScore"));
    }

    void setCPSScoreExceptions(Account address, Address _score) {
        try {
            tokenScore.invoke(address, "setCpsScore", _score);
        } catch (Exception e) {
            throw e;
        }
    }

    void setCPSTreasuryScoreExceptions(Account address, Address _score) {
        try {
            tokenScore.invoke(address, "setCpsTreasuryScore", _score);
        } catch (Exception e) {
            throw e;
        }
    }

    void setBNUSDScoreExceptions(Account address, Address _score) {
        try {
            tokenScore.invoke(address, "setBnUSDScore", _score);
        } catch (Exception e) {
            throw e;
        }
    }

    void setSICXScoreExceptions(Account address, Address _score) {
        try {
            tokenScore.invoke(address, "setSicxScore", _score);
        } catch (Exception e) {
            throw e;
        }
    }

    void setDEXScoreExceptions(Account address, Address _score) {
        try {
            tokenScore.invoke(address, "setDexScore", _score);
        } catch (Exception e) {
            throw e;
        }
    }

    void setRouterScoreExceptions(Account address, Address _score) {
        VarDB<Address> cpsScore = mock(VarDB.class);
        try (MockedStatic<Context> theMock = Mockito.mockStatic(Context.class)){
            theMock.when(() -> Context.call(cpsScore.get(), "is_admin", Context.getCaller())).thenReturn(false);
            tokenScore.invoke(address, "setRouterScore", _score);
        } catch (Exception e) {
            throw e;
        }
    }

//    @Test
//    void setCPSScoreNotAdmin() {
//        Executable setCPSScoreNotOwner = () -> setCPSScoreExceptions(testing_account, score_address);
//        expectErrorMessage(setCPSScoreNotOwner, TAG + ": Only owner can call this method");
//    }
//
//    @Test
//    void setCPSTreasuryScoreNotAdmin() {
//        Executable setCPSTreasuryScoreNotOwner = () -> setCPSTreasuryScoreExceptions(testing_account, score_address);
//        expectErrorMessage(setCPSTreasuryScoreNotOwner, TAG + ": Only owner can call this method");
//    }
//
//    @Test
//    void setBNUSDScoreNotAdmin() {
//        Executable setbnUSDScoreNotOwner = () -> setBNUSDScoreExceptions(testing_account, score_address);
//        expectErrorMessage(setbnUSDScoreNotOwner, TAG + ": Only owner can call this method");
//    }
//
//    @Test
//    void setSICXScoreNotAdmin() {
//        Executable setSICXScoreNotOwner = () -> setSICXScoreExceptions(testing_account, score_address);
//        expectErrorMessage(setSICXScoreNotOwner, TAG + ": Only owner can call this method");
//    }
//
//    @Test
//    void setDEXScoreNotAdmin() {
//        Executable setDEXScoreNotOwner = () -> setDEXScoreExceptions(testing_account, score_address);
//        expectErrorMessage(setDEXScoreNotOwner, TAG + ": Only owner can call this method");
//    }
//
//    @Test
//    void setRouterScoreNotAdmin() {
//        Executable setRouterScoreNotOwner = () -> setRouterScoreExceptions(testing_account, score_address);
//        expectErrorMessage(setRouterScoreNotOwner, TAG + ": Only owner can call this method");
//    }

    @Test
    void transferProposalFundToCPSTreasury() {

        setBMUSDScoreMethod(bnUSDScore);
        setCPSTreasuryScoreMetod(cpsTreasuryScore);
        setCPSScoreMethod(score_address);

        try (MockedStatic<Context> theMock = Mockito.mockStatic(Context.class)) {
            theMock.
                    when(() -> Context.getCaller()).
                    thenReturn(score_address);
            Mockito.when(proposalBudgets.getOrDefault("Proposal 1", null)).thenReturn(BigInteger.valueOf(10));
            theMock.when(() -> Context.getAddress()).thenReturn(tokenScore.getAddress());
            theMock.when(() -> Context.call(BigInteger.class, bnUSDScore, "balanceOf", tokenScore.getAddress())).thenReturn(BigInteger.valueOf(1000).multiply(MULTIPLIER));
            tokenScore.invoke(owner, "transfer_proposal_fund_to_cps_treasury", "Proposal 1", 2, testing_account.getAddress(), testing_account2.getAddress(), "bnUSD", BigInteger.valueOf(100).multiply(MULTIPLIER));

            JsonObject depositProposal = new JsonObject();
            depositProposal.add("method", "deposit_proposal_fund");
            JsonObject params = new JsonObject();
            params.add("ipfs_hash", "Proposal 1");
            params.add("project_duration", 2);
            params.add("sponsor_address", testing_account.getAddress().toString());
            params.add("contributor_address", testing_account2.getAddress().toString());
            params.add("total_budget", BigInteger.valueOf(100).multiply(MULTIPLIER).toString(16));
            params.add("sponsor_reward", BigInteger.valueOf(2).multiply(MULTIPLIER).toString(16));
            params.add("token", "bnUSD");
            depositProposal.add("params", params);
            theMock.verify(() -> Context.call(bnUSDScore, "transfer", cpsTreasuryScore, BigInteger.valueOf(102).multiply(MULTIPLIER), depositProposal.toString().getBytes()), times(1));
        } catch (Exception e) {
            throw e;
        }
    }

    @Test
    void updateProposalFund() {
        VarDB<Address> balancedDollar = mock(VarDB.class);
        VarDB<Address> cpsTreasuryScore = mock(VarDB.class);

        transferProposalFundMethod();
        try (MockedStatic<Context> theMock = Mockito.mockStatic(Context.class)) {
            theMock.when(() -> Context.getCaller()).thenReturn(score_address);
            theMock.when(() -> Context.getBalance(Context.getAddress())).thenReturn(BigInteger.valueOf(1000).multiply(MULTIPLIER));
            theMock.when(() -> Context.call(balancedDollar.get(), "balanceOf", Context.getAddress())).thenReturn(BigInteger.valueOf(1000).multiply(MULTIPLIER));
            tokenScore.invoke(owner, "update_proposal_fund", "Proposal 1", "bnUSD", BigInteger.valueOf(100).multiply(MULTIPLIER), 1);
            Map<String, Object> details = (Map<String, Object>) tokenScore.call("get_proposal_details", 0, 5);
            List<Map<String, Object>> proposalsList = new ArrayList<>();
            proposalsList.add(Map.of("_ipfs_hash", "Proposal 1", "_budget_transfer", BigInteger.valueOf(204).multiply(MULTIPLIER).toString()));
            proposalsList.add(Map.of("_ipfs_hash", "Proposal 2", "_budget_transfer", BigInteger.valueOf(1122).multiply(MULTIPLIER).divide(BigInteger.TEN).toString()));
            Map<String, Object> expectedDetails = Map.of("count", 2, "data", proposalsList);
            assertEquals(details, expectedDetails);

            JsonObject budgetAdjustmentData = new JsonObject();
            budgetAdjustmentData.add("method", "budget_adjustment");
            JsonObject params = new JsonObject();
            params.add("_ipfs_key", "Proposal 1");
            params.add("_added_budget", BigInteger.valueOf(100).multiply(MULTIPLIER).toString(16));
            params.add("_added_sponsor_reward", BigInteger.valueOf(2).multiply(MULTIPLIER).toString(16));
            params.add("_added_installment_count", 1);
            budgetAdjustmentData.add("params", params);

            theMock.verify(() -> Context.call(balancedDollar.get(), "transfer", cpsTreasuryScore.get(), BigInteger.valueOf(102).multiply(MULTIPLIER), budgetAdjustmentData.toString().getBytes()), times(1));
        }
    }

    void setMaxCapIcxAndBnusd(){
        VarDB<Address> cpsScore = mock(VarDB.class);
        try(MockedStatic<Context> theMock = Mockito.mockStatic(Context.class)){
            theMock.when(() -> Context.call(cpsScore.get(), "is_admin", Context.getCaller())).thenReturn(true);
            tokenScore.invoke(owner, "setMaximumTreasuryFundIcx", BigInteger.valueOf(2000).multiply(MULTIPLIER));
            tokenScore.invoke(owner, "setMaximumTreasuryFundBnusd", BigInteger.valueOf(2000).multiply(MULTIPLIER));

        }
    }

    @Test
    void setMaxCapIcxAndBnusdTest(){
        VarDB<Address> cpsScore = mock(VarDB.class);
        try(MockedStatic<Context> theMock = Mockito.mockStatic(Context.class)){
            theMock.when(() -> Context.call(cpsScore.get(), "is_admin", Context.getCaller())).thenReturn(true);
            tokenScore.invoke(owner, "setMaximumTreasuryFundIcx", BigInteger.valueOf(2000).multiply(MULTIPLIER));
            tokenScore.invoke(owner, "setMaximumTreasuryFundBnusd", BigInteger.valueOf(2000).multiply(MULTIPLIER));
        }
    }

    @Test
    void swapIcxBnusd(){
        setSICXScoreMethod(sicxScore);
        setBMUSDScoreMethod(bnUSDScore);
        try(MockedStatic<Context> theMock = Mockito.mockStatic(Context.class)){
            VarDB<Address> routerScore = mock(VarDB.class);
            Address[] path = new Address[]{sicxScore, bnUSDScore};
            Object[] params = new Object[]{path};
            theMock.when(() -> Context.call(BigInteger.valueOf(10).multiply(MULTIPLIER), routerScore.get(), "route", params)).thenReturn(null);
            tokenScore.invoke(owner, "swapIcxBnusd", BigInteger.valueOf(10).multiply(MULTIPLIER));
            theMock.verify(() -> Context.call(BigInteger.valueOf(10).multiply(MULTIPLIER), routerScore.get(), "route", params), times(1));
        }

    }

    @Test
    void swapTokensRemainingToSwapLessThanTen() {
        setMaxCapIcxAndBnusd();
        VarDB<Address> balancedDollar = mock(VarDB.class);
        VarDB<Address> dexScore = mock(VarDB.class);
        VarDB<Address> cpsScore = mock(VarDB.class);

        transferProposalFundMethod();
        try (MockedStatic<Context> theMock = Mockito.mockStatic(Context.class)) {
            theMock.when(() -> Context.getCaller()).thenReturn(score_address);
            theMock.when(() -> Context.call(dexScore.get(), "getPrice", 1)).thenReturn(BigInteger.valueOf(12).multiply(MULTIPLIER).divide(BigInteger.TEN));
            theMock.when(() -> Context.call(dexScore.get(), "getPrice", 2)).thenReturn(BigInteger.valueOf(8).multiply(MULTIPLIER).divide(BigInteger.TEN));
            theMock.when(() -> Context.call(balancedDollar.get(), "balanceOf", Context.getAddress())).thenReturn(BigInteger.valueOf(1995).multiply(MULTIPLIER));
            theMock.when(() -> Context.getBalance(Context.getAddress())).thenReturn(BigInteger.valueOf(10000).multiply(MULTIPLIER));
            tokenScore.invoke(owner, "swap_tokens", 10);
            assertEquals(tokenScore.call("get_swap_state_status"), Map.of("count", 0, "state", 1));
        }
    }

    @Test
    void swapTokensRemainingToSwapMoreThanTen() {
        setMaxCapIcxAndBnusd();
        VarDB<Address> balancedDollar = mock(VarDB.class);
        VarDB<Address> dexScore = mock(VarDB.class);

        transferProposalFundMethod();
        try (MockedStatic<Context> theMock = Mockito.mockStatic(Context.class)) {
            theMock.when(() -> Context.getCaller()).thenReturn(score_address);
            theMock.when(() -> Context.call(dexScore.get(), "getPrice", 1)).thenReturn(BigInteger.valueOf(12).multiply(MULTIPLIER).divide(BigInteger.TEN));
            theMock.when(() -> Context.call(dexScore.get(), "getPrice", 2)).thenReturn(BigInteger.valueOf(8).multiply(MULTIPLIER).divide(BigInteger.TEN));
            theMock.when(() -> Context.call(balancedDollar.get(), "balanceOf", Context.getAddress())).thenReturn(BigInteger.valueOf(1000).multiply(MULTIPLIER));
            theMock.when(() -> Context.getBalance(Context.getAddress())).thenReturn(BigInteger.valueOf(2000).multiply(MULTIPLIER));
            tokenScore.invoke(owner, "swap_tokens", 10);
            assertEquals(tokenScore.call("get_swap_state_status"), Map.of("count", 1, "state", 0));
        }
    }

    @Test
    void swapTokensRemainingToSwapMoreThanTenCountIsZeroFromCPSScore() {
        setMaxCapIcxAndBnusd();
        VarDB<Address> balancedDollar = mock(VarDB.class);
        VarDB<Address> dexScore = mock(VarDB.class);

        transferProposalFundMethod();
        try (MockedStatic<Context> theMock = Mockito.mockStatic(Context.class)) {
            theMock.when(() -> Context.getCaller()).thenReturn(score_address);
            theMock.when(() -> Context.call(dexScore.get(), "getPrice", 1)).thenReturn(BigInteger.valueOf(12).multiply(MULTIPLIER).divide(BigInteger.TEN));
            theMock.when(() -> Context.call(dexScore.get(), "getPrice", 2)).thenReturn(BigInteger.valueOf(8).multiply(MULTIPLIER).divide(BigInteger.TEN));
            theMock.when(() -> Context.call(balancedDollar.get(), "balanceOf", Context.getAddress())).thenReturn(BigInteger.valueOf(1000).multiply(MULTIPLIER));
            theMock.when(() -> Context.getBalance(Context.getAddress())).thenReturn(BigInteger.valueOf(2000).multiply(MULTIPLIER));
            tokenScore.invoke(owner, "swap_tokens", 0);
            assertEquals(tokenScore.call("get_swap_state_status"), Map.of("count", 0, "state", 1));
        }
    }

    @Test
    void resetSwapState() {
        setMaxCapIcxAndBnusd();
        VarDB<Integer> swapState = mock(VarDB.class);
        swapState.set(10);
        setCPSScoreMethod(score_address);
        try (MockedStatic<Context> theMock = Mockito.mockStatic(Context.class)) {
            theMock.when(() -> Context.getCaller()).thenReturn(testing_account.getAddress());
            theMock.when(() -> Context.call(score_address, "is_admin", Context.getCaller())).thenReturn(true);
            tokenScore.invoke(owner, "reset_swap_state");
            assertEquals(tokenScore.call("get_swap_state_status"), Map.of("state", 0, "count", 0));
        }
    }

    private void transferProposalFundMethod() {
        VarDB<Address> balancedDollar = mock(VarDB.class);
        setCPSScoreMethod(score_address);
        try (MockedStatic<Context> theMock = Mockito.mockStatic(Context.class)) {
            theMock.when(() -> Context.getCaller()).thenReturn(score_address);
            theMock.when(() -> Context.call(BigInteger.class, balancedDollar.get(), "balanceOf", Context.getAddress())).thenReturn(BigInteger.valueOf(1000).multiply(MULTIPLIER));
            tokenScore.invoke(owner, "transfer_proposal_fund_to_cps_treasury", "Proposal 1", 2, testing_account.getAddress(), testing_account2.getAddress(), "bnUSD", BigInteger.valueOf(100).multiply(MULTIPLIER));
            tokenScore.invoke(owner, "transfer_proposal_fund_to_cps_treasury", "Proposal 2", 2, testing_account.getAddress(), testing_account2.getAddress(), "bnUSD", BigInteger.valueOf(110).multiply(MULTIPLIER));


        }
    }

    @Test
    void addFundExtraICX(){
        setMaxCapIcxAndBnusd();
        VarDB<Address> balancedDollar = mock(VarDB.class);
        setBMUSDScoreMethod(bnUSDScore);
        setDEXScoreMethod(dexScore);
        setSICXScoreMethod(sicxScore);
        setCPSScoreMethod(score_address);

        try(MockedStatic<Context> theMock = Mockito.mockStatic(Context.class)){
            theMock.when(() -> Context.getBalance(Context.getAddress())).thenReturn(BigInteger.valueOf(2001).multiply(MULTIPLIER));
            theMock.when(() -> Context.call(bnUSDScore, "balanceOf", Context.getAddress())).thenReturn(BigInteger.valueOf(2000).multiply(MULTIPLIER));
            tokenScore.invoke(owner, "add_fund");
            theMock.verify(() -> Context.call(BigInteger.valueOf(1).multiply(MULTIPLIER), score_address, "burn"), times(1));

        }
    }

    @Test
    void addFundExtraBnUSD(){
        addFundMethod();
    }

    private void addFundMethod() {
        setMaxCapIcxAndBnusd();
        VarDB<Address> balancedDollar = mock(VarDB.class);
        setBMUSDScoreMethod(bnUSDScore);
        setDEXScoreMethod(dexScore);
        setSICXScoreMethod(sicxScore);
        setCPSScoreMethod(score_address);

        try(MockedStatic<Context> theMock = Mockito.mockStatic(Context.class)){
            theMock.when(() -> Context.getBalance(Context.getAddress())).thenReturn(BigInteger.valueOf(2000).multiply(MULTIPLIER));
            theMock.when(() -> Context.call(bnUSDScore, "balanceOf", Context.getAddress())).thenReturn(BigInteger.valueOf(2001).multiply(MULTIPLIER));
            tokenScore.invoke(owner, "add_fund");
            JsonObject swapData = new JsonObject();
            swapData.add("method", "_swap");
            JsonObject params = new JsonObject();
            params.add("toToken", sicxScore.toString());
            swapData.add("params", params);
            theMock.verify(() -> Context.call(bnUSDScore, "transfer", dexScore, BigInteger.ONE.multiply(MULTIPLIER), swapData.toString().getBytes()));
        }
    }

    @Test
    void tokenFallbackCallerIsSicxFromIsDex(){
        setBMUSDScoreMethod(bnUSDScore);
        setDEXScoreMethod(dexScore);
        setSICXScoreMethod(sicxScore);
        setCPSScoreMethod(score_address);

        try(MockedStatic<Context> theMock = Mockito.mockStatic(Context.class)){
            theMock.when(() -> Context.getCaller()).thenReturn(sicxScore);
            String data = "data";
            JsonObject swapICX = new JsonObject();
            swapICX.add("method", "_swap_icx");
            tokenScore.invoke(owner, "tokenFallback", dexScore, BigInteger.ONE.multiply(MULTIPLIER), data.getBytes());
            theMock.verify(() -> Context.call(sicxScore, "transfer", dexScore, BigInteger.ONE.multiply(MULTIPLIER), swapICX.toString().getBytes()), times(1));
        }
    }

    @Test
    void tokenFallbackCallerIsSicxFromNotDex(){
        setBMUSDScoreMethod(bnUSDScore);
        setDEXScoreMethod(dexScore);
        setSICXScoreMethod(sicxScore);
        setCPSScoreMethod(score_address);

        try(MockedStatic<Context> theMock = Mockito.mockStatic(Context.class)){
            theMock.when(() -> Context.getCaller()).thenReturn(sicxScore);
            String data = "data";
            JsonObject swapICX = new JsonObject();
            swapICX.add("method", "_swap_icx");
            tokenScore.invoke(owner, "tokenFallback", sicxScore, BigInteger.ONE.multiply(MULTIPLIER), data.getBytes());
            theMock.verify(() -> Context.call(dexScore, "transfer", BigInteger.ONE.multiply(MULTIPLIER), swapICX.toString().getBytes()), times(0));
            theMock.verify(() -> Context.revert(TAG + ": sICX can be approved only from Balanced DEX."), times(1));
        }
    }

    @Test
    void tokenFallBackCallerNotSicx(){
        setMaxCapIcxAndBnusd();
        setBMUSDScoreMethod(bnUSDScore);
        setDEXScoreMethod(dexScore);
        setSICXScoreMethod(sicxScore);
        setCPSScoreMethod(score_address);

        try(MockedStatic<Context> theMock = Mockito.mockStatic(Context.class)){
            theMock.when(() -> Context.getCaller()).thenReturn(dexScore);
            theMock.when(() -> Context.getBalance(Context.getAddress())).thenReturn(BigInteger.valueOf(2000).multiply(MULTIPLIER));
            theMock.when(() -> Context.call(bnUSDScore, "balanceOf", Context.getAddress())).thenReturn(BigInteger.valueOf(2001).multiply(MULTIPLIER));
            String data = "none";
            JsonObject swapICX = new JsonObject();
            swapICX.add("method", "_swap_icx");
            tokenScore.invoke(owner, "tokenFallback", sicxScore, BigInteger.ONE.multiply(MULTIPLIER), data.getBytes());
            JsonObject swapData = new JsonObject();
            swapData.add("method", "_swap");
            JsonObject params = new JsonObject();
            params.add("toToken", sicxScore.toString());
            swapData.add("params", params);
            theMock.verify(() -> Context.call(bnUSDScore, "transfer", dexScore, BigInteger.ONE.multiply(MULTIPLIER), swapData.toString().getBytes()));

        }
    }

    @Test
    void tokenFallBackFromIsCpsScoreMethodReturnFundAmount(){
        setMaxCapIcxAndBnusd();

        setBMUSDScoreMethod(bnUSDScore);
        setDEXScoreMethod(dexScore);
        setSICXScoreMethod(sicxScore);
        setCPSScoreMethod(score_address);

        try(MockedStatic<Context> theMock = Mockito.mockStatic(Context.class)){
            theMock.when(() -> Context.getCaller()).thenReturn(dexScore);
            theMock.when(() -> Context.getBalance(Context.getAddress())).thenReturn(BigInteger.valueOf(2000).multiply(MULTIPLIER));
            theMock.when(() -> Context.call(bnUSDScore, "balanceOf", Context.getAddress())).thenReturn(BigInteger.valueOf(2001).multiply(MULTIPLIER));
            JsonObject jsonObject = new JsonObject();
            jsonObject.add("method", "return_fund_amount");
            JsonObject params = new JsonObject();
            params.add("sponsor_address", testing_account.getAddress().toString());
            jsonObject.add("params", params);
            tokenScore.invoke(owner, "tokenFallback", score_address, BigInteger.ONE.multiply(MULTIPLIER), jsonObject.toString().getBytes());
            JsonObject swapData = new JsonObject();
            swapData.add("method", "_swap");
            JsonObject params_ = new JsonObject();
            params_.add("toToken", sicxScore.toString());
            swapData.add("params", params_);
            theMock.verify(() -> Context.call(bnUSDScore, "transfer", dexScore, BigInteger.ONE.multiply(MULTIPLIER), swapData.toString().getBytes()));
        }
    }

    @Test
    void tokenFallBackFromIsCpsScoreMethodBurnAmount(){
        setMaxCapIcxAndBnusd();

        setBMUSDScoreMethod(bnUSDScore);
        setDEXScoreMethod(dexScore);
        setSICXScoreMethod(sicxScore);
        setCPSScoreMethod(score_address);

        try(MockedStatic<Context> theMock = Mockito.mockStatic(Context.class)){
            theMock.when(() -> Context.getCaller()).thenReturn(dexScore);
            theMock.when(() -> Context.getBalance(Context.getAddress())).thenReturn(BigInteger.valueOf(2000).multiply(MULTIPLIER));
            theMock.when(() -> Context.call(bnUSDScore, "balanceOf", Context.getAddress())).thenReturn(BigInteger.valueOf(2001).multiply(MULTIPLIER));
            JsonObject jsonObject = new JsonObject();
            jsonObject.add("method", "burn_amount");
            JsonObject params = new JsonObject();
            params.add("sponsor_address", testing_account.getAddress().toString());
            jsonObject.add("params", params);
            tokenScore.invoke(owner, "tokenFallback", score_address, BigInteger.ONE.multiply(MULTIPLIER), jsonObject.toString().getBytes());
            JsonObject swapData = new JsonObject();
            swapData.add("method", "_swap");
            JsonObject params_ = new JsonObject();
            params_.add("toToken", sicxScore.toString());
            swapData.add("params", params_);
            theMock.verify(() -> Context.call(dexScore, "transfer", dexScore, BigInteger.ONE.multiply(MULTIPLIER), swapData.toString().getBytes()));
            }
    }

    @Test
    void tokenFallBackFromIsCpsTreasuryScore(){
        setMaxCapIcxAndBnusd();
        setBMUSDScoreMethod(bnUSDScore);
        setDEXScoreMethod(dexScore);
        setSICXScoreMethod(sicxScore);
        setCPSTreasuryScoreMetod(cpsTreasuryScore);
        setCPSScoreMethod(score_address);

        try (MockedStatic<Context> theMock = Mockito.mockStatic(Context.class)) {
            theMock.when(() -> Context.getCaller()).thenReturn(score_address);
            theMock.when(() -> Context.call(BigInteger.class, bnUSDScore, "balanceOf", Context.getAddress())).thenReturn(BigInteger.valueOf(1000).multiply(MULTIPLIER));
            tokenScore.invoke(owner, "transfer_proposal_fund_to_cps_treasury", "Proposal 1", 2, testing_account.getAddress(), testing_account2.getAddress(), "bnUSD", BigInteger.valueOf(100).multiply(MULTIPLIER));
            tokenScore.invoke(owner, "transfer_proposal_fund_to_cps_treasury", "Proposal 2", 2, testing_account.getAddress(), testing_account2.getAddress(), "bnUSD", BigInteger.valueOf(110).multiply(MULTIPLIER));
        }

        try(MockedStatic<Context> theMock = Mockito.mockStatic(Context.class)){
            theMock.when(() -> Context.getCaller()).thenReturn(dexScore);
            theMock.when(() -> Context.getBalance(Context.getAddress())).thenReturn(BigInteger.valueOf(2000).multiply(MULTIPLIER));
            theMock.when(() -> Context.call(bnUSDScore, "balanceOf", Context.getAddress())).thenReturn(BigInteger.valueOf(2001).multiply(MULTIPLIER));
            JsonObject jsonObject = new JsonObject();
            jsonObject.add("method", "disqualify_project");
            JsonObject params = new JsonObject();
            params.add("ipfs_key", "Proposal 1");
            jsonObject.add("params", params);
            tokenScore.invoke(owner, "tokenFallback", cpsTreasuryScore, BigInteger.valueOf(100).multiply(MULTIPLIER), jsonObject.toString().getBytes());
            JsonObject swapData = new JsonObject();
            swapData.add("method", "_swap");
            JsonObject params_ = new JsonObject();
            params_.add("toToken", sicxScore.toString());
            swapData.add("params", params_);
            theMock.verify(() -> Context.call(bnUSDScore, "transfer", dexScore, BigInteger.ONE.multiply(MULTIPLIER), swapData.toString().getBytes()));
            Map<String, Object> details = (Map<String, Object>) tokenScore.call("get_proposal_details", 0, 5);
            List<Map<String, Object>> proposalsList = new ArrayList<>();
            proposalsList.add(Map.of("_ipfs_hash", "Proposal 1", "_budget_transfer", BigInteger.valueOf(2).multiply(MULTIPLIER).toString()));
            proposalsList.add(Map.of("_ipfs_hash", "Proposal 2", "_budget_transfer", BigInteger.valueOf(1122).multiply(MULTIPLIER).divide(BigInteger.TEN).toString()));
            Map<String, Object> expectedDetails = Map.of("count", 2, "data", proposalsList);
            assertEquals(details, expectedDetails);
        }
    }

    @Test
    void tokenFallBackFromIsCpsScoreInvalidMethod(){
        setMaxCapIcxAndBnusd();

        setBMUSDScoreMethod(bnUSDScore);
        setDEXScoreMethod(dexScore);
        setSICXScoreMethod(sicxScore);
        setCPSScoreMethod(score_address);

        try(MockedStatic<Context> theMock = Mockito.mockStatic(Context.class)){
            theMock.when(() -> Context.getCaller()).thenReturn(dexScore);
            theMock.when(() -> Context.getBalance(Context.getAddress())).thenReturn(BigInteger.valueOf(2000).multiply(MULTIPLIER));
            theMock.when(() -> Context.call(bnUSDScore, "balanceOf", Context.getAddress())).thenReturn(BigInteger.valueOf(2001).multiply(MULTIPLIER));
            JsonObject jsonObject = new JsonObject();
            jsonObject.add("method", "return_fund_amount_");
            JsonObject params = new JsonObject();
            params.add("sponsor_address", testing_account.getAddress().toString());
            jsonObject.add("params", params);
            tokenScore.invoke(owner, "tokenFallback", score_address, BigInteger.ONE.multiply(MULTIPLIER), jsonObject.toString().getBytes());
            theMock.verify(() -> Context.revert(TAG + ": Not supported method " + "return_fund_amount_"), times(1));
        }
    }

    @Test
    void tokenFallBackFromIsCpsTreasuryScoreInvalidMethod(){
        setMaxCapIcxAndBnusd();

        setBMUSDScoreMethod(bnUSDScore);
        setDEXScoreMethod(dexScore);
        setSICXScoreMethod(sicxScore);
        setCPSTreasuryScoreMetod(cpsTreasuryScore);
        setCPSScoreMethod(score_address);

        try (MockedStatic<Context> theMock = Mockito.mockStatic(Context.class)) {
            theMock.when(() -> Context.getCaller()).thenReturn(score_address);
            theMock.when(() -> Context.call(BigInteger.class, bnUSDScore, "balanceOf", Context.getAddress())).thenReturn(BigInteger.valueOf(1000).multiply(MULTIPLIER));
            tokenScore.invoke(owner, "transfer_proposal_fund_to_cps_treasury", "Proposal 1", 2, testing_account.getAddress(), testing_account2.getAddress(), "bnUSD", BigInteger.valueOf(100).multiply(MULTIPLIER));
            tokenScore.invoke(owner, "transfer_proposal_fund_to_cps_treasury", "Proposal 2", 2, testing_account.getAddress(), testing_account2.getAddress(), "bnUSD", BigInteger.valueOf(110).multiply(MULTIPLIER));
        }

        try(MockedStatic<Context> theMock = Mockito.mockStatic(Context.class)){
            theMock.when(() -> Context.getCaller()).thenReturn(dexScore);
            theMock.when(() -> Context.getBalance(Context.getAddress())).thenReturn(BigInteger.valueOf(2000).multiply(MULTIPLIER));
            theMock.when(() -> Context.call(bnUSDScore, "balanceOf", Context.getAddress())).thenReturn(BigInteger.valueOf(2001).multiply(MULTIPLIER));
            JsonObject jsonObject = new JsonObject();
            jsonObject.add("method", "disqualify_project_");
            JsonObject params = new JsonObject();
            params.add("ipfs_key", "Proposal 1");
            jsonObject.add("params", params);
            tokenScore.invoke(owner, "tokenFallback", cpsTreasuryScore, BigInteger.valueOf(100).multiply(MULTIPLIER), jsonObject.toString().getBytes());
            theMock.verify(() -> Context.revert(TAG + ": Not supported method " + "disqualify_project_"));
        }
    }

    @Test
    void fallback(){
        setDEXScoreMethod(dexScore);
        try(MockedStatic<Context> theMock = Mockito.mockStatic(Context.class)){
            theMock.when(() -> Context.getCaller()).thenReturn(dexScore);
            theMock.when(() -> Context.getValue()).thenReturn(BigInteger.valueOf(1000).multiply(MULTIPLIER));
            tokenScore.invoke(owner, "fallback");
            theMock.verify(() -> Context.call(BigInteger.valueOf(1000).multiply(MULTIPLIER), score_address, "burn"), times(1));
        }
    }

    @Test
    void fallbackSenderIsNotDex(){
        setDEXScoreMethod(dexScore);
        try(MockedStatic<Context> theMock = Mockito.mockStatic(Context.class)){
            theMock.when(() -> Context.getCaller()).thenReturn(sicxScore);
            theMock.when(() -> Context.getValue()).thenReturn(BigInteger.valueOf(1000).multiply(MULTIPLIER));
            tokenScore.invoke(owner, "fallback");
            theMock.verify(() -> Context.revert(TAG + ": Please send fund using add_fund()."));
        }
    }


    public void expectErrorMessage(Executable contractCall, String errorMessage) {
        AssertionError e = Assertions.assertThrows(AssertionError.class, contractCall);
        assertEquals(errorMessage, e.getMessage());
    }

    void setCPSScoreMethod(Address scoreAddress) {
        VarDB<Address> cpsScore = mock(VarDB.class);
        try (MockedStatic<Context> theMock = Mockito.mockStatic(Context.class)) {
            theMock.when(() -> Context.call(cpsScore.get(), "is_admin", Context.getCaller())).thenReturn(true);
            tokenScore.invoke(owner, "setCpsScore", scoreAddress);
        }
    }

    void setCPSTreasuryScoreMetod(Address scoreAddress) {
        VarDB<Address> cpsScore = mock(VarDB.class);
        try(MockedStatic<Context> theMock = Mockito.mockStatic(Context.class)){
            theMock.when(() -> Context.call(cpsScore.get(), "is_admin", Context.getCaller())).thenReturn(true);
            tokenScore.invoke(owner, "setCpsTreasuryScore", scoreAddress);
        }
    }

    void setBMUSDScoreMethod(Address scoreAddress) {
        VarDB<Address> cpsScore = mock(VarDB.class);
        try(MockedStatic<Context> theMock = Mockito.mockStatic(Context.class)){
            theMock.when(() -> Context.call(cpsScore.get(), "is_admin", Context.getCaller())).thenReturn(true);
            tokenScore.invoke(owner, "setBnUSDScore", scoreAddress);
        }
    }

    void setSICXScoreMethod(Address scoreAddress) {
        VarDB<Address> cpsScore = mock(VarDB.class);
        try(MockedStatic<Context> theMock = Mockito.mockStatic(Context.class)){
            theMock.when(() -> Context.call(cpsScore.get(), "is_admin", Context.getCaller())).thenReturn(true);
            tokenScore.invoke(owner, "setSicxScore", scoreAddress);
        }
    }

    void setDEXScoreMethod(Address scoreAddress) {
        VarDB<Address> cpsScore = mock(VarDB.class);
        try(MockedStatic<Context> theMock = Mockito.mockStatic(Context.class)){
            theMock.when(() -> Context.call(cpsScore.get(), "is_admin", Context.getCaller())).thenReturn(true);
            tokenScore.invoke(owner, "setDexScore", scoreAddress);
        }
    }

    void setRouterScoreMethod(Address scoreAddress) {
        VarDB<Address> cpsScore = mock(VarDB.class);
        try(MockedStatic<Context> theMock = Mockito.mockStatic(Context.class)){
            theMock.when(() -> Context.call(cpsScore.get(), "is_admin", Context.getCaller())).thenReturn(true);
            tokenScore.invoke(owner, "setRouterScore", score_address);
        }
    }



}


















































