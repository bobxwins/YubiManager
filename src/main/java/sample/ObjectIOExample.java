package sample;

    import javafx.collections.FXCollections;
    import javafx.collections.ObservableList;

    import java.io.*;
    import java.nio.charset.StandardCharsets;
    import java.nio.file.Files;
    import java.nio.file.Path;
    import java.nio.file.Paths;
    import java.util.ArrayList;
    import java.util.List;

public class ObjectIOExample {


    public static void write(ObservableList<Entry> entry, Path file) {
        try {

            // write object to file
            OutputStream fos = Files.newOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(new ArrayList<Entry>(entry));
            oos.close();
            EncryptFile encryptFile = new EncryptFile();
            encryptFile.encryption();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static ObservableList<Entry> read(Path file) {
        try {
            DecryptFile decryptFile = new DecryptFile();

            InputStream in = new ByteArrayInputStream(decryptFile.Decryption());
          
            System.out.println("the decrypted data is:"+in);
            ObjectInputStream ois = new ObjectInputStream(in);
            List<Entry> list = (List<Entry>) ois.readObject() ;
            System.out.println(list);
            return FXCollections.observableList(list);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return FXCollections.emptyObservableList();
    }
}
