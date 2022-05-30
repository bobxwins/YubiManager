package sample;

import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.InputEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.nio.file.Paths;

public class TimerHandler {
    static PauseTransition transition;
    static boolean selectedCheckBox;

    public static void timer(Button btnSignOut, AnchorPane anchorPane) throws Exception {
        if (FileUtils.readAllBytes(TimerSpecs.getTimerSpecsDir()).length == 0) {
            return;
        }

        TimerSpecs timerSpecs = (TimerSpecs) SerializedObject.readObject(TimerSpecs.getTimerSpecsDir());

        Duration delay = Duration.seconds(timerSpecs.getTimer());

        if (!timerSpecs.getSelectedCheckBox()) {
            return;
        }
        transition = new PauseTransition(delay);
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

    static void timerDialog(Button btnSignOut, AnchorPane anchorPane) {
        DatabaseHandler databaseHandler = new DatabaseHandler();

        databaseHandler.timerGrid();
        databaseHandler.dialog.setResultConverter(dialogButton -> {
            try {
                if (dialogButton == ButtonType.OK) {
                    if (!databaseHandler.checkBox.isSelected()) {
                        selectedCheckBox = false;
                        // JavaFX.CheckBox can't be serialized,so I have to serialize a boolean instead
                        TimerSpecs timerSpecs = new TimerSpecs(databaseHandler.timerSpinner.getValue(), selectedCheckBox);
                        SerializedObject.writeObject(timerSpecs, Paths.get(TimerSpecs.getTimerSpecsDir()));
                        return null;
                    }
                    selectedCheckBox = true;
                    TimerSpecs timerSpecs = new TimerSpecs(databaseHandler.timerSpinner.getValue(), selectedCheckBox);
                    SerializedObject.writeObject(timerSpecs, Paths.get(TimerSpecs.getTimerSpecsDir()));
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Dialog");
                    alert.setHeaderText(null);
                    alert.setContentText("Timer set! Database will automaticaly be locked after " + databaseHandler.timerSpinner.getValue() + " seconds of inactivity!");

                    alert.showAndWait();

                    TimerHandler.timer(btnSignOut, anchorPane);

                    return null;
                }
            } catch (Exception E) {

            }

            return null;
        });


        databaseHandler.dialog.showAndWait();

    }
   /* void timerGrid () {


        dialog.setTitle("Setting timer");
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 10, 0, 10));
        checkBox = new CheckBox("Seconds of inactivity database will be locked in: ");
        timerSpinner = (Spinner<Integer>) new Spinner(10, 999, 15);
        if (FileUtils.readAllBytes(TimerSpecs.getTimerSpecsDir()).length!=0)
        {
            TimerSpecs timerSpecs = (TimerSpecs) SerializedObject.readObject(TimerSpecs.getTimerSpecsDir());
            checkBox.setSelected(timerSpecs.getSelectedCheckBox());
            timerSpinner.getValueFactory().setValue(timerSpecs.getTimer());
        }

        timerSpinner.setPrefSize(75, 25);
        timerSpinner.setEditable(true);
        grid.add(checkBox, 0, 1);
        grid.add(timerSpinner, 1, 1);
    }
*/
}

