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
import java.util.Arrays;
import java.util.Optional;

import static sample.LoginController.defaultPath;

public class DatabaseHandler {

    boolean pwdInput(PasswordField mpField, PasswordField ybkSecret)
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



   /* boolean newScene ( ) throws Exception
    {

        DirectoryChooser directoryChooser = new DirectoryChooser();
        Stage anotherStage = new Stage();
        directoryChooser.setInitialDirectory(new File(defaultPath));
        File selectedDirectory = directoryChooser.showDialog(anotherStage);

        if (selectedDirectory != null) {
            System.out.println(selectedDirectory.getAbsolutePath());
            String path=  selectedDirectory.getAbsolutePath();
           LoginController.passwordFileName = path+"\\"+LoginController.passwordFileName+".txt";
            FileUtils.write(  LoginController.passwordFileName,"".getBytes(StandardCharsets.UTF_8))
            ;
            System.out.println("the file name is " +   LoginController.passwordFileName);

        }
        else {
            return false;
        }

        return true;
    }
    void dialog() {



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
        Label mpLabel = new Label("Enter Master Password:");
        Label confirMPLabel = new Label("Confirm Master Password:");
        Label ybkLabel = new Label("Enter Yubikey Password:");
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
                        System.out.println("zero!!");
                        return null;
                    }
                    char[] masterPassword = newMpField.getText().toCharArray();
                    char[] ybkPassword = newYbkField.getText().toCharArray();

                    StringBuilder sb = new StringBuilder(128);
                    sb.append(masterPassword);
                    sb.append(ybkPassword);

                    combinedPasswords = sb.toString().toCharArray();

                     LoginController.passwordFileName = fileNameField.getText();
                    System.out.println("confrims?" + confirmMPField.getText());

                    newScene();
                }
            }
            catch (Exception E) {

            }

            return null; // return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(pair -> {
            //  System.out.println("From=" + pair.getKey() + ", To=" + pair.getValue());
        });

    }
    */

}
