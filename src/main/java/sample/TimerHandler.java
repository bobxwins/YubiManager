package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import javafx.util.Pair;
import org.bouncycastle.asn1.cms.Time;

import javax.xml.stream.EventFilter;
import javax.xml.stream.events.XMLEvent;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;

public class TimerHandler {
    public  Dialog<Pair<String, String>> dialog = new Dialog<>();
    public  GridPane grid = new GridPane();
    public  CheckBox checkBox;
    public  Spinner<Integer> timerSpinner;
    static  TimerSpecs timerSpecs;
    private static  boolean selectedCheckBox;
   public static PauseTransition TRANSITION  = new PauseTransition();
   static final EventHandler<InputEvent> filter = event -> TRANSITION.playFromStart();

    public static void timerCountDown(Button btnSignOut, AnchorPane anchorPane)  {

      if(!Files.exists(Paths.get(TimerSpecs.getTimerSpecsDir())))
        {
            return;    }

        timerSpecs = TimerSpecs.getTimerSpecs();

        if (!timerSpecs.getSelectedCheckBox())
        {
            anchorPane.removeEventFilter(InputEvent.ANY, filter);
            return;    }

         TRANSITION.setDuration(Duration.seconds(timerSpecs.getTimer()));
         anchorPane.addEventFilter(InputEvent.ANY, filter);
         TRANSITION.setOnFinished(evt -> {
            try {
                SceneHandler.stageFullScreen(btnSignOut);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Inactivity");
                alert.setHeaderText("Connection closed due to inactivity!");
                alert.show();
               TRANSITION.pause();
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    static void timerDialog(ObservableList observableList,Button btnSignOut, AnchorPane anchorPane) {
    TimerHandler timerHandler = new TimerHandler();
        timerHandler.timerGrid();
        timerHandler.dialog.setResultConverter(dialogButton -> {
            try {
                if (dialogButton == ButtonType.OK) {
                    if (!timerHandler.checkBox.isSelected()) {
                        selectedCheckBox = false;
                        // timerHandler.checkBox is a local object, which can't be accessed outside the scope of
                        //      timerDialog(), so the global boolean "selectedCheckBox" is set in timerDialog()
                        //     so it's state can be accessed in timerCountDown()
                        TimerSpecs updateTimerSpecs = new TimerSpecs(timerHandler.timerSpinner.getValue(),selectedCheckBox);
                        FileProtector fileProtector = new FileProtector();
                        fileProtector.encryption(observableList,updateTimerSpecs);
                        // the TimerSpecs gets encrypted then stored as a file
                        return null;
                    }
                    selectedCheckBox = true;
                    TimerSpecs updateTimerSpecs = new TimerSpecs(timerHandler.timerSpinner.getValue(),selectedCheckBox);
                    FileProtector fileProtector = new FileProtector();
                    fileProtector.encryption(observableList,updateTimerSpecs);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Dialog");
                    alert.setHeaderText(null);
                    alert.setContentText("Timer set! Database will automaticaly be locked after " + timerHandler.timerSpinner.getValue() + " seconds of inactivity!");
                    alert.showAndWait();
                    return null;
                }
            } catch (Exception E) {

            }

            return null;
        });


        timerHandler.dialog.showAndWait();
      //  timerHandler.timerCountDown(btnSignOut,anchorPane);
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
          TimerSpecs readTimerSpecs =  TimerSpecs.getTimerSpecs() ;
            checkBox.setSelected(readTimerSpecs.getSelectedCheckBox());
            timerSpinner.getValueFactory().setValue(readTimerSpecs.getTimer());
        }
        timerSpinner.setPrefSize(75, 25);
        timerSpinner.setEditable(true);
        grid.add(checkBox, 0, 1);
        grid.add(timerSpinner, 1, 1);
    }

}