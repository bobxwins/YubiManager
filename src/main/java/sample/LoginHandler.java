package sample;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextInputDialog;
import javafx.util.Pair;

import java.io.*;
import java.util.Optional;

public class LoginHandler implements Serializable  {
    public Dialog<Pair<String, String>> dialog = new Dialog<>();

    public void dialogManualYbk (String contentText)
    {
        TextInputDialog textDialog = new TextInputDialog();
        textDialog.setTitle("YubiKey manager");
        textDialog.setHeaderText("Manually set the YubiKey Password");
        textDialog.setContentText("Please manually enter the YubiKey password:");

        Optional<String> result = textDialog.showAndWait();
        Global.setManualYbkPwd(result.get());
        }

    }