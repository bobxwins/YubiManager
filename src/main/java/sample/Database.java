package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


import java.io.Serializable;
import java.util.ArrayList;

public class Database implements Serializable {

    Database ( ) {

    }
    Secrets secrets;
    String secretsString;
    NonSecrets nonSecrets;

    public String getSecretString() {
        return secretsString;
    }

    public void setSecretString(String secretStrings) {
        this.secretsString = secretStrings;
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


