package sample;

import javax.crypto.spec.IvParameterSpec;

import javax.crypto.*;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import java.security.SecureRandom;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.SecretKey;

import java.security.Security;

import java.util.Base64;

import java.io.File;
import java.nio.charset.StandardCharsets;


public class EncryptFile {


     static {
        Security.removeProvider("BC");

        Security.addProvider(new BouncyCastleProvider());
    }

    public    String IVdir = "GenIV.txt";

    public    String Saltdir = "Salt.txt";

    public   String IterationCountdir = "IterationCount.txt";

    public    String KeyLengthdir = "KeyLength.txt";

    private static byte[] generatedIV = new byte[16]; //16

    private static byte[] salt = new byte[32]; //32

    private static int iterationCount = 5000;

    private static int keylength = 192;



    public void encryption() {

        try {

            // if entryData == null create empty object, decrypt it

            String folderDir = LoginController.selectedDirectoryPath;
            String pwdDir = LoginController.passwordFilePath;


            SecureRandom secureRandom = SecureRandom.getInstance("DEFAULT", "BC");

            secureRandom.nextBytes(generatedIV);

            secureRandom.nextBytes(salt);

            PBEKeySpec keySpec = new PBEKeySpec(LoginController.combinedPasswords, salt, iterationCount, keylength);

            SecretKeyFactory factory =
                    SecretKeyFactory.getInstance("PBKDF2WITHHMACSHA256", "BC");

            SecretKey key = factory.generateSecret(keySpec);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING", "BC");
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(generatedIV));

            byte[] input = FileUtils.readAllBytes(pwdDir);
            byte[] output = cipher.doFinal(input);

            FileUtils.write(pwdDir, output);

            byte[] EncodedSalt = Base64.getEncoder().encode(salt);

            byte[] EncodedIterationCount = String.valueOf(iterationCount).getBytes(StandardCharsets.UTF_8);

            byte[] EncodedKeyLength = String.valueOf(keylength).getBytes(StandardCharsets.UTF_8);

            byte[] EncodedIV = Base64.getEncoder().encode(generatedIV);

            FileUtils.write(folderDir+Saltdir, EncodedSalt);

            FileUtils.write(folderDir+IterationCountdir, EncodedIterationCount);

            FileUtils.write(folderDir+KeyLengthdir, EncodedKeyLength);

            FileUtils.write(folderDir+IVdir, EncodedIV);



        } catch (Exception e) {


        }

    }

}