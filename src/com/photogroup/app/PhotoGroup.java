package com.photogroup.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.photogroup.util.StringSimilarity;

public class PhotoGroup {

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("Usage:");
            System.out.println("--threshold (-t) <arg>, optional: Create a folder by the photos count, default is 1");
            System.out.println("--path (-p) <arg>, REQUIRED: Photo directory, using \".\" for current folder");
            System.out.println(
                    "--format (-f) <arg>, optional: Date format of the folder name, default is YYYY.M.d. Support format by java.text.SimpleDateFormat");
            System.out.println(
                    "--module (-m) <arg>, optional: 1 only process photos by EXIF date. \n2 process all photos, if the EXIF date does not exist use last modified date instead.(-g gets higher priority)\n3 process all file types by the last modified date(-g gets higher priority). default is 1");
            System.out.println(
                    "--guess (-g), If the photo EXIF data does not exist and it betweens the same taken date pohots which contains EXIF data, will use this date as taken date.");

            System.exit(0);
        }
		String[] photoTypes = { "PNG", "JPG", "JPEG", "GIF" };
        int threshold = 1;
        String photosPath = "";
        int module = 1;
        String format = "YYYY.M.d";
        boolean guess = false;
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

        Map<String, List<String>> photosDate = new HashMap<String, List<String>>();
        Map<String, String> exifDateTime = new TreeMap<String, String>(new Comparator<String>() {
			Pattern p = Pattern.compile("\\d+");

			@Override
			public int compare(String o1, String o2) {
				double similarity = StringSimilarity.similarity(o1, o2);
				if (similarity > 0.2) {
					Matcher m1 = p.matcher(o1);
					Matcher m2 = p.matcher(o2);
					if (m1.find() && m2.find()) {
						int num1 = Integer.parseInt(m1.group(0));
						int num2 = Integer.parseInt(m2.group(0));
						return num1 - num2;
					}
				}
				return o1.compareTo(o2);

			}
		});
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
                if (line.startsWith("Date/Time")) {
                    dateTime = line.substring(line.indexOf(":") + 1).trim();
                    break;
                }
            }
            exifDateTime.put(child.getName(), dateTime);
            
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
    	String[] fileNames = exifDateTime.keySet().toArray(new String[0]);

    	progress = 0;
    	precent = 0;
        for (final File child : photoFolder.listFiles()) {
//            String[] commands = { "jhead.exe", child.getName() };
//            Runtime rt = Runtime.getRuntime();
//            Process proc = rt.exec(commands, null, photoFolder);
//
//            InputStream stdin = proc.getInputStream();
//            InputStreamReader isr = new InputStreamReader(stdin);
//            BufferedReader br = new BufferedReader(isr);
//
//            String line = null;
//
            String dateTime = exifDateTime.get(child.getName());
//
//            while ((line = br.readLine()) != null) {
//                if (line.startsWith("File date") && module == 2) {
//                    dateTime = line.substring(line.indexOf(":") + 1);
//                }
//                if (line.startsWith("Date/Time")) {
//                    dateTime = line.substring(line.indexOf(":") + 1);
//                    break;
//                }
//            }
            
            if(dateTime == null && guess) {
            	String upDate = null;
            	String downDate = null;
				for (int up = (int) progress; up >= 0; up--) {
					String exif = exifDateTime.get(fileNames[up]);
					if (exif != null) {
						upDate = exif.substring(0, exif.indexOf(' '));
						break;
					}
				}
				for (int down = (int) progress; down < fileNames.length; down++) {
					String exif = exifDateTime.get(fileNames[down]);
					if (exif != null) {
						downDate = exif.substring(0, exif.indexOf(' '));
						if (upDate != null && downDate.equals(upDate)) {
							dateTime = exifDateTime.get(fileNames[down]);
						}
						break;
					}
				}
            }
            
            if(dateTime == null && module == 2) {
				int i = child.getName().lastIndexOf('.');
				if (i > 0) {
					String ext = child.getName().substring(i + 1).toUpperCase();
					for(String supportType : photoTypes) {
						if (supportType.equals(ext)) {
							SimpleDateFormat fileDateFormat = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
							dateTime = fileDateFormat.format(child.lastModified());
							break;
						}
					}
					
				}
            }
            
            if(dateTime == null && module == 3) {
            	SimpleDateFormat fileDateFormat = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
            	dateTime = fileDateFormat.format(child.lastModified());
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
//            proc.waitFor();
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

            System.out.println(folderName + " : " + pair.getValue().size());
        }
    }

}
