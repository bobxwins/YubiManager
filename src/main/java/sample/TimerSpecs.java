package sample;

import java.io.Serializable;
import java.util.Timer;

public class TimerSpecs implements Serializable {

        int timer;

       boolean selectedCheckBox ;
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
        return selectedCheckBox;
        // no setter for TimerSpecs selected checkbox is made, as the selectedCheckBox is set when clicking the CheckBox Object
        // when running the application
    }


    public static String getTimerSpecsDir() {
        String timerSpecsDir = Global.getSelectedDirectoryPath() + "Timer.aes";
        return timerSpecsDir;
    }
    public static TimerSpecs getTimerSpecs () {
        byte[] input = FileUtils.readAllBytes(TimerSpecs.getTimerSpecsDir());
        DecryptFile decryptFile = new DecryptFile();
        TimerSpecs timerSpecs =  SerializedObject.readTimerSpecs(decryptFile.Decryption(input));
        return timerSpecs;
    }
}
