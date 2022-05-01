package sample;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.lang.*;

public class Main extends Application   {

    @FXML
    public static void main(String[] args) {
        launch(args);

    }

     @Override
    public void start(Stage primaryStage) throws Exception {

     Parent root = FXMLLoader.load(Main.class.getResource("login/login.fxml"));
         Scene scene = new Scene (root);
         primaryStage.setMaximized(true);
      primaryStage.setScene(scene);
          primaryStage.show();

    }



}
