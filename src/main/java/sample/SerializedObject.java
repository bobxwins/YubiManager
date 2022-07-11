package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class SerializedObject   {
   // private static final long serialVersionUID = 6529685098267757690L;

    public static void writeArrayList(ObservableList  observableList, Path file) throws  Exception {
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


    public static Secrets readSecrets(byte [] inputBytes) {

        try
        {
            InputStream in = new ByteArrayInputStream(inputBytes);
            ObjectInputStream ois = new ObjectInputStream(in);

            Secrets Secrets = (Secrets) ois.readObject();

            ois.close();
            in.close();

            return Secrets;
        }

        catch(Exception e)
        {
            e.printStackTrace();
        }

        return  new Secrets();
    }

    public static Object readDB(byte [] inputBytes) {

        try
        {
            InputStream in = new ByteArrayInputStream(inputBytes);
            ObjectInputStream ois = new ObjectInputStream(in);

            Database Database = (Database) ois.readObject();

            ois.close();
            in.close();

            return Database;
        }

        catch(Exception e)
        {
            e.printStackTrace();
        }

        return  new Database();
    }

    public static byte[] serializeDB(Object  object) throws  Exception {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(object);
            byte[] listBytes = bos.toByteArray();
            return listBytes;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}