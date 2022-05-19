package sample;

import javax.crypto.spec.IvParameterSpec;

import javax.crypto.*;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.SecureRandom;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.SecretKey;

import java.security.Security;

import java.util.Base64;

import java.nio.charset.StandardCharsets;


public class FileProtector {


     static {
        Security.removeProvider("BC");

        Security.addProvider(new BouncyCastleProvider());
    }
    private String folderDir = Global.getSelectedDirectoryPath();
    private String IVdir = folderDir+"GenIV.txt";

    private String Saltdir = folderDir+ "Salt.txt";

    private String IterationCountdir = folderDir+"IterationCount.txt";

    private String KeyLengthdir = folderDir+"KeyLength.txt";

    private static byte[] generatedIV = new byte[16]; //16

    private static byte[] salt = new byte[32]; //32

    private static int iterationCount = 5000;

    private static int keylength = 256;

    public void encryption() {

        try {


            SecureRandom secureRandom = SecureRandom.getInstance("DEFAULT", "BC");

            secureRandom.nextBytes(generatedIV);

            secureRandom.nextBytes(salt);

            PBEKeySpec keySpec = new PBEKeySpec(Global.getCombinedPasswords(), salt, iterationCount, keylength);

            SecretKeyFactory factory =
                    SecretKeyFactory.getInstance("PBKDF2WITHHMACSHA256", "BC");

            SecretKey key = factory.generateSecret(keySpec);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING", "BC");
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(generatedIV));

            byte[] input = FileUtils.readAllBytes(Global.getPasswordFilePath());
            byte[] output = cipher.doFinal(input);

            FileUtils.write(Global.getPasswordFilePath(), output);

            byte[] EncodedSalt = Base64.getEncoder().encode(salt);

            byte[] EncodedIterationCount = String.valueOf(iterationCount).getBytes(StandardCharsets.UTF_8);

            byte[] EncodedKeyLength = String.valueOf(keylength).getBytes(StandardCharsets.UTF_8);

            byte[] EncodedIV = Base64.getEncoder().encode(generatedIV);

            FileUtils.write(Saltdir, EncodedSalt);

            FileUtils.write(IterationCountdir, EncodedIterationCount);

            FileUtils.write(KeyLengthdir, EncodedKeyLength);

            FileUtils.write(IVdir, EncodedIV);
  // serialize Saltdir,IterationCountdir,KeyLengthdir + ivDir til en enkelt fil


        } catch (Exception e) {

        }

    }

}