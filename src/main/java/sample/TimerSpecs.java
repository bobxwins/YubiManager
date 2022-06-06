package sample;

import java.io.Serializable;
import java.util.Timer;

public class TimerSpecs implements Serializable {

    private   int timer;
   // private  Boolean selectedCheckBox1 ;
    public  boolean selectedCheckBox ;
    TimerSpecs (int timerInt,  boolean selectedCheckBoxBool)
    {
        this.timer  =timerInt;
        this.selectedCheckBox=selectedCheckBoxBool;
    }
    TimerSpecs () {}

    public int getTimer() {
        return timer;
    }

    public boolean getSelectedCheckBox() {
        System.out.println("the boolean is:"+selectedCheckBox);
        return selectedCheckBox;
    }


    public static String getTimerSpecsDir() {
        String timerSpecsDir = Global.getSelectedDirectoryPath() + "Timer.aes";
        return timerSpecsDir;
    }



    public static TimerSpecs getTimerSpecs ()
    {
      /*  byte[] input = FileUtils.readAllBytes(TimerSpecs.getTimerSpecsDir());
        DecryptFile decryptFile = new DecryptFile();
        byte [] output = decryptFile.Decryption(input);
        TimerSpecs timerSpecs = SerializedObject.readObject2(output);

        */
        byte[] input = FileUtils.readAllBytes(TimerSpecs.getTimerSpecsDir());
        DecryptFile decryptFile = new DecryptFile();
        TimerSpecs timerSpecs = (TimerSpecs) SerializedObject.readObject(decryptFile.Decryption(input));
      return timerSpecs;
    }

}
