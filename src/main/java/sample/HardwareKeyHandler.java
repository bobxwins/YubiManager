package sample;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Optional;


public class HardwareKeyHandler {

    static String ykManPath  = "C:/Program Files/Yubico/YubiKey Manager";
    // Folder path to the application YubiKey Manager app
    static String textConfigureHwk = " YubiKey challenge response has been set successfully!";
    // Using the command prompt, the command opens the YubiKey Manager, and generates a 38 character long random password, with the keyboard being the  US layout

    public static void cmdProcess(String command) throws Exception  {
            Process p =
                    Runtime.getRuntime().exec("cmd /c cmd.exe /K \"" + "cd " + ykManPath + "&&" + command + "&&" + "y");
    }


    public static void cmdGenerateCR()  {
        // 	Programs a challenge-response credential
        String generatePwdCommand= "ykman otp chalresp 1 -g -f";
        try {
            String textRandomPwdHwk = " A new YubiKey symmetric key has been generated succesfully!";
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

    public static void cmdResponse(String challenge)  {
        // 	Programs a  response

        String calculateResponse= "ykman otp calculate 1 "+ challenge + " | clip";
        // generates the response and copies the output to the Windows Clipboard
        try {
            cmdProcess(calculateResponse);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static void cmdConfigureCR()
    {

        try {
        dialogConfigureCR();
            if (Secrets.getConfigureHwkPwd().length()==0)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Password is empty!");
                alert.showAndWait();
                return;
            }
            String manualCommand= "ykman otp chalresp 1"+Secrets.getConfigureHwkPwd();
          cmdProcess(manualCommand);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText(textConfigureHwk);
            alert.showAndWait();

        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static void dialogConfigureCR()
    {
        TextInputDialog textDialog = new TextInputDialog();
        textDialog.setTitle("Configuring Hardware key");
        textDialog.setHeaderText("Configure the Hardware key challenge response");
        textDialog.setContentText("Enter a string to configure the Hardware key challenge response:");

        Optional<String> result = textDialog.showAndWait();
        Secrets.setConfigureHwPwd(result.get());
    }

}


