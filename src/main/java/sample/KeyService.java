package sample;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.Security;

public class KeyService {



static {
    Security.removeProvider("BC");

    Security.addProvider(new BouncyCastleProvider());
}
  static SecretKey secretKey;

    public static SecretKey getKey() {
        return secretKey;
    }

    public static  void setKey(byte[] saltBytes,byte [] challlengeBytes, int iterationInt, int keyLengthInt) throws Exception
    {
        SecureRandom secureRandom = SecureRandom.getInstance("DEFAULT");
        secureRandom.nextBytes(saltBytes);
        secureRandom.nextBytes(challlengeBytes);
        HardwareKeyService.cmdResponse(Hex.toHexString(challlengeBytes));
        String response = Hex.toHexString(Secrets.getResponse().getBytes(StandardCharsets.UTF_8));
        Secrets.setMasterPassword(Secrets.getManualPassword(),response.toCharArray());
        PBEKeySpec keySpec = new PBEKeySpec(Secrets.getMasterPassword(), saltBytes, iterationInt, keyLengthInt);
        SecretKeyFactory factory =
                SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256", "BC");
        secretKey = factory.generateSecret(keySpec);
    }

    public static void restoreKey () throws Exception {
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
