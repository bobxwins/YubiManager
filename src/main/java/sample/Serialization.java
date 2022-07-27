package sample;

import javafx.collections.ObservableList;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;

public class Serialization {


    public static void recentFilesSerialize(ObservableList  observableList, Path file) throws  Exception {
        try {
           // ArrayList containing observableList gets serialized directly, without any encryption process

            FileOutputStream fos = new FileOutputStream(String.valueOf(file));
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(new ArrayList<>(observableList));
            // Writes ArrayList containing an ObservableList to a file
            oos.close();
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static Object readSerializedObj(byte [] inputBytes) {

        try
        {
            InputStream in = new ByteArrayInputStream(inputBytes);
            ObjectInputStream ois = new ObjectInputStream(in);
            Object object =  ois.readObject();
            ois.close();
            in.close();
            return object;
        }

        catch(Exception e)
        {
            e.printStackTrace();
        }

        return  new Database();
    }

    public static byte[] entrySerialize(Object  object) throws  Exception {
        try {
            // Object that gets serialized before it's encrypted

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