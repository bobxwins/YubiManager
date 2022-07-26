package sample;
import javax.crypto.spec.IvParameterSpec;

import javax.crypto.*;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;


import java.nio.charset.StandardCharsets;
import java.security.Security;
import java.util.Base64;

public class DecryptFile  {

    static {
        Security.removeProvider("BC");
        Security.addProvider(new BouncyCastleProvider());

    }
    static String secretKeyAlgorithm = "PBKDF2WithHmacSHA256";
    static String transformationAlgorithm = "AES/CBC/PKCS7PADDING";
    static String provider = "BC";

    public byte[] Decryption(byte[] input) {

        try {
            Database dbSecrets = (Database) SerializedObject.readDB(input);
            byte[] base64decodedBytes = Base64.getDecoder().decode(dbSecrets.getCipherText());
            NonSecrets nonSecrets= dbSecrets.getNonSecrets();
            Cipher cipher = Cipher.getInstance(transformationAlgorithm, provider);
            cipher.init(Cipher.DECRYPT_MODE, SymmetricKey.getSecretKey(), new IvParameterSpec(nonSecrets.getGeneratedIV()));
            byte[] output = cipher.doFinal(base64decodedBytes);
            return output;

        } catch (Exception e) {

        }
        return "".getBytes(StandardCharsets.UTF_8);

    }
    public static void restoreKey () throws Exception {
        byte [] input = FileUtils.readAllBytes(Files.getPasswordFilePath());
        Database dbSecrets = (Database) SerializedObject.readDB(input);
        NonSecrets nonSecrets= dbSecrets.getNonSecrets();
        SymmetricKey.setSecretKey(Secrets.getMasterPassword(),nonSecrets.getStoredSalt()
                ,nonSecrets.getIterationCount(),nonSecrets.getKeyLength(),
                secretKeyAlgorithm,provider);
        FileProtector.salt = nonSecrets.getStoredSalt();
    }

     public static char [] recreateResponse () throws  Exception {
         byte [] input = FileUtils.readAllBytes(Files.getPasswordFilePath());
         Database dbSecrets = (Database) SerializedObject.readDB(input);
         NonSecrets nonSecrets= dbSecrets.getNonSecrets();
         HardwareKeyHandler.cmdResponse(nonSecrets.getChallenge());
         System.out.println("the output IS " + HardwareKeyHandler.getOutput());
         String response = Hex.toHexString(HardwareKeyHandler.output.getBytes(StandardCharsets.UTF_8));
         char [] responseCharArr = response.toCharArray();
         return responseCharArr;
     }
}
