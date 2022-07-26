package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;


public final class Files {

    private static ObservableList<String> recentFilesData = FXCollections.observableArrayList();

    private static String passwordFilePath;
    private static String SELECTEDDIRECTORYPATH;//=
    private static String DEFAULTHPATH;
    private static String RECENTFILESDIR;

    private  static Label labelRecentFile = new Label();
    private Files(){}  // Private constructor to prevent instantiation

    public static String getPasswordFilePath() {
        return passwordFilePath;
    }

    public static void setPasswordFilePath(String passwordFilePath) {

        Files.passwordFilePath = passwordFilePath;
        labelRecentFile.setText(passwordFilePath );
    }
    public static String getSelectedDirectoryPath() {
        return SELECTEDDIRECTORYPATH;
    }
    public static void setSelectedDirectoryPath(String selectedDirectoryPath) {
        Files.SELECTEDDIRECTORYPATH = selectedDirectoryPath;
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
        DEFAULTHPATH = System.getProperty("user.dir") + "/resources/sample/databases";
        return DEFAULTHPATH;
    }



}