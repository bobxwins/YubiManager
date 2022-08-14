package sample;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;

public class MasterPwdGui {


    static {
        Security.removeProvider("BC");

        Security.addProvider(new BouncyCastleProvider());
    }

     public   PasswordField manualPwdDField = new PasswordField();
     public   PasswordField confirmPwdField = new PasswordField();
     public   PasswordField responseField = new PasswordField();
    // These are the 3 password fields generated when the user creates a new Database.

     public    Dialog<Pair<String, String>> dialog = new Dialog<>();
     public   GridPane grid = new GridPane();


    void updateMasterPwd() throws Exception {
        dialog.setTitle("Updating master password");
        dialog.getDialogPane().setContent(grid);
        newMasterPwd();
        final Button btnOk = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        btnOk.addEventFilter(
                ActionEvent.ACTION,
                event -> {
                    // Checks if conditions are fulfilled

                    if (!Authentication.validateCredentials(manualPwdDField.getText(), confirmPwdField.getText() )) {
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
                    Secrets.setManualPassword(manualPwdDField.getText().toCharArray());
                  //  KeyService.createKey();
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
        GridPane newMasterPwd() throws Exception {

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 10, 0, 10));

        manualPwdDField.setPromptText("Enter Password...");

        confirmPwdField.setPromptText("Confirm Password...");

        grid.add(new Label("Enter Password:"), 0, 1);
        grid.add(manualPwdDField, 1, 1);

        grid.add(new Label("Confirm Password:"), 0, 2);
        grid.add(confirmPwdField, 1, 2);

        return grid;

    }


}
