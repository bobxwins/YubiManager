package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.stage.*;

import java.io.File;

public class LoginController {

    DirectoryChooser directoryChooser = new DirectoryChooser();
    Label label = new Label("no files selected");
    @FXML
    private Button btnSignIn;

    @FXML
    private Button btnCreateDB;

    @FXML
    private  PasswordField mpField;

    @FXML
    private  PasswordField ybkSecret;

    @FXML
    private TableView<Entry> entryTable ;

    private ObservableList<Entry> entryData = FXCollections.observableArrayList();
    EntryHandler entryHandler = new EntryHandler();


    public static char [] combinedPasswords;
    @FXML
    void loadEntry(ActionEvent event) throws Exception {
        char [] password = mpField.getText().toCharArray();
        char []  ybkPassword = ybkSecret.getText().toCharArray();

        StringBuilder sb = new StringBuilder(128);
        sb.append(password);
        sb.append(ybkPassword);
        combinedPasswords = sb.toString().toCharArray();
        Parent root = FXMLLoader.load(Main.class.getResource("PMAuth/pmlayerAuthenticated.fxml"));
        Stage entryWindow = (Stage) btnSignIn.getScene().getWindow();

        if (entryHandler.loadEntries (entryData,entryData)==0) {
            // if 0 entries are returned when attempting to load the encrypted entries
            // after inputting password, return error
            
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Login failed! Wrong Password!");
            alert.showAndWait();
            return;
        }
        System.out.println("the size is:"+entryHandler.loadEntries (entryData,entryData));
        entryWindow.setScene(new Scene(root));
        EntryHandler.Y = (int) (Screen.getPrimary().getBounds().getHeight() / 2) - 150;
    }

    @FXML
    void newDB(ActionEvent event) throws Exception {
        Stage anotherStage = new Stage();
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        File file = directoryChooser.showDialog(anotherStage);
        if (file != null) {
            System.out.println(file.getAbsolutePath());
            label.setText(file.getAbsolutePath()
                    + "  selected");
        }
        Parent root = FXMLLoader.load(Main.class.getResource("PMAuth/pmlayerAuthenticated.fxml"));
        Stage entryWindow = (Stage) btnCreateDB.getScene().getWindow();
        entryWindow.setScene(new Scene(root));
        EntryHandler.Y = (int) (Screen.getPrimary().getBounds().getHeight() / 2) - 150;
    }
    @FXML
    void chooseDB(ActionEvent event) throws Exception {

    }

}