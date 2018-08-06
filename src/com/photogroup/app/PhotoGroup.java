// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2013 Talend – www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
// ============================================================================
package com.photogroup.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class PhotoGroup {

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("Usage:");
            System.out.println("-threshold(-t), optional: Create a folder by the photos count, default is 1");
            System.out.println("-path(-p), REQUIRED: Photo directory, using \".\" for current folder");
            System.out.println(
                    "-format(-f), optional: Date format of the folder name, default is M.d. Support format by java.text.SimpleDateFormat");
            System.out.println(
                    "-module(-m), optional: 1 only process photos by EXIF date. 2 process all photos, if the EXIF date does not exist, use file date instead, default is 1");
            System.exit(0);
        }
        int threshold = 1;
        String photosPath = "";
        int module = 1;
        String format = "M.d";
        // MM.dd, YYYY.MM.dd, M.d
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
            case "-threshold":
                threshold = Integer.parseInt(args[i + 1]);
                i++;
                break;
            case "-t":
                threshold = Integer.parseInt(args[i + 1]);
                i++;
                break;
            case "-path":
                photosPath = args[i + 1];
                i++;
                break;
            case "-p":
                photosPath = args[i + 1];
                i++;
                break;
            case "-module":
                module = Integer.parseInt(args[i + 1]);
                i++;
                break;
            case "-m":
                module = Integer.parseInt(args[i + 1]);
                i++;
                break;
            case "-format":
                format = args[i + 1];
                i++;
                break;
            case "-f":
                format = args[i + 1];
                i++;
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

        HashMap<String, List<String>> photosDate = new HashMap<String, List<String>>();
        final File photoFolder = new File(photosPath);

        float progress = 0;
        float fileCount = photoFolder.listFiles().length;
        int precent = 0;

        for (final File child : photoFolder.listFiles()) {
            String[] commands = { "jhead.exe", child.getName() };
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(commands, null, photoFolder);

            InputStream stdin = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(stdin);
            BufferedReader br = new BufferedReader(isr);

            String line = null;

            String dateTime = null;

            while ((line = br.readLine()) != null) {
                if (line.startsWith("File date") && module == 2) {
                    dateTime = line.substring(line.indexOf(":") + 1);
                }
                if (line.startsWith("Date/Time")) {
                    dateTime = line.substring(line.indexOf(":") + 1);
                    break;
                }
            }
            if (dateTime != null) {

                DateTime parseDateTime = formatter.parseDateTime(dateTime.trim());
                String dateFormat = new SimpleDateFormat("yyyy.MM.dd").format(parseDateTime.toDate());

                if (photosDate.get(dateFormat) == null) {
                    ArrayList<String> photoNameList = new ArrayList<String>();
                    photoNameList.add(child.getName());
                    photosDate.put(dateFormat, photoNameList);
                } else {
                    photosDate.get(dateFormat).add(child.getName());
                }
            }
            proc.waitFor();
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
            Map.Entry<String, List<String>> pair = (Entry<String, List<String>>) it.next();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
            Date photoDate = dateFormat.parse(pair.getKey());
            // Calendar calendar = Calendar.getInstance();
            // calendar.setTime(folderDate);
            String folderName = new SimpleDateFormat(format).format(photoDate);// (calendar.get(Calendar.MONTH) + 1) +
                                                                               // "." + calendar.get(Calendar.DATE);

            if (pair.getValue().size() >= threshold) {
                File dateFolder = new File(photoFolder, folderName);
                if (!dateFolder.exists()) {
                    dateFolder.mkdir();
                }
                for (String photoName : pair.getValue()) {
                    File photo = new File(photoFolder, photoName);
                    if (photo.exists()) {
                        File targetPhoto = new File(dateFolder, photoName);
                        photo.renameTo(targetPhoto);
                    }
                }
            }

            System.out.println(folderName + " : " + pair.getValue());
        }
    }

}
