package sample;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;


public class GetFilesFromAndroid {

    public void readFile()  {

        try {
    String androidFilePath = "/storage/emulated/0/Android/data/com.example.biom/files/myfile.txt";


    String windowsFilePath = System.getProperty("user.dir");
            System.out.println(windowsFilePath);
    List<String> cmd = new LinkedList<>();

            cmd.add("C:\\Users\\bob-w\\AppData\\Local\\Android\\Sdk\\platform-tools\\adb.exe");
            cmd.add("pull");
            cmd.add(androidFilePath);
            cmd.add(windowsFilePath);

    ProcessBuilder pb = new ProcessBuilder(cmd);
            pb.redirectErrorStream(true);

            pb.redirectErrorStream(true); // can use these 2 line if you want to see output or errors in file.
            pb.redirectOutput(new File(windowsFilePath+"/logs.txt"));

    Process p = pb.start();

            p.waitFor();
            Thread.sleep(1000);

    BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));


            int exitval= p.waitFor();
            System.out.println(exitval);

}
        catch (Exception e) {
                e.printStackTrace();
                }
                }
}
