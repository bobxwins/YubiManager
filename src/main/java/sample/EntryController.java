package sample;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import javafx.scene.text.Text;

import javafx.stage.Stage;


import java.awt.*;
import java.io.File;
import java.io.Serializable;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

import java.util.Optional;

import static java.lang.Integer.parseInt;


public class EntryController implements Serializable   {
    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TableView<Entry> entryTable;

    @FXML
    private TableColumn<Entry, String> colTitel;
    @FXML
    private TableColumn<Entry, String> colUsername;
    @FXML
    private TableColumn<Entry, String> colURL;

    @FXML
    private TableColumn<Entry, String> colNotes;

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
    private Button btnPwdGenerator;

    @FXML
    private Menu menuRecent;

    private ObservableList<Entry> entryData = FXCollections.observableArrayList();

    @FXML
    private Button btnEnterMenu;
    @FXML
    private Button btnEditOK;
    @FXML
    private Button btnDeleteRow;
    @FXML
    private Button btnCreate;

    @FXML
    private Button btnReturn;

    @FXML
    private ToggleButton toggleButton;

    @FXML
    private TextField generatedPWDfield;

    @FXML
    private Text textUsername;

    @FXML
    private Hyperlink hyperLink;
    @FXML
    private Text textPassword;
    @FXML
    private Text textNotes;

    @FXML
    private Text textTitel;

    @FXML
    private BorderPane bpEntryMenu;

    @FXML
    private AnchorPane apPwdGenerate;

    @FXML
    private AnchorPane entryPane;

    static int length;

    @FXML
    private Text textGenePwdQuality;

    @FXML
    private Text textGeneGPUClusters;

    @FXML
    private Text textGeneGPU;

    @FXML
    private Text textEntropy;

    @FXML
     private ImageView imgPwdVisible;

    @FXML
    private ImageView imgPwdNotVisible;


    Slider slider = new Slider(4, 999, 1);

    @FXML
    void togglePasswordVisible(ActionEvent event) {



    }

        @FXML
        void createEntry (ActionEvent event) throws Exception {

            showTableView();
            entryData.add(new Entry(tfTitel.getText(), tfUsername.getText(), tfURL.getText(), pfPwdField.getText(), tANotes.getText()));
            tfTitel.setText("");
            tfUsername.setText("");
            tfURL.setText("");
            pfPwdField.setText("");
            tANotes.setText("");

            ObjectIOExample obj = new ObjectIOExample();
            obj.write(entryData, Paths.get(Global.getPasswordFilePath()));
        }

        @FXML
        void generatePwd (ActionEvent event) throws Exception {

            Spinner<Integer> pwdLengthSpinner = (Spinner<Integer>) new Spinner(0, 999, 4);

            slider.setBlockIncrement(1);
            slider.setMin(4);
            slider.setMax(999);
            slider.setValue(4);
            slider.setPrefWidth(570);
            slider.setLayoutY(110);
            slider.setShowTickLabels(true);

            pwdLengthSpinner.setPrefSize(75, 25);
            pwdLengthSpinner.setLayoutX(580);
            pwdLengthSpinner.setLayoutY(100);
            pwdLengthSpinner.setEditable(true);
            apPwdGenerate.getChildren().addAll(pwdLengthSpinner, slider);
            apPwdGenerate.setVisible(true);
            apPwdGenerate.setDisable(false);
            entryPane.setDisable(true);
            entryPane.setVisible(false);
            btnEditOK.setDisable(true);
            btnEditOK.setVisible(false);

            Entry selectedItem = entryTable.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                entryData.set(entryData.indexOf(selectedItem), selectedItem);

                generatedPWDfield.setText(selectedItem.getPassword());
                PasswordUtils.calcCrackingTime(textGenePwdQuality, textGeneGPU, textEntropy, textGeneGPUClusters, generatedPWDfield.getText());
                entryTable.getSelectionModel().clearSelection();
            } else {
                generatedPWDfield.setText(PasswordUtils.getPassword(pwdLengthSpinner.getValue()));

                PasswordUtils.calcCrackingTime(textGenePwdQuality, textGeneGPU, textEntropy, textGeneGPUClusters, generatedPWDfield.getText());

            }

