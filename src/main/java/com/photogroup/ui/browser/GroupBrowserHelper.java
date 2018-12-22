package com.photogroup.ui.browser;

import java.awt.Desktop;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JOptionPane;

import com.photogroup.exception.ExceptionHandler;
import com.photogroup.ui.Messages;
import com.photogroup.ui.dialog.ViewerDialog;
import com.photogroup.util.ComparatorUtil;

public class GroupBrowserHelper {

    public static BufferedImage resizeImageToPreviewIPhone(BufferedImage originalImage, int imageSize) {
        int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        int size = (width <= height) ? width : height;
        int x = (width > height) ? (width - height) / 2 : 0;
        int y = (height > width) ? (height - width) / 2 : 0;

        BufferedImage resizedImage = new BufferedImage(imageSize, imageSize, type);
        Graphics2D g = resizedImage.createGraphics();
        // g.drawImage(originalImage, x, y, size, size, null);
        g.drawImage(originalImage, 0, 0, imageSize, imageSize, x, y, size, size, null);
        g.dispose();
        return resizedImage;
    }

    public static void openToPreview(File file, boolean isImage) {
        if (isImage) {
            ViewerDialog dialog = new ViewerDialog(file);
            dialog.setLocationByPlatform(true);
            dialog.setVisible(true);
        } else {
            try {
                Desktop.getDesktop().open(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static List<String> getGroupTitlesByDate(HashMap<String, List<File>> photoGroup) {
        final List<String> sortTitles = new ArrayList<String>();

        Iterator<?> it = photoGroup.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, List<File>> pair = (Entry<String, List<File>>) it.next();
            sortTitles.add(pair.getKey());
        }

        Collections.sort(sortTitles, ComparatorUtil.DATE_TITLE_COMPARATOR);
        return sortTitles;
    }

    public static String checkFolder(String folderName) {
        String errorMsg = null;
        if (folderName.isEmpty()) {
            errorMsg = Messages.getString("GroupBrowser.check_folder_err"); //$NON-NLS-1$
        } else {
            if (!new File(folderName).exists()) {
                errorMsg = Messages.getString("GroupBrowser.err_folder_1") + folderName //$NON-NLS-1$
                        + Messages.getString("GroupBrowser.err_folder_2"); //$NON-NLS-1$
            }
        }
        return errorMsg;
    }

    public static void openFolder(String folderName) {
        try {
            String checkFolder = GroupBrowserHelper.checkFolder(folderName);
            if (checkFolder == null) {
                File folder = new File(folderName);
                Desktop.getDesktop().open(folder);
            } else {
                JOptionPane.showMessageDialog(null, checkFolder, Messages.getString("GroupBrowser.folder"), //$NON-NLS-1$
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            ExceptionHandler.logError(ex.getMessage());
        }
    }

}
