package com.photogroup.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

    public static void movePhotos(String photosPath, Map<String, List<File>> group) {
        int threshold = SettingStore.getSettingStore().getThreshold();
        Iterator<?> it = group.entrySet().iterator();
        while (it.hasNext()) {
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
    }
}
