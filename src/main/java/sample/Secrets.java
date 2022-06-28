package sample;

import javafx.scene.control.PasswordField;

public class Secrets {
    private Secrets(){}
    private static String MANUALSKEYPWD;
    private static char[] COMBINEDPASSWORD;

    public static String getManualYbkPwd() {
        return MANUALSKEYPWD;
    }

    public static void setManualYbkPwd(String manualSkeyPwd) {
        Secrets.MANUALSKEYPWD = manualSkeyPwd;
    }

    public static char[] getCombinedPasswords () throws Exception{
        return COMBINEDPASSWORD;
    }
    public static void setCombinedPasswords(PasswordField mpField, PasswordField sKeyField) throws Exception{

        char[] manualPassword = mpField.getText().toCharArray();
        char[] sKeyPassword = sKeyField.getText().toCharArray();

        StringBuilder sb = new StringBuilder(128);
        sb.append(manualPassword);
        sb.append(sKeyPassword);
        COMBINEDPASSWORD = sb.toString().toCharArray();
    }
}
