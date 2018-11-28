package com.photogroup.groupby;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.drew.imaging.ImageProcessingException;
import com.photogroup.exception.ExceptionHandler;
import com.photogroup.groupby.metadata.MetadataReader;
import com.photogroup.groupby.position.PostionHelper;
import com.photogroup.util.PhotoNameCompareUtil;

public class PhotoGroup implements Runnable {

    public Object processPrecent = 0;

    private String photosPath;

    private int threshold;

    private int module;

    private String format;

    private boolean guess;

    private boolean gps;

    private boolean report;

    private String[] photoTypes;

    private Map<String, List<File>> photoGroup;

    public PhotoGroup(Map<String, List<File>> photoGroup, String photosPath, int threshold, int module, String format,
            boolean guess, boolean gps, boolean report) {
        this.photoGroup = photoGroup;
        this.photosPath = photosPath;
        this.threshold = threshold;
        this.module = module;
        this.format = format;
        this.guess = guess;
        this.gps = gps;
        this.report = report;

        photoTypes = new String[] { "PNG", "JPG", "JPEG", "GIF" };
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

        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy:MM:dd HH:mm:ss");

        Map<String, List<File>> photosDate = new HashMap<String, List<File>>();
        Comparator<File> comparator = new Comparator<File>() {

            @Override
            public int compare(File o1, File o2) {
                return PhotoNameCompareUtil.compareByName(o1.getName(), o2.getName());
            }
        };
        Map<File, String> exifDateTime = new TreeMap<File, String>(comparator);
        Map<File, String> exifAddress = new TreeMap<File, String>(comparator);

        final File photoFolder = new File(photosPath);

        float progress = 0;
        File[] listFiles = photoFolder.listFiles();
        float fileCount = listFiles.length;
        int precent = 0;

        for (final File child : listFiles) {
            if (child.isFile() && !child.isHidden()) {

                try {
                    exifDateTime.put(child, MetadataReader.dateTaken(child));
                    if (gps) {
                        exifAddress.put(child, PostionHelper.queryPostion(child));
                    }
                } catch (ImageProcessingException | IOException e) {
                    exifDateTime.put(child, null);
                    e.printStackTrace();
                    ExceptionHandler.logError(child.getName() + "|" + e.getMessage());
                }
                progress++;

                int newPrecent = (int) (progress / fileCount * 100);
                if (newPrecent != precent) {
                    // update console
                    precent = newPrecent;
                    System.out.print(precent + "%");
                    for (int i = 0; i <= String.valueOf(precent).length(); i++) {
                        System.out.print("\b");
                    }
                    synchronized (processPrecent) {
                        processPrecent = newPrecent;
                    }
                }
            }
        }
        File[] files = exifDateTime.keySet().toArray(new File[0]);

        progress = 0;
        precent = 0;
        for (final File file : files) { // files
            String dateTime = exifDateTime.get(file);

            if (dateTime == null && guess) {
                String upDate = null;
                String downDate = null;
                for (int up = (int) progress - 1; up >= 0; up--) {
                    if (!PhotoNameCompareUtil.isSameNamingType(files[up].getName(), file.getName())) {
                        break;
                    }
                    String exif = exifDateTime.get(files[up]);
                    if (exif != null) {
                        upDate = exif.substring(0, exif.indexOf(' '));
                        break;
                    }
                }
                for (int down = (int) progress + 1; down < files.length; down++) {
                    if (!PhotoNameCompareUtil.isSameNamingType(files[down].getName(), file.getName())) {
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
                int i = file.getName().lastIndexOf('.');
                if (i > 0) {
                    String ext = file.getName().substring(i + 1).toUpperCase();
                    for (String supportType : photoTypes) {
                        if (supportType.equals(ext)) {
                            SimpleDateFormat fileDateFormat = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
                            dateTime = fileDateFormat.format(file.lastModified());
                            break;
                        }
                    }
                }
            }

            if (dateTime == null && module == 3) {
                SimpleDateFormat fileDateFormat = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
                dateTime = fileDateFormat.format(file.lastModified());
            }
            if (dateTime != null) {

                DateTime parseDateTime = formatter.parseDateTime(dateTime.trim());
                String dateFormat = new SimpleDateFormat("yyyy.MM.dd").format(parseDateTime.toDate());

                if (photosDate.get(dateFormat) == null) {
                    ArrayList<File> photoNameList = new ArrayList<File>();
                    photoNameList.add(file);
                    photosDate.put(dateFormat, photoNameList);
                } else {
                    photosDate.get(dateFormat).add(file);
                }
            }
            progress++;

            int newPrecent = (int) (progress / fileCount * 100);
            if (newPrecent != precent) {
                // update console
                precent = newPrecent;
                System.out.print(precent + "%");
                for (int i = 0; i <= String.valueOf(precent).length(); i++) {
                    System.out.print("\b");
                }
            }
        }
        System.out.println("");

        Iterator<?> it = photosDate.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, List<File>> pair = (Entry<String, List<File>>) it.next();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
            try {
                Date photoDate = dateFormat.parse(pair.getKey());
                String folderName = new SimpleDateFormat(format).format(photoDate);
                String addressName = "";
                Set<String> addressNames = new HashSet<String>();
                List<File> photos = pair.getValue();
                Collections.sort(photos, comparator);
                for (File photo : photos) {
                    if (exifAddress.get(photo) != null) {
                        addressNames.add(exifAddress.get(photo));
                    }
                }
                for (String add : addressNames) {
                    if (addressName.length() + add.length() > 128) { // 255
                        break;
                    } else {
                        addressName += add + ",";
                    }
                }
                if (addressName.endsWith(",")) {
                    addressName = addressName.substring(0, addressName.length() - 1);
                }
                folderName += addressName;

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
            } catch (ParseException e) {
                e.printStackTrace();
                ExceptionHandler.logError(e.getMessage());
            }

        }
    }

}
