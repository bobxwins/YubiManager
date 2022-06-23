package sample;

import javax.crypto.spec.IvParameterSpec;

import javax.crypto.*;

import javafx.collections.ObservableList;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.nio.file.Paths;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Base64;
import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class FileProtector {

     static {
        Security.removeProvider("BC");

        Security.addProvider(new BouncyCastleProvider());
    }
  static  String secureRandomAlgorithm = "DEFAULT";
  static  String secretKeyAlgorithm = "PBKDF2WithHmacSHA512";
  //  static  String secretKeyAlgorithm = "PBKDF2WithHmacSHA3-512";
  static  String transformationAlgorithm ="AES/CBC/PKCS7PADDING";
    // The secret key algoirthm
  static  String provider = "BC";
    static byte[] salt = new byte[32]; // Salt is always at least 128 bits
    static int iterationCount = 100;
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

    Argon2BytesGenerator generator = new Argon2BytesGenerator();
    Argon2Parameters.Builder builder = new Argon2Parameters.Builder();

    public void encryption(ObservableList observableList,Object object) {

        try {
            secureRandom = SecureRandom.getInstance(secureRandomAlgorithm, provider);
            secureRandom.nextBytes(generatedIV);
            Cipher cipher = Cipher.getInstance(transformationAlgorithm, provider);

            cipher.init(Cipher.ENCRYPT_MODE, SymmetricKey.getSecretKey(), new IvParameterSpec(generatedIV));

            byte[] inputTimerSpec = SerializedObject.serializeObject(object);

            byte[] outputTimerSpecs = cipher.doFinal(inputTimerSpec);

            FileUtils.write(TimerSpecs.getTimerSpecsDir(), outputTimerSpecs);

            byte[] inputEntry = SerializedObject.readMemoryObservableList(observableList);
            byte[] outputEntry = cipher.doFinal(inputEntry);
            FileUtils.write(Global.getPasswordFilePath(), outputEntry);
            NonSecrets nonSecrets = new NonSecrets(generatedIV,salt,iterationCount,keyLength,
                    secureRandomAlgorithm, secretKeyAlgorithm,provider, transformationAlgorithm);
            SerializedObject.writeObject(nonSecrets,Paths.get(NonSecrets.getStoredNonSecrets()));

        } catch (Exception e) {

        }
    }
        public static void createKey (char [] password) throws Exception {

            SecureRandom secureRandom = SecureRandom.getInstance(secureRandomAlgorithm, provider);
            secureRandom.nextBytes(salt);
            SymmetricKey.setSecretKey(password, salt, iterationCount, keyLength,
                    secretKeyAlgorithm, provider);

        }

}