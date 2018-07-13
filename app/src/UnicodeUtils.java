public class UnicodeUtils {

    public static boolean isNeighbor(String unicode1, String unicode2){
        if (unicode1 == null || unicode2 == null || unicode1.length() != unicode2.length()){
            return false;
        }

        unicode1 = unicode1.toUpperCase();
        unicode2 = unicode2.toUpperCase();

        if (!unicode1.substring(0, unicode1.length() - 1).equals(unicode2.substring(0, unicode2.length() - 1))){
            return false;
        }

        char char0 = convert(unicode1.charAt(unicode1.length() - 1));


        char char1 = convert(unicode2.charAt(unicode2.length() -1 ));
        return Math.abs( char0 - char1) == 1;
    }

    private static char convert(char c){
        if (c == 'A' || c == 'B' ||c == 'C' ||c == 'D' ||c == 'E' ||c == 'F'){
            return (char) (c - 7);
        } else {
            return c;
        }
    }
}
