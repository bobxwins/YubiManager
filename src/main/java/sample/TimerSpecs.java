package sample;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Timer;

public class TimerSpecs implements Serializable {

        int timer;
    private static int TIMER;
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


    public static  int getTimerS() {
        if ( FileUtils.readAllBytes(Global.getSelectedDirectoryPath()+"timer.txt").length!=0) {
            String timerString = new String(FileUtils.readAllBytes(Global.getSelectedDirectoryPath() + "timer.txt"));
            TIMER = Integer.parseInt(timerString);
            System.out.println("Timer is" + TIMER);
            return TIMER;
        }
        return 15;
    }

    public static void setTimer(int timer) {
        TimerSpecs.TIMER = timer;
        FileUtils.write(Global.getSelectedDirectoryPath()+"timer.txt",String.valueOf(timer).getBytes(StandardCharsets.UTF_8));
        // casts timer to a string, then casts the string to byte array. Integer cannot directly be cast to byte array
    }

}
