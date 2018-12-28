package com.photogroup.ui.widget;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import com.drew.imaging.ImageProcessingException;
import com.photogroup.groupby.metadata.MetadataReader;
import com.photogroup.ui.SettingStore;
import com.photogroup.ui.browser.GroupBrowserHelper;
import com.photogroup.ui.layout.WrapLayout;
import com.photogroup.ui.util.ResourceUtil;
import com.photogroup.ui.util.UIUilt;

public class PhotoGroupPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final int PHOTO_GAP = 1;

    private static final int PREVIEW_SIZE = 168;

    private static final WrapLayout WRAP_LAYOUT_FLOW = new WrapLayout();

    private JPanel panelFlow;

    private JButton btnExpand;

    private Map<JTextField, String> textFieldTitleMap = new HashMap<JTextField, String>();

    private ResultPanel resultPanel;

    private List<ImagePanel> imagePanelList = new ArrayList<ImagePanel>();

    private void init() {
        createWrapLayout();
    }

    private void createWrapLayout() {
        WRAP_LAYOUT_FLOW.setAlignment(FlowLayout.LEFT);
        WRAP_LAYOUT_FLOW.setHgap(PHOTO_GAP);
        WRAP_LAYOUT_FLOW.setVgap(PHOTO_GAP);
    }

    public PhotoGroupPanel(ResultPanel resultPanel, String title, List<File> files) {
        this.resultPanel = resultPanel;
        init();
        setLayout(UIUilt.createGridBagLayout(new int[] { 0, 0 }, new int[] { 0, 0 }, new double[] { 1.0, Double.MIN_VALUE },
                new double[] { 0.0, 0.0 }));
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);

        add(toolBar, UIUilt.createGridBagConstraints(GridBagConstraints.HORIZONTAL, -1, null, 0, 0));

        JButton btnExpand = new JButton();
        btnExpand.setIcon(ResourceUtil.upIcon);
        toolBar.add(btnExpand);

        JTextField textFieldTitle = new JTextField(title);
        toolBar.add(textFieldTitle);
        textFieldTitle.setBorder(ResourceUtil.EMPTY_BORDER);
        textFieldTitleMap.put(textFieldTitle, title);

        JPanel panelFlow = new JPanel();
        panelFlow.setBorder(ResourceUtil.EMPTY_BORDER);
        panelFlow.setLayout(WRAP_LAYOUT_FLOW);
        // panelFlow.setBorder(new LineBorder(new Color(0, 0, 0)));
        panelFlow.setBackground(Color.WHITE);
        // FlowLayout flowLayout = (FlowLayout) panelFlow.getLayout();

        add(panelFlow, UIUilt.createGridBagConstraints(GridBagConstraints.HORIZONTAL, -1, null, 0, 1));

        btnExpand.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (panelFlow.isVisible()) {
                    btnExpand.setIcon(ResourceUtil.downIcon);
                    panelFlow.setVisible(false);
                } else {
                    btnExpand.setIcon(ResourceUtil.upIcon);
                    panelFlow.setVisible(true);
                }
            }
        });

        this.panelFlow = panelFlow;
        this.btnExpand = btnExpand;

        for (File photo : files) {
            addImagePanel(panelFlow, photo);
        }
    }

    private void addImagePanel(JPanel panelFlow, File photo) {
        ImagePanel panel = new ImagePanel(photo);
        imagePanelList.add(panel);

        panel.setBorder(ResourceUtil.IMAGE_PANEL_BORDER);
        panel.setLayout(UIUilt.createGridBagLayout(new int[] { 0 }, new int[] { 23, 23 }, new double[] { 0.0 },
                new double[] { 0.0, 0.0 }));
        panelFlow.add(panel);

        JLabel labelImg = new JLabel();
        // labelImg.setBorder(new EmptyBorder(0, 0, 0, 0));
        labelImg.setPreferredSize(new Dimension(PREVIEW_SIZE, PREVIEW_SIZE));
        BufferedImage bufferedImage = null;
        byte[] thumbnailData = null;
        if (SettingStore.getSettingStore().isUseThumbnail()) {
            try {
                thumbnailData = MetadataReader.thumbnailData(photo);
            } catch (ImageProcessingException | IOException e) {
                e.printStackTrace();
            }
        }
        try {
            if (thumbnailData != null) {
                InputStream thumbByteStream = new ByteArrayInputStream(thumbnailData);
                bufferedImage = ImageIO.read(thumbByteStream);
            } else {
                bufferedImage = ImageIO.read(photo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        boolean buffered = true;
        if (bufferedImage != null) {
            ImageIcon icon = new ImageIcon(GroupBrowserHelper.resizeImageToPreviewIPhone(bufferedImage, PREVIEW_SIZE));
            labelImg.setIcon(icon);
        } else {
            buffered = false;
            labelImg.setIcon(ResourceUtil.documentEmptyImageIcon);
        }
        final boolean isImage = buffered;
        panel.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == 1 && e.getClickCount() == 1) {
                    // if (panelSelectedImage != null) {
                    // panelSelectedImage.setBorder(IMAGE_PANEL_BORDER);
                    // }
                    // panel.setBorder(IMAGE_PANEL_SELECTED_BORDER);
                    // panelSelectedImage = panel;
                    resultPanel.selectionChanged(panel, e.isControlDown(), e.isShiftDown());
                } else if (e.getButton() == 1 && e.getClickCount() == 2) {
                    GroupBrowserHelper.openToPreview(photo, isImage);
                } else if (e.getButton() == 2 && e.getClickCount() == 1) {
                    GroupBrowserHelper.openToPreview(photo, isImage);
                }
            }
        });
        panel.add(labelImg, UIUilt.createGridBagConstraints(-1, GridBagConstraints.NORTHWEST, null, 0, 0));

        JLabel labelName = new JLabel(photo.getName());
        // labelName.setBorder(new EmptyBorder(0, 0, 0, 0));
        labelName.setToolTipText(photo.getName());
        // int height = labelName.getHeight();
        labelName.setPreferredSize(new Dimension(PREVIEW_SIZE, 20));
        panel.add(labelName, UIUilt.createGridBagConstraints(-1, GridBagConstraints.NORTHWEST, null, 0, 1));
    }

    public void collapse() {
        btnExpand.setIcon(ResourceUtil.downIcon);
        panelFlow.setVisible(false);
    }

    public void expand() {
        btnExpand.setIcon(ResourceUtil.upIcon);
        panelFlow.setVisible(true);
    }

    public List<ImagePanel> getImagePanelList() {
        return imagePanelList;
    }
}
