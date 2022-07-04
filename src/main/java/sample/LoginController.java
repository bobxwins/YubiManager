package sample;

import javafx.animation.PauseTransition;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Base64;

public class LoginController {

    @FXML
    private Button btnSignIn;

    @FXML
    private Button btnCreateDB;

    @FXML
    private Button btnLogo;
    @FXML
    private ImageView imgLocked;
    @FXML
    private ImageView imgUnLocked;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private TableColumn<String, String> recent;

    @FXML
    private TableView<String> recentFilesTable;

    @FXML
    void login(ActionEvent event) throws Exception {

        SceneHandler sceneHandler = new SceneHandler();
        if (sceneHandler.loginAuthentication(btnSignIn) == false) {

            return;
        }
    }


    @FXML
    void newDB(ActionEvent event) throws Exception {

        SceneHandler sceneHandler = new SceneHandler();
        sceneHandler.newDBdialog(btnCreateDB);
    }

    @FXML
    void newYbkPwd(ActionEvent event) throws Exception {
        YubiKeyPwdHandler.generateYbkPassword();


    }

    @FXML
    void setYbkPwd(ActionEvent event) throws Exception {
        YubiKeyPwdHandler.manualYbkPwd();

    }

    @FXML
    void openDB(ActionEvent event) throws Exception {

     SceneHandler sceneHandler = new SceneHandler();
      sceneHandler.openDB()  ;

        if (sceneHandler.loginAuthentication(btnSignIn) == false) {

            return;
        }

    }


    @FXML
    void deleteRow(ActionEvent event) throws Exception {
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
        if ((FileUtils.readAllBytes(Global.getRecentFilesDir()).length) != 0) {
            Global.getRecentFilesData().addAll(SerializedObject.readFileObservableList(FileUtils.readAllBytes(Global.getRecentFilesDir())));

            String defaultFile = recentFilesTable.getItems().get(0);
            // sets the default RecentFile to the first element
            Global.setPasswordFilePath(defaultFile);
            Global.setSelectedDirectoryPath(Paths.get(Global.getPasswordFilePath()).getParent() + "\\");
        }


        recent.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()));
        recentFilesTable.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    try {
                        String selectedItem = recentFilesTable.getSelectionModel().getSelectedItem();

                        if (selectedItem != null) {

                            Global.setPasswordFilePath(selectedItem);
                            Global.setSelectedDirectoryPath(Paths.get(Global.getPasswordFilePath()).getParent() + "\\");

                        }

                    } catch (Exception E) {

                    }
                });

        btnLogo.setStyle("-fx-background-radius: 5em; "
        );
        btnLogo.setOnAction(e -> {

                    if (!imgLocked.isVisible()) {
                        imgLocked.setVisible(true);
                        imgUnLocked.setVisible(false);
                        return;
                    }
                    imgLocked.setVisible(false);
                    imgUnLocked.setVisible(true);

                }

        );
        recentFilesTable.setOnMouseClicked((MouseEvent event) -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                // Event that listens to if the mouse has been double clicked
                SceneHandler sceneHandler = new SceneHandler();
                try {
                    if (sceneHandler.loginAuthentication(btnSignIn) == false) {

                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
