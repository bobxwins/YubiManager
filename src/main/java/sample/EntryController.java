package sample;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;

import javafx.scene.text.Text;

import javafx.stage.Stage;


import java.awt.*;
import java.io.File;
import java.io.Serializable;
import java.net.URI;
import java.nio.file.Paths;

import java.util.Optional;

import static java.lang.Integer.parseInt;


public class EntryController implements Serializable   {
    @FXML private  AnchorPane anchorPane;
    @FXML private AnchorPane  apBottomTable;

    @FXML private TableView<Entry> entryTable;

    @FXML private TableColumn<Entry, String> colTitel;
    @FXML private TableColumn<Entry, String> colUsername;
    @FXML private TableColumn<Entry, String> colURL;

    @FXML private TableColumn<Entry, String> colNotes;

    @FXML private TextField tfSearch;
    @FXML private TextField tfTitel;
    @FXML private TextField tfUsername;
    @FXML private TextField tfURL;
    @FXML private PasswordField pfPwdField;
    @FXML private TextArea tANotes;

    @FXML private Button btnSignOut;

    @FXML private Button btnPwdGenerator;

    @FXML private Button btnEnterMenu;
    @FXML private Button btnEditOK;

    @FXML private Button btnCreate;

    @FXML private Button btnReturn;

    @FXML private Button toggleButton;
    @FXML private Button togBtnPwd;

    @FXML private TextField generatedPWDfield;

    @FXML private  TextField tfPwd;

    @FXML private Text textUsername;

    @FXML private Hyperlink hyperLinkURL;
    @FXML private Text textPassword;

    @FXML private Text textNotes;

    @FXML private Text textTitel;

    @FXML private AnchorPane apEntryMenu;

    @FXML private AnchorPane apPwdGenerate;

    @FXML private AnchorPane entryPane;

    @FXML private Text textGenePwdQuality;

    @FXML private Text textBruteForceTime;

    @FXML private Text textEntropy;

    @FXML private ImageView imgPwdVisible;

    @FXML private ImageView imgVisible;

    @FXML private ImageView imgNotVisible;
    @FXML private ImageView imgPwdNotVisible;
    @FXML private ContextMenu ctxTableMenu;

    static int pwdLength;

    private ObservableList<Entry> entryData = FXCollections.observableArrayList();
    PasswordVisbilityHandler pwdvh = new PasswordVisbilityHandler ();

    Slider slider = new Slider(4, 999, 1);
   //Entry selectedItem;//   = entryTable.getSelectionModel().getSelectedItem();

    @FXML void menuRandomPwd (ActionEvent event)
    {
        pfPwdField.setText(PasswordUtils.getPassword(32));
    }

        @FXML
        void createEntry (ActionEvent event) throws Exception {
            entryData.add(new Entry(tfTitel.getText(), tfUsername.getText(), tfURL.getText(), pfPwdField.getText(), tANotes.getText()));
            showTableView();
            tfTitel.setText("");
            tfUsername.setText("");
            tfURL.setText("");
            pfPwdField.setText("");
            tANotes.setText("");
            save();

        }

        @FXML
        void generatePwd (ActionEvent event) throws Exception {

            Spinner<Integer> pwdLengthSpinner = (Spinner<Integer>) new Spinner(0, 999, 12);

            slider.setBlockIncrement(1);
        ;
            slider.setValue(pwdLengthSpinner.getValue());
            slider.setPrefWidth(570);
            slider.setLayoutY(110);
            slider.setShowTickLabels(true);

            pwdLengthSpinner.setPrefSize(75, 25);
            pwdLengthSpinner.setLayoutX(580);
            pwdLengthSpinner.setLayoutY(100);
            pwdLengthSpinner.setEditable(true);
            apPwdGenerate.getChildren().addAll(pwdLengthSpinner, slider);
            ComponentVisibilityHandler cVBH = new ComponentVisibilityHandler();

            cVBH.showPwdGenerateMenu(apPwdGenerate,entryPane,btnEditOK,apBottomTable);

           Entry selectedItem = entryTable.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                entryData.set(entryData.indexOf(selectedItem), selectedItem);

                generatedPWDfield.setText(selectedItem.getPassword());
                PasswordUtils.calcCrackingTime(textGenePwdQuality, textBruteForceTime, textEntropy,  generatedPWDfield.getText());
                entryTable.getSelectionModel().clearSelection();
            } else {
                generatedPWDfield.setText(PasswordUtils.getPassword(pwdLengthSpinner.getValue()));

                PasswordUtils.calcCrackingTime(textGenePwdQuality, textBruteForceTime, textEntropy,  generatedPWDfield.getText());

            }