            pwdLengthSpinner.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {

                pwdLengthSpinner.getEditor().setOnKeyReleased(e ->
                {
                    generatedPWDfield.setText(PasswordUtils.getPassword(parseInt(newValue)));

                    PasswordUtils.calcCrackingTime(textGenePwdQuality, textGeneGPU, textEntropy, textGeneGPUClusters, generatedPWDfield.getText());
                });

                generatedPWDfield.setText(PasswordUtils.getPassword(parseInt(newValue)));

                PasswordUtils.calcCrackingTime(textGenePwdQuality, textGeneGPU, textEntropy, textGeneGPUClusters, generatedPWDfield.getText());
                slider.setValue(parseInt(newValue));

            });

            slider.setOnMouseDragged(e -> {
                Double newData = slider.getValue();
                int value = newData.intValue();
                pwdLengthSpinner.getValueFactory().setValue(value);
            });


            generatedPWDfield.setOnKeyReleased(e ->
                    PasswordUtils.calcCrackingTime(textGenePwdQuality, textGeneGPU, textEntropy, textGeneGPUClusters, generatedPWDfield.getText()));


            btnPwdGenerator.setOnAction(e ->
                    // when Generator button is pressed
            {
                try {
                    length = pwdLengthSpinner.getValue();

                    generatedPWDfield.setText(PasswordUtils.getPassword(length));

                    PasswordUtils.calcCrackingTime(textGenePwdQuality, textGeneGPU, textEntropy, textGeneGPUClusters, generatedPWDfield.getText());

                } catch (Exception E) {

                }

            });

        }
        @FXML
        void deleteAll (ActionEvent event) throws Exception
        {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Warning, this will permanently delete all your passwords");
            alert.setContentText("Are you sure you want to proceed?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                entryData.removeAll(entryData);

                File deleteFile = new File(Global.getPasswordFilePath()).getAbsoluteFile().getParentFile();

                DatabaseHandler databaseHandler = new DatabaseHandler();
                databaseHandler.deleteDir(deleteFile);

                Global.setPasswordFilePath(Global.getRFCArray()[0]);
                //sets the passwordfilepath to the first path written in RecentFilesDir txt document
                FileUtils.write(Global.getRecentFilesDir(), Global.getRecentFilesContent().substring(Global.getRecentFilesContent().indexOf(",")
                        + 1).getBytes(StandardCharsets.UTF_8));
// removes the deleted database from the recentFilesDIR text document.
            }

        }

        void showTableView ()
        {

            entryPane.setVisible(true);
            entryPane.setDisable(false);

            bpEntryMenu.setDisable(true);
            bpEntryMenu.setVisible(false);

            tfSearch.setDisable(false);

            entryTable.setVisible(true);
            entryTable.setDisable(false);

            btnEditOK.setDisable(true);
            btnEditOK.setVisible(false);

        /*apCalc .setDisable(true);
        apCalc.setVisible(false); */
        }

        void entrySpecs () throws Exception {
            bpEntryMenu.setVisible(true);
            bpEntryMenu.setDisable(false);
            tfSearch.setDisable(true);
            btnCreate.setVisible(true);
            btnCreate.setDisable(false);
            entryTable.setVisible(false);
            entryTable.setDisable(true);
        }



    @FXML
    void returnTableView(ActionEvent event) throws Exception
    {
        showTableView();
        apPwdGenerate.setDisable(true);
        apPwdGenerate.setVisible(false);
        apPwdGenerate.getChildren().remove(slider);
    }

    @FXML
    void copyPWD(ActionEvent event) throws Exception
    {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(generatedPWDfield.getText());
        clipboard.setContent(content);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Password succesfully copied!");
        alert.showAndWait();
    }

    @FXML
    void entryMenu(ActionEvent event) throws Exception
    {
        entrySpecs();
    }



    @FXML
    void newDB(ActionEvent event) throws  Exception {
        DatabaseHandler databaseHandler = new DatabaseHandler();
        databaseHandler.newDBdialog(btnReturn);

    }


    @FXML
    void openDB(ActionEvent event) throws Exception {

      DatabaseHandler databaseHandler = new DatabaseHandler();
       if (!databaseHandler.openDB()==true)
        {
            return;
        }
        String pwdFPNewValue= Global.getPasswordFilePath();
        String directoryNewValue =  Global.getSelectedDirectoryPath();
       //the new values of passwordFilePath and selectedDirectoryPath will be lost upon loading the FXML login "login.fxml"
        //so to keep the new values of both Strings,I create 2 new strings that store the values of the new paths,
        //load login.FXML, then set the values of the static path Strings to the new values.

           FXMLLoader fxmlLoader = new FXMLLoader();
           fxmlLoader.setLocation(getClass().getResource("login/login.fxml"));

           Scene scene = new Scene(fxmlLoader.load());
           Stage stage = new Stage();
           stage.setMaximized(true);
           stage.setTitle("New Window");
           stage.setScene(scene);
           stage.show();
           Global.getLabelEnterPwd().setVisible(true);
        Global.setPasswordFilePath( pwdFPNewValue);
        Global.setSelectedDirectoryPath(  directoryNewValue);
    }

