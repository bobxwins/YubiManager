package sample;

import java.io.Serializable;


public class NonSecrets implements Serializable {
// Data that is not meant to concealed, but used to conceal other data sources
private static final long serialVersionUID = 42L;
    byte[] generatedIV, salt;
    int iterationCount, keyLength;
    String MacTag,challenge, cipherText;

    NonSecrets(byte[] IVBytes, byte[] saltByte, int iterationInt, int keyInt,
               String MacTagString,String challengeString, String cipherTextString) {
        this.generatedIV = IVBytes;
        this.salt = saltByte;
        this.iterationCount = iterationInt;
        this.keyLength = keyInt;
        this.MacTag =MacTagString;
        this.challenge=challengeString;
        this.cipherText=cipherTextString;
    }
    NonSecrets() {
    }

    public String getChallenge() {
        return challenge;
    }

    public void setChallenge( String challenge) {
        this.challenge = challenge;
    }

    public String getCipherText() {
        return cipherText;
    }

    public byte[] getGeneratedIV() {
        return generatedIV;
    }

    public  String getMacTag() {
        return MacTag;
    }

    public void setMacTag(String macTag) {
        this.MacTag = macTag;
    }
    public int getIterationCount() {

        return iterationCount;
    }

    public int getKeyLength() {

        return keyLength;
    }

    public byte[] getStoredSalt() {
        // used for decryption
        return salt;
    }


}
