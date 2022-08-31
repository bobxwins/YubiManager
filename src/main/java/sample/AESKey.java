package sample;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.Security;

public class AESKey {

static {
    Security.removeProvider("BC");

    Security.addProvider(new BouncyCastleProvider());
}
  static SecretKey secretKey;

    public static SecretKey getAESKey() {
        return secretKey;
    }

    public static  void setAESKey(byte[] saltBytes, byte [] challlengeBytes, int iterationInt, int keyLengthInt) throws Exception
    {
        SecureRandom secureRandom = SecureRandom.getInstance("DEFAULT");
        secureRandom.nextBytes(saltBytes);
        secureRandom.nextBytes(challlengeBytes);
        SecurityTokenService.responseMacTag(Hex.toHexString(challlengeBytes));
        String response = Hex.toHexString(Secrets.getResponse().getBytes(StandardCharsets.UTF_8));
        Secrets.setMasterPassword(Secrets.getUserCredential(),response.toCharArray());
        PBEKeySpec keySpec = new PBEKeySpec(Secrets.getMasterPassword(), saltBytes, iterationInt, keyLengthInt);
        SecretKeyFactory factory =
                SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256", "BC");
        secretKey = factory.generateSecret(keySpec);
    }

    public static void restoreAESKey() throws Exception {
        byte [] input = FileUtils.readAllBytes(FilePath.getCurrentDBdir());
        Database dbSecrets = (Database) Serialization.readSerializedObj(input);
        NonSecrets nonSecrets= dbSecrets.getNonSecrets();
        PBEKeySpec keySpec = new PBEKeySpec(Secrets.getMasterPassword(), nonSecrets.getStoredSalt(),
                nonSecrets.getIterationCount(), nonSecrets.getKeyLength());
        SecretKeyFactory factory =
                SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256", "BC");
        secretKey = factory.generateSecret(keySpec);
    }
}
