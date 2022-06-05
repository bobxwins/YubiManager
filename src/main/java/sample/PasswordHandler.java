package sample;
import javafx.scene.control.Alert;

import java.io.*;


public class PasswordHandler {
 //ykman.exe --log-level DEBUG --log-file ~C:/Users\bob-w/Documents/YubiManager/src/main/tools/ykman.txt

    static String ykManPath =System.getProperty("user.dir")+"\\tools\\YubiKeyManager\\";
    static String generatePwdCommand= "ykman otp static 1 --generate --length 32 --force --keyboard-layout US";
    // Using the command prompt, the command opens the YubiKey Manager, and generates a 32 character long random password, with the keyboard being the  US layout
    public static void generateYbkPassword()  {

        try {
            Process process =
                Runtime
                    .getRuntime()
                    .exec("cmd /c cmd.exe /K \""+"cd "+ykManPath+"&&"+generatePwdCommand);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("A new YubiKey password has been generated succesfully!");
            alert.showAndWait();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}


