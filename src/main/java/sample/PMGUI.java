package sample;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.io.File;
import java.nio.file.Files;
import java.security.SecureRandom;

public class PMGUI {

     public   PasswordField manualPwdDialog = new PasswordField();
     public    PasswordField confirmPwdDialog = new PasswordField();
     public    PasswordField ybKeyPwdDialog = new PasswordField();
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
                    if (manualPwdDialog.getText().length() == 0 || confirmPwdDialog.getText().length() == 0 || ybKeyPwdDialog.getText().length() == 0) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Information Dialog");
                        alert.setHeaderText(null);
                        alert.setContentText("Please fill all fields!");
                        alert.showAndWait();
                        return null;
                    }
                    byte[] nonSecretsBytes = FileUtils.readAllBytes(NonSecrets.getStoredNonSecrets());

                    NonSecrets nonSecrets = SerializedObject.readObject(nonSecretsBytes);

                    Global.setCombinedPasswords(manualPwdDialog, ybKeyPwdDialog);


                    SecureRandom secureRandom = SecureRandom.getInstance (nonSecrets.getStoredSecureRandomAlgorithm(),
                            nonSecrets.getStoredProvider());
                    secureRandom.nextBytes(nonSecrets.getStoredSalt());

                    SymmetricKey.setSecretKey(Global.getCombinedPasswords(),nonSecrets.getStoredSalt()
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

        manualPwdDialog.setPromptText("Master Password");

        confirmPwdDialog.setPromptText("Confirm Master Password");

        ybKeyPwdDialog.setPromptText("Yubikey Static Password");

        grid.add(new Label("Master Password:"), 0, 1);
        grid.add(manualPwdDialog, 1, 1);

        grid.add(new Label("Confirm Password:"), 0, 2);
        grid.add(confirmPwdDialog, 1, 2);

        grid.add(new Label("Yubikey Password:"), 0, 3);
        grid.add(ybKeyPwdDialog, 1, 3);
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


}
