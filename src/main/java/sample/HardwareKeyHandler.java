package sample;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import org.bouncycastle.util.encoders.Hex;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Optional;


public class HardwareKeyHandler {

    static String ykManPath  = "C:/Program Files/Yubico/YubiKey Manager";
    // Folder path to the application YubiKey Manager app
    static String textConfigureHwk = " YubiKey challenge response has been set successfully!";
    // Using the command prompt, the command opens the YubiKey Manager, and generates a 38 character long random password, with the keyboard being the  US layout
    static String output;
    public static void cmdProcess(String command) throws Exception  {
            Process proc =
                    Runtime.getRuntime().exec("cmd /c cmd.exe /K \"" + "cd " + ykManPath + "&&" + command );
            //+ "&&" + "y"
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
       HardwareKeyHandler.output = outputString;
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



    public static void cmdConfigureCR()
    {
        try {
            dialogConfigureCR();
            if (Secrets.getConfigureHwkPwd().length==0)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Credentials are empty!");
                alert.showAndWait();
                return;
            }
            String hexString = Hex.toHexString(Secrets.getConfigureHwkPwd());
            String manualCommand= "ykman otp chalresp 1 "+ hexString + " -f";
            System.out.println("the hex is:"+hexString);
            Process proc =
                    Runtime.getRuntime().exec("cmd /c cmd.exe /K \"" + "cd " + ykManPath + "&&" + manualCommand );
            // This command does not produce an output string in the cmd terminal, so a BufferReader cannot be used here

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

    public static void dialogConfigureCR()
    {
        TextInputDialog textDialog = new TextInputDialog();
        textDialog.setTitle("Configuring Hardware key");
        textDialog.setHeaderText("Configure the Hardware key challenge response");
        textDialog.setContentText("Enter a string to configure the Hardware key challenge response:");
        Optional<String> result = textDialog.showAndWait();
        byte[] resultBytes = result.get().getBytes(StandardCharsets.UTF_8);
        Secrets.setConfigureHwPwd(resultBytes);
    }


}


