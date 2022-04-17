package sample;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.layout.GridPane;

import javafx.stage.*;
import javafx.util.Pair;

import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;


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

    private ObservableList<Entry> entryData = FXCollections.observableArrayList();

    EntryHandler entryHandler = new EntryHandler();
    public static String defaultPath = System.getProperty("user.dir") + "/resources/sample/passwords";
    public static String passwordFilePath; // filepath to the password file currently in use
    public static String selectedDirectoryPath;
    public static String recentFiles = defaultPath+"/RecentFiles.txt";
    public static char[] combinedPasswords;

    @FXML
    void loadEntry(ActionEvent event) throws Exception {
        passwordFilePath = new String(FileUtils.readAllBytes(recentFiles));
        selectedDirectoryPath = new File(passwordFilePath).getAbsoluteFile().getParent()+"/";
         if (loginAuthentication() == false)
              {
              return;
                  }
            EntryHandler.Y = (int) (Screen.getPrimary().getBounds().getHeight() / 2) - 150;

    }

    @FXML
    void newDB(ActionEvent event)  {
        labelEnterPwd.setVisible(false);
       dialog();
      String storedPath = FileUtils.readAllBytes(recentFiles).toString();
        FileUtils.write(recentFiles,passwordFilePath.getBytes(StandardCharsets.UTF_8));
    }

    @FXML
    void openDB(ActionEvent event) throws Exception {

        FileChooser fileChooser = new FileChooser();
        Stage anotherStage = new Stage();
        fileChooser.setInitialDirectory(new File(defaultPath));
        File file = fileChooser.showOpenDialog(anotherStage);
        if (file != null) {
         labelEnterPwd.setVisible(true);
            passwordFilePath = file.getAbsolutePath();
            FileUtils.write( recentFiles,passwordFilePath.getBytes(StandardCharsets.UTF_8));
            selectedDirectoryPath = file.getAbsoluteFile().getParent() + "/";
            System.out.println("the recent file is :"+  passwordFilePath);

        EntryHandler.Y = (int) (Screen.getPrimary().getBounds().getHeight() / 2) - 150;

        }
    }

    @FXML
    private void initialize() throws Exception {
        recentFileLabel.setText(new String(FileUtils.readAllBytes(recentFiles)));
    }

    boolean newScene ( ) throws Exception
    {

        DirectoryChooser directoryChooser = new DirectoryChooser();
        Stage anotherStage = new Stage();
        directoryChooser.setInitialDirectory(new File(defaultPath));
        File selectedDirectory = directoryChooser.showDialog(anotherStage);

        if (selectedDirectory != null) {
            selectedDirectoryPath=selectedDirectory.getAbsolutePath()+"/"+passwordFilePath+"/";

            new File(selectedDirectoryPath).mkdir();
            passwordFilePath =   selectedDirectoryPath+passwordFilePath+".txt";
            FileUtils.write(  passwordFilePath,"".getBytes(StandardCharsets.UTF_8));

        }
        else {
            return false;
        }
        Parent root = FXMLLoader.load(Main.class.getResource("PMAuth/pmlayerAuthenticated.fxml"));

        Stage entryWindow = (Stage) btnCreateDB.getScene().getWindow();

        entryWindow.setScene(new Scene(root));
        EntryHandler.Y = (int) (Screen.getPrimary().getBounds().getHeight() / 2) - 150;
        return true;
    }
    void dialog() {

        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("TestName");

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(8);
        gridPane.setVgap(8);
        gridPane.setPadding(new Insets(5));

        TextField fileNameField = new TextField("");
        fileNameField.setPromptText("File name...");
        PasswordField newMpField = new PasswordField();
        newMpField.setPromptText("Master Password...");
        PasswordField confirmMPField = new PasswordField();
        confirmMPField.setPromptText("Confirm Master Password...");
        PasswordField newYbkField = new PasswordField();
        newYbkField.setPromptText("Yubikey static Password...");

        Label fileLabel = new Label("Enter new File name:");
        Label mpLabel = new Label("Enter new Master Password:");
        Label confirMPLabel = new Label("Confirm new Master Password:");
        Label ybkLabel = new Label("Enter new  Yubikey Password:");
        gridPane.addRow(0, fileLabel,fileNameField);
        gridPane.addRow(1, mpLabel,newMpField);
        gridPane.addRow(2, confirMPLabel,confirmMPField);
        gridPane.addRow(3, ybkLabel,newYbkField);


        dialog.getDialogPane().setContent(gridPane);

        Platform.runLater(() -> fileNameField.requestFocus());

        dialog.setResultConverter(dialogButton -> {
            try {
                if (dialogButton == ButtonType.OK) {
                    if (!newMpField.getText().equals(confirmMPField.getText())) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Information Dialog");
                        alert.setHeaderText(null);
                        alert.setContentText("Login failed! Wrong Password!");
                        alert.showAndWait();
                        return null;
                    }
                    if (fileNameField.getText().length() == 0 || newMpField.getText().length() == 0 || confirmMPField.getText().length() == 0) {

                        return null;
                    }

                    char[] masterPassword = newMpField.getText().toCharArray();
                    char[] ybkPassword = newYbkField.getText().toCharArray();

                    StringBuilder sb = new StringBuilder(128);
                    sb.append(masterPassword);
                    sb.append(ybkPassword);

                    combinedPasswords = sb.toString().toCharArray();

                     passwordFilePath = fileNameField.getText();

                    newScene();
                }
            }
            catch (Exception E) {

            }

            return null; // return null;
        });

         dialog.showAndWait();

    }

    boolean loginAuthentication () throws  Exception {

        char[] masterPassword = mpField.getText().toCharArray();
        char[] ybkPassword = ybkSecret.getText().toCharArray();

        StringBuilder sb = new StringBuilder(128);
        sb.append(masterPassword);
        sb.append(ybkPassword);
        LoginController.combinedPasswords = sb.toString().toCharArray();

        Parent root = FXMLLoader.load(Main.class.getResource("PMAuth/pmlayerAuthenticated.fxml"));

        Stage entryWindow = (Stage) btnSignIn.getScene().getWindow();

        if (  ObjectIOExample.read(Paths.get(LoginController.passwordFilePath)) != null &&
                ObjectIOExample.read(Paths.get(LoginController.passwordFilePath)).isEmpty()) {
     //   if (entryHandler.loadEntries(entryData, entryData) == 0) {
            // if 0 entries are returned when attempting to load and decrypt the stored encrypted entries...
            // ... after inputting password, returns error

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Login failed! Wrong Password!");
            alert.showAndWait();
            return false;
        }

        entryWindow.setScene(new Scene(root));
        return true;
    }

    void recentFilesWrite() throws  Exception{
        String filename= recentFiles;
        FileWriter fw = new FileWriter(filename,true); //the true will append the new data
        fw.write("add a line\n");//appends the string to the file
        fw.close();
    }
}