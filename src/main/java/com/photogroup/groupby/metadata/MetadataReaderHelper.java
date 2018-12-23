package com.photogroup.groupby.metadata;

import com.drew.metadata.Directory;

public class MetadataReaderHelper {

    private static final String EMPTY = "";

    public static String getDescription(Directory directory, int tagType) {
        return directory.getDescription(tagType) == null ? EMPTY : directory.getDescription(tagType).trim();
    }

    public static Double convertGpsToDegree(String stringDMS, String stringRef) {
        double result;
        String[] split2 = stringDMS.split(" ");
        String degrees = split2[0].trim().substring(0, split2[0].trim().length() - 1);
        String minutes = split2[1].trim().substring(0, split2[1].trim().length() - 1);
        String seconds = split2[2].trim().substring(0, split2[2].trim().length() - 1);

        double decimal = ((Double.valueOf(minutes) * 60) + Double.valueOf(seconds)) / (60 * 60);
        if ("N".equals(stringRef) || "E".equals(stringRef)) {
            result = Double.valueOf(degrees) + decimal;
        } else {
            result = 0 - (Double.valueOf(degrees) + decimal);
        }
        return result;
    }
}
