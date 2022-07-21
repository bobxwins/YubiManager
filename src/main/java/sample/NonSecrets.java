package sample;

import java.io.Serializable;


public class NonSecrets implements Serializable {


    byte[] generatedIV, salt;
    int iterationCount, keyLength;
    String header,challenge;

    NonSecrets(byte[] IVBytes, byte[] saltByte, int iterationInt, int keyInt,
               String headerString,String challengeString) {
        this.generatedIV = IVBytes;
        this.salt = saltByte;
        this.iterationCount = iterationInt;
        this.keyLength = keyInt;
        this.header=headerString;
        this.challenge=challengeString;

    }
    NonSecrets() {
    }

    public  String getHeader() {
        return header;
    }

    public void setHeader( String header) {
        this.header = header;
    }
    public byte[] getStoredSalt() {
        // used for decryption
        return salt;
    }

    public int getIterationCount() {

        return iterationCount;
    }

    public int getKeyLength() {

        return keyLength;
    }
    public String getChallenge() {
        return challenge;
    }

    public void setChallenge( String challenge) {
        this.challenge = challenge;
    }

    public byte[] getGeneratedIV() {
        return generatedIV;
    }

}
