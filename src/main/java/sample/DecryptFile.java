package sample;
import javax.crypto.spec.IvParameterSpec;

import javax.crypto.*;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.spec.PBEKeySpec;
import javax.crypto.SecretKey;

import java.nio.charset.StandardCharsets;
import java.security.Security;

public class DecryptFile  {

    static {
        Security.removeProvider("BC");
        Security.addProvider(new BouncyCastleProvider());
        // a glitch requires me to do this for all classes that uses bouncy castle
    }


    public byte[] Decryption(byte[] input) {

        try {

            KeySpecs keySpecs = (KeySpecs) SerializedObject.readObject(KeySpecs.getKeySpecsDir());

            SecretKeyFactory factory =
                    SecretKeyFactory.getInstance(keySpecs.getSecureRandomAlgorithm(), keySpecs.getProvider());

            PBEKeySpec keySpec = new PBEKeySpec(Global.getCombinedPasswords(),
                    keySpecs.getSalt(), keySpecs.getIterationCount(),keySpecs.getKeyLength());

            SecretKey key = factory.generateSecret(keySpec);

            Cipher cipher = Cipher.getInstance(keySpecs.getAlgorithmModePadding(), keySpecs.getProvider());

            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(keySpecs.getGeneratedIV()));

            byte[] output = cipher.doFinal(input);

            return output;

        } catch (Exception e) {

        }
        return "".getBytes(StandardCharsets.UTF_8);
    }


}
