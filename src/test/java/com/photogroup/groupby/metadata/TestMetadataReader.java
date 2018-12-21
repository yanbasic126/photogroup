package com.photogroup.groupby.metadata;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

import com.photogroup.util.FileUtilForTest;

public class TestMetadataReader {

    @Test
    public void testGPS() throws Exception {
        Double[] gps1 = MetadataReader.gps(FileUtilForTest.createTempFileFromResource("image/IMG_1240.JPG"));
        Double[] gps2 = new Double[] { 39.866572222222224, 116.47231388888889 };
        assertArrayEquals(gps1, gps2);
    }
}
