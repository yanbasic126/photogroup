package com.photogroup.groupby.position;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.photogroup.ui.SettingStore;
import com.photogroup.util.FileUtilForTest;

public class TestPostionHelper {

    @Before
    public void before() {
        SettingStore.getSettingStore().setBaiduKey("1607e140964c4974ddfd87286ae9d6b7");
    }

    @After
    public void after() {
        SettingStore.getSettingStore().setBaiduKey("");
    }

    @Test
    public void testQueryPostion1053() throws Exception {
        assertEquals(getBaiduAddress("image/IMG_1053.JPG"), "王府井银泰in88");
    }

    @Test
    public void testQueryPostion1210() throws Exception {
        assertEquals(getBaiduAddress("image/IMG_1210.JPG"), "光明楼小区");
    }

    @Test
    public void testQueryPostion1240() throws Exception {
        assertEquals(getBaiduAddress("image/IMG_1240.JPG"), "南新园社区");
    }

    @Test
    public void testQueryPostion1197() throws Exception {
        assertEquals(getBaiduAddress("image/IMG_1197.JPG"), "公司");
    }

    @Test
    public void testQueryPostion1188() throws Exception {
        assertEquals(getBaiduAddress("image/IMG_1188.JPG"), "北京王府井天主教堂");
    }

    public static String getBaiduAddress(String imagePath) throws Exception {
        File image = FileUtilForTest.createTempFileFromResource(imagePath);
        return PostionHelper.queryPostion(image);
    }

}
