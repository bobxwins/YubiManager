package sample;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.FileOutputStream;
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
    @FXML private ImageView imgOpen;
    @FXML private ImageView imgLocked;
    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TableColumn<String,String> recent;

    @FXML TableView<String> recentFilesTable;

    private ObservableList<String> recentFilesData = FXCollections.observableArrayList();
    @FXML
    void login(ActionEvent event) throws Exception {

        DatabaseHandler databaseHandler = new DatabaseHandler();
        if (databaseHandler.loginAuthentication(mpField, ybkSecret, btnSignIn ) == false) {
            return;
        }

    }

    @FXML
    void newDB(ActionEvent event) throws Exception {

      Global.getLabelEnterPwd().setVisible(false);
        DatabaseHandler databaseHandler = new DatabaseHandler();
        databaseHandler.newDBdialog(btnCreateDB);


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
    private void initialize() throws Exception {

         recent.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()));
         recentFilesData.addAll(Global.getRFCArray());
         recentFilesTable.setItems(recentFilesData);

         recentFilesTable.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
              try{
                 String selectedItem = recentFilesTable.getSelectionModel().getSelectedItem();
                 if (selectedItem != null) {
                                Global.getLabelEnterPwd().setVisible(true);
                                System.out.println(selectedItem);
                               Global.setPasswordFilePath(selectedItem);
                               Global.setSelectedDirectoryPath( Paths.get(Global.getPasswordFilePath()).getParent()+"\\");
                     System.out.println("the seelcted path is " +  Global.getSelectedDirectoryPath());
                            }
               //   Global.getLabelEnterPwd().setVisible(false);
                  }
                           catch (Exception E) {

                           }     });

        anchorPane.getChildren().addAll(Global.getLabelEnterPwd());
        btnYubikey.setStyle(   "-fx-background-radius: 5em; "
        );
        btnYubikey.setOnAction(e-> {
                    if(!imgOpen.isVisible())
                    {
                        imgOpen.setVisible(true);
                        imgLocked.setVisible(false);
                        return;
                    }
                    imgOpen.setVisible(false);
                    imgLocked.setVisible(true);

        }

                );

         new FileOutputStream( Global.getRecentFilesDir(), true).close();

         Global.setRecentFilesContent(new String(FileUtils.readAllBytes( Global.getRecentFilesDir())));

        DatabaseHandler databaseHandler = new DatabaseHandler();

        databaseHandler.createMenuItems(menuRecent, Global.getLabelEnterPwd());

    }

    }


