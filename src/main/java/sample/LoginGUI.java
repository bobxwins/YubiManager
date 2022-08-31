package sample;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class LoginGUI {

    private   GridPane grid = new GridPane();
    public Dialog<Void> loginDialog = new Dialog<Void>();

   public void dialog(PasswordField userCredential) {
       loginDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK,ButtonType.CLOSE);
        grid(userCredential);
       loginDialog.getDialogPane().setContent(grid);
       loginDialog.setTitle("Login");
    }

    public GridPane grid(PasswordField userCredential) {
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 10, 0, 10));
        userCredential.setPromptText("Enter User Credential...");
        grid.add(new Label("Enter User Credential"), 0, 1);
        grid.add(userCredential, 1, 1);
        return grid;
    }
}
