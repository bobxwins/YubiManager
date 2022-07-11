package sample;


import java.io.Serializable;

public class Database implements Serializable {

    Database ( ) {

    }
    Secrets secrets;
    String cipherText;
    NonSecrets nonSecrets;
   // private static final long serialVersionUID = 6529685098267757690L;

    public String getCipherText() {
        return cipherText;
    }

    public void setCipherText(String cipherTextString) {
        this.cipherText = cipherTextString;
    }

    public Secrets getSecrets() {
        return secrets;
    }

    public void setSecrets(Secrets secrets) {
        this.secrets = secrets;
    }
    public NonSecrets getNonSecrets() {
        return nonSecrets;
    }

    public void setNonSecrets(NonSecrets nonSecrets) {
        this.nonSecrets = nonSecrets;
    }

}


