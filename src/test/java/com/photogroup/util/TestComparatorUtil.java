package com.photogroup.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestComparatorUtil {

    @Test
    public void testFindDateString() {
        assertEquals(ComparatorUtil.findDateString("2018.8.9中央工艺美术学院"), "2018.8.9");
    }
}
