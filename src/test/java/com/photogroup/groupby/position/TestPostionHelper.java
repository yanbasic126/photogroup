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


    public static String getBaiduAddress(String imagePath) throws Exception {
        File image = FileUtilForTest.createTempFileFromResource(imagePath);
        return PostionHelper.queryPostion(image);
    }

}
