
package sample;

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

        System.out.println("the cardinality is: "+cardinality);
        return cardinality;
    }
}
