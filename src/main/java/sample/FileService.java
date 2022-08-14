package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

public class FileService {


    public static void dbFilesListExists(TableView<String> recentFilesTable) throws Exception {
        Path path = Paths.get(FilePath.getDBFilesListDir());
        if (!java.nio.file.Files.exists(path)) {
        return;
        }
           byte [] input= FileUtils.readAllBytes(FilePath.getDBFilesListDir());
           Object serializedOBJ= Serialization.readSerializedObj(input);

            if (  ((Collection<?>) serializedOBJ).size() ==0 ) {
                // only adds the stored ArrayList to the recentFilesDir ObservableList, if there are any elements in the ArrayList
                return;
            }

            FilePath.getDbFilesList().addAll((Collection<? extends String>) serializedOBJ);
            // Adds the stored Serialized ArrayList to the ObservableList, then adds the ObservableList to the TableView
            String defaultFile = recentFilesTable.getItems().get(0);
            // sets the default RecentFile to the first element
            FilePath.setCurrentDBdir(defaultFile);
            FilePath.setSelectedDir(Paths.get(FilePath.getCurrentDBdir()).getParent() + "\\");

        }


    public static boolean dbExists() throws Exception {

        Path path = Paths.get(FilePath.getCurrentDBdir());
        if (java.nio.file.Files.exists(path)) {
            // If the Database exists, return
        return true;
        }
        // if the database doesn't exist,a new one is created with an empty ArrayList and default TimerSpecs value
            FileUtils.write(FilePath.getCurrentDBdir(), "".getBytes(StandardCharsets.UTF_8));
            FileProtector fileProtector = new FileProtector();
            ObservableList<PasswordRecord> passwordRecordData = FXCollections.observableArrayList();
            passwordRecordData.add(new PasswordRecord("", "", "",
                    "", ""));
            TimerSpecs defaultTimer = new TimerSpecs(8,false);
            TimerSpecs.setTimerSpecs(defaultTimer);
            fileProtector.encryption(passwordRecordData, defaultTimer);
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
