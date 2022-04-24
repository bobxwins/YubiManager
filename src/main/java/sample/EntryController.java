package sample;

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
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.Serializable;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.Optional;


public class EntryController implements Serializable {

    public static int i = 0;
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
    private Text textPwdQuality;
    @FXML
    private Button btnSignOut;
    @FXML
    private Button btnEditEntry;

    @FXML
    private Button btnPwdGenerator;

    @FXML
    private AnchorPane anchorPane;

    @FXML private VBox vBoxLabel;

    @FXML private VBox vBoxTf;

    @FXML private AnchorPane apCalc;

    private ObservableList<Entry> entryData = FXCollections.observableArrayList();
    EntryHandler entryHandler = new EntryHandler();
 @ FXML
 private Text textEntropy;
    @FXML
    private Button btnEnterMenu;
    @FXML
    private Button btnEditOK;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnCreate;
    @FXML
    private Button btnClose;
    @FXML
    private TextField generatedPWDfield;
    @FXML
    private Text textCalcEntropy;
    @FXML
    private Text textCalcGPU;
    @FXML
    private Text textCalcGPUClusters;

    @FXML private BorderPane bpEntryMenu;

    @FXML private AnchorPane apPwdGenerate;

    @FXML private AnchorPane entryPane;
    @FXML private TextField tfPwdLength;
   static  int length;
    @FXML
    private MenuItem menuDeleteRow;
    @FXML private MenuItem menuPwdStrength;
    @FXML
    private MenuItem  menuNewDB;
    @FXML
    void createEntry(ActionEvent event) throws Exception {
        entryData.add(new Entry(tfTitel.getText(), tfUsername.getText(),tfURL.getText(),pfPwdField.getText(),tANotes.getText()));
       // entryHandler.createEntryObject(anchorPane);
        tfTitel.setText("");
        tfUsername.setText("");
        tfURL.setText("");
        pfPwdField.setText("");
        tANotes.setText("");
        showTableView();


    }
    @FXML
    void generatePwd(ActionEvent event) throws Exception {


        //   double entropy =  Math.log10(Math.pow(PasswordUtils.ALL_CHARS.length, length))/Math.log10(2);
        //   textEntropy.setText("Entropy bits is: "+(entropy));
        apPwdGenerate.setVisible(true);
        apPwdGenerate.setDisable(false);
        entryPane.setDisable(true);
        entryPane.setVisible(false);
        btnEditOK.setDisable(true);
        btnEditOK.setVisible(false);

        tfPwdLength.setOnKeyReleased( e -> {

            tfPwdLength.textProperty().addListener((observable, oldValue, newValue) -> {



                        boolean isNumeric = newValue.chars().allMatch(Character::isDigit);

                        if (!newValue.matches("\\d*")) {
                            tfPwdLength.setText(newValue.replaceAll("[^\\d]", ""));
                            return;
                        }
                        if (isNumeric == true && tfPwdLength.getLength() >= 1)
                        length = Integer.parseInt(tfPwdLength.getText());
                        tfPwdLength.setText(String.valueOf(PasswordUtils.getPassword(length).

                                length()));
                        PasswordUtils.getPassword(Integer.parseInt(tfPwdLength.getText()));

                        generatedPWDfield.setText(PasswordUtils.getPassword(length));
                        double entropy = Math.log10(Math.pow(PasswordUtils.ALL_CHARS.length, length)) / Math.log10(2);
                        textEntropy.setText("Entropy bits is: " + (entropy));


            });


        });

        btnPwdGenerator.setOnAction(e -> {
            try{
                length= Integer.parseInt(tfPwdLength.getText());
                tfPwdLength.setText(String.valueOf(PasswordUtils.getPassword(length).length()));
                PasswordUtils.getPassword(Integer.parseInt(tfPwdLength.getText()));

                generatedPWDfield.setText(PasswordUtils.getPassword(length));
                double entropy =  Math.log10(Math.pow(PasswordUtils.ALL_CHARS.length,length))/Math.log10(2);
                textEntropy.setText("Entropy bits is: "+(entropy));
            } catch (Exception E) {

            }
        });
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
        entryPane.setVisible(true);
        entryPane.setDisable(false);

        bpEntryMenu.setDisable(true);
        bpEntryMenu.setVisible(false);

        tfSearch.setDisable(false);

        entryTable.setVisible(true);
        entryTable.setDisable(false);

        btnEditOK.setDisable(true);
        btnEditOK.setVisible(false);

        apCalc .setDisable(true);
        apCalc.setVisible(false);
    }

    void entrySpecs() throws Exception {
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


    }

    @FXML
    void copyPWD(ActionEvent event) throws Exception
    {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(generatedPWDfield.getText());
        clipboard.setContent(content);
    }

    @FXML
    void entryMenu(ActionEvent event) throws Exception
    {
        entrySpecs();
    }



    @FXML
    void newDB(ActionEvent event)  {
     //   labelEnterPwd.setVisible(false);
        DatabaseHandler databaseHandler = new DatabaseHandler();
      //  databaseHandler.dialog(menuNewDB);
     //   updateRecentFileString();

    }



