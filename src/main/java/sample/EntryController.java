package sample;
import java.io.File;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
public class EntryController implements Serializable {

    @FXML
    private   TableView <Entry> entryTable ;
    @FXML
    private   TableColumn<Entry,String> colTitel;
    @FXML
    private TableColumn<Entry,String> colUsername;
    @FXML
    private TableColumn<Entry,String> colURL;
    @FXML
    private TableColumn<Entry,String> colPassword;
    @FXML
    private TableColumn<Entry,String> colNotes;
    @FXML
    private TextField tfSearch;
    @FXML
    private TextField tfTitel;
    @FXML
    private TextField tfUsername;
    @FXML
    private TextField tfURL;
    @FXML
    private PasswordField pfPwdField;
    @FXML
    private TextArea tANotes;

    @FXML
    private Button btnSignOut;
    @FXML
    private AnchorPane anchorPane;

    @FXML private VBox vBoxLabel;

    @FXML private VBox vBoxTf;

    private ObservableList<Entry> entryData = FXCollections.observableArrayList();
    EntryHandler entryHandler = new EntryHandler();

    @FXML
    private Button btnEnterMenu;

    @FXML
    private Button btnCancel;
    @FXML
    private Button btnCreate;

    @FXML
    private Button btnDelete;
    @FXML
    void createEntry(ActionEvent event) throws Exception {
        entryData.add(new Entry(tfTitel.getText(), tfUsername.getText(),tfURL.getText(),pfPwdField.getText(),tANotes.getText()));
        entryHandler.createEntryObject(anchorPane);

        tfTitel.setText("");
        tfUsername.setText("");
        tfURL.setText("");
        pfPwdField.setText("");
        tANotes.setText("");
        showTableView();

    }
    @FXML
    void deleteAll(ActionEvent event) throws Exception
    {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Warning, this will permanently delete all your passwords");
        alert.setContentText("Are you sure you want to proceed?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
          entryData.removeAll(entryData);
            File deleteFile = new File(LoginController.passwordFilePath);
            deleteFile.delete();
        } else {
            // ... user chose CANCEL or closed the dialog
        }

    }

    void showTableView()
    {
        vBoxLabel.setVisible(false);
        vBoxTf.setDisable(true);
        vBoxTf.setVisible(false);
        tfSearch.setDisable(false);
        btnCancel.setDisable(true);
        btnCreate.setDisable(true);
        btnCreate.setVisible(false);
        btnCancel.setVisible(false);
        entryTable.setVisible(true);
        entryTable.setDisable(false);
        btnDelete.setDisable(false);

    }
    @FXML
    void enterEntryMenu(ActionEvent event) throws Exception {

            vBoxLabel.setVisible(true);
            vBoxTf.setDisable(false);
            vBoxTf.setVisible(true);
            tfSearch.setDisable(true);
            btnCancel.setDisable(false);
            btnCreate.setDisable(false);
            btnCreate.setVisible(true);
            btnCancel.setVisible(true);
            entryTable.setVisible(false);
            entryTable.setDisable(true);
            btnDelete.setDisable(true);

    }


    @FXML
    void returnTableView(ActionEvent event) throws Exception
    {
        showTableView();

    }
    @FXML
    void loadEntry(ActionEvent event) throws Exception {
      //   entryHandler.loadEntries (entryData,entryData);
    }

    @FXML
    void signOut(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(Main.class.getResource("login/login.fxml"));
        Stage entryWindow= (Stage) btnSignOut.getScene().getWindow();
        entryWindow.setScene(new Scene(root));
    }

    @FXML
    void deleteRow(ActionEvent event) throws  Exception {
        Entry selectedItem = entryTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            entryData.remove(selectedItem);

        }
    }



    @FXML
    void saveEntry(ActionEvent event) throws Exception {
        ObjectIOExample obj = new ObjectIOExample();
        obj.write(entryData, Paths.get(LoginController.passwordFilePath));

       // FileUtils.write(LoginController.recentFiles,LoginController.passwordFilePath.getBytes(StandardCharsets.UTF_8));

    }

    @FXML
   private void initialize() throws Exception {

        colTitel.setCellValueFactory(new PropertyValueFactory<Entry, String>("titel"));
        colUsername.setCellValueFactory(new PropertyValueFactory<Entry, String>("username"));
        colURL.setCellValueFactory(new PropertyValueFactory<Entry, String>("url"));
        colPassword.setCellValueFactory(new PropertyValueFactory<Entry, String>("password"));
        colNotes.setCellValueFactory(new PropertyValueFactory<Entry, String>("Notes"));
        entryData.addAll(ObjectIOExample.read(Paths.get(LoginController.passwordFilePath)));
        entryTable.setItems(entryData);


        filter();
    }

    @FXML
    private void filter()
    {

        FilteredList<Entry> filteredData = new FilteredList<>(entryTable.getItems()
                , p -> true);

        tfSearch.textProperty().addListener((observable, oldValue,    newValue) -> {
            filteredData.setPredicate(entry -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                // Compare all columns  of every entry with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (entry.getTitel().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches titel column.
                } else

                if (entry.getUsername().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches Username
                } else

                if (entry.getUrl().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches URL.
                } else

                if (entry.getPassword().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches pwd.
                } else

                if (entry.getNotes().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches notes
                }
                return false; // Does not match any of the above.

            });
        });
        SortedList<Entry> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(entryTable.comparatorProperty());
        entryTable.setItems(sortedData);
    }



}