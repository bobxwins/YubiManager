package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;

import static sample.LoginController.defaultPath;

public class DatabaseHandler {

   /* boolean pwdInput(PasswordField mpField, PasswordField ybkSecret)
    {
        char[] masterPassword = mpField.getText().toCharArray();
        char[] ybkPassword = ybkSecret.getText().toCharArray();

        Boolean comparePWDS  = Arrays.equals(masterPassword, ybkPassword);

        StringBuilder sb = new StringBuilder(128);
        sb.append(masterPassword);
        sb.append(ybkPassword);
        LoginController.combinedPasswords = sb.toString().toCharArray();
        if (comparePWDS == false)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Login failed! Passwords did not match!");
            alert.showAndWait();
            return false;
        }
        if (LoginController.combinedPasswords.length==0 )
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Please enter passwords!");
            alert.showAndWait();
            return false;
        }
        System.out.println(" comparison is_"+ comparePWDS);
        return comparePWDS; }

    */

    boolean loginAuthentication (PasswordField mpField, PasswordField ybkSecret,Button btnSignIn) throws  Exception {

        char[] masterPassword = mpField.getText().toCharArray();
        char[] ybkPassword = ybkSecret.getText().toCharArray();

        StringBuilder sb = new StringBuilder(128);
        sb.append(masterPassword);
        sb.append(ybkPassword);
        LoginController.combinedPasswords = sb.toString().toCharArray();

        Parent root = FXMLLoader.load(Main.class.getResource("PMAuth/pmlayerAuthenticated.fxml"));

        Stage entryWindow = (Stage) btnSignIn.getScene().getWindow();

        if (  ObjectIOExample.read(Paths.get(LoginController.passwordFilePath)) != null &&
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

    void dialog(Button btn) {

        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("TestName");

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(8);
        gridPane.setVgap(8);
        gridPane.setPadding(new Insets(5));

        TextField fileNameField = new TextField("");
        fileNameField.setPromptText("File name...");
        PasswordField newMpField = new PasswordField();
        newMpField.setPromptText("Master Password...");
        PasswordField confirmMPField = new PasswordField();
        confirmMPField.setPromptText("Confirm Master Password...");
        PasswordField newYbkField = new PasswordField();
        newYbkField.setPromptText("Yubikey static Password...");

        Label fileLabel = new Label("Enter new File name:");
        Label mpLabel = new Label("Enter new Master Password:");
        Label confirMPLabel = new Label("Confirm new Master Password:");
        Label ybkLabel = new Label("Enter new  Yubikey Password:");
        gridPane.addRow(0, fileLabel,fileNameField);
        gridPane.addRow(1, mpLabel,newMpField);
        gridPane.addRow(2, confirMPLabel,confirmMPField);
        gridPane.addRow(3, ybkLabel,newYbkField);


        dialog.getDialogPane().setContent(gridPane);

        Platform.runLater(() -> fileNameField.requestFocus());

        dialog.setResultConverter(dialogButton -> {
            try {
                if (dialogButton == ButtonType.OK) {
                    if (!newMpField.getText().equals(confirmMPField.getText())) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Information Dialog");
                        alert.setHeaderText(null);
                        alert.setContentText("Login failed! Wrong Password!");
                        alert.showAndWait();
                        return null;
                    }
                    if (fileNameField.getText().length() == 0 || newMpField.getText().length() == 0 || confirmMPField.getText().length() == 0) {

                        return null;
                    }

                    char[] masterPassword = newMpField.getText().toCharArray();
                    char[] ybkPassword = newYbkField.getText().toCharArray();

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

    }

    boolean newScene (Button btnCreateDB ) throws Exception
    {

        DirectoryChooser directoryChooser = new DirectoryChooser();
        Stage anotherStage = new Stage();
        directoryChooser.setInitialDirectory(new File(defaultPath));
        File selectedDirectory = directoryChooser.showDialog(anotherStage);

        if (selectedDirectory != null) {
          LoginController.  selectedDirectoryPath=selectedDirectory.getAbsolutePath()+"/"+LoginController.passwordFilePath+"\\";

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
        EntryHandler.Y = (int) (Screen.getPrimary().getBounds().getHeight() / 2) - 150;
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
                    Path path = Paths.get(LoginController.passwordFilePath).getParent();

                    LoginController.selectedDirectoryPath = path.toString();

                    EntryHandler.Y = (int) (Screen.getPrimary().getBounds().getHeight() / 2) - 150;

                });
            }

        }


}
