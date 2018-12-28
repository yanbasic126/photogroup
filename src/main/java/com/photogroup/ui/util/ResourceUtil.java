package com.photogroup.ui.util;

import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.photogroup.util.ImageUtil;

public class ResourceUtil {

    public static final int PREVIEW_SIZE = 168;

    public static final EmptyBorder EMPTY_BORDER = new EmptyBorder(0, 0, 0, 0);

    public static final LineBorder IMAGE_PANEL_BORDER = new LineBorder(UIManager.getColor("Panel.background"), 2); //$NON-NLS-1$

    public static final LineBorder IMAGE_PANEL_SELECTED_BORDER = new LineBorder(UIManager.getColor("Table.selectionBackground"), //$NON-NLS-1$
            2);

    public static final ImageIcon downIcon;

    public static final ImageIcon upIcon;

    // public static final ImageIcon renameIcon;

    public static final ImageIcon lemonIcon;

    public static final ImageIcon lemonSmallIcon;

    // public static final ImageIcon debugIcon;

    public static final ImageIcon profileIcon;

    public static final ImageIcon settingIcon;

    public static final ImageIcon saveIcon;

    public static final ImageIcon folderIcon;

    public static final ImageIcon documentEmptyImageIcon;

    static {
        downIcon = ImageUtil.getImageFromSystemResource("icon/down_16.png"); //$NON-NLS-1$
        upIcon = ImageUtil.getImageFromSystemResource("icon/up_16.png"); //$NON-NLS-1$
        // renameIcon = ImageUtil.getImageIconFromSystemResource("icon/Blue_tag.png");
        lemonIcon = ImageUtil.getImageFromSystemResource("icon/lemon_32.png"); //$NON-NLS-1$
        lemonSmallIcon = ImageUtil.getImageFromSystemResource("icon/lemon_16.png"); //$NON-NLS-1$
        // debugIcon = ImageUtil.getImageFromSystemResource("icon/debug_16.png"); //$NON-NLS-1$
        profileIcon = ImageUtil.getImageFromSystemResource("icon/profile_32.png"); //$NON-NLS-1$
        settingIcon = ImageUtil.getImageFromSystemResource("icon/settings_32.png"); //$NON-NLS-1$
        saveIcon = ImageUtil.getImageFromSystemResource("icon/save_32.png"); //$NON-NLS-1$
        folderIcon = ImageUtil.getImageFromSystemResource("icon/folder_16.png"); //$NON-NLS-1$

        BufferedImage scaleImage = new BufferedImage(PREVIEW_SIZE, PREVIEW_SIZE, Image.SCALE_FAST);
        scaleImage.getGraphics().drawImage(ImageUtil.getImageFromSystemResource("icon/file_64.png").getImage(), 52, 52, null); //$NON-NLS-1$
        documentEmptyImageIcon = new ImageIcon(scaleImage);
    }
}
