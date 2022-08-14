package sample;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

import java.io.BufferedReader;
import java.io.InputStreamReader;


public class HardwareKeyService {

    static String ykManPath  = "C:/Program Files/Yubico/YubiKey Manager";
    // Folder path to the application YubiKey Manager app
    static String textConfigureHwk = " YubiKey challenge response has been set successfully!";

    public static void cmdGenerateCR() throws Exception  {
        // 	Programs a challenge-response credential
        String generatePwdCommand= "ykman otp chalresp 1 -g -f";
        Process proc =
                Runtime.getRuntime().exec("cmd /c cmd.exe /K \"" + "cd " + ykManPath + "&&" + generatePwdCommand );
        String textRandomPwdHwk = "The challenge response mode has been set succesfully!";
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText(textRandomPwdHwk);
        alert.showAndWait();
    }

    public static void cmdConfigureCR() throws  Exception
    {
            TextInputDialog textDialog = new TextInputDialog();
            textDialog.setTitle("Configuring Hardware key");
            textDialog.setHeaderText("Configure the Hardware key's challenge-response");
            textDialog.setContentText("Enter a string to configure the Hardware key's challenge-response:");
            final Button btnOk = (Button) textDialog.getDialogPane().lookupButton(ButtonType.OK);
            btnOk.addEventFilter(
                ActionEvent.ACTION,
                event -> {
                    try {
                        Secrets.setConfigureHwkPwd(textDialog.getEditor().getText());
                        if (Secrets.getConfigureHwkPwd().length() == 0) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Information Dialog");
                            alert.setHeaderText(null);
                            alert.setContentText("String is empty!");
                            alert.showAndWait();
                            event.consume();
                            return;
                        }

                        String manualCommand = "ykman otp chalresp 1 " + Secrets.getConfigureHwkPwd() + " -f";
                      Process proc =
                          Runtime.getRuntime().exec("cmd /c cmd.exe /K \"" + "cd " + ykManPath + "&&" + manualCommand);
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information Dialog");
                        alert.setHeaderText(null);
                        alert.setContentText(textConfigureHwk);
                        alert.showAndWait();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                });

        textDialog.setResultConverter(loginButton -> {
            try {

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        });

       textDialog.showAndWait();

    }
    public static void cmdResponse(String challenge) throws  Exception  {
        String calculateResponse= "ykman otp calculate 1 "+ challenge;
        Process proc =
                 Runtime.getRuntime().exec("cmd /c cmd.exe /K \"" + "cd " + ykManPath
                         + "&&" + calculateResponse );
        BufferedReader stdInput = new BufferedReader(new
                InputStreamReader(proc.getInputStream()));
        while ((!stdInput.ready())) {
            String output = stdInput.readLine();
            if (stdInput.readLine()!=null) {
                Secrets.setResponse(output);
            }
        }
    }

}


