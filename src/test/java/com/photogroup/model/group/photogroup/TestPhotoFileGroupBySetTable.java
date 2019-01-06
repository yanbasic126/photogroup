package com.photogroup.model.group.photogroup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TestPhotoFileGroupBySetTable {

    @Test
    public void testGroupBy() throws Exception {
        PhotoFileGroupBySetTable table = new PhotoFileGroupBySetTable();

        String i = "";
        PhotoFileModel pfm_1 = new PhotoFileModel();
        pfm_1.setFileName("pfm_1" + i);
        pfm_1.setFilePath("filePath_test_1(中文）");
        pfm_1.setBaiduAddress("测试中文地址ABC");
        pfm_1.setDateTaken("2019-1-1");
        table.insert(pfm_1);

        PhotoFileModel pfm_2 = new PhotoFileModel();
        pfm_2.setFileName("pfm_2" + i);
        pfm_2.setFilePath("filePath_test_2(中文）");
        pfm_2.setBaiduAddress("测试中文地址XYZ");
        pfm_2.setDateTaken("2019-1-1");
        table.insert(pfm_2);

        PhotoFileModel pfm_3 = new PhotoFileModel();
        pfm_3.setFileName("pfm_3" + i);
        pfm_3.setFilePath("filePath_test_3(中文）");
        pfm_3.setBaiduAddress("测试中文地址XYZ");
        pfm_3.setDateTaken("2019-1-2");
        table.insert(pfm_3);

        assertEquals(2, table.groupBy("dateTaken").get("2019-1-1").size());
        assertEquals(1, table.groupBy("dateTaken").get("2019-1-2").size());
        assertEquals(2, table.groupBy("baiduAddress").get("测试中文地址XYZ").size());
        assertEquals(1, table.groupBy("baiduAddress").get("测试中文地址ABC").size());

        table.delete(pfm_1);
        assertEquals(1, table.groupBy("dateTaken").get("2019-1-1").size());
        assertEquals(1, table.groupBy("dateTaken").get("2019-1-2").size());
        assertEquals(2, table.groupBy("baiduAddress").get("测试中文地址XYZ").size());
        assertNull(table.groupBy("baiduAddress").get("测试中文地址ABC"));
    }
}
