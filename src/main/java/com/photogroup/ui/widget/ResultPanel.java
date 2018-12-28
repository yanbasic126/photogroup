package com.photogroup.ui.widget;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JPanel;

import com.photogroup.ui.browser.GroupBrowserHelper;
import com.photogroup.ui.util.ResourceUtil;
import com.photogroup.ui.util.UIUilt;

public class ResultPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private HashMap<String, List<File>> groupResult;

    private List<PhotoGroupPanel> groupPanelList = new ArrayList<PhotoGroupPanel>();

    private List<ImagePanel> imagePanelsList = new ArrayList<ImagePanel>();

    private List<ImagePanel> imagePanelsSelectionList = new ArrayList<ImagePanel>();

    public ResultPanel() {
        setBackground(Color.WHITE);
        setLayout(UIUilt.createGridBagLayout(new int[] { 0 }, new int[] { 0 }, new double[] { 1.0 }, new double[] { 0.0, 1.0 }));
    }

    public void setResultData(HashMap<String, List<File>> groupResult) {
        this.groupResult = groupResult;
    }

    public void updateResultPanel(boolean removeAll) {
        if (removeAll) {
            removeAll();
        }
        final List<String> sortTitles = GroupBrowserHelper.getGroupTitlesByDate(groupResult);
        for (String title : sortTitles) {
            PhotoGroupPanel groupPanel = new PhotoGroupPanel(this, title, groupResult.get(title));
            groupPanelList.add(groupPanel);
            imagePanelsList.addAll(groupPanel.getImagePanelList());
            add(groupPanel, UIUilt.createGridBagConstraints(GridBagConstraints.HORIZONTAL, GridBagConstraints.NORTH,
                    new Insets(0, 0, 5, 0), 0, -1));
        }
        updateLayout();
    }

    public void collapseAll() {
        for (PhotoGroupPanel groupPanel : groupPanelList) {
            groupPanel.collapse();
        }
    }

    public void expandAll() {
        for (PhotoGroupPanel groupPanel : groupPanelList) {
            groupPanel.expand();
        }
    }

    private void updateLayout() {
        double[] rowWeights;

        if (groupResult.size() > 1) {
            rowWeights = new double[groupResult.size()];
            for (int i = 0; i < rowWeights.length - 1; i++) {
                rowWeights[i] = 0.0;
            }
            rowWeights[rowWeights.length - 1] = 1.0;
        } else {
            rowWeights = new double[] { 1.0 };
        }

        GridBagLayout panelGroupAllGridBagLayout = (GridBagLayout) getLayout();
        panelGroupAllGridBagLayout.rowWeights = rowWeights;
        setVisible(false);
        setVisible(true);
    }

    public void selectionChanged(ImagePanel panel, boolean controlDown, boolean shiftDown) {
        if (controlDown) {
            if (imagePanelsSelectionList.contains(panel)) {
                imagePanelsSelectionList.remove(panel);
                panel.setBorder(ResourceUtil.IMAGE_PANEL_BORDER);
            } else {
                imagePanelsSelectionList.add(panel);
                panel.setBorder(ResourceUtil.IMAGE_PANEL_SELECTED_BORDER);
            }
        } else {
            for (ImagePanel p : imagePanelsSelectionList) {
                p.setBorder(ResourceUtil.IMAGE_PANEL_BORDER);
            }
            imagePanelsSelectionList.clear();
            imagePanelsSelectionList.add(panel);
            panel.setBorder(ResourceUtil.IMAGE_PANEL_SELECTED_BORDER);
        }
        System.out.println(imagePanelsSelectionList.size());
    }
}
