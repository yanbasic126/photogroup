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
            String lon = MetadataReaderHelper.getDescription(directory, GpsDirectory.TAG_LONGITUDE);
            String lonRef = MetadataReaderHelper.getDescription(directory, GpsDirectory.TAG_LONGITUDE_REF);
            String lat = MetadataReaderHelper.getDescription(directory, GpsDirectory.TAG_LATITUDE);
            String latRef = MetadataReaderHelper.getDescription(directory, GpsDirectory.TAG_LATITUDE_REF);
            if (!lon.isEmpty() && !lat.isEmpty() && !lonRef.isEmpty() && !latRef.isEmpty()) {
                return new Double[] { MetadataReaderHelper.convertGpsToDegree(lat, latRef),
                        MetadataReaderHelper.convertGpsToDegree(lon, lonRef) };
            }
        }
        return null;
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
