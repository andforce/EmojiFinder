package com.andforce.utils;

import java.io.*;

public class FileUtils {

    public static void writeToFile(String file, String str) {
        try {
            FileWriter writer = new FileWriter(file);

            writer.write(str);

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readString(String filePath) {
        File file = new File(filePath);

        BufferedReader bufferedReader = null;

        StringBuilder stringBuilder = new StringBuilder();
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\r\n");
            }
            stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return stringBuilder.toString();
    }
}
