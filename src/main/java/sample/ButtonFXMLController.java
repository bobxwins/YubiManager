
package sample;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;

public class ButtonFXMLController {

    @FXML

    private int   button1ClickCount = 0;
    int   y =  (int) Screen.getPrimary().getBounds().getHeight()/2;
    @FXML

    public void Create(Event e, AnchorPane anchorPane){

        Button button  = new Button(" New Entry "+button1ClickCount);

        // button.setLayoutX(00);
         button.setLayoutY(y+=20);

        anchorPane.getChildren().add(0,button );

        button1ClickCount++;

        String text = "Button1 clicked " +button1ClickCount + " times";

        System.out.println(text);
    }

}
