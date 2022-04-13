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
    static String passwordFileName = System.getProperty("user.dir")+"/java/sample/passwords.txt";
    static String dir = System.getProperty("user.dir")+"/java/sample/";
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
        anchorPane.getChildren().addAll(imgView,entryLabel);
        anchorPane.getChildren().add(newEntryButton);

    }


    int loadEntries  (ObservableList<Entry> entry, List<Entry> loadEntries) throws Exception {

        loadEntries = readEntryFile();
      entry.removeAll(entry);
        entry.addAll( loadEntries);

        return entry.size();
    }

    public static void writeEntrytoFile(String filename, TableView tableView)
            throws IOException {
        FileWriter writer = new FileWriter(filename);
        Entry entry;
        for (int i = 0; i < tableView.getItems().size(); i++) {
            entry = (Entry) tableView.getItems().get(i);

            if (entry.getNotes().getText().length() == 0) {
                 writer.write(entry.getTitel().getText() + "," + entry.getUsername().getText() + "," +
                        entry.getUrl().getText() + ","
                        + entry.getPassword().getText() + ", " + entry.getNotes().getText() + "\n");
            }
             else {
                writer.write(
                        entry.getTitel().getText() + "," + entry.getUsername().getText() + "," +
                                entry.getUrl().getText() + ","
                                + entry.getPassword().getText() + "," + entry.getNotes().getText() + "\n");
                  }

        }
        writer.close();
        EncryptFile encryptFile = new EncryptFile();
         encryptFile.encryption(dir,passwordFileName);

    }

    private  List<Entry> readEntryFile()
            throws IOException {
        DecryptFile decryptFile = new DecryptFile();

        List<Entry> entry = new ArrayList<>();
      Reader decryptedReader = new StringReader(decryptFile.Decryption(dir,passwordFileName));
      BufferedReader reader = new BufferedReader(decryptedReader);
      String line;
        while ((line = reader.readLine()) != null) {
            String[] entries = line.split(",");
           Entry entryObj = (new Entry(entries[0], entries[1],entries[2],entries[3],entries[4]));

           entry.add (entryObj);
        }

     return entry;

    }

}
