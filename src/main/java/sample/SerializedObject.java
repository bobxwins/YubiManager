package sample;

    import javafx.collections.FXCollections;
    import javafx.collections.ObservableList;
    import org.bouncycastle.util.encoders.Hex;

    import java.io.*;
    import java.nio.file.Files;
    import java.nio.file.Path;
    import java.util.ArrayList;
    import java.util.List;

public class SerializedObject {


    public static void writeEntries(ObservableList<Entry> entry, Path file) {
        try {

            // write object to file
            OutputStream fos = Files.newOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(new ArrayList<>(entry));
            oos.close();
            EncryptFile encryptFile = new EncryptFile();
            encryptFile.encryption();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static ObservableList<Entry> readEntries() {
        try {
            DecryptFile decryptFile = new DecryptFile();

            InputStream in = new ByteArrayInputStream(decryptFile.Decryption());

            ObjectInputStream ois = new ObjectInputStream(in);
            List<Entry> list = (List<Entry>) ois.readObject() ;

            return FXCollections.observableList(list);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return FXCollections.emptyObservableList();
    }

    public static void writeRecentFiles(ObservableList<String> recent, Path file) {
        try {

            OutputStream fos = Files.newOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(new ArrayList<>(recent));
            oos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static ObservableList<String> readRecentFiles() throws  Exception {
        try {
            InputStream in = new ByteArrayInputStream(FileUtils.readAllBytes(Global.getRecentFilesDir()));

            ObjectInputStream ois = new ObjectInputStream(in);
            List<String> list = (List<String>) ois.readObject() ;

            return FXCollections.observableList(list);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return FXCollections.emptyObservableList();
    }
}
