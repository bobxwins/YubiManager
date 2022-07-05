package sample;

import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class PasswordVisbilityHandler {
    public  String getSelectedPassword() {
        return selectedPassword;
    }

    private   String selectedPassword;

    public  void setSelectedPassword(String selectedP) {
        this.selectedPassword = selectedP;
    }

    public   void toggleVisbility (Button toggleButton, ImageView imgPwdVisible, ImageView imgPwdNotVisible, Text textPassword,
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
    public static  void toggleVisbility (Button tBtn, ImageView imgPwdVisible, ImageView imgPwdNotVisible, TextField tf, PasswordField pf) {
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
