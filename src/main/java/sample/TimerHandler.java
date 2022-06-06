package sample;

import javafx.animation.PauseTransition;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.InputEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import javafx.util.Pair;

import java.nio.file.Files;
import java.nio.file.Paths;

public class TimerHandler {
    public  Dialog<Pair<String, String>> dialog = new Dialog<>();
    public  GridPane grid = new GridPane();
    public  CheckBox checkBox;
    public  Spinner<Integer> timerSpinner;
    static  TimerSpecs timerSpecs; //= TimerSpecs.getTimerSpecs();
    static  Duration delay ;// = Duration.seconds(timerSpecs.getTimer());
    static  PauseTransition transition  ;//= new PauseTransition(delay);
    static  boolean selectedCheckBox;
    public  static void timerSignOut(Button btnSignOut, AnchorPane anchorPane) throws Exception {

        timerSpecs = TimerSpecs.getTimerSpecs();

         delay = Duration.seconds(timerSpecs.getTimer());

        if (!timerSpecs.getSelectedCheckBox()) {
            return;
        }
        transition  = new PauseTransition(delay);
        System.out.println("the transition delay is "+transition.getDelay());
        System.out.println("the transition duration is:"+ transition.getDuration());

        transition.setOnFinished(evt -> {
            try {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Inactivity");
                alert.setHeaderText("Connection closed due to inactivity!");
                alert.show();
                DatabaseHandler.stageFullScreen(btnSignOut);
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        anchorPane.addEventFilter(InputEvent.ANY, evt -> transition.playFromStart());
        transition.play();

    }

    static void timerDialog(Button btnSignOut, AnchorPane anchorPane, ObservableList observableList) {
        TimerHandler timerHandler = new TimerHandler();
        timerHandler.timerGrid();
        timerHandler.dialog.setResultConverter(dialogButton -> {
            try {
                if (dialogButton == ButtonType.OK) {
                    if (!timerHandler.checkBox.isSelected()) {
                        selectedCheckBox = false;
                        // JavaFX.CheckBox can't be serialized,so I have to serialize a boolean instead
                        TimerSpecs timerSpecs = new TimerSpecs(timerHandler.timerSpinner.getValue(), selectedCheckBox);
                        FileProtector fileProtector = new FileProtector();
                        fileProtector.encryption(observableList,timerSpecs);
                        transition.playFromStart();
                        transition.pause();
                        transition.stop();
                        System.out.println(5);
                        return null;
                    }

                    selectedCheckBox = true;
                    TimerSpecs timerSpecs = new TimerSpecs(timerHandler.timerSpinner.getValue(), selectedCheckBox);
                    FileProtector fileProtector = new FileProtector();
                    fileProtector.encryption(observableList,timerSpecs);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Dialog");
                    alert.setHeaderText(null);
                    alert.setContentText("Timer set! Database will automaticaly be locked after " + timerHandler.timerSpinner.getValue() + " seconds of inactivity!");

                    alert.showAndWait();

                    TimerHandler.timerSignOut(btnSignOut, anchorPane);
                    System.out.println(12);
                    return null;
                }
            } catch (Exception E) {

            }

            return null;
        });


        timerHandler.dialog.showAndWait();

    }
    void timerGrid () {

        dialog.setTitle("Setting timer");
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 8, 0, 10));
        checkBox = new CheckBox("Seconds of inactivity database will be locked in: ");
        timerSpinner = (Spinner<Integer>) new Spinner(8, 999, 15);

        if(Files.exists(Paths.get(TimerSpecs.getTimerSpecsDir()))) {
            byte[] input = FileUtils.readAllBytes(TimerSpecs.getTimerSpecsDir());
            DecryptFile decryptFile = new DecryptFile();
            TimerSpecs timerSpecs = (TimerSpecs) SerializedObject.readObject(decryptFile.Decryption(input));
            checkBox.setSelected(timerSpecs.getSelectedCheckBox());
            timerSpinner.getValueFactory().setValue(timerSpecs.getTimer());
        }

        timerSpinner.setPrefSize(75, 25);
        timerSpinner.setEditable(true);
        grid.add(checkBox, 0, 1);
        grid.add(timerSpinner, 1, 1);
    }

}
