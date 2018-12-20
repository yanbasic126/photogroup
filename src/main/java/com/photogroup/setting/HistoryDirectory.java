package com.photogroup.setting;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.photogroup.util.FileUtil;

public enum HistoryDirectory {
                              INSTANCE;

    private File historyFile;

    private Set<String> historyList;

    private HistoryDirectory() {
        try {
            historyFile = FileUtil.createSettingFileIfNotExist("history.ini");
            historyList = new HashSet<String>();
            BufferedReader br = new BufferedReader(new FileReader(historyFile));
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.isEmpty()) {
                    historyList.add(line);
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Set<String> listDirectory() {
        return historyList;
    }

    public void addDirectory(String fileDirectory) {
        historyList.add(fileDirectory);
        try {
            Files.write(historyFile.toPath(), historyList, StandardCharsets.UTF_8);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void clearHistory() {
        historyList.clear();
        try {
            Files.write(historyFile.toPath(), Collections.emptySet(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
