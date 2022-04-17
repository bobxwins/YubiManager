package sample;
import javax.crypto.spec.IvParameterSpec;

import javax.crypto.*;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.spec.PBEKeySpec;
import javax.crypto.SecretKey;

import java.nio.charset.StandardCharsets;
import java.security.Security;
import java.util.Base64;

public class DecryptFile {
    static {
        Security.removeProvider("BC");
        Security.addProvider(new BouncyCastleProvider());
    }
    public byte[] Decryption() {

        try {
            String folderDir = LoginController.selectedDirectoryPath;
            String pwdDir = LoginController.passwordFilePath;

            byte[] storedkeylength = FileUtils.readAllBytes(folderDir+"KeyLength.txt");

            String keylengthString = new String(storedkeylength);
            int keylengthInt=Integer.parseInt(keylengthString);
            byte[] storedIterationCount = FileUtils.readAllBytes(folderDir+"IterationCount.txt");
            String iterationCountString = new String(storedIterationCount);
            int iterationCountInt=Integer.parseInt( iterationCountString);

            SecretKeyFactory factory =
                    SecretKeyFactory.getInstance("PBKDF2WITHHMACSHA256", "BC");

            PBEKeySpec keySpec = new PBEKeySpec(LoginController.combinedPasswords, getSalt(folderDir), iterationCountInt, keylengthInt);
            System.out.println("key spec is:+++" + keySpec);
            System.out.println("my key spec is:+" + LoginController.combinedPasswords+getSalt(folderDir)+iterationCountInt+keylengthInt);
            SecretKey key = factory.generateSecret(keySpec);
            System.out.println("thsi is the decoded outpuerberbt ");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING", "BC");

            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(getIV(folderDir)));

            byte[] input = FileUtils.readAllBytes(pwdDir);
            byte[] output = cipher.doFinal(input);

            String decodedOutput = new String(output);
            System.out.println("thsi is the decoded output "+ decodedOutput);
            return output;
     //   return decodedOutput;
        } catch (Exception e) {

        }
        return "".getBytes(StandardCharsets.UTF_8);
    }

    public  byte[] getIV (String folderDir) {

        byte[] genIVByte = FileUtils.readAllBytes(folderDir+"GenIV.txt");

        String encodedIV = new String(genIVByte);

        byte[] decodedIV = Base64.getDecoder().decode(encodedIV);

        return decodedIV ;
    }

    public byte[] getSalt (String folderDir) {

        byte[] genSaltbytes = FileUtils.readAllBytes(folderDir+"Salt.txt");

        String encodedSalt = new String(genSaltbytes);

        byte[] decodedSalt = Base64.getDecoder().decode(encodedSalt);

        return decodedSalt ;
    }
}
