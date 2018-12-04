package com.photogroup.ui;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class SettingStore {

    private int threshold;

    private int module;

    private String format;

    private boolean guess;

    private boolean gps;

    private boolean report;

    private boolean includeSubFolder;

    private boolean uiUseThumbnail;

    private Properties settingMap = null;

    private static SettingStore store;

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

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("default_settings.txt");
        if (inputStream != null) {
            settingMap = new Properties();
            settingMap.load(new InputStreamReader(inputStream, "utf-8"));

            threshold = Integer.parseInt(settingMap.getProperty("cmd.threshold"));
            module = Integer.parseInt(settingMap.getProperty("cmd.module"));
            format = settingMap.getProperty("cmd.format");
            guess = Boolean.valueOf(settingMap.getProperty("cmd.guess"));
            gps = Boolean.valueOf(settingMap.getProperty("cmd.gps"));
            report = Boolean.valueOf(settingMap.getProperty("cmd.report"));
            includeSubFolder = Boolean.valueOf(settingMap.getProperty("cmd.subfolder"));
            uiUseThumbnail = Boolean.valueOf(settingMap.getProperty("ui.use_thumbnail"));
        }
    }

    public int getThreshold() {
        return this.threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public int getModule() {
        return this.module;
    }

    public void setModule(int module) {
        this.module = module;
    }

    public String getFormat() {
        return this.format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public boolean isGuess() {
        return this.guess;
    }

    public void setGuess(boolean guess) {
        this.guess = guess;
    }

    public boolean isGps() {
        return this.gps;
    }

    public void setGps(boolean gps) {
        this.gps = gps;
    }

    public boolean isReport() {
        return this.report;
    }

    public void setReport(boolean report) {
        this.report = report;
    }

    public boolean isIncludeSubFolder() {
        return includeSubFolder;
    }

    public void setIncludeSubFolder(boolean includeSubFolder) {
        this.includeSubFolder = includeSubFolder;
    }

    public boolean isUseThumbnail() {
        return uiUseThumbnail;
    }

    public void setUseThumbnail(boolean uiUseThumbnail) {
        this.uiUseThumbnail = uiUseThumbnail;
    }

}
