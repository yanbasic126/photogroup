package com.photogroup.util;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class FileUtil {

    public static void movePhotos(int threshold, String photosPath, Map<String, List<File>> group) {
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
}
