package sample;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.apache.commons.lang3.ObjectUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Base64;

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
    private Menu menuRecent;


    @FXML private  Button  btnYubikey;
    @FXML private ImageView imgLocked;
    @FXML private ImageView imgUnLocked;
    @FXML
    private AnchorPane anchorPane;

    private   byte[] generatedIV = new byte[64]; //16

    private   byte[] salt = new byte[64]; //32

    private   int iterationCount =75285;

    private   int keylength = 1965652;

    private static ObservableList<KeySpecs> keySpecs = FXCollections.observableArrayList();

    @FXML
    private TableColumn<String,String> recent;

    @FXML TableView<String> recentFilesTable;

    @FXML
    void login(ActionEvent event) throws Exception {

        DatabaseHandler databaseHandler = new DatabaseHandler();
        if (databaseHandler.loginAuthentication(mpField, ybkSecret, btnSignIn ) == false) {
            return;
        }


        recentFilesTable.getItems().clear();

        keySpecs.addAll( SerializedObject.readObject(FileUtils.readAllBytes(KeySpecs.getKeySpecsDir())));
        System.out.println(  keySpecs.get(0));
        System.out.println(  keySpecs);

    }

    @FXML
    void newDB(ActionEvent event) throws Exception {

      Global.getLabelEnterPwd().setVisible(false);
        DatabaseHandler databaseHandler = new DatabaseHandler();
        databaseHandler.newDBdialog(btnCreateDB);
        recentFilesTable.getItems().clear();

    }

    @FXML
    void openDB(ActionEvent event) throws Exception {

        DatabaseHandler databaseHandler = new DatabaseHandler();
        if (databaseHandler.openDB()==false)
        {
            return;
        }
           Global.getLabelEnterPwd().setVisible(true);

        }


        @FXML
    void deleteRow(ActionEvent event) throws  Exception {
        String selectedItem = recentFilesTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            Global.getRecentFilesData().remove(selectedItem);
            SerializedObject.writeObject(Global.getRecentFilesData(), Paths.get(Global.getRecentFilesDir()));
           //updates the recentFile text file, after deleting the selected table item
        }

    }



    @FXML
    private void initialize() throws Exception {

       Global.getRecentFilesData().addAll(SerializedObject.readObject(FileUtils.readAllBytes(Global.getRecentFilesDir())));
       recentFilesTable.setItems(Global.getRecentFilesData());

       recent.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()));


         recentFilesTable.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
           try{
            String selectedItem = recentFilesTable.getSelectionModel().getSelectedItem();

                 if (selectedItem != null) {
                                Global.getLabelEnterPwd().setVisible(true);

                               Global.setPasswordFilePath(selectedItem);
                               Global.setSelectedDirectoryPath( Paths.get(Global.getPasswordFilePath()).getParent()+"\\");

                            }

                            }

                           catch (Exception E) {

                           }     });

        anchorPane.getChildren().addAll(Global.getLabelEnterPwd());
        btnYubikey.setStyle(   "-fx-background-radius: 5em; "
        );
        btnYubikey.setOnAction(e-> {
                    if(!imgLocked.isVisible())
                    {
                        imgLocked.setVisible(true);
                        imgUnLocked.setVisible(false);
                        return;
                    }
                    imgLocked.setVisible(false);
                    imgUnLocked.setVisible(true);

        }

                );


        DatabaseHandler databaseHandler = new DatabaseHandler();

        databaseHandler.createMenuItems(menuRecent, Global.getLabelEnterPwd());

    }

    }


