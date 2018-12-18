package com.photogroup.commandline;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import com.photogroup.exception.ExceptionHandler;
import com.photogroup.groupby.PhotoGroup;
import com.photogroup.ui.browser.GroupBrowser;
import com.photogroup.util.FileUtil;

public class CommandLine {

    private static void process(String[] args) {
        int threshold = 1;
        String photosPath = "";
        int module = 3;
        String format = "YYYY.M.d";
        boolean guess = true;
        boolean gps = true;
        boolean report = true;
        boolean subfolder = false;
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
            case "--subfolder":
                subfolder = true;
                break;
            case "-subfolder":
                subfolder = true;
                break;
            case "--help":
                printHelp();
                Runtime.getRuntime().exit(0);
                break;
            case "-h":
                printHelp();
                Runtime.getRuntime().exit(0);
                break;
            default:
                System.out.println("Error! Unsupport parameter: " + args[i]);
                break;
            }
        }
        HashMap<String, List<File>> photoGroup = new HashMap<String, List<File>>();
        try {
            Thread thread = new Thread(
                    new PhotoGroup(photoGroup, photosPath, threshold, module, format, guess, gps, report, subfolder));
            thread.start();
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            ExceptionHandler.logError(e.getMessage());
        }
        FileUtil.movePhotos(photosPath, photoGroup);
    }

    private static void printHelp() {
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
        System.out.println("--subfolder (-subfolder), Include sub folder, read files recursively.");
        // System.out.println("--report (-r), Generate photo process report after running, in the photo directory.
        // Default is true");
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            // Start GUI
            // PhotoGroupWindow.main(null);
            GroupBrowser.main(null);
            return;
        } else {
            process(args);
        }
    }
}
