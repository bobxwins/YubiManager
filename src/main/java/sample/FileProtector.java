package sample;

import javax.crypto.spec.IvParameterSpec;

import javax.crypto.*;

import javafx.collections.ObservableList;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Base64;

public class FileProtector {

    static {
        Security.removeProvider("BC");

        Security.addProvider(new BouncyCastleProvider());
    }

    static String secureRandomAlgorithm = "DEFAULT";
    static String secretKeyAlgorithm = "PBKDF2WithHmacSHA256";
    static String transformationAlgorithm = "AES/CBC/PKCS7PADDING";
    static String provider = "BC";

    static byte[] salt = new byte[32]; // Salt should be at least 128 bits according to NIST
    static int iterationCount = 150000;
    static int keyLength = 256;
    static byte[] generatedIV = new byte[16]; // IV is always 128 bits
    static byte[] challenge = new byte[40];
    static SecureRandom secureRandom ;

    public void encryption(ObservableList entry, TimerSpecs timerSpecs) throws Exception {
            createKey();
            secureRandom.nextBytes(generatedIV);
            Cipher cipher = Cipher.getInstance(transformationAlgorithm, provider);
            cipher.init(Cipher.ENCRYPT_MODE, SymmetricKey.getSecretKey(), new IvParameterSpec(generatedIV));
            Secrets secrets = new Secrets();
            secrets.setTimerSpecs(timerSpecs);
            secrets.setEntry(entry);
            byte[] plainText = Serialization.entrySerialize(secrets);
            byte[] cipherText = cipher.doFinal(plainText);
            String encodedCipherText = Base64.getEncoder().encodeToString(cipherText);
            String MacTag = Authentication.generateHmac(encodedCipherText, SymmetricKey.getSecretKey());
            String challengeString= Hex.toHexString(challenge);
            NonSecrets nonSecrets = new NonSecrets(generatedIV, salt, iterationCount, keyLength,
                    MacTag,challengeString,encodedCipherText);
            Database database = new Database();
            database.setNonSecrets(nonSecrets);
            byte[] dbSerialized = Serialization.entrySerialize(database);// Both the secrets and non-secrets objects are stored in the same file
            FileUtils.write(FilePath.getPasswordFilePath(), dbSerialized);
    }


    public static void createKey() throws Exception {
        secureRandom = SecureRandom.getInstance(secureRandomAlgorithm);
        secureRandom.nextBytes(challenge);
        secureRandom.nextBytes(salt);
        HardwareKeyCmd.cmdResponse(Hex.toHexString(challenge));
        String response = Hex.toHexString(HardwareKeyCmd.output.getBytes(StandardCharsets.UTF_8));
        Secrets.setMasterPassword(Secrets.getManualPassword(),response.toCharArray());
        SymmetricKey.setSecretKey(Secrets.getMasterPassword(), salt, iterationCount, keyLength,
                secretKeyAlgorithm, provider);
    }

}