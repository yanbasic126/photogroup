package com.photogroup.ui.widget;

import java.io.File;

import javax.swing.JPanel;

public class ImagePanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private File file;

    public ImagePanel(File file) {
        this.file = file;
    }

    public File getFile() {
        return this.file;
    }
}
