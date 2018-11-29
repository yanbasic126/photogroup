package com.photogroup.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

    public static String getPrintSize(long size) {
        // 如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
        double value = (double) size;
        if (value < 1024) {
            return String.valueOf(value) + "B";
        } else {
            value = new BigDecimal(value / 1024).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
        }
        // 如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
        // 因为还没有到达要使用另一个单位的时候
        // 接下去以此类推
        if (value < 1024) {
            return String.valueOf(value) + "KB";
        } else {
            value = new BigDecimal(value / 1024).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
        }
        if (value < 1024) {
            return String.valueOf(value) + "MB";
        } else {
            // 否则如果要以GB为单位的，先除于1024再作同样的处理
            value = new BigDecimal(value / 1024).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
            return String.valueOf(value) + "GB";
        }
    }
}
