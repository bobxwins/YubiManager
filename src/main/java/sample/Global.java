package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.nio.charset.StandardCharsets;


public final class Global {

    private static String passwordFilePath;


    private  static int TIMER;
    private static ObservableList<String> recentFilesData = FXCollections.observableArrayList();

    private static  String SELECTEDDIRECTORYPATH;//=
     private static String DEFAULTHPATH;
    private static  String RECENTFILESDIR;
    private static char[] COMBINEDPASSWORD;

    private  static Label labelEnterPwd = new Label("Please Enter Passwords!");
    private  static Label labelRecentFile = new Label();
    private Global(){}  // Private constructor to prevent instantiation

    public static  int getTimer() {
        if ( FileUtils.readAllBytes(SELECTEDDIRECTORYPATH+"timer.txt").length!=0) {
            String timerString = new String(FileUtils.readAllBytes(SELECTEDDIRECTORYPATH + "timer.txt"));
            TIMER = Integer.parseInt(timerString);
            System.out.println("Timer is" + TIMER);
            return TIMER;
        }
        return 15;
    }

    public static void setTimer(int timer) {
        Global.TIMER = timer;
        FileUtils.write(SELECTEDDIRECTORYPATH+"timer.txt",String.valueOf(timer).getBytes(StandardCharsets.UTF_8));
        // casts timer to a string, then casts the string to byte array. Integer cannot directly be cast to byte array
    }

    public static String getPasswordFilePath() {
        return passwordFilePath;
    }

    public static void setPasswordFilePath(String passwordFilePath) {

        Global.passwordFilePath = passwordFilePath;
        labelRecentFile.setText(passwordFilePath );
    }
    public static String getSelectedDirectoryPath() {
        return SELECTEDDIRECTORYPATH;
    }
    public static void setSelectedDirectoryPath(String selectedDirectoryPath) {
        Global.SELECTEDDIRECTORYPATH = selectedDirectoryPath;
    }
// save as
    public static ObservableList<String> getRecentFilesData() {
        return recentFilesData;
    }


    public static String getRecentFilesDir() throws Exception{
        RECENTFILESDIR = getDefaultDir() +"/RecentFiles.txt";
        return RECENTFILESDIR;
    }

    public static String getDefaultDir() {
        DEFAULTHPATH = System.getProperty("user.dir") + "/resources/sample/passwords";
        return DEFAULTHPATH;
    }

    public static Label getLabelEnterPwd() throws Exception{
        labelEnterPwd.setVisible(false);
        labelEnterPwd.setLayoutX(449);
        labelEnterPwd.setLayoutY(290);
        labelEnterPwd.setFont(new Font(33));
        labelEnterPwd.setTextFill(Color.RED);
        labelEnterPwd.setStyle("-fx-font-weight: bold;");
        return labelEnterPwd;
    }


    public static char[] getCombinedPasswords () throws Exception{
        return COMBINEDPASSWORD;
    }
    public static void setCombinedPasswords(PasswordField mpField, PasswordField ybkSecret) throws Exception{

        char[] masterPassword = mpField.getText().toCharArray();
        char[] ybkPassword = ybkSecret.getText().toCharArray();

        StringBuilder sb = new StringBuilder(128);
        sb.append(masterPassword);
        sb.append(ybkPassword);
        COMBINEDPASSWORD = sb.toString().toCharArray();
    }
}