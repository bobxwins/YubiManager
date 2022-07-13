package sample;

import java.io.Serializable;


public class NonSecrets implements Serializable {


    byte[] generatedIV, salt;
    int iterationCount, keyLength;
    String secureRandomAlgorithm,provider,secretKeyAlgorithm,algorithmModePadding,header;

    NonSecrets(byte[] IVBytes, byte[] saltByte, int iterationInt, int keyInt,
               String sRAString, String keyAlgorithmString, String providerString, String algoModePadString, String headerString) {
        this.generatedIV = IVBytes;
        this.salt = saltByte;
        this.iterationCount = iterationInt;
        this.keyLength = keyInt;
        this.secureRandomAlgorithm = sRAString;
        this.secretKeyAlgorithm = keyAlgorithmString;
        this.provider = providerString;
        this.algorithmModePadding = algoModePadString;
        this.header=headerString;

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

    public byte[] getGeneratedIV() {
        return generatedIV;
    }

    public String getSecureRandomAlgorithm() {
        return secureRandomAlgorithm;
    }

    public String getProvider() {
        return provider;
    }
    public String getSecretKeyAlgorithm(){return secretKeyAlgorithm;}

    public String getAlgorithmModePadding() {
        return algorithmModePadding;
    }

}
