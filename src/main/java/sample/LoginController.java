package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;


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
    private Label labelEnterPwd1;

    @FXML
    private Menu menuRecent;
    @FXML
    private Tab tabLogin;
    @FXML
    private  Button  btnYubikey;
  @FXML private ImageView imgOpen;
    @FXML private ImageView imgLocked;
    @FXML
    private AnchorPane anchorPane;

    public static Label labelEnterPwd = new Label("Please Enter Password!");

    private ObservableList<Entry> entryData = FXCollections.observableArrayList();

    public static String defaultPath = System.getProperty("user.dir") + "/resources/sample/passwords";
    public static String passwordFilePath; // filepath to the password file currently in use
    public static String selectedDirectoryPath;
    public static String recentFiles = defaultPath+"/RecentFiles.txt";
    public static char[] combinedPasswords;
    public static String colm;
    @FXML
    void login(ActionEvent event) throws Exception {

        DatabaseHandler databaseHandler = new DatabaseHandler();
        if (databaseHandler.loginAuthentication(mpField, ybkSecret, btnSignIn ) == false) {
            return;
        }

    }

    @FXML
    void newDB(ActionEvent event) throws Exception {

       labelEnterPwd.setVisible(false);
        DatabaseHandler databaseHandler = new DatabaseHandler();
        databaseHandler.newDBdialog(btnCreateDB);


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
        labelEnterPwd.setVisible(false);
        labelEnterPwd.setLayoutX(449);
        labelEnterPwd.setLayoutY(260);
        labelEnterPwd.setFont(new Font(33));
        labelEnterPwd.setTextFill(Color.RED);
        labelEnterPwd.setStyle("-fx-font-weight: bold;");
        anchorPane.getChildren().add(labelEnterPwd);
        btnYubikey.setStyle(   "-fx-background-radius: 5em; "
        );
        btnYubikey.setOnAction(e-> {
                    if(!imgOpen.isVisible())
                    {
                     
                            imgOpen.setVisible(true);
                            imgLocked.setVisible(false);
                        return;
                    }
                      imgOpen.setVisible(false);
                    imgLocked.setVisible(true);



        }

                );


        new FileOutputStream(recentFiles, true).close();
        String recentFilesString = new String(FileUtils.readAllBytes(recentFiles));
         String[] rFSArray = recentFilesString.split(",");
         passwordFilePath = rFSArray[0];
          colm ="yes";
        recentFileLabel.setText(passwordFilePath );
        DatabaseHandler databaseHandler = new DatabaseHandler();


      databaseHandler.createMenuItems(menuRecent, labelEnterPwd);

        selectedDirectoryPath = new File(passwordFilePath).getAbsoluteFile().getParent()+"\\";

    }

    }


