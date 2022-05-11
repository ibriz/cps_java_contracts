package community.icon.cps.score.cpstreasury;

import score.Address;
import score.Context;
import score.VarDB;
import score.annotation.External;
import community.icon.cps.score.cpstreasury.CPSTreasury;

import java.math.BigInteger;

public class SetterGetter {
    private static final String CPS_SCORE = "_cps_score";
    private static final String CPF_TREASURY_SCORE = "_cpf_treasury_score";
    private static final String BALANCED_DOLLAR = "balanced_dollar";

    public final VarDB<Address> cpfTreasuryScore = Context.newVarDB(CPF_TREASURY_SCORE, Address.class);
    public final VarDB<Address> cpsScore = Context.newVarDB(CPS_SCORE, Address.class);
    public final VarDB<Address> balancedDollar = Context.newVarDB(BALANCED_DOLLAR, Address.class);

    @External
    public void setCpsScore(Address _score) {
        validateAdminScore(_score);
        cpsScore.set(_score);
    }

    @External(readonly = true) //Todo java convention in get methods??
    public Address getCpsScore() {
        return cpsScore.get();
    }

    @External
    public void setCpfTreasuryScore(Address _score) {
        validateAdminScore(_score);
        cpfTreasuryScore.set(_score);
    }

    @External(readonly = true)
    public Address getCpfTreasuryScore() {
        return cpfTreasuryScore.get();
    }

    @External
    public void setBnUSDScore(Address _score) {
        validateAdminScore(_score);
        balancedDollar.set(_score);
    }

    @External
    public Address getBnUSDScore() {
        return balancedDollar.get();
    }

    public void validateAdmins() {
        CPSTreasury cpsTreasury = new CPSTreasury();
        Boolean isAdmin = callScore(Boolean.class, cpsScore.get(), "is_admin", Context.getCaller());
        Context.require(isAdmin, CPSTreasury.TAG + ": Only admins can call this method");

    }

    public void validateAdminScore(Address _score) {
        validateAdmins();
        Context.require(_score.isContract(), CPSTreasury.TAG + "Target " + _score + " is not a score.");
    }

    public void validateCpsScore() {
        Context.require(Context.getCaller().equals(cpsScore.get()), CPSTreasury.TAG + ": Only CPS score " + cpsScore.get() + " can send fund using this method.");
    }


    public <T> T callScore(Class<T> t, Address address, String method, Object... params) {
        return Context.call(t, address, method, params);
    }

    public void callScore(Address address, String method, Object... params) {
        Context.call(address, method, params);
    }

    public void callScore(BigInteger amount, Address address, String method, Object... params) {
        Context.call(amount, address, method, params);
    }


}
