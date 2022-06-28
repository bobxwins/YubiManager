package sample;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.io.File;
import java.nio.file.Files;
import java.security.SecureRandom;
import java.util.regex.Pattern;

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
        dialog.setResultConverter(dialogButton -> {
            try {
                if (dialogButton == ButtonType.OK) {
                    if (!manualPwdDialog.getText().equals(confirmPwdDialog.getText())) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Information Dialog");
                        alert.setHeaderText(null);
                        alert.setContentText("Update failed! Password did not match!");
                        alert.showAndWait();

                        return null;
                    }
                    if (manualPwdDialog.getText().length() == 0 || confirmPwdDialog.getText().length() == 0 || sKeyPwdDialog.getText().length() == 0) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Information Dialog");
                        alert.setHeaderText(null);
                        alert.setContentText("Please fill all fields!");
                        alert.showAndWait();
                        return null;
                    }
                    if (manualPwdDialog.getText().length() + sKeyPwdDialog.getText().length()  < 12  ) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Information Dialog");
                        alert.setHeaderText(null);
                        alert.setContentText("Master password must be at least 12 characters!");
                        alert.showAndWait();
                        return null;
                    }
                /*    if ( !Pattern.matches(".*[^A-Za-z0-9]+.*",new String(Secrets.getCombinedPasswords()))
                            || ! Pattern.matches("[a-zA-Z.0-9_]*",new String(Secrets.getCombinedPasswords()))  ) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Information Dialog");
                        alert.setHeaderText(null);
                        alert.setContentText("Master password must contain digits, uppercase, lowercase and special characters!");
                        alert.showAndWait();

                        return null;
                    }
                    */

                    byte[] nonSecretsBytes = FileUtils.readAllBytes(NonSecrets.getStoredNonSecrets());

                    NonSecrets nonSecrets = SerializedObject.readObject(nonSecretsBytes);

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
