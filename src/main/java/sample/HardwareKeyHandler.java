package sample;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;

import java.util.Optional;


public class HardwareKeyHandler {

    static String ykManPath  = "C:/Program Files/Yubico/YubiKey Manager";
    // Folder path to the application YubiKey Manager, by YubiCo.
    static String textRandomPwdHwk = " A new YubiKey password has been generated succesfully!";
    static String textConfigureHwk = " YubiKey password has been set succesfully!";
    // Using the command prompt, the command opens the YubiKey Manager, and generates a 38 character long random password, with the keyboard being the  US layout

    public static void cmdProcess(String command, String contentText)  {

        try {
            Process process =
                    Runtime.getRuntime().exec("cmd /c cmd.exe /K \"" + "cd " + ykManPath + "&&" + command + "&&" + "y");

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText(contentText);
                alert.showAndWait();

        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void cmdGenerateHwkPwd()  {
        String generatePwdCommand= "ykman otp static 1 --generate --length 38 --force --keyboard-layout US";
        try {
            cmdProcess(generatePwdCommand, textRandomPwdHwk);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void cmdConfigureHwkPwd()
    {

        try {
        dialogConfigureHwkPwd();
            if (Secrets.getConfigureHwkPwd().length()==0)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Password is empty!");
                alert.showAndWait();
                return;
            }
            String manualCommand= "ykman otp static 1 --generate "+Secrets.getConfigureHwkPwd()+" --force --keyboard-layout US";
          cmdProcess(manualCommand, textConfigureHwk);

        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static void dialogConfigureHwkPwd()
    {
        TextInputDialog textDialog = new TextInputDialog();
        textDialog.setTitle("YubiKey manager");
        textDialog.setHeaderText("Configure Hardware key Password");
        textDialog.setContentText("Enter a string to configure the Hardware key password:");

        Optional<String> result = textDialog.showAndWait();
        Secrets.setConfigureHwPwd(result.get());
    }

}


