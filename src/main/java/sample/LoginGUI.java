package sample;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class LoginGUI {

    private   GridPane grid = new GridPane();
    public Dialog<Void> loginDialog = new Dialog<Void>();

   public void dialog(PasswordField manualPwd,PasswordField skPwd) {
       loginDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK,ButtonType.CLOSE);
        grid(manualPwd,skPwd);
       loginDialog.getDialogPane().setContent(grid);
       loginDialog.setTitle("Login");
    }


    public GridPane grid(PasswordField manualPwd,PasswordField responseField) {

        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 10, 0, 10));

        manualPwd.setPromptText("Enter Password");

        responseField.setDisable(true); // Prevents the user from accidentally typing in the responseField
        responseField.setPromptText("Hardware Security Key credential");

        grid.add(new Label("Enter Password:"), 0, 1);
        grid.add(manualPwd, 1, 1);

        grid.add(new Label("Hardware Security Key credential"), 0, 2);
        grid.add(responseField, 1, 2);

        return grid;
    }
}
