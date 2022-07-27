package sample;

import javafx.collections.ObservableList;

import java.io.Serializable;
import java.util.ArrayList;


public class Secrets implements Serializable {
    public Secrets(){}
    // Data that either gets encrypted or is used in an encryption process

   // private static final long serialVersionUID = 6529685098267757690L;
    private static byte [] CONFIGUREHWKPWD;
    private static char[] CONCATENATEDPWDS;
    private static char[] MANUALPWD;
    private TimerSpecs timerSpecs;
    private ArrayList<Entry> entry;

    public ArrayList<Entry> getEntry() {
        return entry;
    }

    public void setEntry(ObservableList<Entry> observableList) {
        this.entry = new ArrayList<>(observableList);
    }

    public static byte [] getConfigureHwkPwd() {
        return CONFIGUREHWKPWD;
    }

    public static void setConfigureHwPwd(byte [] configureHwPwdString) {
        Secrets.CONFIGUREHWKPWD = configureHwPwdString;
    }

    public static char[] getMasterPassword() throws Exception{
        return CONCATENATEDPWDS;
    }

    public static void setManualPassword(char[] manualPassword) {
        Secrets.MANUALPWD = manualPassword;
    }

    public static char[] getManualPassword() throws Exception{
        return MANUALPWD;
    }

    public static void setMasterPassword(char[] manualPassword, char[] response) throws Exception{

        setManualPassword(manualPassword);
        StringBuilder sb = new StringBuilder(856);
        sb.append(manualPassword);
        sb.append(response);
        CONCATENATEDPWDS = sb.toString().toCharArray();
    }

    public TimerSpecs getTimerSpecs() {
        return timerSpecs;
    }

    public void setTimerSpecs(TimerSpecs timerSpecs) {
        this.timerSpecs = timerSpecs;
    }

}
