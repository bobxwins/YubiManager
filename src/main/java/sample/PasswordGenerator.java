
package sample;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.SecureRandom;

import static java.lang.Integer.parseInt;

public   class PasswordGenerator {

   //private static SecureRandom rand = new SecureRandom();

   public static String generatePassword(int length) {
       SecureRandom rand = new SecureRandom();
       char[] all_Chars ="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+¤§$^$*.[]{}()?-\"!@#%&/\\,><':;|_~`"
               .toCharArray() ;
       char[] password = new char[length];
       password[0] = all_Chars[rand.nextInt(all_Chars.length)];
        for (int i = 0; i < length; i++) {
            password[i] = all_Chars[rand.nextInt(all_Chars.length)];
        }
        for (int i = 0; i < password.length; i++) {
            int randomPosition = rand.nextInt(password.length);
            char temp = password[i];
            password[i] = password[randomPosition];
            password[randomPosition] = temp;
        }
        return new String(password);
    }

    private static int cardinality( String password)
    {
        char[] symbols = "+¤§$^$*.[]{}()?-\"!@#%&/\\,><':;|_~`".toCharArray();
        char[] lowercase = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        char[] uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        char[] numbers = "0123456789".toCharArray();
        int cardinality =0;

        boolean atleastOneLower = password.matches(".*[abcdefghijklmnopqrstuvwxyz]+.*");

        if (atleastOneLower)
        {
            cardinality=cardinality+lowercase.length;
        }
        boolean atleastOneUpper = password.matches(".*[ABCDEFGHIJKLMNOPQRSTUVWXYZ]+.*");

        if (atleastOneUpper)
        {
            cardinality=cardinality +uppercase.length; //+//UPPERCASE.length;
        }
        boolean atleastOneNumber = password.matches(".*[0-9]+.*");

        if (atleastOneNumber)

        {
            cardinality=cardinality+numbers.length;

        }
        boolean atleastOneSymbol = password.matches(".*[^A-Za-z0-9]+.*");
        if (atleastOneSymbol)
        {
            cardinality=cardinality+symbols.length;
        }
        return cardinality;
    }

    public static void calcBruteforceTime(Text textPwdQuality, Text textBruteForceTime, Text textCalcEntropy, String password)
    {
        double entropy =  Math.log10(Math.pow(cardinality(password),password.length()))/Math.log10(2);
        // The formula above is equal to: E= log2(Cardinality^PasswordLength)
        String quality ="";
        String doubleFormat ="%.15f";
        // puts 3 decimals of the calculated entropy
        textPwdQuality.setStyle("-fx-font-size: 21px;");
        textPwdQuality.setUnderline(false);
        if(entropy <65)
        {
            quality= "Weak";
            textPwdQuality.setFill(Color.RED);
        }
        if(entropy >= 65  )
        {
            quality= "Moderate";
            textPwdQuality.setFill(Color.INDIANRED);
        }
        if(entropy >= 78  )
        {
            quality= "Good";
            textPwdQuality.setFill(Color.DARKGREEN);
        }
        if(entropy >= 112  )
        {
            textPwdQuality.setStyle("-fx-font-size: 25px;");
            quality= "AMAZING!";
            textPwdQuality.setFill(Color.GREEN);
        }

        if (entropy>=128)
        {
            textPwdQuality.setFill(Color.GREEN);
            textPwdQuality.setUnderline(true);
            textPwdQuality.setStyle("-fx-font-size: 30px;");
            quality= "Overkill!!";
            doubleFormat ="%.3g";
        }
        textPwdQuality.setText(quality);
        double possibleCombinations = Math.pow(2,entropy);
        double guessesPerSecond  = 1.0E12; // 10^12 or 1 trillion
        double bruteForceSeconds = possibleCombinations/ guessesPerSecond/2;
        textCalcEntropy.setText("The calculated entropy is: "+(entropy)+" bits"+"\n\nYour password quality is: ");
        textBruteForceTime.setText("Assuming the attacker can guess 1 trillion (1,000,000,000,000)" +"\n"
                + "passwords per second, cracking the password takes maximum: "
                +"\n"+String.format(doubleFormat , bruteForceSeconds)+" seconds"
                +"\nIn hours: "+String.format(doubleFormat, bruteForceSeconds /3600)+" hours" +
                "\nIn days: "+String.format(doubleFormat , bruteForceSeconds /3600/24)+" days"+
                "\nIn years: "+String.format(doubleFormat , bruteForceSeconds /3600/24/365)+" years");
    }

}
