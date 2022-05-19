package sample;

import java.io.Serializable;


public class RecentFiles implements Serializable {

    //private static final long serialVersionUID = 1L;

    RecentFiles(String fileString) {
        this.recentFiles=fileString;
    }
    String recentFiles;


    public String getRecentFiles() {

        return recentFiles;
    }

    public void setRecentFiles(String recentFiles) {
        this.recentFiles = recentFiles;
    }

}
