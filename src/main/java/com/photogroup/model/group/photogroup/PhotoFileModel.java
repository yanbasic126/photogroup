package com.photogroup.model.group.photogroup;

import com.photogroup.model.group.GroupByType;

public class PhotoFileModel {

    @GroupByType
    private String fileName;

    @GroupByType(primaryKey = true, index = true)
    private String filePath;

    @GroupByType
    private String dateTaken;

    @GroupByType
    private String lastModified;

    @GroupByType
    private Double[] position;

    private byte[] thumbnailData;

    @GroupByType
    private String baiduAddress;

    @GroupByType
    private Double[] baiduCoordinate;

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getDateTaken() {
        return this.dateTaken;
    }

    public void setDateTaken(String dateTaken) {
        this.dateTaken = dateTaken;
    }

    public String getLastModified() {
        return this.lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public Double[] getPosition() {
        return this.position;
    }

    public void setPosition(Double[] position) {
        this.position = position;
    }

    public byte[] getThumbnailData() {
        return this.thumbnailData;
    }

    public void setThumbnailData(byte[] thumbnailData) {
        this.thumbnailData = thumbnailData;
    }

    public String getBaiduAddress() {
        return this.baiduAddress;
    }

    public void setBaiduAddress(String baiduAddress) {
        this.baiduAddress = baiduAddress;
    }

    public Double[] getBaiduCoordinate() {
        return this.baiduCoordinate;
    }

    public void setBaiduCoordinate(Double[] baiduCoordinate) {
        this.baiduCoordinate = baiduCoordinate;
    }

    @Override
    public String toString() {
        return this.fileName + ":" + this.filePath;
    }
}
