package sample;

import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class VisibilityHandler {
    private   String selectedPassword;

    public static void editEntryVisibility (Button btnEditOK, Button btnCreate) {

     /*   apBottomTable.setDisable(true);
        apBottomTable.setVisible(false);
        */
        btnEditOK.setDisable(false);
        btnEditOK.setVisible(true);
        btnCreate.setVisible(false);
        btnCreate.setDisable(true);
    }
    public void showPwdGenerateMenu(AnchorPane apPwd, AnchorPane entryPane, Button btnEdit, AnchorPane apBottomTable) {
        apPwd.setVisible(true);
        apPwd.setDisable(false);
        entryPane.setDisable(true);
        entryPane.setVisible(false);
        btnEdit.setDisable(true);
        btnEdit.setVisible(false);
        apBottomTable.setVisible(false);
    }
    public static void entrySpecs (AnchorPane apEntryMenu, TextField tfSearch, Button btnCreate, TableView<Entry> entryTable, AnchorPane apBottomTable) {
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
        public static void showTableView (AnchorPane entryPane,AnchorPane apEntryMenu,TextField tfSearch,TableView<Entry> entryTable,Button btnEditOK
        ,AnchorPane  apBottomTable)
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

    public void returnTableView () {

    }

    public  String getSelectedPassword() {
        return selectedPassword;
    }


    public  void setSelectedPassword(String selectedP) {
        this.selectedPassword = selectedP;
    }

    public   void pwdVisibilityTable(Button toggleButton, ImageView imgPwdVisible, ImageView imgPwdNotVisible, Text textPassword,
                                     String showPwd, String hidePwd, TableView<Entry> entryTable)
    //Toggles the visibilty of the password shown in the tableview at the bottom of the application
    {

        toggleButton.setOnAction(e -> {
            if (!imgPwdVisible.isVisible() ) {
                imgPwdVisible.setVisible(true);
                imgPwdNotVisible.setVisible(false);
                Entry selectedItem  = entryTable.getSelectionModel().getSelectedItem();
                if (selectedItem != null && selectedPassword == null)
                {
                    textPassword.setText(showPwd);
                }
                else {

                    textPassword.setText(getSelectedPassword());

                }
                return;     }

            imgPwdVisible.setVisible(false);
            imgPwdNotVisible.setVisible(true);
            textPassword.setText(hidePwd);

        });
        imgPwdVisible.setVisible(false);
        imgPwdNotVisible.setVisible(true);
        textPassword.setText(hidePwd);
    }
    public static  void pwdVisibilityMenu(Button tBtn, ImageView imgPwdVisible, ImageView imgPwdNotVisible, TextField tf, PasswordField pf) {
        /// toggles the visibility of the password, when editing or creating a new entry

        pf.textProperty().addListener((observable, oldValue,    newValue) -> {
            tf.setText(newValue);

        });
        tBtn.setOnAction(e -> {

            if (!imgPwdVisible.isVisible() ) {
                tf.textProperty().addListener((observable, oldValue, newValue) -> {
                    pf.setText(newValue);

// The textfield listens to the passwordfield and vice versa
                });

                imgPwdVisible.setVisible(true);
                imgPwdNotVisible.setVisible(false);
                tf.setVisible(true);
                pf.setVisible(false);

                return;      }
            imgPwdVisible.setVisible(false);
            imgPwdNotVisible.setVisible(true);
            tf.setVisible(false);
            pf.setVisible(true);
        });
        imgPwdVisible.setVisible(false);
        imgPwdNotVisible.setVisible(true);
        tf.setVisible(false);
        pf.setVisible(true);
    }

}
