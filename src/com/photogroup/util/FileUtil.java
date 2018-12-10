package com.photogroup.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.photogroup.exception.ExceptionHandler;
import com.photogroup.ui.SettingStore;

public class FileUtil {

    public static final String LEMONPHOTO_PATH = "/.lemonphoto";

    public static void movePhotos(String photosPath, Map<String, List<File>> group) {
        int threshold = SettingStore.getSettingStore().getThreshold();
        Iterator<?> it = group.entrySet().iterator();
        while (it.hasNext()) {
            @SuppressWarnings("unchecked")
            Map.Entry<String, List<File>> pair = (Entry<String, List<File>>) it.next();
            if (pair.getValue().size() >= threshold) {
                File dateFolder = new File(photosPath, pair.getKey());
                if (!dateFolder.exists()) {
                    dateFolder.mkdir();
                }
                for (File photo : pair.getValue()) {
                    if (photo.exists()) {
                        File targetPhoto = new File(dateFolder, photo.getName());
                        photo.renameTo(targetPhoto);
                    }
                }
            }
        }
    }

    public static String getBuildVersion() {
        String lineVersion = null;
        InputStream inputStream = FileUtil.class.getClassLoader().getResourceAsStream("version.txt");
        if (inputStream != null) {
            try {
                lineVersion = new BufferedReader(new InputStreamReader(inputStream)).readLine();
            } catch (IOException e1) {
                ExceptionHandler.logError(e1.getMessage());
            }
        }
        return lineVersion;
    }

    public static void download(String url, String target) throws IOException {
        URL website = new URL(url);
        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
        FileOutputStream fos = new FileOutputStream(target);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        fos.close();
    }

    public static String getPrintSize(long size) {
        double value = (double) size;
        if (value < 1024) {
            return String.valueOf(value) + "B";
        } else {
            value = new BigDecimal(value / 1024).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
        }
        if (value < 1024) {
            return String.valueOf(value) + "KB";
        } else {
            value = new BigDecimal(value / 1024).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
        }
        if (value < 1024) {
            return String.valueOf(value) + "MB";
        } else {
            value = new BigDecimal(value / 1024).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
            return String.valueOf(value) + "GB";
        }
    }

    public static File createSettingFileIfNotExist(String fileName) throws IOException {
        String userHome = System.getProperty("user.home");
        File homeDir = new File(userHome + LEMONPHOTO_PATH);
        homeDir.mkdir();
        File settingFile = new File(userHome + LEMONPHOTO_PATH + "/" + fileName);
        if (!settingFile.exists()) {
            // copy setting file
            InputStream is = FileUtil.class.getClassLoader().getResourceAsStream(fileName);
            OutputStream os = new FileOutputStream(settingFile);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            os.close();
            is.close();
        }
        return settingFile;
    }
}
