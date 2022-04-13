package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Login failed!");
        alert.showAndWait();

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