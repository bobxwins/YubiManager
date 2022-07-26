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

    public void encryption(ObservableList observableList, Object timerSpecs) {

        try {
            createKey();
            secureRandom.nextBytes(generatedIV);
            Cipher cipher = Cipher.getInstance(transformationAlgorithm, provider);
            cipher.init(Cipher.ENCRYPT_MODE, SymmetricKey.getSecretKey(), new IvParameterSpec(generatedIV));

            Secrets secrets = new Secrets();
            byte[] inputSecrets = SerializedObject.serializeDB(databaseSecrets(secrets, observableList, timerSpecs));

            byte[] outputSecrets;

            Database database = new Database();

            outputSecrets = cipher.doFinal(inputSecrets);
            String encoded = Base64.getEncoder().encodeToString(outputSecrets);
            database.setCipherText(encoded);
            String header = Authentication.generateHmac(database.getCipherText(), SymmetricKey.getSecretKey());
            NonSecrets nonSecrets = new NonSecrets(generatedIV, salt, iterationCount, keyLength,
                     header,new String(challenge));
            nonSecrets.setChallenge(Hex.toHexString(challenge));
            database.setNonSecrets(nonSecrets);
            byte[] dbSerialized = SerializedObject.serializeDB(database);
            FileUtils.write(Files.getPasswordFilePath(), dbSerialized);

        } catch (Exception e) {

        }
    }


    public static void createKey() throws Exception {

        secureRandom = SecureRandom.getInstance(secureRandomAlgorithm);
        secureRandom.nextBytes(challenge);
        secureRandom.nextBytes(salt);
        HardwareKeyHandler.cmdResponse(Hex.toHexString(challenge));
       // Thread.sleep(1900);
        // the CMD process needs to be completed before the output can be used as a String
        String response = Hex.toHexString(HardwareKeyHandler.output.getBytes(StandardCharsets.UTF_8));
     //   String response = Hex.toHexString(Clipboard.getSystemClipboard().getString().getBytes(StandardCharsets.UTF_8));
        Secrets.setMasterPassword(Secrets.getManualPassword(),response.toCharArray());
        SymmetricKey.setSecretKey(Secrets.getMasterPassword(), salt, iterationCount, keyLength,
                secretKeyAlgorithm, provider);
    }

    public Secrets databaseSecrets(Secrets secrets, ObservableList observableList, Object object) {

        try {
            secrets.setTimerSpecs((TimerSpecs) object);
            secrets.setEntry(observableList);
            return secrets;
        } catch (Exception e) {
        }
        return null;
    }


}