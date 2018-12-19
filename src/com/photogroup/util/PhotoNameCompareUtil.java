package com.photogroup.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhotoNameCompareUtil {

    private static final Pattern PATTERN_NUMBER = Pattern.compile("\\d+");

    private static final Pattern PATTERN_DATE = Pattern.compile("[\\d\\.]+");

    // private static Comparator<File> PHOTO_NAME_COMPARATOR;

    /**
     * @param s1
     * @param s2
     * @return
     */
    public static int compareByName(String n1, String n2) {

        Matcher m1 = PATTERN_NUMBER.matcher(n1);
        Matcher m2 = PATTERN_NUMBER.matcher(n2);
        if (m1.find() && m2.find()) {
            int m1Pos = n1.indexOf(m1.group());
            int m2Pos = n2.indexOf(m2.group());
            if (m1Pos == m2Pos && n1.substring(0, m1Pos).equals(n2.substring(0, m2Pos))) {
                try {
                    int num1 = Integer.parseInt(m1.group(0));
                    int num2 = Integer.parseInt(m2.group(0));
                    if (num1 != num2) {
                        return num1 - num2;
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }

        return n1.compareTo(n2);
    }

    /**
     * @param n1
     * @param n2
     * @return
     */
    public static boolean isSameNamingType(String n1, String n2) {

        Matcher m1 = PATTERN_NUMBER.matcher(n1);
        Matcher m2 = PATTERN_NUMBER.matcher(n2);
        if (m1.find() && m2.find()) {
            int m1Pos = n1.indexOf(m1.group());
            int m2Pos = n2.indexOf(m2.group());
            if (m1Pos == m2Pos && n1.substring(0, m1Pos).equals(n2.substring(0, m2Pos))) {
                return true;
            }
        }

        return false;
    }

    /**
     * Input: 2018.8.9中央工艺美术学院, output: 2018.8.9
     * 
     * @param str
     * @return
     */
    public static String findDateString(String str) {

        Matcher m1 = PATTERN_DATE.matcher(str);
        if (m1.find()) {

            return m1.group(0);
        }
        return str;
    }

}