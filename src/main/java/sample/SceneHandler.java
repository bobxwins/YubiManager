package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

public class SceneHandler {

    PMGUI pmgui = new PMGUI();
      TextField fileNameField = new TextField("");

    void newDBdialog(Button btn) throws Exception {

        pmgui.setPwdGrid();
        pmgui.dialog.setTitle("Creating new database");

        pmgui.dialog.getDialogPane().setContent(pmgui.grid);

        fileNameField.setPromptText("File name...");

        Label fileLabel = new Label("Enter new File name:");

        pmgui.grid.addRow(0, fileLabel, fileNameField);

        Platform.runLater(() -> fileNameField.requestFocus());
        final Button btnOk = (Button) pmgui.dialog.getDialogPane().lookupButton(ButtonType.OK);
        btnOk.addEventFilter(
                ActionEvent.ACTION,
                event -> {
                    // Checks if conditions are fulfilled

                    if (fileNameField.getText().length() == 0 || pmgui.manualPwdDialog.getText().length() == 0
                            || pmgui.confirmPwdDialog.getText().length() == 0 || pmgui.sKeyPwdDialog.getText().length() == 0) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Information Dialog");
                        alert.setHeaderText(null);
                        alert.setContentText("Please fill all fields!");
                        alert.showAndWait();
                        event.consume();
                        return ;
                    }
                    if (!Authentication.validateNewPwd(pmgui.manualPwdDialog.getText(),pmgui.confirmPwdDialog.getText(),pmgui.sKeyPwdDialog.getText())) {
                        // If the conditions are not fulfilled, the event is consumed
                        // to prevent the dialog from closing when clicking OK
                        event.consume();
                        return ;
                    }
                }
        );

        pmgui.dialog.setResultConverter(dialogButton -> {
            try {
                if (dialogButton == ButtonType.OK) {

                    Secrets.setCombinedPasswords(pmgui.manualPwdDialog, pmgui.sKeyPwdDialog);
                    Global.setPasswordFilePath(fileNameField.getText());
                    newScene(btn);

                    Global.getRecentFilesData().add(Global.getPasswordFilePath());
                    if (Global.getRecentFilesData().get(0) != null) {
                        FileUtils.write(Global.getRecentFilesDir(), "".getBytes(StandardCharsets.UTF_8));
                        // empties the file, or generates an empty file if it doesn't exist
                        SerializedObject.writeArrayList(Global.getRecentFilesData(), Paths.get(Global.getRecentFilesDir()));

                    }

                }
            } catch (Exception E) {

            }

            return null;
        });

        pmgui.dialog.showAndWait();

    }

    boolean newScene(Button btnCreateDB) throws Exception {

        Global.setSelectedDirectoryPath(Global.getDefaultDir() + "\\" + Global.getPasswordFilePath() + "\\");

        new File(Global.getSelectedDirectoryPath()).mkdir();
        Global.setPasswordFilePath(Global.getSelectedDirectoryPath() + Global.getPasswordFilePath() + ".txt");

        Parent root = FXMLLoader.load(Main.class.getResource("PMAuth/pmlayerAuthenticated.fxml"));

        Stage entryWindow = (Stage) btnCreateDB.getScene().getWindow();

        entryWindow.setScene(new Scene(root));

        return true;
    }


    static void stageFullScreen(Button btnSignOut) throws Exception {

        Parent root = FXMLLoader.load(Main.class.getResource("login/login.fxml"));
        Stage stage = (Stage) btnSignOut.getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    public boolean openDB() throws Exception {
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("KeyBine File (*.txt)", "*.txt");

        fileChooser.getExtensionFilters().add(extFilter);

        Stage anotherStage = new Stage();
        fileChooser.setInitialDirectory(new File(Global.getDefaultDir()));
        File file = fileChooser.showOpenDialog(anotherStage);
        if (file == null) {

            return false;
        }
        // button  set on action
        Global.setPasswordFilePath(file.getAbsolutePath());

        Global.setSelectedDirectoryPath(file.getAbsoluteFile().getParent() + "\\");

        return true;
    }

}