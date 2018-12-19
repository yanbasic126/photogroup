package com.photogroup.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import com.photogroup.util.FileUtil;

public class SettingStore {

    // private static final String LEMONPHOTO_PATH = "/.lemonphoto";

    private static final String DEFAULT_SETTINGS_FILE = "default_settings.ini";

    private int threshold;

    private int module;

    private String format;

    private boolean guess;

    private boolean gps;

    private boolean report;

    private boolean includeSubFolder;

    private boolean uiUseThumbnail;

    private String baiduKey;

    private String bingKey;

    private String googleKey;

    private Properties settingMap = null;

    private static SettingStore store;

    private File settingFile;

    private SettingStore() {
        try {
            initMap();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SettingStore getSettingStore() {
        if (store == null) {
            store = new SettingStore();
        }
        return store;
    }

    private void initMap() throws IOException {
        settingFile = FileUtil.createSettingFileIfNotExist(DEFAULT_SETTINGS_FILE);
        InputStream inputStream = new FileInputStream(settingFile);
        if (inputStream != null) {
            settingMap = new Properties();
            settingMap.load(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        }
        // update settingMap from package
        InputStream newIs = getClass().getClassLoader().getResourceAsStream(DEFAULT_SETTINGS_FILE);
        Properties newProperties = new Properties();
        newProperties.load(newIs);
        Set<Entry<Object, Object>> newset = newProperties.entrySet();
        for (Entry<Object, Object> e : newset) {
            String key = (String) e.getKey();
            String value = (String) e.getValue();
            if (!settingMap.containsKey(key)) {
                settingMap.put(key, value);
            }
        }
        newIs.close();
        // getClass().getClassLoader().getResourceAsStream("default_settings.ini");

        threshold = Integer.parseInt(settingMap.getProperty("cmd.threshold"));
        module = Integer.parseInt(settingMap.getProperty("cmd.module"));
        format = settingMap.getProperty("cmd.format");
        guess = Boolean.valueOf(settingMap.getProperty("cmd.guess"));
        gps = Boolean.valueOf(settingMap.getProperty("cmd.gps"));
        report = Boolean.valueOf(settingMap.getProperty("cmd.report"));
        includeSubFolder = Boolean.valueOf(settingMap.getProperty("cmd.subfolder"));
        uiUseThumbnail = Boolean.valueOf(settingMap.getProperty("ui.use_thumbnail"));
        baiduKey = settingMap.getProperty("password.baidu_api_key");
        bingKey = settingMap.getProperty("password.bing_api_key");
        googleKey = settingMap.getProperty("password.google_api_key");
    }

    public int getThreshold() {
        return this.threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
        settingMap.setProperty("cmd.threshold", String.valueOf(threshold));
    }

    public int getModule() {
        return this.module;
    }

    public void setModule(int module) {
        this.module = module;
        settingMap.setProperty("cmd.module", String.valueOf(module));
    }

    public String getFormat() {
        return this.format;
    }

    public void setFormat(String format) {
        this.format = format;
        settingMap.setProperty("cmd.format", String.valueOf(format));
    }

    public boolean isGuess() {
        return this.guess;
    }

    public void setGuess(boolean guess) {
        this.guess = guess;
        settingMap.setProperty("cmd.guess", String.valueOf(guess));
    }

    public boolean isGps() {
        return this.gps;
    }

    public void setGps(boolean gps) {
        this.gps = gps;
        settingMap.setProperty("cmd.gps", String.valueOf(gps));
    }

    public boolean isReport() {
        return this.report;
    }

    public void setReport(boolean report) {
        this.report = report;
        settingMap.setProperty("cmd.report", String.valueOf(report));
    }

    public boolean isIncludeSubFolder() {
        return includeSubFolder;
    }

    public void setIncludeSubFolder(boolean includeSubFolder) {
        this.includeSubFolder = includeSubFolder;
        settingMap.setProperty("cmd.subfolder", String.valueOf(includeSubFolder));
    }

    public boolean isUseThumbnail() {
        return uiUseThumbnail;
    }

    public void setUseThumbnail(boolean uiUseThumbnail) {
        this.uiUseThumbnail = uiUseThumbnail;
        settingMap.setProperty("ui.use_thumbnail", String.valueOf(uiUseThumbnail));
    }

    public static void saveSettings() throws Exception {
        if (store != null) {
            FileOutputStream os = new FileOutputStream(store.settingFile);
            store.settingMap.store(os, null);
        }
    }

    public String getBaiduKey() {
        return baiduKey;
    }

    public void setBaiduKey(String baiduKey) {
        this.baiduKey = baiduKey;
        settingMap.setProperty("password.baidu_api_key", String.valueOf(baiduKey));
    }

    public String getBingKey() {
        return bingKey;
    }

    public void setBingKey(String bingKey) {
        this.bingKey = bingKey;
        settingMap.setProperty("password.bing_api_key", String.valueOf(bingKey));
    }

    public String getGoogleKey() {
        return googleKey;
    }

    public void setGoogleKey(String googleKey) {
        this.googleKey = googleKey;
        settingMap.setProperty("password.google_api_key", String.valueOf(googleKey));
    }

}
