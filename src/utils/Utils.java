package utils;

import java.util.List;

public class Utils {

    public Utils() {

    }

    //Get File extension
    public String getFileExtFromStr(String str) {
        try {
            return str.substring(str.lastIndexOf(".") + 1);
        } catch (Exception ex) {
            System.err.println("[Utils Error] Failed to get file extension from string, error - " + ex.getMessage());
        }
        return null;
    }

    public boolean Str1IsStr2(String str1, String str2) {

        try {

            str1 = str1.toLowerCase().replaceAll("\\s+", "");
            str2 = str2.toLowerCase().replaceAll("\\s+", "");

            return str1.equals(str2);

        } catch (Exception ex) {
            System.err.println("[Utils Error] Failed to compare string 1 with string 2, error - " + ex.getMessage());
        }

        return false;
    }

    public boolean emptyString(String str) {
        try {
            return str == null || str.equals("") || str.isEmpty();

        } catch (Exception ex) {
            System.err.println("[Utils Error] Failed to determine empty string, error - " + ex.getMessage());
        }
        return false;
    }

    public void viewStringListContents(List<String> list) {

        try {
            for (Object objLst : list) {
                System.out.println(objLst.toString());
            }
        } catch (Exception ex) {
            System.err.println("[Utils Error] Failed to view string list content, error - " + ex.getMessage());
        }
    }
}
