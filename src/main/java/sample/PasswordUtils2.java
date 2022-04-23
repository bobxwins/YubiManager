
package sample;

import org.apache.commons.lang3.ArrayUtils;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;
import java.util.regex.Pattern;
import java.lang.Math;
public class PasswordUtils2 {

    static char[] SYMBOLS = "^$*.[]{}()?-\"!@#%&/\\,><':;|_~`".toCharArray();
    static char[] LOWERCASE = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    static char[] UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    static char[] NUMBERS = "0123456789".toCharArray();
    static char[] ALL_CHARS ;
   static char [] shiz =   ArrayUtils.addAll(SYMBOLS, LOWERCASE);

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
         int possible =8;
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
}
