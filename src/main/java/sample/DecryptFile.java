package sample;
import javax.crypto.spec.IvParameterSpec;

import javax.crypto.*;

import org.bouncycastle.jce.provider.BouncyCastleProvider;


import java.nio.charset.StandardCharsets;
import java.security.Security;
import java.util.Base64;

public class DecryptFile  {

    static {
        Security.removeProvider("BC");
        Security.addProvider(new BouncyCastleProvider());

    }


    public byte[] Decryption(byte[] input) {

        try {
            byte[] nonSecretsBytes = FileUtils.readAllBytes(NonSecrets.getStoredNonSecrets());

            NonSecrets nonSecrets = SerializedObject.readObject(nonSecretsBytes);

            SymmetricKey.setSecretKey(Global.getCombinedPasswords(),nonSecrets.getStoredSalt()
                    ,nonSecrets.getStoredIterationCount(),nonSecrets.getStoredKeyLength(),
                    nonSecrets.getStoredSecretKeyAlgorithm(),nonSecrets.getStoredProvider());

            Cipher cipher = Cipher.getInstance(nonSecrets.getStoredAlgorithmModePadding(), nonSecrets.getStoredProvider());

            cipher.init(Cipher.DECRYPT_MODE, SymmetricKey.getSecretKey(), new IvParameterSpec(nonSecrets.getStoredGeneratedIV()));

            byte[] output = cipher.doFinal(input);


            return output;

        } catch (Exception e) {

        }
        return "".getBytes(StandardCharsets.UTF_8);
    }


}