            pwdLengthSpinner.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {

                pwdLengthSpinner.getEditor().setOnKeyReleased(e ->
                {
                    generatedPWDfield.setText(PasswordUtils.getPassword(parseInt(newValue)));

                    PasswordUtils.calcCrackingTime(textGenePwdQuality, textBruteForceTime, textEntropy,  generatedPWDfield.getText());
                });

                generatedPWDfield.setText(PasswordUtils.getPassword(parseInt(newValue)));

                PasswordUtils.calcCrackingTime(textGenePwdQuality, textBruteForceTime, textEntropy, generatedPWDfield.getText());
                slider.setValue(parseInt(newValue));

            });

            slider.setOnMouseDragged(e -> {
                Double newData = slider.getValue();
                int value = newData.intValue();
                pwdLengthSpinner.getValueFactory().setValue(value);
            });


            generatedPWDfield.setOnKeyReleased(e ->
                    PasswordUtils.calcCrackingTime(textGenePwdQuality, textBruteForceTime, textEntropy,  generatedPWDfield.getText()));


            btnPwdGenerator.setOnAction(e ->
                    // when Generator button is pressed
            {
                try {
                    pwdLength = pwdLengthSpinner.getValue();

                    generatedPWDfield.setText(PasswordUtils.getPassword(pwdLength));

                    PasswordUtils.calcCrackingTime(textGenePwdQuality, textBruteForceTime, textEntropy,  generatedPWDfield.getText());

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
                entryData.remove(entryData);

                File deleteFile = new File(Global.getPasswordFilePath()).getAbsoluteFile().getParentFile();

                FileHandler fileHandler= new FileHandler();
                fileHandler.deleteDir(deleteFile);
                Global.getPasswordFilePath();

                // find the index of the RecentFiles, delete
                SerializedObject.writeArrayList(Global.getRecentFilesData(), Paths.get(Global.getRecentFilesDir()));
                SceneHandler.stageFullScreen(btnSignOut);
            }

        }

        void showTableView ()
        {

            entryPane.setVisible(true);
            entryPane.setDisable(false);

             apEntryMenu.setDisable(true);
             apEntryMenu.setVisible(false);

            tfSearch.setDisable(false);

            entryTable.setVisible(true);
            entryTable.setDisable(false);

            btnEditOK.setDisable(true);
            btnEditOK.setVisible(false);
            apBottomTable.setDisable(false);
            apBottomTable.setVisible(true);

        }

        void entrySpecs () throws Exception {
            apEntryMenu.setVisible(true);
            apEntryMenu.setDisable(false);
            tfSearch.setDisable(true);
            btnCreate.setVisible(true);
            btnCreate.setDisable(false);
            entryTable.setVisible(false);
            entryTable.setDisable(true);
            apBottomTable.setDisable(true);
            apBottomTable.setVisible(false);
        }
    @FXML
    void returnTableView(ActionEvent event) throws Exception
    {
        showTableView();
        apPwdGenerate.setDisable(true);
        apPwdGenerate.setVisible(false);
        apPwdGenerate.getChildren().remove(slider);
        // Empties the edited entry after clicking the cancel button,so the values
        // of the previously edited entry doesn't get transferred to the entry about to be created
        tfTitel.setText("");
        tfUsername.setText("");
        tfURL.setText("");
        pfPwdField.setText("");
        tANotes.setText("");
    }

    @FXML
    void copyGeneratedPWD(ActionEvent event) throws Exception
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
    void copyUsername(ActionEvent event) throws Exception
    {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
     Entry  selectedItem = entryTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            content.putString(selectedItem.getUsername());
            clipboard.setContent(content);
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Username succesfully copied!");
        alert.showAndWait();
    }

    @FXML
    void copyPwd(ActionEvent event) throws Exception
    { final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
     Entry selectedItem = entryTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            content.putString(selectedItem.getPassword());
            clipboard.setContent(content);
        }
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
        SceneHandler sceneHandler = new SceneHandler();
        sceneHandler.newDBdialog(btnReturn);

    }


    @FXML
    void openDB(ActionEvent event) throws Exception {

      SceneHandler sceneHandler = new SceneHandler();
       if (!sceneHandler.openDB()==true)
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
      //     stage.setMaximized(true);
           stage.setTitle("New Window");
           stage.setScene(scene);
           stage.show();
         //  Global.getLabelEnterPwd().setVisible(true);
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
     //  stage.setMaximized(true);
       stage.setTitle("New Window");
       stage.setScene(scene);
       stage.show();
}

    @FXML
    void signOut(ActionEvent event) throws Exception {
        SceneHandler.stageFullScreen(btnSignOut);
    }

    @FXML
    void deleteRow(ActionEvent event) throws  Exception {
     Entry  selectedItem = entryTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            entryData.remove(selectedItem);
           save();
        }

    }

   // static Entry selectedItem ;
    @FXML void editEntry (ActionEvent event) throws  Exception {
        Entry selectedItem = entryTable.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {

            entrySpecs();
            tfTitel.setText(selectedItem.getTitle());
            tfUsername.setText(selectedItem.getUsername());
            tfURL.setText(selectedItem.getUrl());
            pfPwdField.setText(selectedItem.getPassword());
            tANotes.setText(selectedItem.getNotes());
          //the values inside the fields from the selected row is set to be the values stored inside the EntryTable
            // otherwise the values in the fieds from the selected entry would be empty
            apBottomTable.setDisable(true);
            apBottomTable.setVisible(false);
            btnEditOK.setDisable(false);
            btnEditOK.setVisible(true);
            btnCreate.setVisible(false);
            btnCreate.setDisable(true);
            btnEditOK.setOnAction(e -> {
                try{
                    entryData.set(entryData.indexOf(selectedItem),selectedItem);
                    selectedItem.setTitle( tfTitel.getText());
                    selectedItem.setUsername( tfUsername.getText());
                    selectedItem.setURL( tfURL.getText());
                    selectedItem.setPassword( pfPwdField.getText());
                    selectedItem.setNotes(tANotes.getText());
                    // updates the value of both the tableview at the top and bottom of the page,
                    // with the newly added values, after clicking the OK button
                    showTableView();
                    save();

                } catch (Exception E) {

                }
            });

        }
    }


         void save () throws Exception {

    Global.setEntryData(entryData);
    FileProtector fileProtector = new FileProtector();
    Secrets secrets = new Secrets();
    secrets.setEntry(entryData);
    secrets.setTimerSpecs(TimerSpecs.getTimerSpecs());
    fileProtector.encryption(entryData,secrets.getTimerSpecs());
    textTitel.setText(tfTitel.getText());
    textUsername.setText(tfUsername.getText());

  pwdvh.setSelectedPassword(pfPwdField.getText());
   imgVisible.setVisible(false);
   imgNotVisible.setVisible(true);
  imgPwdNotVisible.setVisible(true);
  imgPwdVisible.setVisible(false);
   hyperLinkURL.setText(tfURL.getText());
    textNotes.setText(tANotes.getText());

             tfTitel.setText("");
             tfUsername.setText("");
             tfURL.setText("");
             pfPwdField.setText("");
             tANotes.setText("");
 }
    @FXML
    void saveEntry(ActionEvent event) throws Exception {

      save();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Database succesfully saved!");
        alert.showAndWait();
        
    }

    @FXML
    void updateMasterPwd(ActionEvent event) throws Exception {
        PMGUI pmgui  = new PMGUI();
        pmgui.updatePasswords();
          save();
    }
