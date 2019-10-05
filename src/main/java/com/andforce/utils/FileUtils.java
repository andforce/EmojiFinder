package com.andforce.utils;

import java.io.*;

public class FileUtils {

    public static void writeToFile(String file, String str) {

        try {
            File file1 = new File(file);
            if (!file1.exists()){
                file1.createNewFile();
            }

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
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append('\n');
            }
//            stringBuilder.lastIndexOf("\n");
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

    public static File writeToFile(InputStream inputString, String filePath) {
        File file = new File(filePath);
        return write(inputString, file);
    }

    public static File writeToFile(InputStream inputString, File file) {
        return write(inputString, file);
    }

    private static File write(InputStream inputString, File file) {
        if (file.exists()) {
            file.delete();
        }

        FileOutputStream fos;
        try {
            fos = new FileOutputStream(file);

            byte[] b = new byte[1024];

            int len;
            while ((len = inputString.read(b)) != -1) {
                fos.write(b, 0, len);
            }
            inputString.close();
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }
}
