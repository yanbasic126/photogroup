package com.photogroup.ui.widget;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

import com.drew.imaging.ImageProcessingException;
import com.photogroup.groupby.metadata.MetadataReader;
import com.photogroup.ui.SettingStore;
import com.photogroup.ui.layout.WrapLayout;
import com.photogroup.ui.util.UIUilt;

public class FileListAccessory extends JPanel implements PropertyChangeListener {

    private static final int PREVIEW_SIZE = 64;

    private static final Map<String, ImageIcon> iconCache = new HashMap<String, ImageIcon>();

    private JPanel panelFlow;

    private JFileChooser chooser;

    private JCheckBox chckbxPreview;

    public FileListAccessory(JFileChooser chooser) {

        this.chooser = chooser;
        setBorder(new LineBorder(UIManager.getColor("activeCaptionBorder")));

        setLayout(UIUilt.createGridBagLayout(new int[] { 0, 0 }, new int[] { 0, 0, 1 }, new double[] { 1.0, Double.MIN_VALUE },
                new double[] { 0.0, 1.0, Double.MIN_VALUE }));

        chooser.addPropertyChangeListener(this);

        setPreferredSize(new Dimension((PREVIEW_SIZE + 2) * 4, 0));

        JPanel panelPreview = new JPanel();
        add(panelPreview, UIUilt.createGridBagConstraints(-1, -1, new Insets(0, 0, 5, 5), 0, 0));

        panelPreview.setLayout(UIUilt.createGridBagLayout(new int[] { 0, 0, 0 }, new int[] { 0, 0 },
                new double[] { 0.0, 0.0, Double.MIN_VALUE }, new double[] { 0.0, Double.MIN_VALUE }));

        JLabel labelhead = new JLabel("Content Preview");
        panelPreview.add(labelhead, UIUilt.createGridBagConstraints(-1, -1, new Insets(0, 0, 5, 5), 0, 0));

        chckbxPreview = new JCheckBox();
        panelPreview.add(chckbxPreview, UIUilt.createGridBagConstraints(-1, -1, null, 1, 0));
        chckbxPreview.setSelected(true);

        panelFlow = new JPanel();
        WrapLayout wl_panelFlow = new WrapLayout();
        wl_panelFlow.setAlignment(FlowLayout.LEFT);
        wl_panelFlow.setHgap(1);
        wl_panelFlow.setVgap(1);
        panelFlow.setLayout(wl_panelFlow);
        // panelFlow.setBorder(new LineBorder(UIManager.getColor("activeCaptionBorder")));
        panelFlow.setBackground(Color.WHITE);
        add(panelFlow, UIUilt.createGridBagConstraints(GridBagConstraints.BOTH, -1, null, 0, 1));
    }

    public void propertyChange(PropertyChangeEvent e) {
        if (chckbxPreview != null && chckbxPreview.isSelected()) {

            String prop = e.getPropertyName();

            if (JFileChooser.DIRECTORY_CHANGED_PROPERTY.equals(prop)
                    || JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(prop)) {
                File file = (File) e.getNewValue();
                if (file != null && file.isDirectory()) {
                    panelFlow.removeAll();
                    int maxShow = (int) (Math.ceil(chooser.getHeight() / PREVIEW_SIZE) * 4);
                    int i = 0;
                    for (File f : file.listFiles()) {
                        if (i > maxShow) {
                            break;
                        }
                        // if (f.isFile()) {
                        addImagePanel(panelFlow, f);
                        i++;
                        // }

                    }
                    panelFlow.setVisible(false);
                    panelFlow.setVisible(true);
                }
            }
        }
    }

    private void addImagePanel(JPanel panelFlow, File file) {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(UIUilt.createGridBagLayout(new int[] { 0 }, new int[] { 23, 23 }, new double[] { 0.0 },
                new double[] { 0.0, 0.0 }));
        panelFlow.add(panel);

        JLabel labelImg = new JLabel();
        // labelImg.setBorder(new EmptyBorder(0, 0, 0, 0));
        labelImg.setPreferredSize(new Dimension(PREVIEW_SIZE, PREVIEW_SIZE));

        BufferedImage bufferedImage = null;
        if (file.isFile()) {
            byte[] thumbnailData = null;
            if (SettingStore.getSettingStore().isUseThumbnail()) {
                try {
                    thumbnailData = MetadataReader.thumbnailData(file);
                } catch (ImageProcessingException | IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                if (thumbnailData != null) {
                    InputStream thumbByteStream = new ByteArrayInputStream(thumbnailData);
                    bufferedImage = ImageIO.read(thumbByteStream);
                }
                // else {
                // bufferedImage = ImageIO.read(photo);
                // }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (bufferedImage != null) {
            ImageIcon icon = new ImageIcon(resizeImageToPreviewIPhone(bufferedImage));
            labelImg.setIcon(icon);
        } else {
            String ext = file.isDirectory() ? "dir" : file.getName();
            if (file.getName().lastIndexOf('.') > 0) {
                ext = file.getName().substring(file.getName().lastIndexOf('.'));
            }
            ImageIcon imageIcon = iconCache.get(ext);
            if (imageIcon == null || ext.equals(".lnk") || ext.equals(".exe")) {

                BufferedImage scaleImage = new BufferedImage(PREVIEW_SIZE, PREVIEW_SIZE, Image.SCALE_FAST);
                ImageIcon sysIcon = (ImageIcon) chooser.getFileSystemView().getSystemIcon(file);
                scaleImage.getGraphics().drawImage(((ImageIcon) chooser.getFileSystemView().getSystemIcon(file)).getImage(),
                        (PREVIEW_SIZE - sysIcon.getIconWidth()) / 2, (PREVIEW_SIZE - sysIcon.getIconHeight()) / 2, null);
                imageIcon = new ImageIcon(scaleImage);
                iconCache.put(ext, imageIcon);
            }
            labelImg.setIcon(imageIcon);
        }
        panel.add(labelImg, UIUilt.createGridBagConstraints(-1, GridBagConstraints.NORTHWEST, null, 0, 0));

        JLabel labelName = new JLabel(file.getName());
        // labelName.setBorder(new EmptyBorder(0, 0, 0, 0));
        labelName.setToolTipText(file.getName());
        // int height = labelName.getHeight();
        labelName.setPreferredSize(new Dimension(PREVIEW_SIZE, 20));
        panel.add(labelName, UIUilt.createGridBagConstraints(-1, GridBagConstraints.NORTHWEST, null, 0, 1));

    }

    private BufferedImage resizeImageToPreviewIPhone(BufferedImage originalImage) {
        int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        int size = (width <= height) ? width : height;
        int x = (width > height) ? (width - height) / 2 : 0;
        int y = (height > width) ? (height - width) / 2 : 0;

        BufferedImage resizedImage = new BufferedImage(PREVIEW_SIZE, PREVIEW_SIZE, type);
        Graphics2D g = resizedImage.createGraphics();
        // g.drawImage(originalImage, x, y, size, size, null);
        g.drawImage(originalImage, 0, 0, PREVIEW_SIZE, PREVIEW_SIZE, x, y, size, size, null);
        g.dispose();
        return resizedImage;
    }
}