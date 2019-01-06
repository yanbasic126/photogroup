package com.photogroup.model;

import java.io.File;
import java.util.Comparator;
import java.util.List;

import com.photogroup.util.ComparatorUtil;

public class GroupModel implements Comparator<GroupModel> {

    private String title;

    private List<File> files;

    public GroupModel(String title, List<File> files) {
        this.title = title;
        this.files = files;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<File> getFiles() {
        return files;
    }

    @Override
    public int compare(GroupModel gm1, GroupModel gm2) {
        return ComparatorUtil.DATE_TITLE_COMPARATOR.compare(gm1.getTitle(), gm2.getTitle());
    }
}
