package com.photogroup.ui.widget;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;

public class ImageTransferable implements Transferable {

    private File file;

    public ImageTransferable(File file) {
        this.file = file;
    }

    public File getFile() {
        return this.file;
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[] {};
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return false;
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        // TODO Auto-generated method stub
        return this.file;
    }

}
