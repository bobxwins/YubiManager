package sample;

import java.io.Serializable;


public class PasswordRecord implements Serializable {
    private static final long serialVersionUID = 42L;
    PasswordRecord(String titelString, String usernameString, String urlString, String passwordString, String noteString) {
        this.title = titelString;
        this.username = usernameString;
        this.url = urlString;
        this.password = passwordString;
        this.notes = noteString;
    }

    String title, username, url, password, notes;
    PasswordRecord() {}

    public String getTitle() {
        return title;}

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUsername() {

        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getUrl() {

        return url;
    }
    public void setURL(String url) {
        this.url = url;
    }

    public String getPassword() {

        return password;
    }


    public String getNotes() {

        return notes;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }


}

