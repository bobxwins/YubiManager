package sample;

public class TimerStatic {
    static int timer;
    static boolean selectedCheckBox;

    public static int getTimer() {
        return timer;
    }

    public static void setTimer(int timer) {
        TimerStatic.timer = timer;
    }

    public static boolean getSelectedCheckBox() {
        return selectedCheckBox;
    }

    public static void setSelectedCheckBox(boolean selectedCheckBox) {
        TimerStatic.selectedCheckBox = selectedCheckBox;
    }


}
