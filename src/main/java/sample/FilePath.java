package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;


public final class FilePath {

    private static ObservableList<String> recentFilesDir = FXCollections.observableArrayList();

    private static String passwordFilePath;
    private static String SELECTEDDIRECTORYPATH;//=
    private static String DEFAULTHPATH;
    private static String RECENTFILESDIR;

    private  static Label labelRecentFile = new Label();
    private FilePath(){}  // Private constructor to prevent instantiation

    public static String getPasswordFilePath() {
        return passwordFilePath;
    }

    public static void setPasswordFilePath(String passwordFilePath) {

        FilePath.passwordFilePath = passwordFilePath;
        labelRecentFile.setText(passwordFilePath );
    }
    public static String getSelectedDirectoryPath() {
        return SELECTEDDIRECTORYPATH;
    }
    public static void setSelectedDirectoryPath(String selectedDirectoryPath) {
        FilePath.SELECTEDDIRECTORYPATH = selectedDirectoryPath;
    }
// save as
    public static ObservableList<String> getRecentFilesDir() {
        return recentFilesDir;
    }


    public static String getRecentFileDir() throws Exception{
        RECENTFILESDIR = getDefaultDir() +"/RecentFiles.txt";
        return RECENTFILESDIR;
    }

    public static String getDefaultDir() {
        DEFAULTHPATH = System.getProperty("user.dir") + "/resources/sample/databases";
        return DEFAULTHPATH;
    }



}