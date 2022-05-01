package sample;

import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;


public class DatabaseHandler {
    private Timeline timer;
    PasswordField masterPasswordField  = new PasswordField();
    PasswordField confirmPasswordField = new PasswordField();
    PasswordField yubikeyPasswordField = new PasswordField();
    // These are the 3 password fields generated when the user creates a new Database.

    Dialog<Pair<String, String>> dialog = new Dialog<>();
    GridPane grid = new GridPane();

   void updateRecentFileString() throws  Exception

    {
        String updateRecentFilesContent = "," + new String(FileUtils.readAllBytes(Global.getRecentFilesDir()));

        String[] rFCArray = updateRecentFilesContent.split(",");
        boolean contains = Stream.of(rFCArray).anyMatch(x -> x.equals(Global.getPasswordFilePath()));
        if (  contains == true) {

            return;
        }

        String combinedRecentString = Global.getPasswordFilePath() + updateRecentFilesContent;
        FileUtils.write( Global.getRecentFilesDir(), combinedRecentString.getBytes(StandardCharsets.UTF_8));
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

        updateRecentFileString();

      return true;
}


    boolean loginAuthentication (PasswordField mpField, PasswordField ybkSecret,Button btnSignIn ) throws  Exception {

            Global.setCombinedPasswords(mpField,ybkSecret);

            Parent root = FXMLLoader.load(Main.class.getResource("PMAuth/pmlayerAuthenticated.fxml"));

            Stage stage = (Stage) btnSignIn.getScene().getWindow();

            if (ObjectIOExample.read(Paths.get(Global.getPasswordFilePath())) != null &&
                    ObjectIOExample.read(Paths.get(Global.getPasswordFilePath())).isEmpty()) {

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
        setGrid();

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
        updateRecentFileString();

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
        entryWindow.setMaximized(true);
        entryWindow.setScene(new Scene(root));

        return true;
    }
    public  void createMenuItems(Menu menuRecent,Label label) throws Exception {


        for (int i = 0; i < Global.getRFCArray().length; i++) {

            MenuItem menuItems = new MenuItem(Global.getRFCArray()[i]);


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
    void setGrid()
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

    void updatePasswords() {
    dialog.setTitle("Updating passwords");
    dialog.getDialogPane().setContent(grid);
    setGrid();
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
    Stage stage= (Stage) btnSignOut.getScene().getWindow();
    Screen screen = Screen.getPrimary();
    Rectangle2D bounds = screen.getVisualBounds();
    stage.setX(bounds.getMinX());
    stage.setY(bounds.getMinY());
    stage.setWidth(bounds.getWidth());
    stage.setHeight(bounds.getHeight());
    stage.setScene(new Scene(root));

}
}



