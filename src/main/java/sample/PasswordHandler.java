package sample;

import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class PasswordHandler {

    public static void toggleVisbility (ToggleButton toggleButton, ImageView imgPwdVisible, ImageView imgPwdNotVisible, Text textPassword,
                                  String showPwd, String hidePwd)
    {

        toggleButton.setOnAction(e -> {
            if (!imgPwdVisible.isVisible() ) {
                imgPwdVisible.setVisible(true);
                imgPwdNotVisible.setVisible(false);
                textPassword.setText(showPwd);
                return;     }

            imgPwdVisible.setVisible(false);
            imgPwdNotVisible.setVisible(true);
            textPassword.setText(hidePwd);

        });

    }
    void passwordVisbility () {

    }


}
