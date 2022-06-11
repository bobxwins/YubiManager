package sample;

import javafx.application.Platform;
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
    void newDBdialog(Button btn) throws Exception {

     pmgui.setPwdGrid();
     pmgui.dialog.setTitle("Creating new database");

     pmgui.dialog.getDialogPane().setContent(pmgui.grid);

        TextField fileNameField = new TextField("");
        fileNameField.setPromptText("File name...");

        Label fileLabel = new Label("Enter new File name:");

        pmgui.grid.addRow(0, fileLabel,fileNameField);

        Platform.runLater(() -> fileNameField.requestFocus());

        pmgui.dialog.setResultConverter(dialogButton -> {
            try {
                if (dialogButton == ButtonType.OK) {
                    if (! pmgui.manualPwdDialog.getText().equals( pmgui.confirmPwdDialog.getText())) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Information Dialog");
                        alert.setHeaderText(null);
                        alert.setContentText("Passwords do not match!");
                        alert.showAndWait();
                        return null;
                    }
                    if (fileNameField.getText().length() == 0 ||  pmgui.manualPwdDialog.getText().length() == 0
                            ||  pmgui.confirmPwdDialog.getText().length() == 0 ||  pmgui.ybKeyPwdDialog.getText().length() == 0) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Information Dialog");
                        alert.setHeaderText(null);
                        alert.setContentText("Please fill all fields!");
                        alert.showAndWait();

                        return null;
                    }

                    Global.setCombinedPasswords( pmgui.manualPwdDialog, pmgui.ybKeyPwdDialog);
               //     SymmetricKey.setSecretKey(Global.getCombinedPasswords());

                    Global.setPasswordFilePath(fileNameField.getText());

                    newScene(btn);
                }
            }
            catch (Exception E) {

            }

            return null;
        });

        pmgui.dialog.showAndWait();

        Global.getRecentFilesData().add(Global.getPasswordFilePath());
        if (Global.getRecentFilesData().get(0) != null) {
            FileUtils.write(Global.getRecentFilesDir(), "".getBytes(StandardCharsets.UTF_8));
            // empties the file, or generates an empty file if it doesn't exist
            SerializedObject.writeObservableList(Global.getRecentFilesData(), Paths.get(Global.getRecentFilesDir()));
        }
    }

    boolean newScene (Button btnCreateDB ) throws Exception
    {

        DirectoryChooser directoryChooser = new DirectoryChooser();
        Stage anotherStage = new Stage();

        directoryChooser.setInitialDirectory(new File(Global.getDefaultDir()));
        File selectedDirectory = directoryChooser.showDialog(anotherStage);

        if (selectedDirectory != null) {
            Global.setSelectedDirectoryPath(selectedDirectory.getAbsolutePath()+"\\"+Global.getPasswordFilePath()+"\\");

            new File( Global.getSelectedDirectoryPath()).mkdir();
            Global.setPasswordFilePath( Global.getSelectedDirectoryPath()+Global.getPasswordFilePath()+".aes");
            FileUtils.write( Global.getPasswordFilePath(),"".getBytes(StandardCharsets.UTF_8));

        }
        else {
            return false;
        }
        Parent root = FXMLLoader.load(Main.class.getResource("PMAuth/pmlayerAuthenticated.fxml"));

        Stage entryWindow = (Stage) btnCreateDB.getScene().getWindow();

        entryWindow.setScene(new Scene(root));

        return true;
    }


    boolean loginAuthentication (PasswordField mpField, PasswordField ybkSecret,Button btnSignIn ) throws  Exception {

        Global.setCombinedPasswords(mpField,ybkSecret);
       // SymmetricKey.setSecretKey(Global.getCombinedPasswords());

        byte[] input = FileUtils.readAllBytes(Global.getPasswordFilePath());
        DecryptFile decryptFile = new DecryptFile();

        if (SerializedObject.readFileObservableList(decryptFile.Decryption(input)) != null &&
                SerializedObject.readFileObservableList(decryptFile.Decryption(input)).isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Login failed! Wrong Password!");
            alert.showAndWait();
            return false;
        }
        Parent root = FXMLLoader.load(Main.class.getResource("PMAuth/pmlayerAuthenticated.fxml"));

        Stage stage = (Stage) btnSignIn.getScene().getWindow();
        stage.setScene(new Scene(root));
        return true;

    }

    public  void createMenuItems(Menu menuRecent,Label label) throws Exception {


        for (int i = 0; i < Global.getRecentFilesData().size(); i++) {
            MenuItem menuItems = new MenuItem(Global.getRecentFilesData().get(i));

            menuRecent.getItems().addAll(menuItems);

            menuItems.setOnAction(e ->
            {
                Global.setPasswordFilePath( menuItems.getText());
                Global.setSelectedDirectoryPath( Paths.get(Global.getPasswordFilePath()).getParent()+"\\");

                label.setVisible(true);

            });
        }

    }
    static void stageFullScreen(Button btnSignOut) throws Exception
    {
        Parent root = FXMLLoader.load(Main.class.getResource("login/login.fxml"));
        Stage stage= (Stage)btnSignOut.getScene().getWindow();
        stage.setScene(new Scene(root));

    }

    public boolean openDB() throws Exception {
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("AES File (*.aes)", "*.aes");

        fileChooser.getExtensionFilters().add(extFilter);

        Stage anotherStage = new Stage();
        fileChooser.setInitialDirectory(new File(Global.getDefaultDir()));
        File file = fileChooser.showOpenDialog(anotherStage);
        if (file == null) {

            return false;
        }
        // button  set on action
        Global.setPasswordFilePath( file.getAbsolutePath());

        Global.setSelectedDirectoryPath(  file.getAbsoluteFile().getParent() + "\\");

        return true;
    }


}
