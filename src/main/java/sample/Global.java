package sample;

import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.File;
import java.io.FileOutputStream;

public final class Global {

    private static String passwordFilePath;

    static {
        try {
            passwordFilePath = Global.getRFCArray()[0];
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // getRFCArray() throws an exception, so the exception has to be caught everytime the function is invoked.

    private static  String selectedDirectoryPath =  new File(passwordFilePath).getAbsoluteFile().getParent()+"\\";
     private static String defaultPath ;
    private static  String recentFilesDir ;
    private static char[] combinedPasswords;
    private static String recentFilesContent ;
    private static String []  rFCArray ;
    private  static Label labelEnterPwd = new Label("Please Enter Password!");
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

    public static String getRecentFilesContent() {
        return recentFilesContent;
    }
    public static void setRecentFilesContent(String recentFileContent) {
        Global.recentFilesContent = recentFileContent;
    }

    public static String getRecentFilesDir() throws Exception{
        recentFilesDir = getDefaultDir() +"/RecentFiles.txt";
        return recentFilesDir;
    }

    public static String getDefaultDir() {
        defaultPath  = System.getProperty("user.dir") + "/resources/sample/passwords";
        return defaultPath;
    }

    public static String [] getRFCArray() throws Exception{
        recentFilesContent = new String(FileUtils.readAllBytes(getRecentFilesDir())) ;
        rFCArray = recentFilesContent.split(",");
        return rFCArray;
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

   /* public static Label getLabelRecentFiles() throws Exception{
        labelRecentFile.setLayoutX(151);
        labelRecentFile.setLayoutY(39);
        labelRecentFile.setFont(new Font(27));
        labelRecentFile.setTextFill(Color.TURQUOISE);
        labelRecentFile.setStyle("-fx-font-weight: bold;");
        return labelRecentFile;
    }

    public static void setLabelRecentFiles(String passwordFilePath) throws Exception{
        labelRecentFile.setText(passwordFilePath );
    }
*/
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