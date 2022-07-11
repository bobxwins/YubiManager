 package sample;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.regex.Pattern;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


 public class Authentication  {

     LoginGUI loginGui = new LoginGUI();
     PasswordField mpField = new PasswordField();
     PasswordField skField = new PasswordField();

     public ObservableList<Entry> authenticated() throws Exception {
         // Restores the database, by decrypting it and storing it in memory,
         // only if the database exists and if the user is authenticated
         if (FileHandler.dbExists()) {
             DecryptFile decryptFile = new DecryptFile();
             byte[] input = FileUtils.readAllBytes(Global.getPasswordFilePath());
             Secrets decryptedSecrets = SerializedObject.readSecrets(decryptFile.Decryption(input));
             TimerSpecs storedTimerSpecs = decryptedSecrets.getTimerSpecs();
             TimerSpecs.setTimerSpecs(storedTimerSpecs);
             ObservableList<Entry> observableList = FXCollections.observableList(decryptedSecrets.getEntry());
             return observableList;
         }
         return FXCollections.emptyObservableList();
     }

    public static String generateHmac(String data, SecretKey key) throws Exception {

        String encodedKey = Base64.getEncoder().encodeToString(key.getEncoded());
        SecretKeySpec secretKeySpec = new SecretKeySpec(encodedKey.getBytes()," HMACSHA512");
        Mac mac = Mac.getInstance("HMACSHA512");
        mac.init(secretKeySpec);
        byte[] digest = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        String encoded = Base64.getEncoder().encodeToString(digest);
        return  encoded;
    }

  public static boolean verifyHmac( String generatedHeader) {
      byte [] input = FileUtils.readAllBytes(Global.getPasswordFilePath());
      Database dbSecrets = (Database) SerializedObject.readDB(input);
      NonSecrets nonSecrets= dbSecrets.getNonSecrets();
      String storedHeader = nonSecrets.getHeader();
      if (storedHeader.equals(generatedHeader))
      {
          return true;
      }

        return false;
  }


  public static boolean validatePwdCredentials(String manualPwd, String confirmPwd, String sKeyPwd)
     {
         // checks if the master password has valid criteria, when creating a new database or updating the master password

         String combined =manualPwd+ sKeyPwd;
         String regex = "^(?=.*?\\p{Lu})(?=.*?\\p{Ll})(?=.*?\\d)" +
                 "(?=.*?[`~!@#$%^&*()\\-_=+\\\\|\\[{\\]};:'\",<.>/?]).*$";

         Pattern.compile(regex).matcher(combined).matches();

         if (combined.length() < 12) {
             Alert alert = new Alert(Alert.AlertType.ERROR);
             alert.setTitle("Information Dialog");
             alert.setHeaderText(null);
             alert.setContentText("The Master password must be at least 12 characters long!");
             alert.showAndWait();
             return false;
         }

         if (!manualPwd.equals(confirmPwd)) {
             Alert alert = new Alert(Alert.AlertType.ERROR);
             alert.setTitle("Information Dialog");
             alert.setHeaderText(null);
             alert.setContentText("The manually entered passwords do not match!");
             alert.showAndWait();
             return false;
         }

         if ( !Pattern.compile(regex).matcher(combined).matches() ) {
             Alert alert = new Alert(Alert.AlertType.ERROR);
             alert.setTitle("Information Dialog");
             alert.setHeaderText(null);
             alert.setContentText("Master password must contain digits, uppercase, and lowercase characters!");
             alert.showAndWait();
             return false;
         }

         return true;
     }


     boolean loginAuthentication(Button btnSignIn) throws Exception {
         loginGui.dialog(mpField, skField);
         Platform.runLater(() -> mpField.requestFocus());
         final  Button btnOk = (Button) loginGui.loginDialog.getDialogPane().lookupButton(ButtonType.OK);
         btnOk.addEventFilter(
                 ActionEvent.ACTION,
                 event -> {
                     // Checks if conditions are fulfilled

                     try {
                         Secrets.setMasterPassword(mpField, skField);
                         byte[] input = FileUtils.readAllBytes(Global.getPasswordFilePath());

                         Database dbSecrets = (Database) SerializedObject.readDB(input);
                         NonSecrets nonSecrets= dbSecrets.getNonSecrets();
                         DecryptFile.restoreKey();
                         String generatedHeader = generateHmac(Global.getPasswordFilePath(),SymmetricKey.getSecretKey());
                         nonSecrets.setHeader(generatedHeader);

                         if (!verifyHmac(generatedHeader))
                         {
                             Alert alert = new Alert(Alert.AlertType.ERROR);
                             alert.setTitle("Information Dialog");
                             alert.setHeaderText(null);
                             alert.setContentText("Login failed! Wrong Password!");
                             alert.showAndWait();
                             // If the conditions are not fulfilled, the event is consumed
                             // to prevent the dialog from closing
                             event.consume();
                             return;
                         }

                     } catch (Exception e) {
                         e.printStackTrace();
                     }

                 }   );


         loginGui.loginDialog.setResultConverter(dialogButton -> {
             try {
                 if (dialogButton == ButtonType.OK) {

                     Parent root = FXMLLoader.load(Main.class.getResource("authenticated/authenticated.fxml"));

                     Stage stage = (Stage) btnSignIn.getScene().getWindow();
                     stage.setScene(new Scene(root));


                 }
             } catch (Exception E) {

             }

             return null;
         });

         loginGui.loginDialog.showAndWait();

         return false;
     }

 }
