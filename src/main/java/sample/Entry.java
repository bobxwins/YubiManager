package sample;

import java.io.Serializable;


public class Entry implements Serializable {

    private static final long serialVersionUID = 1L;

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
    public void setTitel(String titel) {
        this.titel = titel;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public void setURL(String url) {
        this.url = url;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

}
