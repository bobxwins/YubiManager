package sample;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.*;


import java.io.File;

import java.io.FileOutputStream;



public class LoginController {

    @FXML
    private Button btnSignIn;

    @FXML
    private Button btnCreateDB;

    @FXML
    private PasswordField mpField;

    @FXML
    private PasswordField ybkSecret;

    @FXML
    private Label recentFileLabel ;
    @FXML
    private Label labelEnterPwd;

    @FXML
    private Menu menuRecent;
    @FXML
    private Tab tabLogin;

     //tabPane.getSlectionModel().select(0);
    private ObservableList<Entry> entryData = FXCollections.observableArrayList();

    public static String defaultPath = System.getProperty("user.dir") + "/resources/sample/passwords";
    public static String passwordFilePath; // filepath to the password file currently in use
    public static String selectedDirectoryPath;
    public static String recentFiles = defaultPath+"/RecentFiles.txt";
    public static char[] combinedPasswords;

    @FXML
    void login(ActionEvent event) throws Exception {

        DatabaseHandler databaseHandler = new DatabaseHandler();
         if (databaseHandler.loginAuthentication(mpField,ybkSecret,btnSignIn,tabLogin) == false)
              {
              return;
                  }
    }



    @FXML
    void newDB(ActionEvent event) throws Exception {
        labelEnterPwd.setVisible(false);
        DatabaseHandler databaseHandler = new DatabaseHandler();
        databaseHandler.dialog(btnCreateDB);


    }

    @FXML
    void openDB(ActionEvent event) throws Exception {

        DatabaseHandler databaseHandler = new DatabaseHandler();
        if (databaseHandler.openDB()==false)
        {
            return;
        }
            labelEnterPwd.setVisible(true);

        }


    @FXML
    private void initialize() throws Exception {

        new FileOutputStream(recentFiles, true).close();
        String recentFilesString = new String(FileUtils.readAllBytes(recentFiles));
         String[] rFSArray = recentFilesString.split(",");
       passwordFilePath = rFSArray[0];

        recentFileLabel.setText(passwordFilePath );
        DatabaseHandler databaseHandler = new DatabaseHandler();
        databaseHandler.createMenuItems(menuRecent,labelEnterPwd);

        selectedDirectoryPath = new File(passwordFilePath).getAbsoluteFile().getParent()+"\\";

    }

    }


