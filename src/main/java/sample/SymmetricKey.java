package sample;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import java.security.Security;
import java.util.Base64;

public class SymmetricKey {



static {
    Security.removeProvider("BC");

    Security.addProvider(new BouncyCastleProvider());
}
  static SecretKey secretKey;
   // The IV is not used by the key, but generated in this class
    // as it's stored as a serialized object along with the other non-secrets, used to create the secret key.

    public static SecretKey getSecretKey() {
        return secretKey;
    }

    public static  void setSecretKey(char [] Password
             ,byte [] saltBytes, int iterationInt, int keyLengthInt
            , String secretKeyAlgorithmString , String providerString

    ) throws Exception {
        PBEKeySpec keySpec = new PBEKeySpec(Password, saltBytes, iterationInt, keyLengthInt);
        SecretKeyFactory factory =
                SecretKeyFactory.getInstance(secretKeyAlgorithmString, providerString);
         secretKey = factory.generateSecret(keySpec);
    }


}
