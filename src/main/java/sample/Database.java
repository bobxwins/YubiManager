package sample;


import java.io.Serializable;

public class Database implements Serializable {

    private static final long serialVersionUID = 42L;
    // To ensure a consistent serialVersionUID value across different java compiler implementations,
    // a serialVersionUID value is explicitly declared
    Database ( ) {

    }
    Secrets secrets;
    NonSecrets nonSecrets;

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


