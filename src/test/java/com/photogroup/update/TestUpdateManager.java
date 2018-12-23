package com.photogroup.update;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TestUpdateManager {

    @Test
    public void testUpdateManager() {
        UpdateManager updateManager = new UpdateManager();
        assertTrue(updateManager.getFilename().startsWith("lemonphoto"));
    }
}
