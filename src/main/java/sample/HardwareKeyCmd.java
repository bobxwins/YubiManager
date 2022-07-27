package sample;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;


public class HardwareKeyCmd {

    static String ykManPath  = "C:/Program Files/Yubico/YubiKey Manager";
    // Folder path to the application YubiKey Manager app
    static String textConfigureHwk = " YubiKey challenge response has been set successfully!";
    // Using the command prompt, the command opens the YubiKey Manager, and generates a 38 character long random password, with the keyboard being the  US layout
    static String output;
    public static void cmdProcess(String command) throws Exception  {
            Process proc =
                    Runtime.getRuntime().exec("cmd /c cmd.exe /K \"" + "cd " + ykManPath + "&&" + command );

     BufferedReader stdInput = new BufferedReader(new
                InputStreamReader(proc.getInputStream()));

          while ((!stdInput.ready())) {
              output = stdInput.readLine();
             if (stdInput.readLine()!=null) {
                 setOutput(output);
             }
        }

    }

    static String getOutput () {
        return output;
    }
    static void setOutput ( String outputString) {
       HardwareKeyCmd.output = outputString;
    }
    public static void cmdGenerateCR()  {
        // 	Programs a challenge-response credential
        String generatePwdCommand= "ykman otp chalresp 1 -g -f";
        try {
            String textRandomPwdHwk = "The challenge response mode has been set succesfully!";
            cmdProcess(generatePwdCommand);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText(textRandomPwdHwk);
            alert.showAndWait();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
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
                        System.out.println("the hex is:" + Secrets.getConfigureHwkPwd());

                        Process proc =
                                Runtime.getRuntime().exec("cmd /c cmd.exe /K \"" + "cd " + ykManPath + "&&" + manualCommand);

                        // This command does not produce an output string in the cmd terminal, so a BufferReader cannot be used here

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information Dialog");
                        alert.setHeaderText(null);
                        alert.setContentText(textConfigureHwk);
                        alert.showAndWait();
                    } catch (IOException e) {
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
        // 	Programs a response
        String calculateResponse= "ykman otp calculate 1 "+ challenge;
        try {
            cmdProcess(calculateResponse);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}


