package sample;
import javax.crypto.spec.IvParameterSpec;

import javax.crypto.*;

import org.bouncycastle.jce.provider.BouncyCastleProvider;


import java.nio.charset.StandardCharsets;
import java.security.Security;
import java.util.Base64;

public class DecryptFile  {

    static {
        Security.removeProvider("BC");
        Security.addProvider(new BouncyCastleProvider());

    }


    public byte[] Decryption(byte[] input) {

        try {
            Database dbSecrets = (Database) SerializedObject.readDB(input);
            byte[] base64decodedBytes = Base64.getDecoder().decode(dbSecrets.getCipherText());
            NonSecrets nonSecrets= dbSecrets.getNonSecrets();
            Cipher cipher = Cipher.getInstance(nonSecrets.getStoredAlgorithmModePadding(), nonSecrets.getStoredProvider());
            cipher.init(Cipher.DECRYPT_MODE, SymmetricKey.getSecretKey(), new IvParameterSpec(nonSecrets.getStoredGeneratedIV()));
            byte[] output = cipher.doFinal(base64decodedBytes);
            System.out.println("ratio ten?");
            return output;

        } catch (Exception e) {

        }
        return "".getBytes(StandardCharsets.UTF_8);

    }
    public static void restoreKey () throws Exception {
        byte [] input = FileUtils.readAllBytes(Global.getPasswordFilePath());
        Database dbSecrets = (Database) SerializedObject.readDB(input);
        NonSecrets nonSecrets= dbSecrets.getNonSecrets();
        SymmetricKey.setSecretKey(Secrets.getMasterPassword(),nonSecrets.getStoredSalt()
                ,nonSecrets.getStoredIterationCount(),nonSecrets.getStoredKeyLength(),
                nonSecrets.getStoredSecretKeyAlgorithm(),nonSecrets.getStoredProvider());
        FileProtector.salt = nonSecrets.getStoredSalt();
    }

}
