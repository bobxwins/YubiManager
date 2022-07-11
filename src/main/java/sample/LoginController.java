package sample;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;

import java.io.File;
import java.nio.file.Paths;
import java.util.Optional;

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

        Authentication authentication = new Authentication ();
        if (authentication.loginAuthentication(btnSignIn) == false) {
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
  Authentication authentication = new Authentication ();
        if (authentication.loginAuthentication(btnSignIn) == false) {

            return;
        }

    }


    @FXML
    void deleteRow(ActionEvent event) throws Exception {
        String selectedItem = recentFilesTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Warning, this will permanently delete all your passwords for this database");
            alert.setContentText("Are you sure you want to proceed?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {

                Global.getRecentFilesData().remove(selectedItem);
                //updates the recentFile text file, after deleting the selected table item
                File deleteFile = new File(Global.getPasswordFilePath()).getAbsoluteFile().getParentFile();
                FileHandler fileHandler = new FileHandler();
                fileHandler.deleteDir(deleteFile);
                SerializedObject.writeArrayList(Global.getRecentFilesData(), Paths.get(Global.getRecentFilesDir()));
            }
        }

    }
    @FXML
    private void helpInfo() throws  Exception
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setContentText("If you struggle a lot with creating and remembering a unique and secure passwords for each of your accounts, consider using KeyBine today!"
                 );
        alert.showAndWait();
    }

    @FXML
    private void initialize() throws Exception {
        recentFilesTable.setPlaceholder(new Label("0 databases available. Click the Create New database button to get started!"));
        recentFilesTable.setItems(Global.getRecentFilesData());
        recentFilesTable.getItems().clear();
       FileHandler.recentFileExists(recentFilesTable);
        recent.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()));
        recentFilesTable.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    try {
                        String selectedItem = recentFilesTable.getSelectionModel().getSelectedItem();

                        if (selectedItem != null) {
                            Global.setPasswordFilePath(selectedItem);
                            Global.setSelectedDirectoryPath(Paths.get(Global.getPasswordFilePath()).getParent() + "\\");
                            recentFilesTable.setOnMouseClicked((MouseEvent event) -> {
                                if (selectedItem != null && event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                                    // Event that listens to if the mouse has been double clicked
                                    Authentication authentication = new Authentication();
                                    try {
                                        if (authentication.loginAuthentication(btnSignIn) == false) {

                                            return;
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

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
    }

}