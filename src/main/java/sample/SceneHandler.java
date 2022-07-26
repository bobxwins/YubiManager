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

        masterPwdGui.setMasterPwdGui();
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

                    if (fileNameField.getText().length() == 0 || masterPwdGui.manualPwdDField.getText().length() == 0
                            || masterPwdGui.confirmPwdField.getText().length() == 0 ) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Information Dialog");
                        alert.setHeaderText(null);
                        alert.setContentText("Please fill all fields!");
                        alert.showAndWait();
                        event.consume();
                        return ;
                    }
                    if (!Authentication.validateCredentials(masterPwdGui.manualPwdDField.getText(), masterPwdGui.confirmPwdField.getText())) {
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
                    Secrets.setManualPassword(masterPwdGui.manualPwdDField.getText().toCharArray());
                    Files.setPasswordFilePath(fileNameField.getText());
                    System.out.println(Files.getPasswordFilePath());
                    newScene(btn);

                    Files.getRecentFilesData().add(Files.getPasswordFilePath());
                    if (Files.getRecentFilesData().get(0) != null) {
                        FileUtils.write(Files.getRecentFilesDir(), "".getBytes(StandardCharsets.UTF_8));
                        // empties the file, or generates an empty file if it doesn't exist
                        SerializedObject.writeArrayList(Files.getRecentFilesData(), Paths.get(Files.getRecentFilesDir()));

                    }

                }
            } catch (Exception E) {

            }

            return null;
        });

        masterPwdGui.dialog.showAndWait();

    }

    boolean newScene(Button btnCreateDB) throws Exception {

        Files.setSelectedDirectoryPath(Files.getDefaultDir() + "\\" + Files.getPasswordFilePath() + "\\");

        new File(Files.getSelectedDirectoryPath()).mkdir();
        Files.setPasswordFilePath(Files.getSelectedDirectoryPath() + Files.getPasswordFilePath() + ".txt");

        Parent root = FXMLLoader.load(Main.class.getResource("authenticated/authenticated.fxml"));

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
        fileChooser.setInitialDirectory(new File(Files.getDefaultDir()));
        File file = fileChooser.showOpenDialog(anotherStage);
        if (file == null) {

            return false;
        }
        // button  set on action
        Files.setPasswordFilePath(file.getAbsolutePath());

        Files.setSelectedDirectoryPath(file.getAbsoluteFile().getParent() + "\\");

        return true;
    }

}