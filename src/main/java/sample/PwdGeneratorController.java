/*
package sample;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import static java.lang.Integer.parseInt;

public class PwdGeneratorController {

    static int pwdLength;
    //  private static final long serialVersionUID = 6529685098267757690L;
  //  private ObservableList<PasswordRecord> passwordRecordList = FXCollections.observableArrayList();
  //  VisibilityService visibilityService = new VisibilityService();
    private ObservableList<PasswordRecord> passwordRecordList = FXCollections.observableArrayList();
    Slider sliderPwdGenerator = new Slider(4, 999, 1);
    Spinner<Integer> spinnerPwdGenerator;

    @FXML private TextField generatedPWDfield;
    @FXML private Text textGenePwdQuality;
    @FXML private Text textBruteForceTime;
    @FXML private Text textEntropy;
    @FXML private AnchorPane apPwdGenerate;
    @FXML private AnchorPane entryPane;
    @FXML private AnchorPane apBottomTable;
    @FXML private TableView<PasswordRecord> entryTable;
    @FXML private Button btnPwdGenerator;
    @FXML private Button btnEditOK;
    @FXML private Button btnReturn;
    @FXML private PasswordField pfPwdField;

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
    void generatePwd (ActionEvent event) throws Exception {
        VisibilityService cVBH = new VisibilityService();
        cVBH.showPwdGenerateMenu(apPwdGenerate,entryPane,btnEditOK,apBottomTable);
        spinnerPwdGenerator();
       // sliderPwdGenerator();
        btnPwdGenerator();
        textFieldPwdGenerator();
        apPwdGenerate.getChildren().addAll(spinnerPwdGenerator, sliderPwdGenerator);
        PasswordRecord selectedItem = entryTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            passwordRecordList.set(passwordRecordList.indexOf(selectedItem), selectedItem);
            generatedPWDfield.setText(selectedItem.getPassword());
            PasswordGenerator.pwdBruteforceTime(textGenePwdQuality, textBruteForceTime, textEntropy,  generatedPWDfield.getText());
            entryTable.getSelectionModel().clearSelection();
        }
        else {
            generatedPWDfield.setText(PasswordGenerator.generatePassword(spinnerPwdGenerator.getValue()));
            PasswordGenerator.pwdBruteforceTime(textGenePwdQuality, textBruteForceTime, textEntropy,  generatedPWDfield.getText());
        }

    }

    @FXML void calcBruteForceMenu(ActionEvent event)
    {
        pfPwdField.setText(PasswordGenerator.generatePassword(32));
    }

    @FXML
    void newDB(ActionEvent event) throws  Exception {
        SceneHandler sceneHandler = new SceneHandler();
        sceneHandler.newDBdialog(btnReturn);
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
                generatedPWDfield.setText(PasswordGenerator.generatePassword(parseInt(newValue)));

                PasswordGenerator.pwdBruteforceTime(textGenePwdQuality, textBruteForceTime, textEntropy,  generatedPWDfield.getText());
            });

            generatedPWDfield.setText(PasswordGenerator.generatePassword(parseInt(newValue)));

            PasswordGenerator.pwdBruteforceTime(textGenePwdQuality, textBruteForceTime, textEntropy, generatedPWDfield.getText());
            sliderPwdGenerator.setValue(parseInt(newValue));

        });
    }

    public void sliderPwdGenerator(Slider sliderPwdGenerator)
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
    public void btnPwdGenerator( ) {

        btnPwdGenerator.setOnAction(e ->
                // when the btnPwdGenerator button is pressed, the length of the integer pwdLenght is set to be equal to
                // the value of the pwdSpinner
                // then the string generated by generatePassword() is put inside the generatePwdField textfield
                //which is then used to calculate the the estimated cracking time of the password.
        {
            try {
                pwdLength = spinnerPwdGenerator.getValue();

                generatedPWDfield.setText(PasswordGenerator.generatePassword(pwdLength));

                PasswordGenerator.pwdBruteforceTime(textGenePwdQuality, textBruteForceTime, textEntropy,  generatedPWDfield.getText());

            } catch (Exception E) {

            }

        });
    }
    void textFieldPwdGenerator() {
        generatedPWDfield.textProperty().addListener((observable, oldValue, newValue) -> {
            //  System.out.println("textfield changed from " + oldValue + " to " + newValue);
            PasswordGenerator.pwdBruteforceTime(textGenePwdQuality, textBruteForceTime, textEntropy,  newValue);
        });
    }
}

*/

