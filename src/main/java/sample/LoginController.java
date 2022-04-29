package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

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
    private Menu menuRecent;

    @FXML private  Button  btnYubikey;
    @FXML private ImageView imgOpen;
    @FXML private ImageView imgLocked;
    @FXML
    private AnchorPane anchorPane;

    @FXML
    void login(ActionEvent event) throws Exception {

        DatabaseHandler databaseHandler = new DatabaseHandler();
        if (databaseHandler.loginAuthentication(mpField, ybkSecret, btnSignIn ) == false) {
            return;
        }

    }

    @FXML
    void newDB(ActionEvent event) throws Exception {

      Global.getLabelEnterPwd().setVisible(false);
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
           Global.getLabelEnterPwd().setVisible(true);

        }


    @FXML
    private void initialize() throws Exception {
         Global.setLabelRecentFiles(Global.getPasswordFilePath());

        anchorPane.getChildren().addAll(Global.getLabelEnterPwd(), Global.getLabelRecentFiles());
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

         new FileOutputStream( Global.getRecentFilesDir(), true).close();

         Global.setRecentFilesContent(new String(FileUtils.readAllBytes( Global.getRecentFilesDir())));

        DatabaseHandler databaseHandler = new DatabaseHandler();

        databaseHandler.createMenuItems(menuRecent, Global.getLabelEnterPwd());

    }

    }


