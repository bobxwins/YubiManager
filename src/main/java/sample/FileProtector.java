package sample;

import javax.crypto.spec.IvParameterSpec;

import javax.crypto.*;

import javafx.collections.ObservableList;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

import java.security.SecureRandom;
import java.security.Security;
import java.util.Base64;

public class FileProtector {

    static {
        Security.removeProvider("BC");

        Security.addProvider(new BouncyCastleProvider());
    }

    public void encryption(ObservableList entry, TimerSpecs timerSpecs) throws Exception {
        byte[] salt = new byte[32]; // Salt should be at least 128 bits according to NIST
        byte[] challenge = new byte[64];
        byte[] generatedIV = new byte[16]; // IV is always 128 bits
        int iterationCount = 150000;
        int keyLength = 256;
        AESKey.setAESKey(salt,challenge,iterationCount,keyLength);
        // creates the secret key
        SecureRandom secureRandom = SecureRandom.getInstance("DEFAULT");
        secureRandom.nextBytes(generatedIV);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7PADDING", "BC");
        cipher.init(Cipher.ENCRYPT_MODE, AESKey.getAESKey(), new IvParameterSpec(generatedIV));
        Secrets secrets = new Secrets();
        secrets.setTimerSpecs(timerSpecs);
        secrets.setPwdRecord(entry);
        byte[] plainText = Serialization.entrySerialize(secrets);
        byte[] cipherText = cipher.doFinal(plainText);
        String encodedCipherText = Base64.getEncoder().encodeToString(cipherText);
        String MacTag = Authentication.computeAuthMacTag(encodedCipherText, AESKey.getAESKey());
        String challengeString= Hex.toHexString(challenge);
        NonSecrets nonSecrets = new NonSecrets(generatedIV, salt, iterationCount, keyLength,
                    MacTag,challengeString,encodedCipherText);
        Database database = new Database();
        database.setNonSecrets(nonSecrets);
        byte[] dbSerialized = Serialization.entrySerialize(database);
        FileUtils.write(FilePath.getCurrentDBdir(), dbSerialized);
    }

}