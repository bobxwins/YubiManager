package sample;

    import javafx.collections.FXCollections;
    import javafx.collections.ObservableList;

    import java.io.*;
    import java.nio.file.Files;
    import java.nio.file.Path;
    import java.util.ArrayList;
    import java.util.List;

public class SerializedObject {

    public static void writeObject(ObservableList  object, Path file) throws  Exception {
        try {
            FileOutputStream fos = new FileOutputStream(String.valueOf(file));
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(new ArrayList<>(object));
            oos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static ObservableList readObject(byte [] inputBytes) {
        try {

            InputStream in = new ByteArrayInputStream(inputBytes);

            ObjectInputStream ois = new ObjectInputStream(in);
            List  list = (List ) ois.readObject() ;

            return FXCollections.observableList(list);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return FXCollections.emptyObservableList();
    }

}
