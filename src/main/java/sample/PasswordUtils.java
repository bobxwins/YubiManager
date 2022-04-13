/*
package sample;

import java.util.regex.Pattern;

public class PasswordUtils {

    public static char[] getUserPassword() throws Exception {
        char[] password =  Main.PassText.toCharArray();
        // initiliases an array of character of unspecified length and value
        if (new String(password).length() < 12)
        {
            throw new Exception("Password too short!");

        }
        if  (Pattern.matches("[a-zA-Z0-9]*",new String(password)) )
            // checker længden og om der kun bruges "normale bogstaver" og tal
            throw new Exception("special characters missing!");
        return password;
    }

}
*/