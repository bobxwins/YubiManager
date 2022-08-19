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


public class PasswordRecordController implements Serializable   {
    @FXML private AnchorPane anchorPane;
    @FXML private AnchorPane apBottomTable;
    @FXML private AnchorPane apEntryPage;
    @FXML private AnchorPane apPwdGenerate;
    @FXML private AnchorPane entryPane;
    @FXML private Button btnCreate;
    @FXML private Button btnEnterMenu;
    @FXML private Button btnEditOK;
    @FXML private Button btnReturn;
    @FXML private Button btnLockDB;
    @FXML private Button btnPwdGenerator;
    @FXML private Button toggleButton;
    @FXML private Button togBtnPwd;
    @FXML private ContextMenu ctxTableMenu;
    @FXML private ImageView imgNotVisible;
    @FXML private ImageView imgPwdVisible;
    @FXML private ImageView imgVisible;
    @FXML private ImageView imgPwdNotVisible;
    @FXML private Hyperlink hyperLinkURL;
    @FXML private TableView<PasswordRecord> passwordRecordTableView;
    @FXML private TableColumn<PasswordRecord, String> colTitel;
    @FXML private TableColumn<PasswordRecord, String> colUsername;
    @FXML private TableColumn<PasswordRecord, String> colURL;
    @FXML private TableColumn<PasswordRecord, String> colNotes;
    @FXML private TextField tfSearch;
    @FXML private TextField tfTitel;
    @FXML private TextField tfUsername;
    @FXML private TextField tfURL;
    @FXML private PasswordField pfPwdField;
    @FXML private TextArea tANotes;
    @FXML private TextField bruteForceTextField;
    @FXML private TextField tfPwd;
    @FXML private Text textUsername;
    @FXML private Text textPassword;
    @FXML private Text textNotes;
    @FXML private Text textTitel;
    @FXML private Text textGenePwdQuality;
    @FXML private Text textBruteForceTime;
    @FXML private Text textEntropy;

    static int pwdLength;
  //  private static final long serialVersionUID = 6529685098267757690L;
    private ObservableList<PasswordRecord> passwordRecordList = FXCollections.observableArrayList();
    VisibilityService visibilityService = new VisibilityService();
    Slider sliderPwdGenerator = new Slider(4, 999, 1);
    Spinner<Integer> spinnerPwdGenerator;
    @FXML
    void generateChallengeResponse(ActionEvent event) throws Exception {
        HardwareKeyService.cmdGenerateCR();
        save();
    }

    @FXML
    void configureChallengeResponse(ActionEvent event) throws Exception {
        HardwareKeyService.cmdConfigureCR();
        save();
    }

