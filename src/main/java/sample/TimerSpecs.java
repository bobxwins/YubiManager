package sample;

import java.io.Serializable;

public class TimerSpecs implements Serializable {
 //  private static final long serialVersionUID = 6529685098267757690L;
    private static TimerSpecs timerSpecs;
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

 public static TimerSpecs getTimerSpecs() {
        return timerSpecs;
    }
    public static void setTimerSpecs(TimerSpecs timerSpecsObj) {
        TimerSpecs.timerSpecs=timerSpecsObj;
    }

}
