package com.photogroup.groupby.position;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import com.photogroup.util.FileUtil;

enum UserDefineDirectory {

                          INSTANCE;

    private static final String CUSTOM_NAME_MAPPING = "custom_name_mapping.ini";

    private Properties directoryMap = null;

    private UserDefineDirectory() {
        try {
            initMap();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initMap() throws IOException {
        InputStream inputStream = new FileInputStream(FileUtil.createSettingFileIfNotExist(CUSTOM_NAME_MAPPING));
        // File directory = new File("custom_name_mapping.ini");
        // InputStream inputStream = getClass().getClassLoader().getResourceAsStream(CUSTOM_NAME_MAPPING);
        if (inputStream != null) {
            // if (directory.exists() && directory.canRead()) {
            directoryMap = new Properties();
            // FileInputStream is = new FileInputStream(directory);
            // BufferedInputStream in = new BufferedInputStream(is);
            directoryMap.load(new InputStreamReader(inputStream, "utf-8"));
            // }
        }
    }

    public String replace(String source) {
        return directoryMap != null && directoryMap.containsKey(source) ? directoryMap.getProperty(source) : source;
    }

    public static void main(String[] args) {
        System.out.println(INSTANCE.replace("双龙南里-西区"));
        System.out.println(INSTANCE.replace("新鸿基地产大楼"));
        System.out.println(INSTANCE.replace("新鸿基地产大"));
    }

}
