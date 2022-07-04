package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.nio.charset.StandardCharsets;


public final class Global {


    private static ObservableList OBSERVABLELIST;
    private static ObservableList<String> recentFilesData = FXCollections.observableArrayList();


    private static String passwordFilePath;
    private static String SELECTEDDIRECTORYPATH;//=
    private static String DEFAULTHPATH;
    private static String RECENTFILESDIR;

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


    static void setEntryData (ObservableList observableList) {
        Global.OBSERVABLELIST =observableList;
    }

}