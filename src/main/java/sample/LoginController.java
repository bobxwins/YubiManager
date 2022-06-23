package sample;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.nio.file.Paths;

public class LoginController {

    @FXML private Button btnSignIn;

    @FXML private Button btnCreateDB;

    @FXML private  Button btnLogo;

    @FXML private PasswordField mpField;

    @FXML
    private PasswordField sKeyField;

    @FXML private Menu menuRecent;

    @FXML private ImageView imgLocked;
    @FXML private ImageView imgUnLocked;
    @FXML private AnchorPane anchorPane;


    @FXML private TableColumn<String,String> recent;

    @FXML private TableView<String> recentFilesTable;

    @FXML void login(ActionEvent event) throws Exception {

        SceneHandler sceneHandler = new SceneHandler();
        if (sceneHandler.loginAuthentication(mpField, sKeyField, btnSignIn ) == false) {

            return;
        }

    }

    @FXML void newDB(ActionEvent event) throws Exception {

        Global.getLabelEnterPwd().setVisible(false);
        SceneHandler sceneHandler = new SceneHandler();
        sceneHandler.newDBdialog(btnCreateDB);
        recentFilesTable.getItems().clear();

    }

    @FXML void newYbkPwd(ActionEvent event) throws Exception {
     YubiKeyPwdHandler.generateYbkPassword();


    }

    @FXML void setYbkPwd(ActionEvent event) throws Exception {
         YubiKeyPwdHandler.manualYbkPwd();

    }

    @FXML
    void openDB(ActionEvent event) throws Exception {

        SceneHandler sceneHandler = new SceneHandler();
        if (sceneHandler.openDB()==false)
        {
            return;
        }
           Global.getLabelEnterPwd().setVisible(true);

        }


        @FXML
    void deleteRow(ActionEvent event) throws  Exception {
        String selectedItem = recentFilesTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            Global.getRecentFilesData().remove(selectedItem);
            SerializedObject.writeObservableList(Global.getRecentFilesData(), Paths.get(Global.getRecentFilesDir()));
           //updates the recentFile text file, after deleting the selected table item


        }

    }



    @FXML
    private void initialize() throws Exception {

        recentFilesTable.setItems(Global.getRecentFilesData());
        recentFilesTable.getItems().clear();
        if((FileUtils.readAllBytes(Global.getRecentFilesDir()).length)!=0)
        {
            Global.getRecentFilesData().addAll(SerializedObject.readFileObservableList(FileUtils.readAllBytes(Global.getRecentFilesDir())));

            String defaultFile = recentFilesTable.getItems().get(0);
            // sets the default RecentFile to the first element
            Global.setPasswordFilePath(defaultFile);
            Global.setSelectedDirectoryPath( Paths.get(Global.getPasswordFilePath()).getParent()+"\\");
        }


         recent.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()));
         recentFilesTable.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
           try{
            String selectedItem = recentFilesTable.getSelectionModel().getSelectedItem();

                 if (selectedItem != null) {
                                Global.getLabelEnterPwd().setVisible(true);

                               Global.setPasswordFilePath(selectedItem);
                               Global.setSelectedDirectoryPath( Paths.get(Global.getPasswordFilePath()).getParent()+"\\");

                            }

                            }

                           catch (Exception E) {

                           }     });

        anchorPane.getChildren().addAll(Global.getLabelEnterPwd());
        btnLogo.setStyle(   "-fx-background-radius: 5em; "
        );
        btnLogo.setOnAction(e-> {
                    if(!imgLocked.isVisible())
                    {
                        imgLocked.setVisible(true);
                        imgUnLocked.setVisible(false);
                        return;
                    }
                    imgLocked.setVisible(false);
                    imgUnLocked.setVisible(true);

        }

                );


        SceneHandler sceneHandler = new SceneHandler();

        sceneHandler.createMenuItems(menuRecent, Global.getLabelEnterPwd());

    }

    }
