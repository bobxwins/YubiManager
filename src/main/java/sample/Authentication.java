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
     PasswordField userCredentialField = new PasswordField();
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

     public static String computeAuthMacTag(String cipherText, SecretKey key ) throws Exception {
         byte [] cipherBytes = cipherText.getBytes(StandardCharsets.UTF_8);
         Mac mac = Mac.getInstance("HMACSHA256", "BC");
         mac.init(key);
         mac.update(cipherBytes);
         byte[] digest = mac.doFinal(cipherBytes);
         String encodedDigest = Base64.getEncoder().encodeToString(digest);
         return  encodedDigest;
     }

  public static boolean verifyAuthMacTag(String generatedAuthMacTag) {
      byte [] input = FileUtils.readAllBytes(FilePath.getCurrentDBdir());
      Database dbSecrets = (Database) Serialization.readSerializedObj(input);
      NonSecrets nonSecrets= dbSecrets.getNonSecrets();
      String storedMacTag = nonSecrets.getMacTag();
      if (storedMacTag.equals(generatedAuthMacTag))
      {
          return true;
      }
        return false;
  }


  public static boolean validateUserCredentials(String userCred, String confirmUserCred)
     {// Verifies if the criteria for the user credential are met, when creating a new database or updating the master password
         if (userCred.length() < 6) {
             Alert alert = new Alert(Alert.AlertType.ERROR);
             alert.setTitle("Information Dialog");
             alert.setHeaderText(null);
             alert.setContentText("The user credential must be at least 6 characters long!");
             alert.showAndWait();
             return false;
         }

         if (!userCred.equals(confirmUserCred)) {
             Alert alert = new Alert(Alert.AlertType.ERROR);
             alert.setTitle("Information Dialog");
             alert.setHeaderText(null);
             alert.setContentText("The manually entered passwords do not match!");
             alert.showAndWait();
             return false;
         }

         String regex = "^(?=.*?\\p{Lu})(?=.*?\\p{Ll})(?=.*?\\d)" +
                 "(?=.*?[`~!@#$%^&*()\\-_=+\\\\|\\[{\\]};:'\",<.>/?]).*$";
         Pattern.compile(regex).matcher(userCred).matches();
         if ( !Pattern.compile(regex).matcher(userCred).matches() ) {
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
         userCredentialField.setText(new String(DecryptFile.recreateResponse()));
         loginGui.dialog(userCredentialField);
         Platform.runLater(() -> userCredentialField.requestFocus());
         final  Button btnOk = (Button) loginGui.loginDialog.getDialogPane().lookupButton(ButtonType.OK);
         btnOk.addEventFilter(
                 ActionEvent.ACTION,
                 event -> {
                     // Checks if conditions are fulfilled
                     try {
                         Secrets.setMasterPassword(userCredentialField.getText().toCharArray(), responseField.getText().toCharArray());
                         byte[] input = FileUtils.readAllBytes(FilePath.getCurrentDBdir());
                         Database dbSecrets = (Database) Serialization.readSerializedObj(input);
                         NonSecrets nonSecrets= dbSecrets.getNonSecrets();
                         AESKey.restoreAESKey();
                         String generatedMacTag = computeAuthMacTag(nonSecrets.getCipherText(), AESKey.getAESKey());
                         nonSecrets.setMacTag(generatedMacTag);
                         if (!verifyAuthMacTag(generatedMacTag))
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
                     Parent root = FXMLLoader.load(Main.class.getResource("Main-Operations/MainOperations.fxml"));
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
