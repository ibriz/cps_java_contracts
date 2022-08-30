package community.icon.cps.score.cpftreasury;

import score.Address;
import score.Context;

import static community.icon.cps.score.cpftreasury.Constants.TAG;

public class Validations {
    public static void validateAdmins() {
        Context.require((Boolean) Context.call(CPFTreasury.cpsScore.get(), "is_admin", Context.getCaller()),
                TAG + ": Only Admins can call this method");

    }


    public static void validateAdminScore(Address _score) {
        validateAdmins();
        Context.require(_score.isContract(), TAG + ": Target " + _score + " is not a SCORE");
    }

    public static void validateCpsScore() {
        Address cpsScore = CPFTreasury.cpsScore.get();
        Context.require(Context.getCaller().equals(cpsScore),
                TAG + ": Only " + cpsScore + " SCORE can send fund using this method.");
    }
}
