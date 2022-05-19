package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.File;


public final class Global {

    private static String passwordFilePath;

    static {
        try {
       //     passwordFilePath = Global.getRFCArray()[0];
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // getRFCArray() throws an exception, so the exception has to be caught everytime the function is invoked.
    private static ObservableList<String> recentFilesData = FXCollections.observableArrayList();
    private static  String selectedDirectoryPath ;//=  new File(passwordFilePath).getAbsoluteFile().getParent()+"\\";
     private static String defaultPath ;
    private static  String recentFilesDir ;
    private static char[] combinedPasswords;
   // private static String recentFilesData ;
    private static String []  rFCArray ;
    private  static Label labelEnterPwd = new Label("Please Enter Passwords!");
    private  static Label labelRecentFile = new Label();
    private Global(){}  // Private constructor to prevent instantiation

    public static String getPasswordFilePath() {
        return passwordFilePath;
    }

    public static void setPasswordFilePath(String passwordFilePath) {

        Global.passwordFilePath = passwordFilePath;
        labelRecentFile.setText(passwordFilePath );
    }
    public static String getSelectedDirectoryPath() {
        return selectedDirectoryPath;
    }
    public static void setSelectedDirectoryPath(String selectedDirectoryPath) {
        Global.selectedDirectoryPath = selectedDirectoryPath;
    }
// save as
    public static ObservableList<String> getRecentFilesData() {
        return recentFilesData;
    }

    public static String getRecentFilesDir() throws Exception{
        recentFilesDir = getDefaultDir() +"/RecentFiles.txt";
        return recentFilesDir;
    }

    public static String getDefaultDir() {
        defaultPath  = System.getProperty("user.dir") + "/resources/sample/passwords";
        return defaultPath;
    }

  /*  public static String [] getRFCArray() throws Exception{
        recentFilesData = new String(FileUtils.readAllBytes(getRecentFilesDir())) ;
        rFCArray = recentFilesData.split(",");
        return rFCArray;
    }
    */

    public static Label getLabelEnterPwd() throws Exception{
        labelEnterPwd.setVisible(false);
        labelEnterPwd.setLayoutX(449);
        labelEnterPwd.setLayoutY(290);
        labelEnterPwd.setFont(new Font(33));
        labelEnterPwd.setTextFill(Color.RED);
        labelEnterPwd.setStyle("-fx-font-weight: bold;");
        return labelEnterPwd;
    }


    public static char[] getCombinedPasswords() throws Exception{
        return combinedPasswords;
    }
    public static void setCombinedPasswords(PasswordField mpField, PasswordField ybkSecret) throws Exception{

        char[] masterPassword = mpField.getText().toCharArray();
        char[] ybkPassword = ybkSecret.getText().toCharArray();

        StringBuilder sb = new StringBuilder(128);
        sb.append(masterPassword);
        sb.append(ybkPassword);
        combinedPasswords= sb.toString().toCharArray();
    }
}