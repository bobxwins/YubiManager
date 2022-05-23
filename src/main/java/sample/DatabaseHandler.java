package sample;

import javafx.application.Platform;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;


public class DatabaseHandler {

    PasswordField masterPasswordField  = new PasswordField();
    PasswordField confirmPasswordField = new PasswordField();
    PasswordField yubikeyPasswordField = new PasswordField();
    // These are the 3 password fields generated when the user creates a new Database.

   public   Dialog<Pair<String, String>> dialog = new Dialog<>();
   public  GridPane grid = new GridPane();
   public  CheckBox checkBox;
   public  Spinner<Integer> timerSpinner;


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

        // updateRecentFileString();

      return true;
}


    boolean loginAuthentication (PasswordField mpField, PasswordField ybkSecret,Button btnSignIn ) throws  Exception {

            Global.setCombinedPasswords(mpField,ybkSecret);

            Parent root = FXMLLoader.load(Main.class.getResource("PMAuth/pmlayerAuthenticated.fxml"));

            Stage stage = (Stage) btnSignIn.getScene().getWindow();
            DecryptFile decryptFile = new DecryptFile();
            if (SerializedObject.readObservableList(decryptFile.Decryption()) != null &&
                    SerializedObject.readObservableList(decryptFile.Decryption()).isEmpty()) {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Login failed! Wrong Password!");
                alert.showAndWait();
                return false;
            }

            stage.setScene(new Scene(root));
            return true;

    }

    void newDBdialog(Button btn) throws Exception {
        dialog.setTitle("Creating new database");
        dialog.getDialogPane().setContent(grid);
        setPwdGrid();

        TextField fileNameField = new TextField("");
        fileNameField.setPromptText("File name...");

        Label fileLabel = new Label("Enter new File name:");

        grid.addRow(0, fileLabel,fileNameField);
       // grid.addRow()
        Platform.runLater(() -> fileNameField.requestFocus());

        dialog.setResultConverter(dialogButton -> {
            try {
                if (dialogButton == ButtonType.OK) {
                    if (!masterPasswordField.getText().equals(confirmPasswordField.getText())) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Information Dialog");
                        alert.setHeaderText(null);
                        alert.setContentText("Passwords do not match!");
                        alert.showAndWait();
                        return null;
                    }
                    if (fileNameField.getText().length() == 0 || masterPasswordField.getText().length() == 0
                            || confirmPasswordField.getText().length() == 0 || yubikeyPasswordField.getText().length() == 0) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Information Dialog");
                        alert.setHeaderText(null);
                        alert.setContentText("Please fill all fields!");
                        alert.showAndWait();

                        return null;
                    }

                    Global.setCombinedPasswords(masterPasswordField,yubikeyPasswordField);

                    Global.setPasswordFilePath(fileNameField.getText());

                    newScene(btn);
                }
            }
            catch (Exception E) {

            }

            return null;
        });

        dialog.showAndWait();

        Global.getRecentFilesData().add(Global.getPasswordFilePath());
        FileUtils.write(Global.getRecentFilesDir(),"".getBytes(StandardCharsets.UTF_8));
        // empties th file, or generates an empty file if it doesn't exist
      SerializedObject.writeObservableList( Global.getRecentFilesData(),Paths.get(Global.getRecentFilesDir()));


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
            Global.setPasswordFilePath(   Global.getSelectedDirectoryPath()+Global.getPasswordFilePath()+".aes");
            FileUtils.write( Global.getPasswordFilePath(),"".getBytes(StandardCharsets.UTF_8));

        }
        else {
            return false;
        }
        Parent root = FXMLLoader.load(Main.class.getResource("PMAuth/pmlayerAuthenticated.fxml"));

        Stage entryWindow = (Stage) btnCreateDB.getScene().getWindow();
       //  entryWindow.setMaximized(true);
        entryWindow.setScene(new Scene(root));

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
    void deleteDir(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                if (! Files.isSymbolicLink(f.toPath())) {
                    deleteDir(f);
                }
            }
        }
        file.delete();
    }
    void setPwdGrid()
    {

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 10, 0, 10));

        masterPasswordField.setPromptText("Master Password");

        confirmPasswordField.setPromptText("Confirm Master Password");

        yubikeyPasswordField.setPromptText("Yubikey Static Password");

        grid.add(new Label("Master Password:"), 0, 1);
        grid.add(masterPasswordField, 1, 1);

        grid.add(new Label("Confirm Password:"), 0, 2);
        grid.add(confirmPasswordField, 1, 2);

        grid.add(new Label("Yubikey Password:"), 0, 3);
        grid.add(yubikeyPasswordField, 1, 3);

    }

     void timerGrid () {

         dialog.setTitle("Setting timer");
         dialog.getDialogPane().setContent(grid);
         dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

         grid.setHgap(10);
         grid.setVgap(10);
         grid.setPadding(new Insets(0, 10, 0, 10));
         checkBox = new CheckBox("Seconds of inactivity database will be locked in: ");
         timerSpinner = (Spinner<Integer>) new Spinner(10, 999, 15);
         if (FileUtils.readAllBytes(TimerSpecs.getTimerSpecsDir()).length!=0)
         {
             TimerSpecs timerSpecs = (TimerSpecs) SerializedObject.readObject(TimerSpecs.getTimerSpecsDir());
             checkBox.setSelected(timerSpecs.getSelectedCheckBox());
           timerSpinner.getValueFactory().setValue(timerSpecs.getTimer());
         }

         timerSpinner.setPrefSize(75, 25);
         timerSpinner.setEditable(true);
         grid.add(checkBox, 0, 1);
         grid.add(timerSpinner, 1, 1);
     }



    void updatePasswords() {
    dialog.setTitle("Updating passwords");
    dialog.getDialogPane().setContent(grid);
    setPwdGrid();
    dialog.setResultConverter(dialogButton -> {
        try {
            if (dialogButton == ButtonType.OK) {
                if (!masterPasswordField.getText().equals(confirmPasswordField.getText())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Information Dialog");
                    alert.setHeaderText(null);
                    alert.setContentText("Update failed! Password did not match!");
                    alert.showAndWait();

                    return null;
                }
                if (masterPasswordField.getText().length() == 0 || confirmPasswordField.getText().length() == 0 || yubikeyPasswordField.getText().length() == 0) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Information Dialog");
                    alert.setHeaderText(null);
                    alert.setContentText("Please fill all fields!");
                    alert.showAndWait();
                   return null;
                }

                    Global.setCombinedPasswords(masterPasswordField,yubikeyPasswordField);


                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Dialog");
                    alert.setHeaderText(null);
                    alert.setContentText("Passwords succesfully updated!");
                    alert.showAndWait();
                     }
        } catch (Exception E) {


        }
        return null;
    });

    dialog.showAndWait();

}
static void stageFullScreen(Button btnSignOut) throws Exception
{
    Parent root = FXMLLoader.load(Main.class.getResource("login/login.fxml"));
    Stage stage= (Stage)btnSignOut.getScene().getWindow();
    stage.setScene(new Scene(root));

}


}



