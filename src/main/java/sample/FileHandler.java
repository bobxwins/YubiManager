package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

public class FileHandler {


    public static void recentFileExists (TableView<String> recentFilesTable) throws Exception {
        Path path = Paths.get(FilePath.getRecentFileDir());
        if (!java.nio.file.Files.exists(path)) {

        return;
        }
           byte [] input= FileUtils.readAllBytes(FilePath.getRecentFileDir());
           Object serializedOBJ= Serialization.readSerializedObj(input);

            if (  ((Collection<?>) serializedOBJ).size() ==0 ) {
                // only adds the stored ArrayList to the recentFilesDir ObservableList, if there are any elements in the ArrayList
                return;
            }

            FilePath.getRecentFilesDir().addAll((Collection<? extends String>) serializedOBJ);
            // Adds the stored Serialized ArrayList to the ObservableList, then adds the ObservableList to the TableView
            String defaultFile = recentFilesTable.getItems().get(0);
            // sets the default RecentFile to the first element
            FilePath.setPasswordFilePath(defaultFile);
            FilePath.setSelectedDirectoryPath(Paths.get(FilePath.getPasswordFilePath()).getParent() + "\\");

        }


    public static boolean dbExists() throws Exception {

        // if the database doesn't exist, creates a new empty one then encrypts it with an empty ArayList
        // and the default TimerSpecs values

        Path path = Paths.get(FilePath.getPasswordFilePath());
        if (java.nio.file.Files.exists(path)) {
        return true;
        }
            FileUtils.write(FilePath.getPasswordFilePath(), "".getBytes(StandardCharsets.UTF_8));
            FileProtector fileProtector = new FileProtector();
            ObservableList<Entry> entryData = FXCollections.observableArrayList();
            entryData.add(new Entry("", "", "",
                    "", ""));

            TimerSpecs defaultTimer = new TimerSpecs(8,false);
            TimerSpecs.setTimerSpecs(defaultTimer);
            fileProtector.encryption(entryData, defaultTimer);
            return false;
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
