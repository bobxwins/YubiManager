package sample;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
                    if (!validate(pmgui.manualPwdDialog.getText(),pmgui.confirmPwdDialog.getText(),pmgui.sKeyPwdDialog.getText())) {
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
           /*        if (!validate()) {
                       return null;
                   }
                   */
                    Secrets.setCombinedPasswords(pmgui.manualPwdDialog, pmgui.sKeyPwdDialog);
                    Global.setPasswordFilePath(fileNameField.getText());
                    newScene(btn);

                    Global.getRecentFilesData().add(Global.getPasswordFilePath());
                    if (Global.getRecentFilesData().get(0) != null) {
                        FileUtils.write(Global.getRecentFilesDir(), "".getBytes(StandardCharsets.UTF_8));
                        // empties the file, or generates an empty file if it doesn't exist
                        SerializedObject.writeObservableList(Global.getRecentFilesData(), Paths.get(Global.getRecentFilesDir()));

                    }

                }
            } catch (Exception E) {

            }

            return null;
        });

        pmgui.dialog.showAndWait();

    }

    boolean newScene(Button btnCreateDB) throws Exception {

        DirectoryChooser directoryChooser = new DirectoryChooser();
        Stage anotherStage = new Stage();

        directoryChooser.setInitialDirectory(new File(Global.getDefaultDir()));
        File selectedDirectory = directoryChooser.showDialog(anotherStage);

        if (selectedDirectory != null) {
            Global.setSelectedDirectoryPath(selectedDirectory.getAbsolutePath() + "\\" + Global.getPasswordFilePath() + "\\");

            new File(Global.getSelectedDirectoryPath()).mkdir();
            Global.setPasswordFilePath(Global.getSelectedDirectoryPath() + Global.getPasswordFilePath() + ".txt");

          // FileUtils.write(Global.getPasswordFilePath(), "".getBytes(StandardCharsets.UTF_8));

        } else {
            return false;
        }


        Parent root = FXMLLoader.load(Main.class.getResource("PMAuth/pmlayerAuthenticated.fxml"));

        Stage entryWindow = (Stage) btnCreateDB.getScene().getWindow();

        entryWindow.setScene(new Scene(root));

        return true;
    }


    GUI gui = new GUI();
    PasswordField mpField = new PasswordField();
    PasswordField skField = new PasswordField();

    boolean loginAuthentication(Button btnSignIn) throws Exception {
        gui.dialog(mpField, skField);
        Platform.runLater(() -> mpField.requestFocus());
        final  Button btnOk = (Button) gui.loginDialog.getDialogPane().lookupButton(ButtonType.OK);
        btnOk.addEventFilter(
                ActionEvent.ACTION,
                event -> {
                    // Checks if conditions are fulfilled

                    try {
                        Secrets.setCombinedPasswords(mpField, skField);
                        byte[] input = FileUtils.readAllBytes(Global.getPasswordFilePath());

                        Database dbSecrets = (Database) SerializedObject.readDB(input);
                        NonSecrets nonSecrets= dbSecrets.getNonSecrets();
                        DecryptFile.restoreKey();
                        String generatedHeader = Authentication.generateHmac("Global.getPasswordFilePath()",SymmetricKey.getSecretKey());
                        nonSecrets.setHeader(generatedHeader);

                        if (!Authentication.verifyHmac(generatedHeader))
                        {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Information Dialog");
                            alert.setHeaderText(null);
                            alert.setContentText("Login failed! Wrong Password!");
                            alert.showAndWait();
                            // If the conditions are not fulfilled, the event is consumed
                            // to prevent the dialog from closing
                            event.consume();
                            return;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }   );


        gui.loginDialog.setResultConverter(dialogButton -> {
            try {
                if (dialogButton == ButtonType.OK) {

                    Parent root = FXMLLoader.load(Main.class.getResource("PMAuth/pmlayerAuthenticated.fxml"));

                    Stage stage = (Stage) btnSignIn.getScene().getWindow();
                    stage.setScene(new Scene(root));


                }
            } catch (Exception E) {

            }

            return null;
        });

        gui.loginDialog.showAndWait();

        return false;
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


    boolean validate(String manualPwd,String confirmPwd, String sKeyPwd)
    {
        String combined =manualPwd+ sKeyPwd;

        boolean atleastOneSymbol = combined.matches(".*[^A-Za-z0-9]+.*");
        boolean alphanumeric=combined.matches("[a-zA-Z.0-9_]*");

        if (combined.length() < 12) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("The Master password must be at least 12 characters long!");
            alert.showAndWait();
            return false;
        }

        if (!manualPwd.equals(confirmPwd)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("The manually entered passwords do not match!");
            alert.showAndWait();
            return false;
        }


        if ( !alphanumeric ) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Master password must contain digits, uppercase, and lowercase characters!");
            alert.showAndWait();
            return false;
        }
/*
        if ( !atleastOneSymbol ) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Master password must contain special characters!");
            alert.showAndWait();
            return false;
        }
        */

        return true;
    }

}