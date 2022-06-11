package sample;

import javax.crypto.spec.IvParameterSpec;

import javax.crypto.*;

import javafx.collections.ObservableList;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.nio.file.Paths;

import java.security.SecureRandom;
import java.security.Security;
import java.util.Base64;


public class FileProtector {

     static {
        Security.removeProvider("BC");

        Security.addProvider(new BouncyCastleProvider());
    }
  static  String secureRandomAlgorithm = "DEFAULT";
  static  String secretKeyAlgorithm = "PBKDF2WITHHMACSHA256";
  static  String transformationAlgorithm ="AES/CBC/PKCS5PADDING";
    // The secret key algoirthm
  static  String provider = "BC";
    static byte[] salt = new byte[32]; // Salt is always at least 128 bits
    static int iterationCount = 100000;
    static int keyLength = 256;
    static byte [] generatedIV;

    public void encryption(ObservableList observableList,Object object) {

        try {
            generatedIV = new byte[16]; // IV is always 128 bits
            SecureRandom secureRandom = SecureRandom.getInstance (secureRandomAlgorithm,provider);
            secureRandom.nextBytes(generatedIV);
            secureRandom.nextBytes(salt);

            Cipher cipher = Cipher.getInstance(transformationAlgorithm, provider);
            SymmetricKey.setSecretKey(Global.getCombinedPasswords(),salt,iterationCount,keyLength,
                    secretKeyAlgorithm,provider);
            cipher.init(Cipher.ENCRYPT_MODE, SymmetricKey.getSecretKey(), new IvParameterSpec(generatedIV));

           byte[] inputTimerSpec = SerializedObject.serializeObject(object);

           byte[] outputTimerSpecs = cipher.doFinal(inputTimerSpec);

           FileUtils.write(TimerSpecs.getTimerSpecsDir(), outputTimerSpecs);

           byte[] inputEntry = SerializedObject.readMemoryObservableList(observableList);
           byte[] outputEntry = cipher.doFinal(inputEntry);
           FileUtils.write(Global.getPasswordFilePath(), outputEntry);
          storeNonSecrets();

        } catch (Exception e) {

        }

    }
    public static void storeNonSecrets () throws Exception {

        NonSecrets nonSecrets = new NonSecrets(generatedIV,salt,iterationCount,keyLength,secureRandomAlgorithm, secretKeyAlgorithm,provider, transformationAlgorithm);
        SerializedObject.writeObject(nonSecrets,Paths.get(NonSecrets.getStoredNonSecrets()));
    }
}