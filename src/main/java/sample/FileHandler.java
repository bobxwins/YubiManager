package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileHandler {


    public static void recentFileExists (TableView<String> recentFilesTable) throws Exception {
        Path path = Paths.get(Global.getRecentFilesDir());
        if (Files.exists(path)) {
            Global.getRecentFilesData().addAll(SerializedObject.readFileObservableList(FileUtils.readAllBytes(Global.getRecentFilesDir())));
            String defaultFile = recentFilesTable.getItems().get(0);
            // sets the default RecentFile to the first element
            Global.setPasswordFilePath(defaultFile);
            Global.setSelectedDirectoryPath(Paths.get(Global.getPasswordFilePath()).getParent() + "\\");
      //      return false;
        }
      //  return true;
    }

    public static boolean dbExists() throws Exception {
        Path path = Paths.get(Global.getPasswordFilePath());
        if (!Files.exists(path)) {
            FileUtils.write(Global.getPasswordFilePath(), "".getBytes(StandardCharsets.UTF_8));
            FileProtector.createKey(Secrets.getMasterPassword());
            FileProtector fileProtector = new FileProtector();
            ObservableList<Entry> entryData = FXCollections.observableArrayList();
            entryData.add(new Entry("", "", "",
                    "", ""));
            ObservableList observableList = FXCollections.emptyObservableList();
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
                if (! Files.isSymbolicLink(f.toPath())) {
                    deleteDir(f);
                }
            }
        }
        file.delete();
    }


}
