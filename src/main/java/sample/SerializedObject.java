package sample;

    import javafx.collections.FXCollections;
    import javafx.collections.ObservableList;

    import java.io.*;
    import java.nio.file.Path;
    import java.util.ArrayList;
    import java.util.List;

public class SerializedObject {

    public static byte[] readMemoryObservableList(ObservableList  observableList) throws  Exception {
        try {

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(new ArrayList<>(observableList));
            byte[] listBytes = bos.toByteArray();
            oos.close();
            bos.close();
            return listBytes;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void writeObservableList(ObservableList  observableList, Path file) throws  Exception {
        try {

            FileOutputStream fos = new FileOutputStream(String.valueOf(file));
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(new ArrayList<>(observableList));
            oos.close();
            fos.close();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static ObservableList readFileObservableList(byte [] inputBytes) {
        try {

            InputStream in = new ByteArrayInputStream(inputBytes);

            ObjectInputStream ois = new ObjectInputStream(in);
            List  list = (List ) ois.readObject() ;
            in.close();
            ois.close();
            return FXCollections.observableList(list);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return FXCollections.emptyObservableList();
    }

    public static void writeObject(Object  object, Path file) throws  Exception {
        try {
            FileOutputStream fos = new FileOutputStream(String.valueOf(file));
            // writes an object given as parameter to a path given as parameter
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(object);
            fos.close();
            oos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static NonSecrets readObject(byte [] inputBytes) {

        try
        {
            InputStream in = new ByteArrayInputStream(inputBytes);
            ObjectInputStream ois = new ObjectInputStream(in);

            NonSecrets nonSecrets = (NonSecrets) ois.readObject();

            ois.close();
            in.close();

            return nonSecrets;
        }

        catch(Exception e)
        {
            e.printStackTrace();
        }

         return  new NonSecrets();
    }

    public static TimerSpecs readTimerSpecs(byte [] inputBytes) {

        try
        {
            InputStream in = new ByteArrayInputStream(inputBytes);
            ObjectInputStream ois = new ObjectInputStream(in);

            TimerSpecs  timerSpecs = (TimerSpecs) ois.readObject();

            ois.close();
            in.close();

            return timerSpecs;
        }

        catch(Exception e)
        {
            e.printStackTrace();
        }

        return  new TimerSpecs();
    }

    public static byte[] serializeObject(Object  object) throws  Exception {
        try {

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(object);
            byte[] listBytes = bos.toByteArray();
            oos.close();
            bos.close();
            return listBytes;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
