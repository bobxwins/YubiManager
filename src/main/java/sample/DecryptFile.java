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
    static String transformationAlgorithm = "AES/CBC/PKCS7PADDING";
    static String provider = "BC";

    public byte[] Decryption(byte[] input) {
        try {
            Database dbSecrets = (Database) Serialization.readSerializedObj(input);
            NonSecrets nonSecrets= dbSecrets.getNonSecrets();
            byte[] decodedCipher = Base64.getDecoder().decode(nonSecrets.getCipherText());
            Cipher cipher = Cipher.getInstance(transformationAlgorithm, provider);
            cipher.init(Cipher.DECRYPT_MODE, AESKey.getAESKey(), new IvParameterSpec(nonSecrets.getGeneratedIV()));
            byte[] output = cipher.doFinal(decodedCipher);
            return output;
        } catch (Exception e) {
        }
        return "".getBytes(StandardCharsets.UTF_8);
    }

     public static char [] recreateResponse () throws  Exception {
         byte [] input = FileUtils.readAllBytes(FilePath.getCurrentDBdir());
         Database dbSecrets = (Database) Serialization.readSerializedObj(input);
         NonSecrets nonSecrets= dbSecrets.getNonSecrets();
         SecurityTokenService.responseMacTag(nonSecrets.getChallenge());
         System.out.println("the output IS " + Secrets.getResponse());
         String response = Hex.toHexString(Secrets.getResponse().getBytes(StandardCharsets.UTF_8));
         char [] responseCharArr = response.toCharArray();
         return responseCharArr;
     }
}
