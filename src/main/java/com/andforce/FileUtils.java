package com.andforce;

import java.io.FileWriter;
import java.io.IOException;

public class FileUtils {

    public static void writeToFile(String file, String str){
        try {
            FileWriter writer = new FileWriter(file);

            writer.write(str);

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
