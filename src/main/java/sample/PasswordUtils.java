
package sample;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.math.BigInteger;
import java.security.SecureRandom;

import java.util.Random;

public   class PasswordUtils {


    static char[] SYMBOLS = "+¤§$^$*.[]{}()?-\"!@#%&/\\,><':;|_~`".toCharArray();

    static char[] LOWERCASE = "abcdefghijklmnopqrstuvwxyzæøå".toCharArray();
    static char[] UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZÆØÅ".toCharArray();
    static char[] NUMBERS = "0123456789".toCharArray();
    static char[] ALL_CHARS ;
    final static BigInteger BRUTEFORCEGPU = new BigInteger ("6877000000"); // 68,77 billions password guesses pr second
    final static BigInteger BRUTEFOCEGPUCLUSTERS = new BigInteger ("10000000000000"); // 10 trillion password guesses pr second
    static double BRUTEFORCETIMEGPU;
    static double BRUTEFORCETIMEGPUCLUSTERS;


    static SecureRandom rand = new SecureRandom();

    public static String getPassword(int length) {
// generates a random password

     /*   if (length <4)
        {
            length=4;
        } */

        StringBuilder sb = new StringBuilder();
        sb.append(SYMBOLS);
        sb.append(LOWERCASE);
        sb.append(UPPERCASE);
        sb.append(NUMBERS);
       ALL_CHARS = sb.toString().toCharArray();

        char[] password = new char[length];

        //get the requirements out of the way
        password[0] = LOWERCASE[rand.nextInt(LOWERCASE.length)];
        password[1] = UPPERCASE[rand.nextInt(UPPERCASE.length)];
        password[2] = NUMBERS[rand.nextInt(NUMBERS.length)];
        password[3] = SYMBOLS[rand.nextInt(SYMBOLS.length)];

        //populate rest of the password with random chars,until password array's length is equal to the specified length
        for (int i = 4; i < length; i++) {
            password[i] = ALL_CHARS[rand.nextInt(ALL_CHARS.length)];

            // returns a random char from the ALL_CHARS char array
        }

        //shuffle it up
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
    public static int cardinality( String password)
    {
        int cardinality =0;


        boolean atleastOneLower = password.matches(".*[abcdefghijklmnopqrstuvwxyzæøå]+.*");

        if (atleastOneLower)
        {

            cardinality=cardinality+LOWERCASE.length;


        }
        boolean atleastOneUpper = password.matches(".*[ABCDEFGHIJKLMNOPQRSTUVWXYZÆØÅ]+.*");


        if (atleastOneUpper)
        {
            cardinality=cardinality+UPPERCASE.length;

        }

        boolean atleastOneNumber = password.matches(".*[0-9]+.*");

        if (atleastOneNumber)

        {
            cardinality=cardinality+NUMBERS.length;

        }

        boolean atleastOneSymbol = password.matches(".*[^A-Åa-å0-9]+.*");

        if (atleastOneSymbol)

        {

            cardinality=cardinality+SYMBOLS.length;

        }


        return cardinality;
    }

    public static void calcCrackingTime(Text textPwdQuality, Text textCalcGPU,Text textCalcEntropy, Text textCalcGPUClusters,String password)
    {
        double entropy =  Math.log10(Math.pow(cardinality(  password),password.length()))/Math.log10(2);

        // Statement above is equal to: E= log2(Cardinality^PasswordLength)

        String quality ="";
        String doubleFormat ="%.3f";
        // puts 3 decimals of the calculated entropy
        textPwdQuality.setStyle("-fx-font-size: 21px;");
        textPwdQuality.setUnderline(false);
        if(entropy <28)
        {
            quality= "Very weak";
            textPwdQuality.setFill(Color.RED);

        }
        if(entropy >= 28  )
        {
            quality= "Weak";

            textPwdQuality.setFill(Color.INDIANRED);
        }
        if(entropy >= 35  )
        {
            quality= "Fair";
            textPwdQuality.setFill(Color.DARKGOLDENROD);
        }
        if(entropy >= 59  )
        {
            quality= "Strong";
            textPwdQuality.setFill(Color.GREEN);

        }
        if(entropy >= 127 )
        {
            textPwdQuality.setStyle("-fx-font-size: 25px;");
            quality= "AMAZING!";

        }



        BRUTEFORCETIMEGPU=Math.pow(2,entropy)/ BRUTEFORCEGPU.doubleValue()/2;

        if (entropy>194)
        {
            textPwdQuality.setFill(Color.GREEN);
            textPwdQuality.setUnderline(true);
            textPwdQuality.setStyle("-fx-font-size: 30px;");
            quality= "Overkill!!";
             doubleFormat ="%.3g";
             // uses scientific notations
        }

        textPwdQuality.setText(quality);

        textCalcEntropy.setText("The calculated entropy is: "+(entropy)+" bits"+"\n\nYour password quality is: ");
        textCalcGPU.setText( "Estimated time for brute forcing the passwords with 1 GPU is: "
                +"\n"+String.format(doubleFormat, BRUTEFORCETIMEGPU)+" seconds"
                +"\nIn hours: "+String.format(doubleFormat, BRUTEFORCETIMEGPU/3600)+" hours"+
                "\nIn days: "+String.format(doubleFormat,BRUTEFORCETIMEGPU/3600/24)+" days"+
                "\nIn years: "+String.format(doubleFormat,BRUTEFORCETIMEGPU/3600/24/365)+" years");

        BRUTEFORCETIMEGPUCLUSTERS=Math.pow(2,entropy)/ BRUTEFOCEGPUCLUSTERS.doubleValue()/2;

        textCalcGPUClusters.setText("Estimated time for brute forcing the passwords with GPU Clusters is: "
                +"\n"+String.format(doubleFormat ,BRUTEFORCETIMEGPUCLUSTERS)+" seconds"
                +"\nIn hours: "+String.format(doubleFormat, BRUTEFORCETIMEGPUCLUSTERS/3600)+" hours" +
                "\nIn days: "+String.format(doubleFormat ,BRUTEFORCETIMEGPUCLUSTERS/3600/24)+" days"+
                "\nIn years: "+String.format(doubleFormat , BRUTEFORCETIMEGPUCLUSTERS/3600/24/365)+" years");

    }
}




