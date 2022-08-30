package community.icon.cps.score.cpftreasury;

import score.Address;
import score.annotation.External;

public class SetterGetter {
    /**
     * Sets the cps score address. Only owner can set the method
     *
     * @param _score: Score address of cps score
     */
    @External
    public void setCpsScore(Address _score) {
        Validations.validateAdminScore(_score);
        CPFTreasury.cpsScore.set(_score);
    }

    /**
     * Returns the cps score address
     *
     * @return cps score address
     */
    @External(readonly = true)
    public Address getCpsScore() {
        return CPFTreasury.cpsScore.get();
    }

    /**
     * Sets the cps treasury score address. Only cps admins can set the method
     *
     * @param _score: Score address of cps treasury score
     */
    @External
    public void setCpsTreasuryScore(Address _score) {
        Validations.validateAdminScore(_score);
        CPFTreasury.cpsTreasuryScore.set(_score);
    }

    /**
     * Returns the cps treasury score address
     *
     * @return cps treasury score address
     */
    @External(readonly = true)
    public Address getCpsTreasuryScore() {
        return CPFTreasury.cpsTreasuryScore.get();
    }

    /**
     * Sets the bnUSD score address. Only cps admins can set the method
     *
     * @param _score: Score address of bnUSD score
     */
    @External
    public void setBnUSDScore(Address _score) {
        Validations.validateAdminScore(_score);
        CPFTreasury.balancedDollar.set(_score);
    }

    /**
     * Returns the bnUSD score address
     *
     * @return cps bnUSD address
     */
    @External(readonly = true)
    public Address getBnUSDScore() {
        return CPFTreasury.balancedDollar.get();
    }

    /**
     * Sets the sicx score address. Only cps admins can set the method
     *
     * @param _score: Score address of sicx score
     */
    @External
    public void setSicxScore(Address _score) {
        Validations.validateAdminScore(_score);
        CPFTreasury.sICXScore.set(_score);
    }

    /**
     * Reruns the sicx score address
     *
     * @return sicx score address
     */
    @External(readonly = true)
    public Address getSicxScore() {
        return CPFTreasury.sICXScore.get();
    }

    /**
     * Sets the dex score address. Only owner can set the method
     *
     * @param _score: Score address of dex score
     */
    @External
    public void setDexScore(Address _score) {
        Validations.validateAdminScore(_score);
        CPFTreasury.dexScore.set(_score);
    }

    /**
     * Returns the dex score address
     *
     * @return dex score address
     */
    @External(readonly = true)
    public Address getDexScore() {
        return CPFTreasury.dexScore.get();
    }

    /**
     * Sets the router score address. Only owner can set the method
     *
     * @param _score: Score address of router score
     */
    @External
    public void setRouterScore(Address _score) {
        Validations.validateAdminScore(_score);
        CPFTreasury.routerScore.set(_score);
    }

    /**
     * Returns the router score address
     *
     * @return router score address
     */
    @External(readonly = true)
    public Address getRouterScore() {
        return CPFTreasury.routerScore.get();
    }
}
