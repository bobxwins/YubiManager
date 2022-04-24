
package sample;

import org.apache.commons.lang3.ArrayUtils;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;
import java.util.regex.Pattern;
import java.lang.Math;

public class PasswordUtils2 {


    static char[] SYMBOLS = "+¤§$^$*.[]{}()?-\"!@#%&/\\,><':;|_~`".toCharArray();

    static char[] LOWERCASE = "abcdefghijklmnopqrstuvwxyzæøå".toCharArray();
    static char[] UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZÆØÅ".toCharArray();
    static char[] NUMBERS = "0123456789".toCharArray();
    static char[] ALL_CHARS ;


    static Random rand = new SecureRandom();

    public static String getPassword(int length) {


        StringBuilder sb = new StringBuilder();
        sb.append(SYMBOLS);
        sb.append(LOWERCASE);
        sb.append(UPPERCASE);
        sb.append(NUMBERS);
       ALL_CHARS = sb.toString().toCharArray();

        assert length >= 12;
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


        double entropy =  Math.log10(Math.pow(ALL_CHARS.length, password.length))/Math.log10(2);


        System.out.println("entropy IS "+entropy);

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
}
