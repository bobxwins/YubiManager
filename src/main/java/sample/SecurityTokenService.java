package sample;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

import java.io.BufferedReader;
import java.io.InputStreamReader;


public class SecurityTokenService {

    static String ykManPath  = "C:/Program Files/Yubico/YubiKey Manager";
    // Folder path to the application YubiKey Manager app

    public static void generateTokenKey() throws Exception  {
        // 	Programs a random secret key
        String generateSecretKey= "ykman otp chalresp 1 -g -f";
        Process proc =
                Runtime.getRuntime().exec("cmd /c cmd.exe /K \"" + "cd " + ykManPath + "&&" + generateSecretKey );
        String textRandomPwdHwk = "A security token key has been generated succesfully!";
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText(textRandomPwdHwk);
        alert.showAndWait();
    }

    public static void setTokenKey() throws  Exception
    {
            TextInputDialog textDialog = new TextInputDialog();
            textDialog.setTitle("Configuration");
           textDialog.setHeaderText("");
            textDialog.setContentText("Enter a string to set the Token Key");
            final Button btnOk = (Button) textDialog.getDialogPane().lookupButton(ButtonType.OK);
            btnOk.addEventFilter(
                ActionEvent.ACTION,
                event -> {
                    try {
                        Secrets.setTokenKeyConfiguration(textDialog.getEditor().getText());
                        if (Secrets.getTokenKeyConfiguration().length() == 0) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Information Dialog");
                            alert.setHeaderText(null);
                            alert.setContentText("String is empty!");
                            alert.showAndWait();
                            event.consume();
                            return;
                        }

                        String manualCommand = "ykman otp chalresp 1 " + Secrets.getTokenKeyConfiguration() + " -f";
                      Process proc =
                          Runtime.getRuntime().exec("cmd /c cmd.exe /K \"" + "cd " + ykManPath + "&&" + manualCommand);
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information Dialog");
                        alert.setHeaderText(null);
                        alert.setContentText("The security token has been set successfully!");
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
    public static void responseMacTag(String challenge) throws  Exception  {
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


