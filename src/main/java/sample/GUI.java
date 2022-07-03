package sample;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class GUI {

    public   GridPane grid = new GridPane();
    public Dialog<Void> loginDialog = new Dialog<Void>();

   public void dialog(PasswordField manualPwd,PasswordField skPwd) {
       loginDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK,ButtonType.CLOSE);
        grid(manualPwd,skPwd);
       loginDialog.getDialogPane().setContent(grid);
       loginDialog.setTitle("Login");
    }


    public GridPane grid (PasswordField manualPwd,PasswordField skPwd) {

        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 10, 0, 10));

        manualPwd.setPromptText("Manual Password");

        skPwd.setPromptText("Security Key Password");

        grid.add(new Label("Manual Password:"), 0, 1);
        grid.add(manualPwd, 1, 1);

        grid.add(new Label("Security Key Password:"), 0, 2);
        grid.add(skPwd, 1, 2);

        return grid;
    }
}
