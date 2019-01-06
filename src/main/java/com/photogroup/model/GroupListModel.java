package com.photogroup.model;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import com.photogroup.util.ComparatorUtil;

public class GroupListModel implements Comparator<GroupModel> {

    private Set<GroupModel> groupList;

    public GroupListModel() {
        groupList = new TreeSet<GroupModel>(this);
    }

    public Set<GroupModel> getGroupList() {
        return groupList;
    }

    public void addPhotoGroup(GroupModel photoGroup) {
        this.groupList.add(photoGroup);
    }

    public boolean containsTitle(String title) {
        for (GroupModel gm : this.groupList) {
            if (gm.getTitle().equals(title)) {
                return true;
            }
        }
        return false;
    }

    public GroupModel getGroupModelbyTitle(String title) {
        for (GroupModel gm : this.groupList) {
            if (gm.getTitle().equals(title)) {
                return gm;
            }
        }
        return null;
    }

    @Override
    public int compare(GroupModel gm1, GroupModel gm2) {
        return ComparatorUtil.DATE_TITLE_COMPARATOR.compare(gm1.getTitle(), gm2.getTitle());
    }

    public int size() {
        return this.groupList.size();
    }

    public void remove(String title) {
        GroupModel removed = null;
        for (GroupModel gm : this.groupList) {
            if (gm.getTitle().equals(title)) {
                removed = gm;
                break;
            }
        }
        if (removed != null) {
            this.groupList.remove(removed);
        }
    }

    public void updateTitle(String oldTitle, String newTitle) {
        for (GroupModel gm : this.groupList) {
            if (gm.getTitle().equals(oldTitle)) {
                gm.setTitle(newTitle);
                break;
            }
        }
    }
}
