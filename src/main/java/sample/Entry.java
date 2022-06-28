package sample;

import java.io.Serializable;


public class Entry implements Serializable {

    //private static final long serialVersionUID = 1L;

    Entry(String titelString, String usernameString, String urlString, String passwordString, String noteString) {
        this.title = titelString;
        this.username = usernameString;
        this.url = urlString;
        this.password = passwordString;
        this.notes = noteString;
    }

   Entry (int timerInt,  boolean selectedCheckBoxBool)
    {
        this.timer  =timerInt;
        this.selectedCheckBox=selectedCheckBoxBool;
    }
    Entry () {}

    int timer;

    boolean selectedCheckBox ;

    String title, username, url, password, notes;


    public String getTitle() {

        return title;
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


    public String getNotes() {

        return notes;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public int getTimer() {
        return timer;
    }
    public boolean getSelectedCheckBox() {
        return selectedCheckBox;
        // no setter for TimerSpecs selected checkbox is made, as the selectedCheckBox is set when clicking the CheckBox Object
        // when running the application
    }


    public static String getTimerSpecsDir() {
        String timerSpecsDir = Global.getSelectedDirectoryPath() + "Timer.aes";
        return timerSpecsDir;
    }
    public static TimerSpecs getTimerSpecs () {
        byte[] input = FileUtils.readAllBytes(TimerSpecs.getTimerSpecsDir());
        DecryptFile decryptFile = new DecryptFile();
        TimerSpecs timerSpecs =  SerializedObject.readTimerSpecs(decryptFile.Decryption(input));
        return timerSpecs;
    }
}

