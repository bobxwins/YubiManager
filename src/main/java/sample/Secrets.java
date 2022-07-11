package sample;

import javafx.collections.ObservableList;
import javafx.scene.control.PasswordField;


import java.io.Serializable;
import java.util.ArrayList;


public class Secrets implements Serializable {
    public Secrets(){}
   // private static final long serialVersionUID = 6529685098267757690L;
    private static String MANUALSKEYPWD;
    private static char[] COMBINEDPASSWORD;
    private static ObservableList OBSERVABLELIST;

    Secrets secrets;
   TimerSpecs timerSpecs;
    ArrayList<Entry> entry;

    public ArrayList<Entry> getEntry() {
        return entry;
    }

    public void setEntry(ObservableList<Entry> observableList) {
        this.entry = new ArrayList<>(observableList);
    }

    public Secrets getSecrets() {
        return secrets;
    }

    public void setSecrets(Secrets secrets) {
        this.secrets = secrets;
    }

    public static String getManualYbkPwd() {
        return MANUALSKEYPWD;
    }

    public static void setManualYbkPwd(String manualSkeyPwd) {
        Secrets.MANUALSKEYPWD = manualSkeyPwd;
    }

    public static char[] getMasterPassword() throws Exception{
        return COMBINEDPASSWORD;
    }
    public static void setMasterPassword(PasswordField mpField, PasswordField sKeyField) throws Exception{

        char[] manualPassword = mpField.getText().toCharArray();
        char[] sKeyPassword = sKeyField.getText().toCharArray();

        StringBuilder sb = new StringBuilder(128);
        sb.append(manualPassword);
        sb.append(sKeyPassword);
        COMBINEDPASSWORD = sb.toString().toCharArray();
    }

    public TimerSpecs getTimerSpecs() {
        return timerSpecs;
    }

    public void setTimerSpecs(TimerSpecs timerSpecs) {
        this.timerSpecs = timerSpecs;
    }

    static void setEntryData (ObservableList observableList) {
        Secrets.OBSERVABLELIST =observableList;
    }

}
