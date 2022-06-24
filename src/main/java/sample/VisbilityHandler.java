package sample;

import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class VisbilityHandler {
    private static  String selectedPassword;
    public static void setSelectedPassword(String selectedP) {
        VisbilityHandler.selectedPassword = selectedP;
    }

    public static void toggleVisbility (ToggleButton toggleButton, ImageView imgPwdVisible, ImageView imgPwdNotVisible, Text textPassword,
                                        String showPwd, String hidePwd)
            //Toggles the visibilty of the password shown in the tableview at the bottom of the application
    {

        toggleButton.setOnAction(e -> {
            if (toggleButton.isSelected() ) {
                imgPwdVisible.setVisible(true);
                imgPwdNotVisible.setVisible(false);
                textPassword.setText(showPwd);
                return;     }

            imgPwdVisible.setVisible(false);
            imgPwdNotVisible.setVisible(true);
            textPassword.setText(hidePwd);

        });
        imgPwdVisible.setVisible(false);
        imgPwdNotVisible.setVisible(true);
        textPassword.setText(hidePwd);
    }
    public static  void toggleVisbility (ToggleButton tBtn, ImageView imgPwdVisible, ImageView imgPwdNotVisible, TextField tf, PasswordField pf) {
      /// toggles the visibility of the password in the create/edit entry menu

        pf.textProperty().addListener((observable, oldValue,    newValue) -> {
            tf.setText(newValue);


        });
        tBtn.setOnAction(e -> {

            if (tBtn.isSelected() ) {
                tf.textProperty().addListener((observable, oldValue, newValue) -> {
                    pf.setText(newValue);


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
