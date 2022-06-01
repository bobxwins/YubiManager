package sample;

import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.nio.charset.StandardCharsets;

public class PasswordHandler {

    private static  String selectedPassword;

    public static void setSelectedPassword(String selectedP) {
        PasswordHandler.selectedPassword = selectedP;
    }

    public static String getSelectedPassword() {
        return selectedPassword;
    }

    public static void toggleVisbility (ToggleButton toggleButton, ImageView imgPwdVisible, ImageView imgPwdNotVisible, Text textPassword,
                                  String showPwd, String hidePwd)
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
   public static  void toggleVisbility (ToggleButton tBtn,ImageView imgPwdVisible, ImageView imgPwdNotVisible,TextField tf, PasswordField pf) {

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
                System.out.println("IT IS VISIBLE!");
                tf.setVisible(true);
                pf.setVisible(false);

      return;      }
            imgPwdVisible.setVisible(false);
            imgPwdNotVisible.setVisible(true);
            tf.setVisible(false);
            pf.setVisible(true);
            System.out.println("IT IS NOT VISIBLE");
        });
                imgPwdVisible.setVisible(false);
                imgPwdNotVisible.setVisible(true);
                tf.setVisible(false);
                pf.setVisible(true);
                System.out.println("IT IS NOT VISIBLE");


    }

   }

