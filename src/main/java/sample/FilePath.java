package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;


public final class FilePath {

    private static ObservableList<String> DBFILESLIST = FXCollections.observableArrayList();
    // An ObservableList of Strings containing the database directories
    private static String DATBASEFILEPATH;
    private static String SELECTEDDIRECTORYPATH;
    private static String DEFAULTHPATH;
    private static String DBFILEDIR;
    private FilePath(){}  // Private constructor to prevent instantiation

    public static String getCurrentDBdir() {
        // The path of the current database file that the user has opened
        return FilePath.DATBASEFILEPATH;
    }
    public static void setCurrentDBdir(String databaseFilePath) {
        FilePath.DATBASEFILEPATH = databaseFilePath;
    }
    public static String getDBFilesListDir() throws Exception{
        // The directory of the file containing a serialized ArrayList of all the database files
        FilePath.DBFILEDIR = getDefaultDir() +"/DatabaseList.txt";
        return FilePath.DBFILEDIR;
    }

    public static ObservableList<String> getDbFilesList() {
        return DBFILESLIST;
    }

    public static String getSelectedDir() {
        // The path of the selected database
        return FilePath.SELECTEDDIRECTORYPATH;
    }
    public static void setSelectedDir(String selectedDir) {
        FilePath.SELECTEDDIRECTORYPATH = selectedDir;
    }


    public static String getDefaultDir() {
        DEFAULTHPATH = System.getProperty("user.dir") + "/resources/sample/databases";
        return DEFAULTHPATH;
    }



}