@FXML
    void timerDialog(ActionEvent event) throws Exception {
   TimerHandler.timerDialog(entryData);
   TimerHandler.timerCountDown(btnSignOut,anchorPane);
    }

   static String hidePwd = "";
    @FXML
   private void initialize() throws Exception {
        entryTable.setPlaceholder(new Label("0 entries in the database. Click the + button to add new entries!"));
        anchorPane.setOnContextMenuRequested(e ->
                ctxTableMenu.show(anchorPane, e.getScreenX(), e.getScreenY()));

        String image = Main.class.getResource("PMAuth/magnifying-glass.png").toExternalForm();
        tfSearch.setStyle("-fx-background-image: url('" + image + "'); " +
                " -fx-background-repeat: no-repeat; -fx-background-position: right; -fx-background-size: 38 24;");

        for (int i = 0; i < 12; i++) {
            hidePwd = '\u2022' + hidePwd;
            // Putting password string as 12 bullets, to hide the content and length of the user's passwords.
        }

        String finalHidePwd = hidePwd;
        entryTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            Entry selectedItem = entryTable.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                textTitel.setText(selectedItem.getTitle());
                textNotes.setText(selectedItem.getNotes());
                textUsername.setText(selectedItem.getUsername());
                hyperLinkURL.setText(selectedItem.getUrl());
                hyperLinkURL.setOnAction(e ->
                {
                    try {
                        Desktop.getDesktop().browse(new URI("www." + selectedItem.getUrl()));
                        // opens the hyperlink in the user's main browser
                    } catch (Exception E) {

                    }

                });
             /*   PasswordVisbilityHandler */ pwdvh = new PasswordVisbilityHandler ();
                pwdvh.toggleVisbility (  toggleButton,   imgPwdVisible,  imgPwdNotVisible,   textPassword,
                        selectedItem.getPassword(),   finalHidePwd,entryTable);
                textPassword.setText(finalHidePwd);

            }
        });
        PasswordVisbilityHandler.toggleVisbility(togBtnPwd,imgVisible,imgNotVisible,tfPwd,pfPwdField);

            colTitel.setCellValueFactory(new PropertyValueFactory<>("title"));
            colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
            colURL.setCellValueFactory(new PropertyValueFactory<>("url"));
            colNotes.setCellValueFactory(new PropertyValueFactory<>("Notes"));
            Authentication authentication = new Authentication();
           entryData.addAll(authentication.restoreDatabase());
            TimerHandler.timerCountDown(btnSignOut,anchorPane);
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

                if (entry.getTitle().toLowerCase().contains(lowerCaseFilter)) {
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