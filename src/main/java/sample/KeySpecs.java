package sample;

import java.io.Serializable;


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
/*
    public void setSalt(byte[] salt) {
        this.salt = salt;
    }
*/
    public int getIterationCount() {

        return iterationCount;
    }

   /* public void setIterationCount(int iterationCount) {
        this.iterationCount = iterationCount;
    }
*/
    public int getKeyLength() {

        return keyLength;
    }

    /*public void setKeyLength(int keyLength) {
        this.keyLength = keyLength;
    }
    */

    public byte[] getGeneratedIV() {
        return generatedIV;
    }

  /*  public void setGeneratedIV(byte[] generatedIV) {
        this.generatedIV = generatedIV;
    }
*/
    public static String getKeySpecsDir() {
        String keySpecsDir = Global.getSelectedDirectoryPath() + "KeySpecs.txt";
        return keySpecsDir;
    }


}
