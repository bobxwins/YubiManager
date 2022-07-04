package sample;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Base64;

public class SymmetricKey {



static {
    Security.removeProvider("BC");

    Security.addProvider(new BouncyCastleProvider());
}
  static SecretKey secretKey;

    public static SecretKey getSecretKey() {
        return secretKey;
    }

    public static  void setSecretKey(char [] Password
             ,byte [] saltBytes, int iterationInt, int keyLengthInt
            , String secretKeyAlgorithmString , String providerString
            ) throws Exception
    {

        PBEKeySpec keySpec = new PBEKeySpec(Password, saltBytes, iterationInt, keyLengthInt);
        SecretKeyFactory factory =
                SecretKeyFactory.getInstance(secretKeyAlgorithmString, providerString);
         secretKey = factory.generateSecret(keySpec);

    }


}
