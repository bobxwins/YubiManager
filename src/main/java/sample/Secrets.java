package sample;

import javafx.collections.ObservableList;
import javafx.scene.control.PasswordField;


import java.io.Serializable;
import java.util.ArrayList;


public class Secrets implements Serializable {
    public Secrets(){}
   // private static final long serialVersionUID = 6529685098267757690L;
    private static String CONFIGUREHWKPWD;
    private static char[] CONCATENATEDPWDS;
    private TimerSpecs timerSpecs;
    private ArrayList<Entry> entry;

    public ArrayList<Entry> getEntry() {
        return entry;
    }

    public void setEntry(ObservableList<Entry> observableList) {
        this.entry = new ArrayList<>(observableList);
    }

    public static String getConfigureHwkPwd() {
        return CONFIGUREHWKPWD;
    }

    public static void setConfigureHwPwd(String configureHwPwdString) {
        Secrets.CONFIGUREHWKPWD = configureHwPwdString;
    }

    public static char[] getMasterPassword() throws Exception{
        return CONCATENATEDPWDS;
    }
    public static void setMasterPassword(PasswordField mpField, PasswordField sKeyField) throws Exception{

        char[] manualPassword = mpField.getText().toCharArray();
        char[] sKeyPassword = sKeyField.getText().toCharArray();

        StringBuilder sb = new StringBuilder(128);
        sb.append(manualPassword);
        sb.append(sKeyPassword);
        CONCATENATEDPWDS = sb.toString().toCharArray();
    }

    public TimerSpecs getTimerSpecs() {
        return timerSpecs;
    }

    public void setTimerSpecs(TimerSpecs timerSpecs) {
        this.timerSpecs = timerSpecs;
    }



}
