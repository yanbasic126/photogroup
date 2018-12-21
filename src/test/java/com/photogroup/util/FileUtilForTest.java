package com.photogroup.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtilForTest {

    public static File createTempFileFromResource(String path) throws IOException, FileNotFoundException {
        InputStream is = ClassLoader.getSystemResourceAsStream(path);
        File res = File.createTempFile("resource", ".tmp");
        FileOutputStream outputStream = new FileOutputStream(res);
        int read = 0;
        byte[] bytes = new byte[1024];

        while ((read = is.read(bytes)) != -1) {
            outputStream.write(bytes, 0, read);
        }

        if (is != null) {
            is.close();
        }
        if (outputStream != null) {
            outputStream.close();
        }
        return res;
    }
}
