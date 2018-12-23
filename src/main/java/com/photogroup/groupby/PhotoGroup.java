package com.photogroup.groupby;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.photogroup.util.ComparatorUtil;
import com.photogroup.util.FileUtil;

public class PhotoGroup implements Runnable {

    public Object processPrecent = 0;

    private String photosPath;

    // private int threshold;

    private int module;

    private String format;

    private boolean guess;

    private boolean gps;

    // private boolean report;

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
        Map<File, String> exifDateTime = new TreeMap<File, String>(PhotoGroupHelper.DATENAME_COMPARATOR);
        Map<File, String> exifAddress = new TreeMap<File, String>(PhotoGroupHelper.DATENAME_COMPARATOR);

        float progress = 0;
        int precent = 0;

        File[] listFiles = PhotoGroupHelper.listFiles(photosPath, subfolder);
        float fileCount = listFiles.length;

        for (final File child : listFiles) {
            try {
                PhotoGroupHelper.fillMetadataMap(child, exifDateTime, exifAddress, gps);
            } catch (IOException e) {
                e.printStackTrace();
            }
            progress++;
            int newPrecent = (int) (progress / fileCount * 100);
            if (newPrecent != precent) {
                precent = newPrecent;
                PhotoGroupHelper.printPrecent(newPrecent);
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
                if (PhotoGroupHelper.PHOTO_TYPES.contains(ext)) {
                    dateTime = PhotoGroupHelper.simpleDateFormat(file.lastModified());
                    break;
                }
            }

            if (dateTime == null && module == 3) {
                dateTime = PhotoGroupHelper.simpleDateFormat(file.lastModified());
            }
            PhotoGroupHelper.addFileDate(file, dateTime, photosDate);
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
            @SuppressWarnings("unchecked")
            Map.Entry<LocalDate, List<File>> pair = (Entry<LocalDate, List<File>>) it.next();
            List<File> photos = pair.getValue();

            String folderName = pair.getKey().format(pattern) + PhotoGroupHelper.findAddressName(photos, exifAddress);
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
}
