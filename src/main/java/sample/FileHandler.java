package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileHandler {


    public static void recentFileExists (TableView<String> recentFilesTable) throws Exception {
        Path path = Paths.get(Files.getRecentFilesDir());
        if (java.nio.file.Files.exists(path)) {
            Files.getRecentFilesData().addAll(SerializedObject.readFileObservableList(FileUtils.readAllBytes(Files.getRecentFilesDir())));
            // Adds the stored Serialized ArrayList to the TableView
            String defaultFile = recentFilesTable.getItems().get(0);
            // sets the default RecentFile to the first element
            Files.setPasswordFilePath(defaultFile);
            Files.setSelectedDirectoryPath(Paths.get(Files.getPasswordFilePath()).getParent() + "\\");
      //      return false;
        }
      //  return true;
    }

    public static boolean dbExists() throws Exception {

        // if the database doesn't exist, creates a new empty one then encrypts it with an empty ArayList
        // and the default TimerSpecs values

        Path path = Paths.get(Files.getPasswordFilePath());
        if (!java.nio.file.Files.exists(path)) {
            FileUtils.write(Files.getPasswordFilePath(), "".getBytes(StandardCharsets.UTF_8));
            FileProtector fileProtector = new FileProtector();
            ObservableList<Entry> entryData = FXCollections.observableArrayList();
            entryData.add(new Entry("", "", "",
                    "", ""));

            TimerSpecs defaultTimer = new TimerSpecs(8,false);
            TimerSpecs.setTimerSpecs(defaultTimer);
            fileProtector.encryption(entryData, defaultTimer);
            return false;
        }
        return true;
    }
    void deleteDir(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                if (! java.nio.file.Files.isSymbolicLink(f.toPath())) {
                    deleteDir(f);
                }
            }
        }
        file.delete();
    }


}
