package sample;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.io.File;
import java.nio.file.Files;
import java.security.SecureRandom;
import java.util.Base64;

public class PMGUI {

     public   PasswordField manualPwdDialog = new PasswordField();
     public    PasswordField confirmPwdDialog = new PasswordField();
     public    PasswordField sKeyPwdDialog = new PasswordField();
    // These are the 3 password fields generated when the user creates a new Database.

   public    Dialog<Pair<String, String>> dialog = new Dialog<>();
   public   GridPane grid = new GridPane();

    void updatePasswords() {
        dialog.setTitle("Updating passwords");
        dialog.getDialogPane().setContent(grid);
        setPwdGrid();
        final Button btnOk = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        btnOk.addEventFilter(
                ActionEvent.ACTION,
                event -> {
                    // Checks if conditions are fulfilled
                    SceneHandler sceneHandler = new SceneHandler();
                    if (!sceneHandler.validate(manualPwdDialog.getText(),confirmPwdDialog.getText(),sKeyPwdDialog.getText())) {
                        // If the conditions are not fulfilled, the event is consumed
                        // to prevent the dialog from closing
                        event.consume();
                        return ;
                    }
                }
        );


        dialog.setResultConverter(dialogButton -> {
            try {
                if (dialogButton == ButtonType.OK) {

                    byte[] nonSecretsBytes = FileUtils.readAllBytes(Global.getPasswordFilePath());
                    Database dbSecrets = (Database) SerializedObject.readDB(nonSecretsBytes);
                    NonSecrets nonSecrets= dbSecrets.getNonSecrets();

                    Secrets.setCombinedPasswords(manualPwdDialog, sKeyPwdDialog);

                    SecureRandom secureRandom = SecureRandom.getInstance (nonSecrets.getStoredSecureRandomAlgorithm(),
                            nonSecrets.getStoredProvider());
                    secureRandom.nextBytes(nonSecrets.getStoredSalt());

                    SymmetricKey.setSecretKey(Secrets.getCombinedPasswords(),nonSecrets.getStoredSalt()
                    ,nonSecrets.getStoredIterationCount(),nonSecrets.getStoredKeyLength(),
                   nonSecrets.getStoredSecretKeyAlgorithm(),nonSecrets.getStoredProvider());

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
     GridPane setPwdGrid()
    {

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 10, 0, 10));

        manualPwdDialog.setPromptText("Manual Password");

        confirmPwdDialog.setPromptText("Confirm manual Password");

        sKeyPwdDialog.setPromptText("Security Key Password");

        grid.add(new Label("Manual Password:"), 0, 1);
        grid.add(manualPwdDialog, 1, 1);

        grid.add(new Label("Confirm manual Password:"), 0, 2);
        grid.add(confirmPwdDialog, 1, 2);

        grid.add(new Label("Security Key Password:"), 0, 3);
        grid.add(sKeyPwdDialog, 1, 3);
        return grid;

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

    void loginPasswords() {
        dialog.setTitle("Updating passwords");
        dialog.getDialogPane().setContent(grid);
        setPwdGrid();
        dialog.setResultConverter(dialogButton -> {
            try {
                if (dialogButton == ButtonType.OK) {
                    if (!manualPwdDialog.getText().equals(confirmPwdDialog.getText())) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Information Dialog");
                        alert.setHeaderText(null);
                        alert.setContentText("Password is incorrect!");
                        alert.showAndWait();

                        return null;
                    }

                }
            } catch (Exception E) {


            }
            return null;
        });

        dialog.showAndWait();

    }


}
