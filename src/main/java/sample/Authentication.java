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


 public class Authentication  {

     LoginGUI loginGui = new LoginGUI();
     PasswordField manualPwdField = new PasswordField();
     PasswordField responseField = new PasswordField();

     public ObservableList<PasswordRecord> authenticated() throws Exception {
         // Restores the database, by decrypting it and storing it in memory,
         // only if the database exists and if the user is authenticated
         if (FileService.dbExists()) {
             DecryptFile decryptFile = new DecryptFile();
             byte[] input = FileUtils.readAllBytes(FilePath.getCurrentDBdir());
             Secrets decryptedSecrets = (Secrets) Serialization.readSerializedObj(decryptFile.Decryption(input));
             TimerSpecs storedTimerSpecs = decryptedSecrets.getTimerSpecs();
             TimerSpecs.setTimerSpecs(storedTimerSpecs);
             ObservableList<PasswordRecord> observableList = FXCollections.observableList(decryptedSecrets.getPwdRecord());
             return observableList;
         }
         return FXCollections.emptyObservableList();
     }

    public static String computeHMac(String cipherText, SecretKey key ) throws Exception {
        byte [] cipherBytes = cipherText.getBytes(StandardCharsets.UTF_8);
        Mac mac = Mac.getInstance("HMACSHA1", "BC");
        mac.init(key);
        mac.update(cipherBytes);
        byte[] digest = mac.doFinal(cipherBytes);
        String encoded = Base64.getEncoder().encodeToString(digest);
        return  encoded;
    }

  public static boolean verifyHMac(String generatedMacTag) {
      byte [] input = FileUtils.readAllBytes(FilePath.getCurrentDBdir());
      Database dbSecrets = (Database) Serialization.readSerializedObj(input);
      NonSecrets nonSecrets= dbSecrets.getNonSecrets();
      String storedMacTag = nonSecrets.getMacTag();
      if (storedMacTag.equals(generatedMacTag))
      {
          return true;
      }
        return false;
  }


  public static boolean validateCredentials(String manualPwd, String confirmPwd)
     {// Verifies if the criteria for the manual password are met, when creating a new database or updating the master password
         if (manualPwd.length() < 6) {
             Alert alert = new Alert(Alert.AlertType.ERROR);
             alert.setTitle("Information Dialog");
             alert.setHeaderText(null);
             alert.setContentText("The manual password must be at least 6 characters long!");
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

         String regex = "^(?=.*?\\p{Lu})(?=.*?\\p{Ll})(?=.*?\\d)" +
                 "(?=.*?[`~!@#$%^&*()\\-_=+\\\\|\\[{\\]};:'\",<.>/?]).*$";
         Pattern.compile(regex).matcher(manualPwd).matches();
         if ( !Pattern.compile(regex).matcher(manualPwd).matches() ) {
             Alert alert = new Alert(Alert.AlertType.ERROR);
             alert.setTitle("Information Dialog");
             alert.setHeaderText(null);
             alert.setContentText("The manual password must contain digits, uppercase, and lowercase characters!");
             alert.showAndWait();
             return false;
         }

         return true;
     }

     boolean loginAuthentication(Button btnSignIn) throws Exception {
         responseField.setText(new String(DecryptFile.recreateResponse()));
         loginGui.dialog(manualPwdField, responseField);
         Platform.runLater(() -> manualPwdField.requestFocus());
         final  Button btnOk = (Button) loginGui.loginDialog.getDialogPane().lookupButton(ButtonType.OK);
         btnOk.addEventFilter(
                 ActionEvent.ACTION,
                 event -> {
                     // Checks if conditions are fulfilled
                     try {
                         Secrets.setMasterPassword(manualPwdField.getText().toCharArray(), responseField.getText().toCharArray());
                         byte[] input = FileUtils.readAllBytes(FilePath.getCurrentDBdir());
                         Database dbSecrets = (Database) Serialization.readSerializedObj(input);
                         NonSecrets nonSecrets= dbSecrets.getNonSecrets();
                         KeyService.restoreKey();
                         String generatedMacTag = computeHMac(nonSecrets.getCipherText(), KeyService.getKey());
                         nonSecrets.setMacTag(generatedMacTag);
                         if (!verifyHMac(generatedMacTag))
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
         loginGui.loginDialog.setResultConverter(loginButton -> {
             try {
                 if (loginButton == ButtonType.OK) {
                     Parent root = FXMLLoader.load(Main.class.getResource("Entry-Management/Entry-Management.fxml"));
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
