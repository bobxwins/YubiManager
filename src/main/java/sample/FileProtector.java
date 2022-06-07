package sample;

import javax.crypto.spec.IvParameterSpec;

import javax.crypto.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.bouncycastle.crypto.io.SignerOutputStream;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.security.SecureRandom;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.SecretKey;

import java.security.Security;


public class FileProtector {

     static {
        Security.removeProvider("BC");

        Security.addProvider(new BouncyCastleProvider());
    }

    private    byte[] generatedIV = new byte[16];

    private    byte[] salt = new byte[32];
    private    int iterationCount = 5000;

    private int keyLength = 192;
   private String secureRandomAlgorithm = "DEFAULT";
   private String secretKeyalgorithm = "PBKDF2WITHHMACSHA256";
   private String provider = "BC";
   private String algorithmModePadding ="AES/CBC/PKCS5PADDING";

    public void encryption(ObservableList observableList,Object object) {

        try {
            SecureRandom secureRandom = SecureRandom.getInstance(secureRandomAlgorithm, provider);

            secureRandom.nextBytes(generatedIV);

            secureRandom.nextBytes(salt);

            KeySpecs keySpecs = new KeySpecs(generatedIV,salt,iterationCount,keyLength, secretKeyalgorithm,provider, algorithmModePadding);

            SerializedObject.writeObject(keySpecs,Paths.get(KeySpecs.getKeySpecsDir()));


            PBEKeySpec keySpec = new PBEKeySpec(Global.getCombinedPasswords(), salt, iterationCount, keyLength);

            SecretKeyFactory factory =
                    SecretKeyFactory.getInstance(secretKeyalgorithm, provider);

            SecretKey key = factory.generateSecret(keySpec);

            Cipher cipher = Cipher.getInstance(algorithmModePadding, provider);
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(generatedIV));

           byte[] inputTimerSpec = SerializedObject.serializeObject(object);

           byte[] outputTimerSpecs = cipher.doFinal(inputTimerSpec);

           FileUtils.write(TimerSpecs.getTimerSpecsDir(), outputTimerSpecs);

           byte[] inputEntry = SerializedObject.serializeObservableList(observableList);
           byte[] outputEntry = cipher.doFinal(inputEntry);
           FileUtils.write(Global.getPasswordFilePath(), outputEntry);
        } catch (Exception e) {

        }

    }

}