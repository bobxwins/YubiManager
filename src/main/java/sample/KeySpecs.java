package sample;

import java.io.Serializable;


public class KeySpecs implements Serializable {

    byte[] generatedIV, salt;
    int iterationCount, keyLength;



    String secureRandomAlgorithm,provider, algorithmModePadding;

    KeySpecs(byte[] IVByte, byte[] saltByte, int iterationInt, int keyInt, String sRAString, String providerString, String algoModePadString) {
        this.generatedIV = IVByte;
        this.salt = saltByte;
        this.iterationCount = iterationInt;
        this.keyLength = keyInt;
        this.secureRandomAlgorithm = sRAString;
        this.provider = providerString;
        this.algorithmModePadding = algoModePadString;
    }
    KeySpecs() {
    }


    public byte[] getSalt() {

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

    public String getAlgorithmModePadding() {
        return algorithmModePadding;
    }


    public static String getKeySpecsDir() {
        String keySpecsDir = Global.getSelectedDirectoryPath() + "KeySpecs.txt";
        return keySpecsDir;
    }

}
