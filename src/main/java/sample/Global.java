package sample;

public final class Global {

    private static char [] passwordText;

    private Global(){}  // Private constructor to prevent instantiation

    public static char[] getPasswordText() {
        return passwordText;
    }

    public static void setPassword(char [] passwordText) {

        Global.passwordText = passwordText;
    }
}