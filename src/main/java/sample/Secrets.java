package sample;

import javafx.collections.ObservableList;
import org.bouncycastle.util.encoders.Hex;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


public class Secrets implements Serializable {
    public Secrets(){}
    // Data that either gets encrypted or is used in an encryption process
    private static String RESPONSE;
    private static final long serialVersionUID = 42L;
    private static String CONFIGUREHWKPWD;
    private static char[] CONCATENATEDPWDS;
    private static char[] MANUALPWD;
    private TimerSpecs timerSpecs;
    private ArrayList<PasswordRecord> passwordRecord;

    public ArrayList<PasswordRecord> getPwdRecord() {
        return passwordRecord;
    }

    public void setPwdRecord(ObservableList<PasswordRecord> observableList) {
        this.passwordRecord = new ArrayList<>(observableList);
    }

    public static String  getConfigureHwkPwd() {
        byte[] configureBytes =  Secrets.CONFIGUREHWKPWD.getBytes(StandardCharsets.UTF_8);
        String hexString = Hex.toHexString(configureBytes);
        Secrets.CONFIGUREHWKPWD = hexString;
        return CONFIGUREHWKPWD;
    }

    public static void setConfigureHwkPwd(String  configurePwd) {
        Secrets.CONFIGUREHWKPWD = configurePwd;
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
    public static String getResponse() {
        return Secrets.RESPONSE;
    }
    public static void setResponse(String bufferReaderOutput) {
        Secrets.RESPONSE = bufferReaderOutput;
    }
    public TimerSpecs getTimerSpecs() {
        return timerSpecs;
    }

    public void setTimerSpecs(TimerSpecs timerSpecs) {
        this.timerSpecs = timerSpecs;
    }

}