@FXML
void openRecent (ActionEvent event) throws Exception

        {

      FXMLLoader fxmlLoader = new FXMLLoader();
     fxmlLoader.setLocation(getClass().getResource("login/login.fxml"));

       Scene scene = new Scene(fxmlLoader.load());
       Stage stage = new Stage();
       stage.setMaximized(true);
       stage.setTitle("New Window");
       stage.setScene(scene);
       stage.show();
}

    @FXML
    void signOut(ActionEvent event) throws Exception {
        DatabaseHandler.stageFullScreen(btnSignOut);
    }

    @FXML
    void deleteRow(ActionEvent event) throws  Exception {
        Entry selectedItem = entryTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            entryData.remove(selectedItem);
            ObjectIOExample obj = new ObjectIOExample();
            obj.write(entryData, Paths.get(Global.getPasswordFilePath()));
        }

    }


    @FXML void editEntry (ActionEvent event) throws  Exception {

        Entry selectedItem = entryTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            entrySpecs();

            tfTitel.setText(selectedItem.getTitel());
            tfUsername.setText(selectedItem.getUsername());
            tfURL.setText(selectedItem.getUrl());
            pfPwdField.setText(selectedItem.getPassword());
            tANotes.setText(selectedItem.getNotes());
          //sets the TextFields and passwordfield to be equal to the fields of the selected row

            btnEditOK.setDisable(false);
            btnEditOK.setVisible(true);
            btnCreate.setVisible(false);
            btnCreate.setDisable(true);
            btnEditOK.setOnAction(e -> {
                try{
                    entryData.set(entryData.indexOf(selectedItem),selectedItem);
                    selectedItem.setTitel( tfTitel.getText());
                    selectedItem.setUsername( tfUsername.getText());
                    selectedItem.setURL( tfURL.getText());
                    selectedItem.setPassword( pfPwdField.getText());
                    selectedItem.setNotes(tANotes.getText());


                    showTableView();
                    ObjectIOExample obj = new ObjectIOExample();
                    obj.write(entryData, Paths.get(Global.getPasswordFilePath()));
                } catch (Exception E) {

                }
            });

        }
    }



    @FXML
    void saveEntry(ActionEvent event) throws Exception {
        ObjectIOExample obj = new ObjectIOExample();
        obj.write(entryData, Paths.get(Global.getPasswordFilePath()));

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Database succesfully saved!");
        alert.showAndWait();
        
    }


    @FXML
    void updateMasterPwd(ActionEvent event) throws Exception {
        DatabaseHandler databaseHandler = new DatabaseHandler();
        databaseHandler.updatePasswords();
        ObjectIOExample obj = new ObjectIOExample();
        obj.write(entryData, Paths.get(Global.getPasswordFilePath()));

    }


    @FXML
   private void initialize() throws Exception  {

        String pwd="";
        for (int i = 0; i < 12; i++) {
            pwd='\u2022'+ pwd;
            // Putting password string as 12 bullets, to hide the content and length of the user's passwords.
        }

        String finalPwd = pwd;
        entryTable.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {

                        Entry selectedItem = entryTable.getSelectionModel().getSelectedItem();

                        if (selectedItem != null) {
                            textTitel.setText(selectedItem.getTitel());
                            textNotes.setText(selectedItem.getNotes());
                            textUsername.setText(selectedItem.getUsername());
                            hyperLink.setText(selectedItem.getUrl());
                            hyperLink.setOnAction(e ->
                            {
                                try {
                                    Desktop.getDesktop().browse(new URI(selectedItem.getUrl()));
                                    // opens the hyperlink in the user's main browser
                                 }
                                catch (Exception E) {

                                }
                            textNotes.setText(selectedItem.getNotes());

                            if (!selectedItem.getPassword().isEmpty()) {
                                textPassword.setText(finalPwd);
                            } else {
                                textPassword.setText(selectedItem.getPassword());
                            }
                        });

                     //   boolean isSelected = toggleButton.isSelected();
                           // toggleButton.setSelected(false);
                        toggleButton.setOnAction(e -> {
                            if (!imgPwdVisible.isVisible() ) {
                                imgPwdVisible.setVisible(true);
                                imgPwdNotVisible.setVisible(false);
                                textPassword.setText(selectedItem.getPassword());
                        return;     }

                                imgPwdVisible.setVisible(false);
                                imgPwdNotVisible.setVisible(true);
                                textPassword.setText(finalPwd);


                        });


    }
                });

        DatabaseHandler databaseHandler = new DatabaseHandler();

        databaseHandler.createMenuItems(menuRecent,Global.getLabelEnterPwd());

         btnEnterMenu.setStyle(   "-fx-background-radius: 5em; "
                 );

        btnDeleteRow.setStyle(
                "-fx-background-radius: 5em; " );


            colTitel.setCellValueFactory(new PropertyValueFactory<Entry, String>("titel"));
            colUsername.setCellValueFactory(new PropertyValueFactory<Entry, String>("username"));
            colURL.setCellValueFactory(new PropertyValueFactory<Entry, String>("url"));
            colNotes.setCellValueFactory(new PropertyValueFactory<Entry, String>("Notes"));


            entryData.addAll(ObjectIOExample.read(Paths.get(Global.getPasswordFilePath())));

            entryTable.setItems(entryData);
            filter();
            btnEnterMenu.setOnAction(e -> {
                try {
                    entrySpecs();
                } catch (Exception E) {

                }
            });

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