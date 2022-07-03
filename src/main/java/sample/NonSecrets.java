package sample;

import java.io.Serializable;


public class NonSecrets implements Serializable {


    byte[] generatedIV, salt;
    int iterationCount, keyLength;
    String secureRandomAlgorithm,provider,secretKeyAlgorithm,algorithmModePadding;

    NonSecrets(byte[] IVBytes, byte[] saltByte, int iterationInt, int keyInt,
               String sRAString, String keyAlgorithmString, String providerString, String algoModePadString) {
        this.generatedIV = IVBytes;
        this.salt = saltByte;
        this.iterationCount = iterationInt;
        this.keyLength = keyInt;
        this.secureRandomAlgorithm = sRAString;
        this.secretKeyAlgorithm = keyAlgorithmString;
        this.provider = providerString;
        this.algorithmModePadding = algoModePadString;

    }
    NonSecrets() {
    }


    public byte[] getStoredSalt() {
        // used for decryption
        return salt;
    }

    public int getStoredIterationCount() {

        return iterationCount;
    }

    public int getStoredKeyLength() {

        return keyLength;
    }

    public byte[] getStoredGeneratedIV() {
        return generatedIV;
    }

    public String getStoredSecureRandomAlgorithm() {
        return secureRandomAlgorithm;
    }

    public String getStoredProvider() {
        return provider;
    }
    public String getStoredSecretKeyAlgorithm (){return secretKeyAlgorithm;}

    public String getStoredAlgorithmModePadding() {
        return algorithmModePadding;
    }

   /* public static String getNonSecretsDir() {
        String nonSecretsDir = Global.getSelectedDirectoryPath() + "NonSecrets.txt";
        return nonSecretsDir;
    }
    */


}
