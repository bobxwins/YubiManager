
package sample;

import java.util.regex.Pattern;

public class PasswordUtils {

    public static char[] getUserPassword() throws Exception {
        // initiliases an array of character of unspecified length and value
        if (LoginController.combinedPasswords.length < 12)
        {
            throw new Exception("Password too short!");

        }
        if  (Pattern.matches("[a-zA-Z0-9]*",new String(LoginController.combinedPasswords)) )
            // checker længden og om der kun bruges "normale bogstaver" og tal
            throw new Exception("special characters missing!");
        return LoginController.combinedPasswords;
    }

}
