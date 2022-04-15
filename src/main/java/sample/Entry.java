package sample;

import java.io.Serializable;


import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Entry implements Serializable {


    Entry(String titelString, String usernameString, String urlString, String passwordString,String noteString) {
        this.titel=titelString;
        this.username=usernameString;
        this.url=urlString;
          this.password=passwordString;

        this.notes=noteString;

    }
    String titel, username, url,password,notes;


    public String getTitel() {

        return titel;
    }

    public String getUsername() {

        return username;
    }

    public String getUrl() {

        return url;
    }

     public String getPassword() {

        return password;
    }


   /* public PasswordField getPassword() {
        password.setText(password.getText());
        return password;
    }
    */

    public String getNotes() {

        return notes;
    }


}
