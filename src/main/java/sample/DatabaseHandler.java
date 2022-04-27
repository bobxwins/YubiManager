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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

import static sample.LoginController.defaultPath;

public class DatabaseHandler {

    PasswordField masterPasswordField = new PasswordField();
    PasswordField confirmPasswordField = new PasswordField();
    PasswordField yubikeyPasswordField = new PasswordField();
    Label masterPasswordLabel = new Label ("Master Password");
    Dialog<Pair<String, String>> dialog = new Dialog<>();
    GridPane grid = new GridPane();

   void updateRecentFileString()

    {

        String recentFilesString = "," + new String(FileUtils.readAllBytes(LoginController.recentFiles));

        String[] rFSArray = recentFilesString.split(",");
        boolean contains = Stream.of(rFSArray).anyMatch(x -> x.equals(LoginController.passwordFilePath));
        if (  contains == true) {

            return;
        }

        String combinedRecentString = LoginController.passwordFilePath + recentFilesString;
        System.out.println("the combined string is " + combinedRecentString);
        FileUtils.write(LoginController.recentFiles, combinedRecentString.getBytes(StandardCharsets.UTF_8));
    }


    public boolean openDB() {
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XLS File (*.xlsx)", "*.xlsx");

        fileChooser.getExtensionFilters().add(extFilter);

        Stage anotherStage = new Stage();
        fileChooser.setInitialDirectory(new File(defaultPath));
        File file = fileChooser.showOpenDialog(anotherStage);
        if (file == null) {

            return false;
        }
        LoginController.passwordFilePath = file.getAbsolutePath();
        System.out.println("the password file path is :"+ LoginController.passwordFilePath);
        LoginController.selectedDirectoryPath = file.getAbsoluteFile().getParent() + "\\";
        System.out.println("the selectedDirectoryPath  is :"+ LoginController.selectedDirectoryPath);
        updateRecentFileString();
      return true;
}




    boolean loginAuthentication (PasswordField mpField, PasswordField ybkSecret,Button btnSignIn, Tab tab) throws  Exception {

            char[] masterPassword = mpField.getText().toCharArray();
            char[] ybkPassword = ybkSecret.getText().toCharArray();

            StringBuilder sb = new StringBuilder(128);
            sb.append(masterPassword);
            sb.append(ybkPassword);
            LoginController.combinedPasswords = sb.toString().toCharArray();

            Parent root = FXMLLoader.load(Main.class.getResource("PMAuth/pmlayerAuthenticated.fxml"));

            Stage entryWindow = (Stage) btnSignIn.getScene().getWindow();

            if (ObjectIOExample.read(Paths.get(LoginController.passwordFilePath)) != null &&
                    ObjectIOExample.read(Paths.get(LoginController.passwordFilePath)).isEmpty()) {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Login failed! Wrong Password!");
                alert.showAndWait();
                return false;
            }

            entryWindow.setScene(new Scene(root));
            return true;

    }

    void newDBdialog(Button btn) {
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
                    if (fileNameField.getText().length() == 0 || masterPasswordField.getText().length() == 0 || confirmPasswordField.getText().length() == 0) {

                        return null;
                    }

                    char[] masterPassword = masterPasswordField.getText().toCharArray();
                    char[] ybkPassword = yubikeyPasswordField.getText().toCharArray();

                    StringBuilder sb = new StringBuilder(128);
                    sb.append(masterPassword);
                    sb.append(ybkPassword);

                   LoginController.combinedPasswords = sb.toString().toCharArray();

                   LoginController.passwordFilePath = fileNameField.getText();

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
        directoryChooser.setInitialDirectory(new File(defaultPath));
        File selectedDirectory = directoryChooser.showDialog(anotherStage);

        if (selectedDirectory != null) {
          LoginController.selectedDirectoryPath=selectedDirectory.getAbsolutePath()+"\\"+LoginController.passwordFilePath+"\\";

            new File(LoginController.selectedDirectoryPath).mkdir();
            LoginController.passwordFilePath =  LoginController. selectedDirectoryPath+LoginController.passwordFilePath+".xlsx";
            FileUtils.write(  LoginController.passwordFilePath,"".getBytes(StandardCharsets.UTF_8));

        }
        else {
            return false;
        }
        Parent root = FXMLLoader.load(Main.class.getResource("PMAuth/pmlayerAuthenticated.fxml"));

        Stage entryWindow = (Stage) btnCreateDB.getScene().getWindow();

        entryWindow.setScene(new Scene(root));

        return true;
    }
    public  void createMenuItems(Menu menuRecent, Label labelEnterPwd) {
        String recentFilesString = new String(FileUtils.readAllBytes(LoginController.recentFiles));

        String[] rFSArray = recentFilesString.split(",");

        for (int i = 0; i < rFSArray.length; i++) {

            MenuItem menuItems = new MenuItem(rFSArray[i]);


                menuRecent.getItems().addAll(menuItems);

                menuItems.setOnAction(e ->
                {
                    labelEnterPwd.setVisible(true);
                    LoginController.passwordFilePath = menuItems.getText();
                    String path = Paths.get(LoginController.passwordFilePath).getParent()+"\\";

                    LoginController.selectedDirectoryPath = path;

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
                if (masterPasswordField.getText().length() == 0 || confirmPasswordField.getText().length() == 0) {

                    return null;
                }

                char[] masterPassword = masterPasswordField.getText().toCharArray();
                char[] ybkPassword = yubikeyPasswordField.getText().toCharArray();

                StringBuilder sb = new StringBuilder(128);
                sb.append(masterPassword);
                sb.append(ybkPassword);

                LoginController.combinedPasswords = sb.toString().toCharArray();

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
}



