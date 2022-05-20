package sample;

import java.io.Serializable;


public class KeySpecs implements Serializable {

    //private static final long serialVersionUID = 1L;
    private static String folderDir = Global.getSelectedDirectoryPath();

    KeySpecs(byte[] IVByte, byte[] saltByte, int iterationInt, int keyInt) {
        this.generatedIV=IVByte;

        this.salt = saltByte;
        this.iterationCount=iterationInt;
        this.keyLength=keyInt;
    }
    byte[] generatedIV,salt ;
    Object iterationCount,keyLength;
    public  byte[] getSalt() {
        return salt;
    }

    public  void setSalt(byte[] salt) {
      this.salt = salt;
    }

    public   Object getIterationCount() {

        return iterationCount;
    }

    public  void setIterationCount(String iterationCount) {
        this.iterationCount = iterationCount;
    }

    public Object getKeyLength() {
        return keyLength;
    }

    public   void setKeyLength(String keyLength) {
       this. keyLength = keyLength;
    }

  public   byte[] getGeneratedIV ()
{
    return generatedIV;
}

    public   void setGeneratedIV(byte[] generatedIV) {
        this.generatedIV=generatedIV;
    }

    public static String getKeySpecsDir ()
    { String keySpecsDir = folderDir+"KeySpecs.txt";
        return keySpecsDir;
    }

}
