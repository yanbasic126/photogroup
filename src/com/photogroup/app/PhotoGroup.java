package com.photogroup.app;

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
import com.photogroup.app.ui.PhotoGroupWindow;
import com.photogroup.app.ui.ProgressMonitor;
import com.photogroup.metadata.MetadataReader;
import com.photogroup.position.PostionHelper;
import com.photogroup.util.PhotoNameUtil;

public class PhotoGroup {

    public static final String BAIDU_API_KEY = "1607e140964c4974ddfd87286ae9d6b7";

    private ProgressMonitor progressMonitor;

    public void startGrouping(String[] args) {

        if (args.length == 0) {
            // Start GUI
            PhotoGroupWindow.main(null);
            return;
        }
        String[] photoTypes = { "PNG", "JPG", "JPEG", "GIF" };
        int threshold = 1;
        String photosPath = "";
        int module = 3;
        String format = "YYYY.M.d";
        boolean guess = true;
        boolean gps = true;
        boolean report = true;
        // MM.dd, YYYY.MM.dd, M.d
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
            case "--threshold":
                threshold = Integer.parseInt(args[i + 1]);
                i++;
                break;
            case "-t":
                threshold = Integer.parseInt(args[i + 1]);
                i++;
                break;
            case "--path":
                photosPath = args[i + 1];
                i++;
                break;
            case "-p":
                photosPath = args[i + 1];
                i++;
                break;
            case "--module":
                module = Integer.parseInt(args[i + 1]);
                i++;
                break;
            case "-m":
                module = Integer.parseInt(args[i + 1]);
                i++;
                break;
            case "--format":
                format = args[i + 1];
                i++;
                break;
            case "-f":
                format = args[i + 1];
                i++;
                break;
            case "--guess":
                guess = true;
                break;
            case "-g":
                guess = true;
                break;
            case "--gps":
                gps = true;
                break;
            case "-gps":
                gps = true;
                break;
            case "--help":
                printHelp();
                System.exit(0);
                break;
            case "-h":
                printHelp();
                System.exit(0);
                break;
            default:
                System.out.println("Error! Unsupport parameter: " + args[i]);
                System.exit(0);
                break;
            }
        }

        if (photosPath.isEmpty()) {
            System.out.println("Error! Directory is not specified, use -path(-p) to set");
            System.exit(0);
        }

        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy:MM:dd HH:mm:ss");

        Map<String, List<File>> photosDate = new HashMap<String, List<File>>();
        Comparator<File> comparator = new Comparator<File>() {

            @Override
            public int compare(File o1, File o2) {
                return PhotoNameUtil.compareByName(o1.getName(), o2.getName());
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
            if (child.isFile()) {

                try {
                    exifDateTime.put(child, MetadataReader.dateTaken(child));
                    if (gps) {
                        exifAddress.put(child, PostionHelper.queryPostion(child));
                    }
                } catch (ImageProcessingException | IOException e) {
                    exifDateTime.put(child, null);
                    System.out.println(child.getName());
                    e.printStackTrace();
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
                    if (!PhotoNameUtil.isSameNamingType(files[up].getName(), file.getName())) {
                        break;
                    }
                    String exif = exifDateTime.get(files[up]);
                    if (exif != null) {
                        upDate = exif.substring(0, exif.indexOf(' '));
                        break;
                    }
                }
                for (int down = (int) progress + 1; down < files.length; down++) {
                    if (!PhotoNameUtil.isSameNamingType(files[down].getName(), file.getName())) {
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
                if (progressMonitor != null) {
                    progressMonitor.setProgress(newPrecent);
                }
            }
        }
        if (progressMonitor != null) {
            progressMonitor.setDone(true);
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

                if (pair.getValue().size() >= threshold) {
                    File dateFolder = new File(photoFolder, folderName);
                    if (!dateFolder.exists()) {
                        dateFolder.mkdir();
                    }
                    for (File photo : pair.getValue()) {
                        if (photo.exists()) {
                            File targetPhoto = new File(dateFolder, photo.getName());
                            // photo.renameTo(targetPhoto);
                        }
                    }
                }

                System.out.println(folderName + " : " + pair.getValue().size());
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    }

    private void printHelp() {
        System.out.println("Usage:");
        System.out.println("--threshold (-t) <arg>, optional: Create a folder by the photos count, default is 1");
        System.out.println("--path (-p) <arg>, REQUIRED: Photo directory, using \".\" for current folder");
        System.out.println(
                "--format (-f) <arg>, optional: Date format of the folder name, default is YYYY.M.d. Support format by java.text.SimpleDateFormat");
        System.out.println(
                "--module (-m) <arg>, optional: 1 only process photos by EXIF date. \n2 process all photos, if the EXIF date does not exist use last modified date instead.(-g gets higher priority)\n3 process all file types by the last modified date(-g gets higher priority). Default is 3");
        System.out.println(
                "--guess (-g), If the photo EXIF data does not exist and it betweens the same taken date pohots which contains EXIF data, will use this date as taken date. Default is true");
        System.out.println(
                "--gps (-gps), Add address in folder name by GPS data. Require internet access to baidu map API. Default is true");
        System.out.println("--report (-r), Generate photo process report after running, in the photo directory. Default is true");

    }

    public void setProgressMonitorForGUI(ProgressMonitor monitor) {
        progressMonitor = monitor;
    }

    public static void main(String[] args) {
        new PhotoGroup().startGrouping(args);
    }

}
