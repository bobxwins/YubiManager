package sample;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;

import java.util.Optional;


public class YubiKeyPwdHandler {

    static String ykManPath  = "C:/Program Files/Yubico/YubiKey Manager";
    // Folder path to the application YubiKey Manager, by YubiCo.
    static String generatePwdCommand= "ykman otp static 1 --generate --length 32 --force --keyboard-layout US";
    static String manualCommand= "ykman otp static 1 " ;
    static String textGeneratedYbk = " A new YubiKey password has been generated succesfully!";
    static String textManualYbk = " YubiKey password has been set succesfully!";
    // Using the command prompt, the command opens the YubiKey Manager, and generates a 32 character long random password, with the keyboard being the  US layout

    public static void cmdProcess(String command, String contentText)  {

        try {
            Process process =
                    Runtime.getRuntime().exec("cmd /c cmd.exe /K \""+"cd "+ykManPath+"&&"+command);

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

    public static void generateYbkPassword()  {

        try {
            cmdProcess(generatePwdCommand,textGeneratedYbk);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void manualYbkPwd ()
    {
        try {
        dialogManualYbk();
            if (Global.getManualYbkPwd().length()==0)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Password is empty!");
                alert.showAndWait();
                return;
            }

          cmdProcess(manualCommand+Global.getManualYbkPwd(),textManualYbk);

        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static void dialogManualYbk ()
    {
        TextInputDialog textDialog = new TextInputDialog();
        textDialog.setTitle("YubiKey manager");
        textDialog.setHeaderText("Manually set the YubiKey Password");
        textDialog.setContentText("Please manually enter the YubiKey password:");

        Optional<String> result = textDialog.showAndWait();
        Global.setManualYbkPwd(result.get());
    }

}


