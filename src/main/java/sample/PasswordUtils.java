
package sample;

import javafx.scene.control.TextField;
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
    final static BigInteger BRUTEFORCEGPU = new BigInteger ("6877000000"); // 6877 millions password guesses pr second
    final static BigInteger BRUTEFOCEGPUCLUSTERS = new BigInteger ("10000000000000"); // 10 trillion password guesses pr second
    static double BRUTEFORCETIMEGPU;
    static double BRUTEFORCETIMEGPUCLUSTERS;


    static Random rand = new SecureRandom();

    public static String getPassword(int length) {
// generates a random password

        StringBuilder sb = new StringBuilder();
        sb.append(SYMBOLS);
        sb.append(LOWERCASE);
        sb.append(UPPERCASE);
        sb.append(NUMBERS);
       ALL_CHARS = sb.toString().toCharArray();

       // assert length >= 12 : "Password is too short!!";
        char[] password = new char[length];

        //get the requirements out of the way
        password[0] = LOWERCASE[rand.nextInt(LOWERCASE.length)];
        password[1] = UPPERCASE[rand.nextInt(UPPERCASE.length)];
        password[2] = NUMBERS[rand.nextInt(NUMBERS.length)];
        password[3] = SYMBOLS[rand.nextInt(SYMBOLS.length)];

        //populate rest of the password with random chars
        for (int i = 4; i < length; i++) {
            password[i] = ALL_CHARS[rand.nextInt(ALL_CHARS.length)];
        }

        //shuffle it up
        for (int i = 0; i < password.length; i++) {
            int randomPosition = rand.nextInt(password.length);
            char temp = password[i];
            password[i] = password[randomPosition];
            password[randomPosition] = temp;



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
        double entropy =  Math.log10(Math.pow(PasswordUtils.cardinality(  password),password.length()))/Math.log10(2);
//
        String quality ="";
        textPwdQuality.setStyle("-fx-font-size: 21px;");
        textPwdQuality.setUnderline(false);
        if(entropy <28)
        {
            quality= "Very weak";
            textPwdQuality.setFill(Color.RED);

        }
        if(entropy > 28 && entropy < 35 )
        {
            quality= "Weak";

            textPwdQuality.setFill(Color.INDIANRED);
        }
        if(entropy > 35 && entropy < 59 )
        {
            quality= "Fair";
            textPwdQuality.setFill(Color.DARKGOLDENROD);
        }
        if(entropy > 60 && entropy < 127 )
        {
            quality= "Strong";
            textPwdQuality.setFill(Color.GREEN);

        }
        if(entropy >= 127 )
        {
            textPwdQuality.setFill(Color.DARKGREEN);
            textPwdQuality.setUnderline(true);
            textPwdQuality.setStyle("-fx-font-size: 30px;");
            quality= "Overkill";

        }


        textPwdQuality.setText(quality);

        PasswordUtils.BRUTEFORCETIMEGPU=Math.pow(2,entropy)/ PasswordUtils.BRUTEFORCEGPU.doubleValue()/2;
        if (entropy>174)
        {
            PasswordUtils.BRUTEFORCETIMEGPU=Double.POSITIVE_INFINITY;

        }
        textCalcEntropy.setText("The calculated entropy is: "+(entropy)+" bits"+"\n\nYour password quality is: ");
        textCalcGPU.setText( "Estimated time for brute forcing the passwords with a GPU is: "
                +"\n"+String.format("%.3f", PasswordUtils.BRUTEFORCETIMEGPU)+" seconds"
                +"\nIn hours: "+String.format("%.3f", PasswordUtils.BRUTEFORCETIMEGPU/3600)+" hours"+
                "\nIn days: "+String.format("%.3f",PasswordUtils.BRUTEFORCETIMEGPU/3600/24)+" days"+
                "\nIn years: "+String.format("%.3f",PasswordUtils.BRUTEFORCETIMEGPU/3600/24/365)+" years");

        PasswordUtils.BRUTEFORCETIMEGPUCLUSTERS=Math.pow(2,entropy)/ PasswordUtils.BRUTEFOCEGPUCLUSTERS.doubleValue()/2;

        if (entropy>174)
        {
            PasswordUtils.BRUTEFORCETIMEGPUCLUSTERS=Double.POSITIVE_INFINITY;
        }

        textCalcGPUClusters.setText("Estimated time for brute forcing the passwords with GPU Clusters is: "
                +"\n"+String.format("%.3f" ,PasswordUtils.BRUTEFORCETIMEGPUCLUSTERS)+" seconds"
                +"\nIn hours: "+String.format("%.3f", PasswordUtils.BRUTEFORCETIMEGPUCLUSTERS/3600)+" hours" +
                "\nIn days: "+String.format("%.3f" ,PasswordUtils.BRUTEFORCETIMEGPUCLUSTERS/3600/24)+" days"+
                "\nIn years: "+String.format("%.3f" , PasswordUtils.BRUTEFORCETIMEGPUCLUSTERS/3600/24/365)+" years");

    }
}




