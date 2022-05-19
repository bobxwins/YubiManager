package sample;

import java.io.Serializable;


public class EncryptionSpecs implements Serializable {

    //private static final long serialVersionUID = 1L;

    EncryptionSpecs(byte[] IVByte, byte[] saltByte, int iteration, int key) {
        this.generatedIV=IVByte;
        this.salt = saltByte;
        this.iterationCount=iteration;
        this.keyLength=key;
    }
    byte[] generatedIV,salt;
    int iterationCount,keyLength;

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

    public int getIterationCount() {
        return iterationCount;
    }

    public void setIterationCount(int iterationCount) {
        this.iterationCount = iterationCount;
    }

    public int getKeyLength() {
        return keyLength;
    }

    public void setKeyLength(int keyLength) {
        this.keyLength = keyLength;
    }


        public byte[] getGeneratedIV ()
{
    return generatedIV;
}

    public void setGeneratedIV(byte[] generatedIV) {
        this.generatedIV=generatedIV;
    }

}
