package community.icon.cps.score.cpstreasury.db;
import community.icon.cps.score.cpstreasury.SetterGetter;

import score.*;
import community.icon.cps.score.cpstreasury.utils.consts;

import java.math.BigInteger;
import java.util.Map;

public class ProposalData extends SetterGetter{
    public static class ProposalAttributes{
        public String ipfs_hash;
        public int project_duration;
        public BigInteger total_budget;
        public BigInteger sponsor_reward;
        public String token;
        public String contributor_address;
        public String sponsor_address;
        public String status;
    }
    private final BranchDB<String, VarDB<String>> ipfsHash = Context.newBranchDB(consts.IPFS_HASH, String.class);
    private final BranchDB<String, VarDB<BigInteger>> totalBudget = Context.newBranchDB(consts.TOTAL_BUDGET, BigInteger.class);
    private final BranchDB<String, VarDB<BigInteger>> sponsorReward = Context.newBranchDB(consts.SPONSOR_REWARD, BigInteger.class);
    private final BranchDB<String, VarDB<Integer>> projectDuration = Context.newBranchDB(consts.PROJECT_DURATION, Integer.class);
    private final BranchDB<String, VarDB<Address>> sponsorAddress = Context.newBranchDB(consts.SPONSOR_ADDRESS, Address.class);
    private final BranchDB<String, VarDB<Address>> contributorAddress = Context.newBranchDB(consts.CONTRIBUTOR_ADDRESS, Address.class);
    private final BranchDB<String, VarDB<String>> token = Context.newBranchDB(consts.TOKEN, String.class);
    private final BranchDB<String, VarDB<BigInteger>> withdrawAmount = Context.newBranchDB(consts.WITHDRAW_AMOUNT, BigInteger.class);
    private final BranchDB<String, VarDB<BigInteger>> sponsorWithdrawAmount = Context.newBranchDB(consts.SPONSOR_WITHDRAW_AMOUNT, BigInteger.class);
    private final BranchDB<String, VarDB<BigInteger>> remainingAmount = Context.newBranchDB(consts.REMAINING_AMOUNT, BigInteger.class);
    private final BranchDB<String, VarDB<BigInteger>> sponsorRemainingAmount = Context.newBranchDB(consts.SPONSOR_REMAINING_AMOUNT, BigInteger.class);
    private final BranchDB<String, VarDB<Integer>> installmentCount = Context.newBranchDB(consts.INSTALLMENT_COUNT, Integer.class);
    private final BranchDB<String, VarDB<Integer>> sponsorRewardCount = Context.newBranchDB(consts.SPONSOR_REWARD_COUNT, Integer.class);
    private final BranchDB<String, VarDB<String>> status = Context.newBranchDB(consts.STATUS, String.class);




    public void addDataToProposalDB(ProposalAttributes _proposals, String prefix){
        ipfsHash.at(prefix).set(_proposals.ipfs_hash);
        totalBudget.at(prefix).set(_proposals.total_budget);
        sponsorReward.at(prefix).set(_proposals.sponsor_reward);
        projectDuration.at(prefix).set(_proposals.project_duration);
        sponsorAddress.at(prefix).set(Address.fromString(_proposals.sponsor_address));
        contributorAddress.at(prefix).set(Address.fromString(_proposals.contributor_address));
        withdrawAmount.at(prefix).set(BigInteger.ZERO);
        sponsorWithdrawAmount.at(prefix).set(BigInteger.ZERO);
        remainingAmount.at(prefix).set(_proposals.total_budget);
        sponsorRemainingAmount.at(prefix).set(_proposals.sponsor_reward);
        installmentCount.at(prefix).set(_proposals.project_duration);
        sponsorRewardCount.at(prefix).set(_proposals.project_duration);
        token.at(prefix).set(_proposals.token);
        status.at(prefix).set(_proposals.status);
    }

