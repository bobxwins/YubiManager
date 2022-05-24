package sample;

    import javafx.collections.FXCollections;
    import javafx.collections.ObservableList;

    import java.io.*;
    import java.nio.file.Path;
    import java.util.ArrayList;
    import java.util.List;

public class SerializedObject {

    public static void writeObservableList(ObservableList  observableList, Path file) throws  Exception {
        try {

            FileOutputStream fos = new FileOutputStream(String.valueOf(file));
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(new ArrayList<>(observableList));
            oos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static ObservableList readObservableList(byte [] inputBytes) {
        try {

            InputStream in = new ByteArrayInputStream(inputBytes);

            ObjectInputStream ois = new ObjectInputStream(in);
            List  list = (List ) ois.readObject() ;

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
            oos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static Object readObject(String filename) {

        try
        {

            FileInputStream file = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(file);

            Object  object = in.readObject();

            in.close();
            file.close();

            return object;
        }

        catch(IOException ex)
        {
            System.out.println("IOException is caught");
        }

        catch(ClassNotFoundException ex)
        {
            System.out.println("ClassNotFoundException is caught");
        }
         return  new Object();
    }

}
