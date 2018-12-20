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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import com.drew.imaging.ImageProcessingException;
import com.photogroup.exception.ExceptionHandler;
import com.photogroup.groupby.metadata.MetadataReader;
import com.photogroup.groupby.position.PostionHelper;
import com.photogroup.util.ComparatorUtil;
import com.photogroup.util.FileUtil;

public class PhotoGroup implements Runnable {

    /**
     * 
     */
    private static final int FILENAME_LEN = 128;

    private static final String DATE_TIME_PATTERN = "yyyy:MM:dd HH:mm:ss";

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);

    // TODO remove simple formatter
    private static final SimpleDateFormat simpleFormatter = new SimpleDateFormat(DATE_TIME_PATTERN);

    public Object processPrecent = 0;

    private String photosPath;

    // private int threshold;

    private int module;

    private String format;

    private boolean guess;

    private boolean gps;

    // private boolean report;

    private static final Set<String> photoTypes = new HashSet<String>() {

        {
            add("PNG");
            add("JPG");
            add("JPEG");
            add("GIF");
        }
    };;

    private Map<String, List<File>> photoGroup;

    private boolean subfolder;

    public PhotoGroup(Map<String, List<File>> photoGroup, String photosPath, /* int threshold, */ int module, String format,
            boolean guess, boolean gps, /* boolean report, */ boolean includeSubFolder) {
        this.photoGroup = photoGroup;
        this.photosPath = photosPath;
        // this.threshold = threshold;
        this.module = module;
        this.format = format;
        this.guess = guess;
        this.gps = gps;
        // this.report = report;
        this.subfolder = includeSubFolder;
    }

    @Override
    public void run() {
        createPhotoGroup();
    }

    private void createPhotoGroup() {

        if (photosPath.isEmpty()) {
            System.out.println("Error! Directory is not specified, use -path(-p) to set");
            return;
        }

        Map<LocalDate, List<File>> photosDate = new HashMap<LocalDate, List<File>>();
        Comparator<File> comparator = new Comparator<File>() {

            @Override
            public int compare(File o1, File o2) {
                return ComparatorUtil.compareByName(o1.getName(), o2.getName());
            }
        };
        Map<File, String> exifDateTime = new TreeMap<File, String>(comparator);
        Map<File, String> exifAddress = new TreeMap<File, String>(comparator);

        float progress = 0;
        int precent = 0;

        File[] listFiles = listFiles();
        float fileCount = listFiles.length;

        for (final File child : listFiles) {
            try {
                fillMetadataMap(child, exifDateTime, exifAddress);
            } catch (IOException e) {
                e.printStackTrace();
            }
            progress++;
            int newPrecent = (int) (progress / fileCount * 100);
            if (newPrecent != precent) {
                precent = newPrecent;
                printPrecent(newPrecent);
                synchronized (processPrecent) {
                    processPrecent = newPrecent;
                }
            }
        }

        // this files list is sorted
        File[] files = exifDateTime.keySet().toArray(new File[0]);
        progress = 0;
        // precent = 0;
        for (final File file : files) { // files
            String dateTime = exifDateTime.get(file);

            if (dateTime == null && guess) {
                String upDate = null;
                String downDate = null;
                for (int up = (int) progress - 1; up >= 0; up--) {
                    if (!ComparatorUtil.isSameNamingType(files[up].getName(), file.getName())) {
                        break;
                    }
                    String exif = exifDateTime.get(files[up]);
                    if (exif != null) {
                        upDate = exif.substring(0, exif.indexOf(' '));
                        break;
                    }
                }
                for (int down = (int) progress + 1; down < files.length; down++) {
                    if (!ComparatorUtil.isSameNamingType(files[down].getName(), file.getName())) {
                        break;
                    }
                    String exif = exifDateTime.get(files[down]);
                    if (exif != null) {
                        downDate = exif.substring(0, exif.indexOf(' '));
                        if (upDate != null && downDate.equals(upDate)) {
                            dateTime = exifDateTime.get(files[down]);
                        }
                        break;
                    }
                }
            }

            if (dateTime == null && module == 2) {
                String ext = FileUtil.getExtension(file);
                if (photoTypes.contains(ext)) {
                    dateTime = simpleFormatter.format(file.lastModified());
                    break;
                }
            }

            if (dateTime == null && module == 3) {
                dateTime = simpleFormatter.format(file.lastModified());
            }
            addFileDate(file, dateTime, photosDate);
            progress++;
            //
            // int newPrecent = (int) (progress / fileCount * 100);
            // if (newPrecent != precent) {
            // // update console
            // precent = newPrecent;
            // printPrecent(precent);
            // }
        }
        System.out.println("");

        DateTimeFormatter pattern = DateTimeFormatter.ofPattern(format);
        Iterator<?> it = photosDate.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<LocalDate, List<File>> pair = (Entry<LocalDate, List<File>>) it.next();
            Set<String> addressNames = new HashSet<String>();
            List<File> photos = pair.getValue();
            Collections.sort(photos, comparator);
            for (File photo : photos) {
                if (exifAddress.get(photo) != null) {
                    addressNames.add(exifAddress.get(photo));
                }
            }

            String folderName = pair.getKey().format(pattern) + contactAddress(addressNames);
            // if (pair.getValue().size() >= threshold) {
            // File dateFolder = new File(photoFolder, folderName);
            // if (!dateFolder.exists()) {
            // dateFolder.mkdir();
            // }
            // for (File photo : pair.getValue()) {
            // if (photo.exists()) {
            // File targetPhoto = new File(dateFolder, photo.getName());
            // photo.renameTo(targetPhoto);
            // }
            // }
            // }

            photoGroup.put(folderName, pair.getValue());
            System.out.println(folderName + " : " + pair.getValue().size());
        }
    }

    private String contactAddress(Set<String> addressNames) {
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

    private void addFileDate(final File file, String dateTime, Map<LocalDate, List<File>> photosDate) {
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

    // update console
    private void printPrecent(int precent) {
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
     * @throws IOException
     */
    private void fillMetadataMap(final File child, Map<File, String> exifDateTime, Map<File, String> exifAddress)
            throws IOException {
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
     * @return files
     */
    private File[] listFiles() {
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
