package sample;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;


public class KeySpecs implements Serializable {

    byte[] generatedIV, salt;
    int iterationCount, keyLength;

    KeySpecs(byte[] IVByte, byte[] saltByte, int iterationInt, int keyInt) {
        this.generatedIV = IVByte;
        this.salt = saltByte;
        this.iterationCount = iterationInt;
        this.keyLength = keyInt;
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

    public static String getKeySpecsDir() {
        String keySpecsDir = Global.getSelectedDirectoryPath() + "KeySpecs.txt";
        return keySpecsDir;
    }

}
