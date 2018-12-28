package com.photogroup.util;

import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ImageUtil {

    public static ImageIcon getImageFromSystemResource(String name) {
        try {
            return new ImageIcon(ImageIO.read(ClassLoader.getSystemResource(name)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
