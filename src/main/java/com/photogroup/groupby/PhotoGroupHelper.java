package com.photogroup.groupby;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.drew.imaging.ImageProcessingException;
import com.photogroup.exception.ExceptionHandler;
import com.photogroup.groupby.metadata.MetadataReader;
import com.photogroup.groupby.position.PostionHelper;
import com.photogroup.util.ComparatorUtil;

public class PhotoGroupHelper {

    public static final Comparator<File> DATENAME_COMPARATOR;

    public static final Set<String> PHOTO_TYPES;

    private static final int FILENAME_LEN = 128;

    private static final String DATE_TIME_PATTERN = "yyyy:MM:dd HH:mm:ss";

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);

    // TODO remove simple formatter
    private static final SimpleDateFormat simpleFormatter = new SimpleDateFormat(DATE_TIME_PATTERN);

    static {
        final Set<String> photoTypes = new HashSet<String>();

        photoTypes.add("PNG");
        photoTypes.add("JPG");
        photoTypes.add("JPEG");
        photoTypes.add("GIF");
        PHOTO_TYPES = Collections.unmodifiableSet(photoTypes);

        DATENAME_COMPARATOR = new Comparator<File>() {

            @Override
            public int compare(File o1, File o2) {
                return ComparatorUtil.compareByName(o1.getName(), o2.getName());
            }
        };
    }

    public static void addFileDate(final File file, String dateTime, Map<LocalDate, List<File>> photosDate) {
        if (dateTime != null) {
            LocalDate localDate = LocalDate.parse(dateTime.trim(), formatter);
            if (photosDate.get(localDate) == null) {
                ArrayList<File> photoNameList = new ArrayList<File>();
                photoNameList.add(file);
                photosDate.put(localDate, photoNameList);
            } else {
                photosDate.get(localDate).add(file);
            }
        }
    }

    public static String simpleDateFormat(long lastModified) {
        return simpleFormatter.format(lastModified);
    }

    private static String contactAddress(Set<String> addressNames) {
        String addressName = "";
        for (String add : addressNames) {
            if (addressName.length() + add.length() > FILENAME_LEN) { // 255
                break;
            } else {
                addressName += add + ",";
            }
        }
        if (addressName.endsWith(",")) {
            addressName = addressName.substring(0, addressName.length() - 1);
        }
        return addressName;
    }

    public static String findAddressName(List<File> photos, Map<File, String> exifAddressMap) {
        Set<String> addressNames = new HashSet<String>();
        Collections.sort(photos, DATENAME_COMPARATOR);
        for (File photo : photos) {
            if (exifAddressMap.get(photo) != null) {
                addressNames.add(exifAddressMap.get(photo));
            }
        }
        return contactAddress(addressNames);
    }

    // update console
    public static void printPrecent(int precent) {
        System.out.print(precent + "%");
        for (int i = 0; i <= String.valueOf(precent).length(); i++) {
            System.out.print("\b");
        }
    }

    /**
     * Read file metadata and store into datatime map and address map
     * 
     * @param child
     * @param exifDateTime
     * @param exifAddress
     * @param gps
     * @throws IOException
     */
    public static void fillMetadataMap(final File child, Map<File, String> exifDateTime, Map<File, String> exifAddress,
            boolean gps) throws IOException {
        try {
            exifDateTime.put(child, MetadataReader.dateTaken(child));
            if (gps) {
                String postion = PostionHelper.queryPostion(child);
                if (postion != null) {
                    exifAddress.put(child, PostionHelper.queryPostion(child));
                }
            }
        } catch (ImageProcessingException e) {
            exifDateTime.put(child, null);
            e.printStackTrace();
            ExceptionHandler.logError(child.getName() + "|" + e.getMessage());
        }
    }

    /**
     * get files list in folder
     * 
     * @param photosPath
     * @param subfolder
     * 
     * @return files
     */
    public static File[] listFiles(String photosPath, boolean subfolder) {
        // File[] listFiles;
        final File photoFolder = new File(photosPath);

        final List<File> recursiveFiles = new ArrayList<File>();
        if (subfolder) {
            try {
                Files.find(Paths.get(photosPath), Integer.MAX_VALUE, (filePath, fileAttr) -> fileAttr.isRegularFile())
                        .forEach(filePath -> {
                            recursiveFiles.add(new File(filePath.toUri()));
                        });
            } catch (IOException e) {
                e.printStackTrace();
                ExceptionHandler.logError(e.getMessage());
            }
        } else {
            recursiveFiles.addAll(Arrays.asList(photoFolder.listFiles()));
        }

        List<File> listFiles = new ArrayList<File>();
        for (File child : recursiveFiles) {
            if (child.isFile() && !child.isHidden()) {
                listFiles.add(child);
            }
        }
        return listFiles.toArray(new File[] {});
    }
}
