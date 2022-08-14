
package sample;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.math.BigInteger;
import java.security.SecureRandom;

import static java.lang.Integer.parseInt;

public   class PasswordGenerator {
   private static char[] SYMBOLS = "+¤§$^$*.[]{}()?-\"!@#%&/\\,><':;|_~`".toCharArray();
   private static char[] LOWERCASE = "abcdefghijklmnopqrstuvwxyz".toCharArray();
   private static char[] UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
   private static char[] NUMBERS = "0123456789".toCharArray();
   private static char[] ALL_CHARS ="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+¤§$^$*.[]{}()?-\"!@#%&/\\,><':;|_~`".toCharArray() ;
   private final static BigInteger GUESSAMOUNT = new BigInteger ("1000000000000"); // 10^12
   private static double BRUTEFORCETIME;
   private static SecureRandom rand = new SecureRandom();

   public static String generatePassword(int length) {
        char[] password = new char[length];
        password[0] = ALL_CHARS[rand.nextInt(ALL_CHARS.length)];
        for (int i = 1; i < length; i++) {
            password[i] = ALL_CHARS[rand.nextInt(ALL_CHARS.length)];// returns a random char from the ALL_CHARS char array
            System.out.println("the passwrd[i] is "+password[i]);
            //populate rest of the password  array with the specified amount of random ALL_CHARS
        }

        //shuffles up the array
        for (int i = 0; i < password.length; i++) {
            int randomPosition = rand.nextInt(password.length);
            // integer with a random value within the range of the password.
            char temp = password[i];
            // The value of the temporary char is assigned to be of the current position of the password array
            password[i] = password[randomPosition];
            // replaces the value of a char from the current password array, with another random char
            password[randomPosition] = temp;
            // The generated temp char is put in a random position of the password array.
        }
        return new String(password);
    }

    private static int cardinality( String password)
    {
        int cardinality =0;

        boolean atleastOneLower = password.matches(".*[abcdefghijklmnopqrstuvwxyz]+.*");

        if (atleastOneLower)
        {
            cardinality=cardinality+LOWERCASE.length;
        }
        boolean atleastOneUpper = password.matches(".*[ABCDEFGHIJKLMNOPQRSTUVWXYZ]+.*");

        if (atleastOneUpper)
        {

            cardinality=cardinality +UPPERCASE.length; //+//UPPERCASE.length;

        }


        boolean atleastOneNumber = password.matches(".*[0-9]+.*");

        if (atleastOneNumber)

        {
            cardinality=cardinality+NUMBERS.length;

        }
        boolean atleastOneSymbol = password.matches(".*[^A-Za-z0-9]+.*");
       // boolean atleastOneSymbol = password.matches(".*[+¤§$^$*.-\\[-\\]{}()?-\\\\\"!@#%&/\\,><':;|_~`]]+.*");
        if (atleastOneSymbol)

        {

            cardinality=cardinality+SYMBOLS.length;

        }


        return cardinality;
    }

    public static void calcBruteforceTime(Text textPwdQuality, Text textBruteForceTime, Text textCalcEntropy, String password)
    {
        double entropy =  Math.log10(Math.pow(cardinality(  password),password.length()))/Math.log10(2);

        // Statement above is equal to: E= log2(Cardinality^PasswordLength)

        String quality ="";
        String doubleFormat ="%.15f";
        // puts 3 decimals of the calculated entropy
        textPwdQuality.setStyle("-fx-font-size: 21px;");
        textPwdQuality.setUnderline(false);
        if(entropy <64)
        {
            quality= "Very weak";
            textPwdQuality.setFill(Color.RED);

        }
        if(entropy >= 64  )
        {
            quality= "Weak";

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
             // uses scientific notations
        }

        textPwdQuality.setText(quality);
 
        BRUTEFORCETIME =Math.pow(2,entropy)/ GUESSAMOUNT.doubleValue()/2;
        textCalcEntropy.setText("The calculated entropy is: "+(entropy)+" bits"+"\n\nYour password quality is: ");
        textBruteForceTime.setText("Assuming the attacker can guess 1 trillion (1,000,000,000,000)" +"\n"
                + "passwords per second, cracking the password takes: "
                +"\n"+String.format(doubleFormat , BRUTEFORCETIME)+" seconds"
                +"\nIn hours: "+String.format(doubleFormat, BRUTEFORCETIME /3600)+" hours" +
                "\nIn days: "+String.format(doubleFormat , BRUTEFORCETIME /3600/24)+" days"+
                "\nIn years: "+String.format(doubleFormat , BRUTEFORCETIME /3600/24/365)+" years");

    }

}




