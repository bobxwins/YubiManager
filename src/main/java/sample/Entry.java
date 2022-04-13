package sample;

import java.io.Serializable;


import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class Entry implements Serializable {
     String passwordString = new String();

    Entry(String titelString, String usernameString, String urlString, String passwordString,String noteString) {
        this.titel=new TextField(titelString);
        this.username=new TextField(usernameString);
        this.url=new TextField(urlString);
          this.password=new TextField(passwordString);
     //   this.password=new PasswordField();
        this.notes=new TextField(noteString);

    }
    TextField titel, username, url,password, notes;
   // PasswordField password;

    public TextField getTitel() {
     titel.setDisable(true);
        return titel;
    }

    public TextField getUsername() {

        return username;
    }

    public TextField getUrl() {

        return url;
    }

     public TextField getPassword() {

        return password;
    }


   /* public PasswordField getPassword() {
        password.setText(password.getText());
        return password;
    }
    */

    public TextField getNotes() {

        return notes;
    }


}