    @FXML
    void copyPwd(ActionEvent event) throws Exception
    { final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        PasswordRecord selectedItem = passwordRecordTableView.getSelectionModel().getSelectedItem();
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
    void copyBruteForcePWD(ActionEvent event) throws Exception
    {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(bruteForceTextField.getText());
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
        PasswordRecord selectedItem = passwordRecordTableView.getSelectionModel().getSelectedItem();
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

    @FXML void createPwdEntry(ActionEvent event) throws Exception {
            passwordRecordList.add(new PasswordRecord(tfTitel.getText(), tfUsername.getText(),
                    tfURL.getText(), pfPwdField.getText(), tANotes.getText()));
            VisibilityService.showTableView(entryPane, apEntryPage,tfSearch,
                    passwordRecordTableView, btnEditOK,apBottomTable);
            //Hides and disables the "Create PasswordRecord menu" + shows and enables the TableView, the Search bar etc
            save();
        }

    @FXML void editPwdEntry(ActionEvent event) throws  Exception {
        PasswordRecord selectedItem = passwordRecordTableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            VisibilityService.entrySpecs (apEntryPage,tfSearch,btnCreate, passwordRecordTableView,apBottomTable);
            tfTitel.setText(selectedItem.getTitle());
            tfUsername.setText(selectedItem.getUsername());
            tfURL.setText(selectedItem.getUrl());
            pfPwdField.setText(selectedItem.getPassword());
            tANotes.setText(selectedItem.getNotes());
            //the values inside the fields from the selected row is set to be the values stored inside the EntryTable
            // otherwise the values in the fieds from the selected entry would be empty
            VisibilityService.editEntryVisibility (btnEditOK,btnCreate);

            btnEditOK.setOnAction(e -> {
                try{
                    passwordRecordList.set(passwordRecordList.indexOf(selectedItem),selectedItem);
                    //Updates the specified row(Index of selectedItem) with the selectedItem.
                    selectedItem.setTitle( tfTitel.getText());
                    selectedItem.setUsername( tfUsername.getText());
                    selectedItem.setURL( tfURL.getText());
                    selectedItem.setPassword( pfPwdField.getText());
                    selectedItem.setNotes(tANotes.getText());
                    // updates the value of both the tableview at the top and bottom of the page,
                    // with the newly added values, after clicking the OK button
                    VisibilityService.showTableView(entryPane, apEntryPage,tfSearch, passwordRecordTableView, btnEditOK,apBottomTable);
                    save();

                } catch (Exception E) {

                }
            });

        }
    }

    @FXML
    void deleteDatabase (ActionEvent event) throws Exception
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Warning, this will permanently delete all your passwords for this database");
        alert.setContentText("Are you sure you want to proceed?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            passwordRecordList.remove(passwordRecordList);
            File deleteFile = new File(FilePath.getCurrentDBdir()).getAbsoluteFile().getParentFile();
            FileService fileService = new FileService();
            fileService.deleteDir(deleteFile);
            Serialization.dbFileListSerialize(FilePath.getDbFilesList(), Paths.get(FilePath.getDBFilesListDir()));
            SceneHandler.stageFullScreen(btnLockDB);
        }

    }
    @FXML
    void deletePwdEntry(ActionEvent event) throws  Exception {
        PasswordRecord selectedItem = passwordRecordTableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            passwordRecordList.remove(selectedItem);
            save();
        }

    }

    @FXML
    void pwdEntryPage(ActionEvent event) throws Exception
    {
        VisibilityService.entrySpecs (apEntryPage,tfSearch,btnCreate, passwordRecordTableView,apBottomTable);
        // Shows the page for managing a password entry
    }


    @FXML void generatePwd(ActionEvent event)
    {
        pfPwdField.setText(PasswordGenerator.generatePassword(32));
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
        String pwdFPNewValue= FilePath.getCurrentDBdir();
        String directoryNewValue =  FilePath.getSelectedDir();
       //the new values of passwordFilePath and selectedDirectoryPath will be lost upon loading the FXML login "login.fxml"
        //so to keep the new values of both Strings,I create 2 new strings that store the values of the new paths,
        //load login.FXML, then set the values of the static path Strings to the new values.

           FXMLLoader fxmlLoader = new FXMLLoader();
           fxmlLoader.setLocation(getClass().getResource("login/login.fxml"));

           Scene scene = new Scene(fxmlLoader.load());
           Stage stage = new Stage();

           stage.setTitle("New Window");
           stage.setScene(scene);
           stage.show();

        FilePath.setCurrentDBdir( pwdFPNewValue);
        FilePath.setSelectedDir(  directoryNewValue);
    }


    @FXML
    void returnTableView(ActionEvent event) throws Exception
    {
        VisibilityService.showTableView(entryPane, apEntryPage,tfSearch, passwordRecordTableView, btnEditOK,apBottomTable);
        apPwdGenerate.setDisable(true);
        apPwdGenerate.setVisible(false);
        apPwdGenerate.getChildren().remove(sliderPwdGenerator);
        // Empties the edited entry after clicking the cancel button,so the values
        // of the previously edited entry doesn't get transferred to the entry about to be created
        tfTitel.setText("");
        tfUsername.setText("");
        tfURL.setText("");
        pfPwdField.setText("");
        tANotes.setText("");
    }


    @FXML
    void lockDB(ActionEvent event) throws Exception {
        SceneHandler.stageFullScreen(btnLockDB);
    }

   void save () throws Exception {
    FileProtector fileProtector = new FileProtector();
    Secrets secrets = new Secrets();
    secrets.setPwdRecord(passwordRecordList);
    secrets.setTimerSpecs(TimerSpecs.getTimerSpecs());
    fileProtector.encryption(passwordRecordList,secrets.getTimerSpecs());
    textTitel.setText(tfTitel.getText());
    textUsername.setText(tfUsername.getText());
    hyperLinkURL.setText(tfURL.getText());
    textNotes.setText(tANotes.getText());
    visibilityService.setSelectedPassword(pfPwdField.getText());
  // sets the password of the selected PasswordRecord to be the same as the newly added or edited password
    imgVisible.setVisible(false);
    imgNotVisible.setVisible(true);
    imgPwdNotVisible.setVisible(true);
    imgPwdVisible.setVisible(false);
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
    void bruteForceTimePage(ActionEvent event) throws Exception {
        VisibilityService visibilityService = new VisibilityService();
        visibilityService.showPwdGenerateMenu(apPwdGenerate,entryPane,btnEditOK,apBottomTable);
        spinnerPwdGenerator();
        sliderPwdGenerator();
        btnPwdGenerator();
        bruteForceTextField();
        apPwdGenerate.getChildren().addAll(spinnerPwdGenerator, sliderPwdGenerator);
        PasswordRecord selectedItem = passwordRecordTableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            passwordRecordList.set(passwordRecordList.indexOf(selectedItem), selectedItem);
            bruteForceTextField.setText(selectedItem.getPassword());
            PasswordGenerator.calcBruteforceTime(textGenePwdQuality, textBruteForceTime, textEntropy,  bruteForceTextField.getText());
            passwordRecordTableView.getSelectionModel().clearSelection();
        }
        else {
            bruteForceTextField.setText(PasswordGenerator.generatePassword(spinnerPwdGenerator.getValue()));
            PasswordGenerator.calcBruteforceTime(textGenePwdQuality, textBruteForceTime, textEntropy,  bruteForceTextField.getText());
        }
    }
    public void btnPwdGenerator( ) {

        btnPwdGenerator.setOnAction(e ->
                // when the btnPwdGenerator button is pressed, the length of the integer pwdLenght is set to be equal to
                // the value of the pwdSpinner
                // then the string generated by generatePassword() is put inside the bruteForceField TextField
                //which is then used to calculate the the estimated cracking time of the password.
        {
            try {
                pwdLength = spinnerPwdGenerator.getValue();
                bruteForceTextField.setText(PasswordGenerator.generatePassword(pwdLength));
                PasswordGenerator.calcBruteforceTime(textGenePwdQuality, textBruteForceTime, textEntropy,  bruteForceTextField.getText());
            } catch (Exception E) {

            }

        });
    }

    public void spinnerPwdGenerator() {
        spinnerPwdGenerator = (Spinner<Integer>) new Spinner(0, 999, 12);
        spinnerPwdGenerator.setPrefSize(75, 25);
        spinnerPwdGenerator.setLayoutX(580);
        spinnerPwdGenerator.setLayoutY(100);
        spinnerPwdGenerator.setEditable(true);
        spinnerPwdGenerator.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
        spinnerPwdGenerator.getEditor().setOnKeyReleased(e ->
            {
                bruteForceTextField.setText(PasswordGenerator.generatePassword(parseInt(newValue)));
                PasswordGenerator.calcBruteforceTime(textGenePwdQuality, textBruteForceTime, textEntropy,  bruteForceTextField.getText());
            });
            bruteForceTextField.setText(PasswordGenerator.generatePassword(parseInt(newValue)));
            PasswordGenerator.calcBruteforceTime(textGenePwdQuality, textBruteForceTime, textEntropy, bruteForceTextField.getText());
            sliderPwdGenerator.setValue(parseInt(newValue));
        });
    }

    public void sliderPwdGenerator()
    {
        sliderPwdGenerator.setBlockIncrement(1);
        sliderPwdGenerator.setValue(spinnerPwdGenerator.getValue());
        sliderPwdGenerator.setPrefWidth(570);
        sliderPwdGenerator.setLayoutY(110);
        sliderPwdGenerator.setShowTickLabels(true);
        sliderPwdGenerator.setOnMouseDragged(e -> {
          Double newData = sliderPwdGenerator.getValue();
          int value = newData.intValue();
          spinnerPwdGenerator.getValueFactory().setValue(value);
        });
    }

    void bruteForceTextField() {
        bruteForceTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            PasswordGenerator.calcBruteforceTime(textGenePwdQuality, textBruteForceTime, textEntropy,  newValue);
        });
    }


    @FXML
    void updateMasterPwd(ActionEvent event) throws Exception {
        MasterPwdGui masterPwdGui = new MasterPwdGui();
        masterPwdGui.updateMasterPwd();
          save();
    }