    @FXML
    void openDB(ActionEvent event) throws Exception {
/*
Parent root = FXMLLoader.load(Main.class.getResource("login/login.fxml"));
        Stage entryWindow= (Stage) btnSignOut.getScene().getWindow();
        entryWindow.setScene(new Scene(root));


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
*/
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
    @FXML void editEntry (ActionEvent event) throws  Exception {

        Entry selectedItem = entryTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            entrySpecs();

             tfTitel.setText(selectedItem.getTitel());
            tfUsername.setText(selectedItem.getUsername());
            tfURL.setText(selectedItem.getUrl());
            pfPwdField.setText(selectedItem.getPassword());
            tANotes.setText(selectedItem.getNotes());
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
                    tfTitel.setText("");
                    tfUsername.setText("");
                    tfURL.setText("");
                    pfPwdField.setText("");
                    tANotes.setText("");
                    showTableView();
                } catch (Exception E) {

                }
            });

        }
    }



    @FXML
    void saveEntry(ActionEvent event) throws Exception {
        ObjectIOExample obj = new ObjectIOExample();
        obj.write(entryData, Paths.get(LoginController.passwordFilePath));

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Database succesfully saved!");
        alert.showAndWait();
        
    }


    @FXML
   private void initialize() throws Exception {

          apCalc.setDisable(true);
         apCalc.setVisible(false);
        btnClose.setOnAction((e -> {
        showTableView();


        }));



        colTitel.setCellValueFactory(new PropertyValueFactory<Entry, String>("titel"));
        colUsername.setCellValueFactory(new PropertyValueFactory<Entry, String>("username"));
        colURL.setCellValueFactory(new PropertyValueFactory<Entry, String>("url"));
        colPassword.setCellValueFactory(new PropertyValueFactory<Entry, String>("password"));
        colNotes.setCellValueFactory(new PropertyValueFactory<Entry, String>("Notes"));
        entryData.addAll(ObjectIOExample.read(Paths.get(LoginController.passwordFilePath)));
        entryTable.setItems(entryData);
        filter();
       /* btnEnterMenu.setOnAction(e -> {
            try{
          entrySpecs();
            } catch (Exception E) {

            }
        });
        */
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

    @FXML
    void calculatePwd(ActionEvent event) throws Exception {
        entryTable.setVisible(false);
        entryTable.setDisable(true);
         apCalc.setVisible(true);
         apCalc.setDisable(false);
        Entry selectedItem = entryTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {

            entryData.set(entryData.indexOf(selectedItem), selectedItem);
            selectedItem.getPassword();

            double entropy =  Math.log10(Math.pow(PasswordUtils.cardinality(  selectedItem.getPassword()),selectedItem.getPassword().length()))/Math.log10(2);

            String quality ="";
            textPwdQuality.setStyle("-fx-font-size: 21px;");
            textPwdQuality.setUnderline(false);
                if(entropy <28)
                {
           quality= "Very weak";
                    textPwdQuality.setFill(Color.DARKRED);

                }
                if(entropy > 28 && entropy < 35 )
                {
                    quality= "Weak";

                    textPwdQuality.setFill(Color.RED);
                }
                 if(entropy > 35 && entropy < 59 )
                 {
                     quality= "Moderate";
                     textPwdQuality.setFill(Color.DARKBLUE);
                 }
                 if(entropy > 60 && entropy < 127 )
                 {
                     quality= "Strong";
                     textPwdQuality.setFill(Color.GREEN);

                 }
                 if(entropy >= 127 )
                 {
                     textPwdQuality.setFill(Color.DARKGREEN);
                     textPwdQuality.setUnderline(true);
                     textPwdQuality.setStyle("-fx-font-size: 30px;");
                     quality= "Overkill";

                 }


          //  DecimalFormat numberFormat = new DecimalFormat("#.000000000000000000000");
            System.out.println(Math.round(1000/2.7));
            textPwdQuality.setText(quality);

            PasswordUtils.BRUTEFORCETIMEGPU=Math.pow(2,entropy)/ PasswordUtils.BRUTEFORCEGPU.doubleValue()/2;
            if (entropy>174)
            {
                PasswordUtils.BRUTEFORCETIMEGPU=Double.POSITIVE_INFINITY;

            }
            textCalcEntropy.setText("The calculated entropy is: "+(entropy)+" bits"+"\nYour password quality is: ");
            textCalcGPU.setText( "Estimated time for brute forcing the passwords with a GPU is: "
                    +"\n"+String.format("%.3f", PasswordUtils.BRUTEFORCETIMEGPU)+" seconds"
            +"\nEstimated hours is: "+String.format("%.3f", PasswordUtils.BRUTEFORCETIMEGPU/3600)+" hours"+
                    "\nEstimated days is: "+String.format("%.3f",PasswordUtils.BRUTEFORCETIMEGPU/3600/24)+" days"+
                    "\nEstimated years is: "+String.format("%.3f",PasswordUtils.BRUTEFORCETIMEGPU/3600/24/365)+" years");

            PasswordUtils.BRUTEFORCETIMEGPUCLUSTERS=Math.pow(2,entropy)/ PasswordUtils.BRUTEFOCEGPUCLUSTERS.doubleValue()/2;

            if (entropy>174)
            {
                PasswordUtils.BRUTEFORCETIMEGPUCLUSTERS=Double.POSITIVE_INFINITY;
            }

            textCalcGPUClusters.setText("Estimated time for brute forcing the passwords with GPU Clusters is: "
                    +"\n"+String.format("%.3f" ,PasswordUtils.BRUTEFORCETIMEGPUCLUSTERS)+" seconds"
                    +"\nEstimated hours is: "+String.format("%.3f", PasswordUtils.BRUTEFORCETIMEGPUCLUSTERS/3600)+" hours" +
                    "\nEstimated days is: "+String.format("%.3f" ,PasswordUtils.BRUTEFORCETIMEGPUCLUSTERS/3600/24)+" days"+
                    "\nEstimated years is: "+String.format("%.3f" , PasswordUtils.BRUTEFORCETIMEGPUCLUSTERS/3600/24/365)+" years");

        }
    }

}