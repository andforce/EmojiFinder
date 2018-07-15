package com.andforce;

public class UnicodeUtils {

    public static boolean isNeighbor(String unicode1, String unicode2){
        if (unicode1 == null || unicode2 == null || unicode1.length() != unicode2.length()){
            return false;
        }

        unicode1 = unicode1.toUpperCase();
        unicode2 = unicode2.toUpperCase();

        int neighborCount = 0;

        for (int i = 0; i < unicode1.length(); i++) {

            char c1 = unicode1.charAt(i);
            char c2 = unicode2.charAt(i);

            if (Math.abs( c1 - c2) == 1 && (i == 0 || i == unicode1.length() -1)){
                neighborCount ++;
            }
        }

        return neighborCount == 1;
    }

    private static char convert(char c){
        if (c == 'A' || c == 'B' ||c == 'C' ||c == 'D' ||c == 'E' ||c == 'F'){
            return (char) (c - 7);
        } else {
            return c;
        }
    }

    public static String convertUnicode(String str) {
        str = (str == null ? "" : str);
        String tmp;
        StringBuilder sb = new StringBuilder(1000);
        char c;
        int i, j;
        sb.setLength(0);
        for (i = 0; i < str.length(); i++)
        {
            c = str.charAt(i);
            sb.append("\\u");
            j = (c >>>8); //取出高8位
            tmp = Integer.toHexString(j).toUpperCase();
            if (tmp.length() == 1)
                sb.append("0");
            sb.append(tmp);
            j = (c & 0xFF); //取出低8位
            tmp = Integer.toHexString(j).toUpperCase();
            if (tmp.length() == 1)
                sb.append("0");
            sb.append(tmp);

        }
        return sb.toString();
    }

    public static String convertUnicode(char str) {
        StringBuilder sb = new StringBuilder();
        sb.append("\\u");
        int pos = (str >>>8); //取出高8位
        String tmp = Integer.toHexString(pos).toUpperCase();
        if (tmp.length() == 1) {
            sb.append("0");
        }
        sb.append(tmp);
        pos = (str & 0xFF); //取出低8位
        tmp = Integer.toHexString(pos).toUpperCase();
        if (tmp.length() == 1) {
            sb.append("0");
        }
        sb.append(tmp);

        return sb.toString();
    }
}
