package sample;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

public class MasterPwdGui {

     public   PasswordField manualPwdDialog = new PasswordField();
     public    PasswordField confirmPwdDialog = new PasswordField();
     public    PasswordField sKeyPwdDialog = new PasswordField();
    // These are the 3 password fields generated when the user creates a new Database.

   public    Dialog<Pair<String, String>> dialog = new Dialog<>();
   public   GridPane grid = new GridPane();

    void updateMasterPwd() {
        dialog.setTitle("Updating master password");
        dialog.getDialogPane().setContent(grid);
        setMasterPwdGui();
        final Button btnOk = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        btnOk.addEventFilter(
                ActionEvent.ACTION,
                event -> {
                    // Checks if conditions are fulfilled

                    if (!Authentication.validatePwdCredentials(manualPwdDialog.getText(),confirmPwdDialog.getText(),sKeyPwdDialog.getText())) {
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
                    PasswordUtils passwordUtils = new PasswordUtils();
                    passwordUtils.updateMasterPwd(manualPwdDialog,sKeyPwdDialog);
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
     GridPane setMasterPwdGui()
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


}