    public Map<String, ?> getDataFromProposalDB(String prefix){
        return Map.ofEntries(
                Map.entry(consts.IPFS_HASH, ipfsHash.at(prefix).getOrDefault("")),
                Map.entry(consts.TOTAL_BUDGET, totalBudget.at(prefix).getOrDefault(BigInteger.ZERO)),
                Map.entry(consts.SPONSOR_REWARD, sponsorReward.at(prefix).getOrDefault(BigInteger.ZERO)),
                Map.entry(consts.PROJECT_DURATION, projectDuration.at(prefix).getOrDefault(0)),
                Map.entry(consts.SPONSOR_ADDRESS, sponsorAddress.at(prefix).get()),
                Map.entry(consts.CONTRIBUTOR_ADDRESS, contributorAddress.at(prefix).get()),
                Map.entry(consts.WITHDRAW_AMOUNT, withdrawAmount.at(prefix).getOrDefault(BigInteger.ZERO)),
                Map.entry(consts.INSTALLMENT_COUNT, installmentCount.at(prefix).getOrDefault(0)),
                Map.entry(consts.SPONSOR_REWARD_COUNT, sponsorRewardCount.at(prefix).getOrDefault(0)),
                Map.entry(consts.SPONSOR_WITHDRAW_AMOUNT, sponsorWithdrawAmount.at(prefix).getOrDefault(BigInteger.ZERO)),
                Map.entry(consts.REMAINING_AMOUNT, remainingAmount.at(prefix).getOrDefault(BigInteger.ZERO)),
                Map.entry(consts.SPONSOR_REMAINING_AMOUNT, sponsorRemainingAmount.at(prefix).getOrDefault(BigInteger.ZERO)),
                Map.entry(consts.TOKEN, token.at(prefix).getOrDefault("")),
                Map.entry(consts.STATUS, status.at(prefix).getOrDefault(""))
        );
    }

    public String getIpfsHash(String prefix){
        return ipfsHash.at(prefix).get();
    }

    public Address getSponsorAddress(String prefix){
        return sponsorAddress.at(prefix).get();
    }

    public Address getContributorAddress(String prefix){
        return contributorAddress.at(prefix).get();
    }

    public void setTotalBudget(String prefix, BigInteger totalBudget){
        this.totalBudget.at(prefix).set(totalBudget);
    }

    public BigInteger getTotalBudget(String prefix){
        return totalBudget.at(prefix).getOrDefault(BigInteger.ZERO);
    }

    public void setSponsorReward(String prefix, BigInteger sponsorReward){
        this.sponsorReward.at(prefix).set(sponsorReward);
    }

    public BigInteger getSponsorReward(String prefix){
        return sponsorReward.at(prefix).getOrDefault(BigInteger.ZERO);
    }

    public void setProjectDuration(String prefix, int projectDuration){
        this.projectDuration.at(prefix).set(projectDuration);
    }

    public int getProjectDuration(String prefix){
        return projectDuration.at(prefix).getOrDefault(0);
    }

    public void setWithdrawAmount(String prefix, BigInteger withdrawAmount){
        this.withdrawAmount.at(prefix).set(withdrawAmount);
    }

    public BigInteger getWithdrawAmount(String prefix){
        return withdrawAmount.at(prefix).getOrDefault(BigInteger.ZERO);
    }

    public void setInstallmentCount(String prefix, int installmentCount){
        this.installmentCount.at(prefix).set(installmentCount);
    }

    public int getInstallmentCount(String prefix){
        return installmentCount.at(prefix).getOrDefault(0);
    }

    public void setSponsorRewardCount(String prefix, int sponsorRewardCount){
        this.sponsorRewardCount.at(prefix).set(sponsorRewardCount);
    }

    public int getSponsorRewardCount(String prefix){
        return sponsorRewardCount.at(prefix).getOrDefault(0);
    }

    public void setSponsorWithdrawAmount(String prefix, BigInteger sponsorWithdrawAmount){
        this.sponsorWithdrawAmount.at(prefix).set(sponsorWithdrawAmount);
    }

    public BigInteger getSponsorWithdrawAmount(String prefix){
        return sponsorWithdrawAmount.at(prefix).getOrDefault(BigInteger.ZERO);
    }

    public void setRemainingAmount(String prefix, BigInteger remainingAmount){
        this.remainingAmount.at(prefix).set(remainingAmount);
    }

    public BigInteger getRemainingAmount(String prefix){
        return remainingAmount.at(prefix).getOrDefault(BigInteger.ZERO);
    }

    public void setSponsorRemainingAmount(String prefix, BigInteger sponsorRemainingAmount){
        this.sponsorRemainingAmount.at(prefix).set(sponsorRemainingAmount);
    }

    public BigInteger getSponsorRemainingAmount(String prefix){
        return sponsorRemainingAmount.at(prefix).getOrDefault(BigInteger.ZERO);
    }

    public void setStatus(String prefix, String status){
        this.status.at(prefix).set(status);
    }

    public String getStatus(String prefix){
        return status.at(prefix).get();
    }

    public String getToken(String prefix){
        return token.at(prefix).get();
    }
}
