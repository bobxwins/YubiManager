package sample;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;

public class EntryHandler implements Serializable  {


    public static int   Y =  (int) (Screen.getPrimary().getBounds().getHeight()/2)-150;

    String imgPath = EntryHandler.class.getResource("PMAuth/folder.png").toString();
    public void createEntryObject  (AnchorPane anchorPane) throws Exception {

        Button newEntryButton  = new Button(" New Entry ");
        Image image = new Image(String.valueOf(new File(imgPath)));
        ImageView imgView=new ImageView(image);
        newEntryButton.setLayoutY(Y+=30);
        newEntryButton.setLayoutX(37);
        newEntryButton.setStyle("-fx-background-color: transparent;");
        newEntryButton.setGraphic(imgView);
        newEntryButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        newEntryButton.setOnAction(e -> {
            try{

            } catch (Exception E) {

            }
        });
        imgView.setLayoutX(37);
        imgView.setLayoutY(Y+=30);
        imgView.setFitHeight(48.9);
        imgView.setFitWidth(48.0);
        imgView.setPickOnBounds(true);
        Label entryLabel = new Label("EntryName");
        entryLabel.setLayoutX(105);
        entryLabel.setLayoutY(Y+=5);
        TableView tableView = new TableView();

        anchorPane.getChildren().addAll(imgView,entryLabel);
        anchorPane.getChildren().add(newEntryButton);

    }


}