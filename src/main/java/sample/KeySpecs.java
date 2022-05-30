package sample;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;


public class KeySpecs implements Serializable {

    byte[] generatedIV, salt;
    int iterationCount, keyLength;



    String algorithm,provider,transformation;

    KeySpecs(byte[] IVByte, byte[] saltByte, int iterationInt, int keyInt, String algorithmString, String providerString, String transformationString) {
        this.generatedIV = IVByte;
        this.salt = saltByte;
        this.iterationCount = iterationInt;
        this.keyLength = keyInt;
        this.algorithm = algorithmString;
        this.provider = providerString;
        this.transformation= transformationString;
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

    public String getAlgorithm() {
        return algorithm;
    }

    public String getProvider() {
        return provider;
    }

    public String getTransformation() {
        return transformation;
    }


    public static String getKeySpecsDir() {
        String keySpecsDir = Global.getSelectedDirectoryPath() + "KeySpecs.txt";
        return keySpecsDir;
    }

}
