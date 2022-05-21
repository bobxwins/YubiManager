package sample;

import javax.crypto.spec.IvParameterSpec;

import javax.crypto.*;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.nio.file.Paths;
import java.security.SecureRandom;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.SecretKey;

import java.security.Security;


public class FileProtector {


     static {
        Security.removeProvider("BC");

        Security.addProvider(new BouncyCastleProvider());
    }
    private String folderDir = Global.getSelectedDirectoryPath();

    private    byte[] generatedIV = new byte[16];

    private    byte[] salt = new byte[32];
    private    int iterationCount = 5000;

    private    int keyLength = 192;

    public void encryption() {

        try {
            SecureRandom secureRandom = SecureRandom.getInstance("DEFAULT", "BC");

            secureRandom.nextBytes(generatedIV);

            secureRandom.nextBytes(salt);

            KeySpecs keySpecs = new KeySpecs(generatedIV,salt,iterationCount,keyLength);

            SerializedObject.writeObject(keySpecs,Paths.get(KeySpecs.getKeySpecsDir()));

            PBEKeySpec keySpec = new PBEKeySpec(Global.getCombinedPasswords(), salt, iterationCount, keyLength);

            SecretKeyFactory factory =
                    SecretKeyFactory.getInstance("PBKDF2WITHHMACSHA256", "BC");

            SecretKey key = factory.generateSecret(keySpec);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING", "BC");
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(generatedIV));

            byte[] input = FileUtils.readAllBytes(Global.getPasswordFilePath());
            byte[] output = cipher.doFinal(input);

            FileUtils.write(Global.getPasswordFilePath(), output);

        } catch (Exception e) {

        }

    }

}