@FXML
    void timerDialog(ActionEvent event) throws Exception {
   TimerService.timerDialog(passwordRecordList);
   TimerService.timerCountDown(btnLockDB,anchorPane);
    }

    @FXML
   private void initialize() throws Exception {
        passwordRecordTableView.setPlaceholder(new Label("0 entries in the database. Click the + button to add new entries!"));
        anchorPane.setOnContextMenuRequested(e ->
                ctxTableMenu.show(anchorPane, e.getScreenX(), e.getScreenY()));

        String image = Main.class.getResource("Entry-Management/magnifying-glass.png").toExternalForm();
        tfSearch.setStyle("-fx-background-image: url('" + image + "'); " +
                " -fx-background-repeat: no-repeat; -fx-background-position: right; -fx-background-size: 38 24;");

        passwordRecordTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            PasswordRecord selectedItem = passwordRecordTableView.getSelectionModel().getSelectedItem();
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
                String hidePwd = "";
                for (int i = 0; i < 12; i++) {
                    hidePwd = '\u2022' + hidePwd;
                    // Putting password string as 12 bullets, to hide the content and length of the user's passwords.
                }
                visibilityService = new VisibilityService();
                visibilityService.pwdVisibilityTable(  toggleButton,   imgPwdVisible,  imgPwdNotVisible,   textPassword,
                        selectedItem.getPassword(), hidePwd, passwordRecordTableView);
                textPassword.setText(hidePwd);

            }
        });
        VisibilityService.pwdVisibilityMenu(togBtnPwd,imgVisible,imgNotVisible,tfPwd,pfPwdField);

            colTitel.setCellValueFactory(new PropertyValueFactory<>("title"));
            colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
            colURL.setCellValueFactory(new PropertyValueFactory<>("url"));
            colNotes.setCellValueFactory(new PropertyValueFactory<>("Notes"));
            Authentication authentication = new Authentication();
            passwordRecordList.addAll(authentication.authenticated());
            TimerService.timerCountDown(btnLockDB,anchorPane);
            passwordRecordTableView.setItems(passwordRecordList);
            searchFilter();
            btnEnterMenu.setOnAction(e -> {
                try {
                    VisibilityService.entrySpecs (apEntryPage,tfSearch,btnCreate, passwordRecordTableView,apBottomTable);
                } catch (Exception E) {

                }
            });
           save();
           // Saves the database each time a user logs in,
        // so the challenge and response are never used more than once
    }

    @FXML
    private void searchFilter()
    {

        FilteredList<PasswordRecord> filteredData = new FilteredList<>(passwordRecordTableView.getItems()
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
        SortedList<PasswordRecord> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(passwordRecordTableView.comparatorProperty());
        passwordRecordTableView.setItems(sortedData);

    }

}

