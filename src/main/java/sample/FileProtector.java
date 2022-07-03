package sample;

import javax.crypto.spec.IvParameterSpec;

import javax.crypto.*;

import javafx.collections.ObservableList;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Base64;

public class FileProtector {

    static {
        Security.removeProvider("BC");

        Security.addProvider(new BouncyCastleProvider());
    }
    static  String secureRandomAlgorithm = "DEFAULT";
    static  String secretKeyAlgorithm = "PBKDF2WithHmacSHA512";
    static  String transformationAlgorithm ="AES/CBC/PKCS7PADDING";
    static  String provider = "BC";
    static byte[] salt =new byte[32]; // Salt is always at least 128 bits
    static int iterationCount = 100000;
    static int keyLength = 256;
    static byte[] generatedIV = new byte[16]; // IV is always 128 bits
    static SecureRandom secureRandom;

  static {
        try {
            secureRandom = SecureRandom.getInstance(secureRandomAlgorithm, provider);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
    }


    public void encryption(ObservableList observableList,Object timerSpecs) {

        try {
            secureRandom = SecureRandom.getInstance(secureRandomAlgorithm);
            secureRandom.nextBytes(generatedIV);
            Cipher cipher = Cipher.getInstance(transformationAlgorithm, provider);

            cipher.init(Cipher.ENCRYPT_MODE, SymmetricKey.getSecretKey(), new IvParameterSpec(generatedIV));

            Secrets secrets = new Secrets();
            byte[] inputSecrets = SerializedObject.serializeDB(databaseSecrets(secrets,observableList,timerSpecs));
            byte[] outputSecrets = cipher.doFinal(inputSecrets);

            NonSecrets nonSecrets = new NonSecrets(generatedIV,salt,iterationCount,keyLength,
                    secureRandomAlgorithm, secretKeyAlgorithm,provider, transformationAlgorithm);

            Database database = new Database();
            database.setNonSecrets(nonSecrets);
            String encoded = Base64.getEncoder().encodeToString(outputSecrets);
            database.setSecretString(encoded);
            byte[] dbSerialized= SerializedObject.serializeDB(database);
            FileUtils.write(Global.getPasswordFilePath(), dbSerialized);
            System.out.println("DID IT WORK?????");

        } catch (Exception e) {

        }
    }


    public static void createKey (char [] password) throws Exception {
        secureRandom.nextBytes(salt);
        SymmetricKey.setSecretKey(password, salt, iterationCount, keyLength,
                secretKeyAlgorithm, provider);
    }


    public Secrets databaseSecrets( Secrets secrets,ObservableList observableList, Object object) {

        try {
            secrets.setHeader("This is NOT a header");
            secrets.setTimerSpecs((TimerSpecs) object);
            secrets.setEntry(observableList);
            return secrets;
        } catch (Exception e) {
        }
        return null;
    }

}