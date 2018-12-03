package com.photogroup.groupby.metadata;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifThumbnailDirectory;
import com.drew.metadata.exif.GpsDirectory;

public class MetadataReader {

    private static final Map<File, Metadata> metadataCache = new HashMap<File, Metadata>();

    private static Metadata readMetadataWithCache(File file) throws ImageProcessingException, IOException {
        Metadata cacheMetadata = metadataCache.get(file);
        if (null == cacheMetadata) {
            cacheMetadata = ImageMetadataReader.readMetadata(file);
            metadataCache.put(file, cacheMetadata);
        }
        return cacheMetadata;
    }

    public static String dateTaken(File file) throws ImageProcessingException, IOException {
        String dateTaken = null;
        Metadata metadata = readMetadataWithCache(file);
        ExifIFD0Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
        if (directory != null) {
            if (null != directory.getDescription(ExifIFD0Directory.TAG_DATETIME)) {
                dateTaken = directory.getDescription(ExifIFD0Directory.TAG_DATETIME);
            } else if (null != directory.getDescription(ExifIFD0Directory.TAG_DATETIME_ORIGINAL)) {
                dateTaken = directory.getDescription(ExifIFD0Directory.TAG_DATETIME_ORIGINAL);
            } else if (null != directory.getDescription(ExifIFD0Directory.TAG_DATETIME_DIGITIZED)) {
                dateTaken = directory.getDescription(ExifIFD0Directory.TAG_DATETIME_DIGITIZED);
            }
        }
        return dateTaken;
    }

    /**
     * @param file
     * @return lat, lon
     * @throws ImageProcessingException
     * @throws IOException
     */
    public static Double[] gps(File file) throws ImageProcessingException, IOException {
        Metadata metadata = readMetadataWithCache(file);
        GpsDirectory directory = metadata.getFirstDirectoryOfType(GpsDirectory.class);
        if (directory != null) {
            final String empty = "";
            String lon = directory.getDescription(GpsDirectory.TAG_LONGITUDE) == null ? empty
                    : directory.getDescription(GpsDirectory.TAG_LONGITUDE).trim();
            String lonRef = directory.getDescription(GpsDirectory.TAG_LONGITUDE_REF) == null ? empty
                    : directory.getDescription(GpsDirectory.TAG_LONGITUDE_REF).trim();
            String lat = directory.getDescription(GpsDirectory.TAG_LATITUDE) == null ? empty
                    : directory.getDescription(GpsDirectory.TAG_LATITUDE).trim();
            String latRef = directory.getDescription(GpsDirectory.TAG_LATITUDE_REF) == null ? empty
                    : directory.getDescription(GpsDirectory.TAG_LATITUDE_REF).trim();
            if (!lon.isEmpty() && !lat.isEmpty() && !lonRef.isEmpty() && !latRef.isEmpty()) {
                return new Double[] { convertGpsToDegree(lat, latRef), convertGpsToDegree(lon, lonRef) };
            }
        }
        return null;
    }

    private static Double convertGpsToDegree(String stringDMS, String stringRef) {
        double result;
        String[] split2 = stringDMS.split(" ");
        String degrees = split2[0].trim().substring(0, split2[0].trim().length() - 1);
        String minutes = split2[1].trim().substring(0, split2[1].trim().length() - 1);
        String seconds = split2[2].trim().substring(0, split2[2].trim().length() - 1);

        double decimal = ((Double.valueOf(minutes) * 60) + Double.valueOf(seconds)) / (60 * 60);
        if (stringRef.equals("N") || stringRef.equals("E")) {
            result = Double.valueOf(degrees) + decimal;
        } else {
            result = 0 - (Double.valueOf(degrees) + decimal);
        }
        return result;
    }

    /**
     * @param file
     * @return
     * @throws ImageProcessingException
     * @throws IOException
     * @throws MetadataException
     */
    public static int orientation(File file) throws ImageProcessingException, IOException, MetadataException {
        Metadata metadata = readMetadataWithCache(file);

        int orientation = 1;
        ExifIFD0Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
        if (directory != null) {
            orientation = directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);
        }
        return orientation;
    }

    /**
     * @param file
     * @return
     * @throws ImageProcessingException
     * @throws IOException
     */
    public static byte[] thumbnailData(File file) throws ImageProcessingException, IOException {
        Metadata metadata = readMetadataWithCache(file);
        ExifThumbnailDirectory directory = metadata.getFirstDirectoryOfType(ExifThumbnailDirectory.class);
        if (directory != null) {
            return directory.getThumbnailData();
        } else {
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
        Metadata metadata = ImageMetadataReader.readMetadata(new File(args[0]));

        for (Directory directory : metadata.getDirectories()) {
            for (Tag tag : directory.getTags()) {
                System.out.println(tag.getTagName() + ":  " + tag.getDescription());
            }
            if (directory.hasErrors()) {
                for (String error : directory.getErrors()) {
                    System.err.format("ERROR: %s", error);
                }
            }
        }
    }

}
