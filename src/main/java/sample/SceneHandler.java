package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

public class SceneHandler {

    MasterPwdGui masterPwdGui = new MasterPwdGui();
    TextField fileNameField = new TextField("");

    void newDBdialog(Button btn) throws Exception {

        masterPwdGui.newMasterPwd();
        masterPwdGui.dialog.setTitle("Creating new database");

        masterPwdGui.dialog.getDialogPane().setContent(masterPwdGui.grid);

        fileNameField.setPromptText("File name...");

        Label fileLabel = new Label("Enter new File name:");

        masterPwdGui.grid.addRow(0, fileLabel, fileNameField);

        Platform.runLater(() -> fileNameField.requestFocus());
        final Button btnOk = (Button) masterPwdGui.dialog.getDialogPane().lookupButton(ButtonType.OK);
        btnOk.addEventFilter(
                ActionEvent.ACTION,
                event -> {
                    // Checks if conditions are fulfilled

                    if (fileNameField.getText().length() == 0 || masterPwdGui.userCredential.getText().length() == 0
                            || masterPwdGui.confirmUserCredential.getText().length() == 0 ) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Information Dialog");
                        alert.setHeaderText(null);
                        alert.setContentText("Please fill all fields!");
                        alert.showAndWait();
                        event.consume();
                        return ;
                    }
                    if (!Authentication.validateUserCredentials(masterPwdGui.userCredential.getText(), masterPwdGui.confirmUserCredential.getText())) {
                        // If the conditions are not fulfilled, the event is consumed
                        // to prevent the dialog from closing when clicking OK
                        event.consume();
                        return ;
                    }
                }
        );
        masterPwdGui.dialog.setResultConverter(dialogButton -> {
            try {
                if (dialogButton == ButtonType.OK) {
                    Secrets.setUserCredential(masterPwdGui.userCredential.getText().toCharArray());
                    FilePath.setCurrentDBdir(fileNameField.getText());
                    newScene(btn);
                    FilePath.getDbFilesList().add(FilePath.getCurrentDBdir()); // Updates the Database FileList ObservableList
                    FileUtils.write(FilePath.getDBFilesListDir(), "".getBytes(StandardCharsets.UTF_8)); // Creates a new file
                    Serialization.dbFileListSerialize(FilePath.getDbFilesList(), Paths.get(FilePath.getDBFilesListDir()));
                    //Casts ObservableList to ArrayList, Serializes ArrayList & stores the ArrayList
                }
            } catch (Exception E) {
            }
            return null;
        });

        masterPwdGui.dialog.showAndWait();

    }

    boolean newScene(Button btnCreateDB) throws Exception {

        FilePath.setSelectedDir(FilePath.getDefaultDir() + "\\" + FilePath.getCurrentDBdir() + "\\");

        new File(FilePath.getSelectedDir()).mkdir();
        FilePath.setCurrentDBdir(FilePath.getSelectedDir() + FilePath.getCurrentDBdir() + ".txt");

        Parent root = FXMLLoader.load(Main.class.getResource("Main-Operations/MainOperations.fxml"));

        Stage entryWindow = (Stage) btnCreateDB.getScene().getWindow();

        entryWindow.setScene(new Scene(root));

        return true;
    }


    static void stageLockDB(Button btnSignOut) throws Exception {
        Parent root = FXMLLoader.load(Main.class.getResource("login/login.fxml"));
        Stage stage = (Stage) btnSignOut.getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    public boolean openDB() throws Exception {
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("KeyBine File (*.txt)", "*.txt");

        fileChooser.getExtensionFilters().add(extFilter);

        Stage anotherStage = new Stage();
        fileChooser.setInitialDirectory(new File(FilePath.getDefaultDir()));
        File file = fileChooser.showOpenDialog(anotherStage);
        if (file == null) {

            return false;
        }
        // button  set on action
        FilePath.setCurrentDBdir(file.getAbsolutePath());
        FilePath.setSelectedDir(file.getAbsoluteFile().getParent() + "\\");

        return true;
    }

}