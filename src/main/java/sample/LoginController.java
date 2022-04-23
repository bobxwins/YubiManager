package sample;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.*;

import javafx.stage.*;
import org.apache.commons.lang3.ArrayUtils;

import java.io.File;

import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

import java.util.stream.Stream;


public class LoginController {

    @FXML
    private Button btnSignIn;

    @FXML
    private Button btnCreateDB;

    @FXML
    private PasswordField mpField;

    @FXML
    private PasswordField ybkSecret;

    @FXML
    private Label recentFileLabel ;
    @FXML
    private Label labelEnterPwd;

    @FXML
    private Menu menuRecent;
// save as excel file


    private ObservableList<Entry> entryData = FXCollections.observableArrayList();

    public static String defaultPath = System.getProperty("user.dir") + "/resources/sample/passwords";
    public static String passwordFilePath; // filepath to the password file currently in use
    public static String selectedDirectoryPath;
    public static String recentFiles = defaultPath+"/RecentFiles.txt";
    public static char[] combinedPasswords;

    @FXML
    void login(ActionEvent event) throws Exception {
        String recentFilesString = new String(FileUtils.readAllBytes(recentFiles));

        String[] rFSArray = recentFilesString.split(",");
        passwordFilePath = rFSArray[0];
        selectedDirectoryPath = new File(passwordFilePath).getAbsoluteFile().getParent()+"\\";
        DatabaseHandler databaseHandler = new DatabaseHandler();
         if (databaseHandler.loginAuthentication(mpField,ybkSecret,btnSignIn) == false)
              {
              return;
                  }
            EntryHandler.Y = (int) (Screen.getPrimary().getBounds().getHeight() / 2) - 150;

    }

    @FXML
    void newDB(ActionEvent event)  {
        labelEnterPwd.setVisible(false);
        DatabaseHandler databaseHandler = new DatabaseHandler();
        databaseHandler.dialog(btnCreateDB);
        updateRecentFileString();

    }

    @FXML
    void openDB(ActionEvent event) throws Exception {

        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XLS File (*.xlsx)", "*.xlsx");

        fileChooser.getExtensionFilters().add(extFilter);

        Stage anotherStage = new Stage();
        fileChooser.setInitialDirectory(new File(defaultPath));
        File file = fileChooser.showOpenDialog(anotherStage);
        if (file ==null)
        {
            return;
        }
            labelEnterPwd.setVisible(true);

            passwordFilePath = file.getAbsolutePath();
            selectedDirectoryPath = file.getAbsoluteFile().getParent() + "\\";
            EntryHandler.Y = (int) (Screen.getPrimary().getBounds().getHeight() / 2) - 150;
        updateRecentFileString();

        }


    @FXML
    private void initialize() throws Exception {
        new FileOutputStream(recentFiles, true).close();
        String recentFilesString = new String(FileUtils.readAllBytes(recentFiles));
         String[] rFSArray = recentFilesString.split(",");
     //   passwordFilePath = rFSArray[0];
        passwordFilePath = rFSArray[rFSArray.length-1];
        recentFileLabel.setText(passwordFilePath );
        DatabaseHandler databaseHandler = new DatabaseHandler();
        databaseHandler.createMenuItems(menuRecent,labelEnterPwd);

    }

    void updateRecentFileString()

    {

        String recentFilesString = "," + new String(FileUtils.readAllBytes(recentFiles));

        String[] rFSArray = recentFilesString.split(",");
        boolean contains2 = Stream.of(rFSArray).anyMatch(x -> x.equals(passwordFilePath));
         if (  contains2 == true) {

            return;
        }

            String combinedRecentString = passwordFilePath + recentFilesString;
            System.out.println("the combined string is " + combinedRecentString);
            FileUtils.write(recentFiles, combinedRecentString.getBytes(StandardCharsets.UTF_8));


    }
    }


