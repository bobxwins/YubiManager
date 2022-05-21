package sample;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

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
    private Menu menuRecent;


    @FXML private  Button  btnYubikey;
    @FXML private ImageView imgLocked;
    @FXML private ImageView imgUnLocked;
    @FXML
    private AnchorPane anchorPane;


    @FXML
    private TableColumn<String,String> recent;

    @FXML private TableView<String> recentFilesTable;

    @FXML
    void login(ActionEvent event) throws Exception {

        DatabaseHandler databaseHandler = new DatabaseHandler();
        if (databaseHandler.loginAuthentication(mpField, ybkSecret, btnSignIn ) == false) {

            return;
        }

        recentFilesTable.getItems().clear();

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
            SerializedObject.writeObservableList(Global.getRecentFilesData(), Paths.get(Global.getRecentFilesDir()));
           //updates the recentFile text file, after deleting the selected table item


        }

    }



    @FXML
    private void initialize() throws Exception {
        recentFilesTable.setItems(Global.getRecentFilesData());

        if((FileUtils.readAllBytes(Global.getRecentFilesDir()).length)!=0)
        {
            Global.getRecentFilesData().addAll(SerializedObject.readObservableList(FileUtils.readAllBytes(Global.getRecentFilesDir())));

            String defaultFile = recentFilesTable.getItems().get(0);
            // sets the default RecentFile to the first element
            Global.setPasswordFilePath(defaultFile);
            Global.setSelectedDirectoryPath( Paths.get(Global.getPasswordFilePath()).getParent()+"\\");
        }


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
