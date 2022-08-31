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
    private static String CONFIGURETOKENKEY;
    private static char[] MASTERPASSWORD;
    private static char[] USERCREDENTIAL;
    private TimerSpecs timerSpecs;
    private ArrayList<PasswordRecord> passwordRecord;

    public ArrayList<PasswordRecord> getPwdRecord() {
        return passwordRecord;
    }

    public void setPwdRecord(ObservableList<PasswordRecord> observableList) {
        this.passwordRecord = new ArrayList<>(observableList);
    }

    public static String getTokenKeyConfiguration() {
        byte[] configureBytes =  Secrets.CONFIGURETOKENKEY.getBytes(StandardCharsets.UTF_8);
        String hexString = Hex.toHexString(configureBytes);
        Secrets.CONFIGURETOKENKEY = hexString;
        return CONFIGURETOKENKEY;
    }

    public static void setTokenKeyConfiguration(String  configurePwd) {
        Secrets.CONFIGURETOKENKEY = configurePwd;
    }

    public static char[] getMasterPassword() throws Exception{
        return MASTERPASSWORD;
    }

    public static void setUserCredential(char[] userCredential) {
        Secrets.USERCREDENTIAL = userCredential;
    }

    public static char[] getUserCredential() throws Exception{
        return USERCREDENTIAL;
    }

    public static void setMasterPassword(char[] userCredential, char[] response) throws Exception{

        setUserCredential(userCredential);
        StringBuilder sb = new StringBuilder(856);
        sb.append(userCredential);
        sb.append(response);
        MASTERPASSWORD = sb.toString().toCharArray();
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
