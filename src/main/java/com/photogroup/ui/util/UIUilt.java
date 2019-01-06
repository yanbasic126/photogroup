package com.photogroup.ui.util;

import java.awt.Desktop;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.photogroup.ui.Messages;

public class UIUilt {

    public static JLabel createSimpleLinkLabel(String text, String url) {
        JLabel labelWithLink = new JLabel(
                Messages.getString("UIUilt.linkheader") + text + Messages.getString("UIUilt.linkfooter")); //$NON-NLS-1$ //$NON-NLS-2$
        labelWithLink.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                try {
                    int dialog = JOptionPane.showConfirmDialog(labelWithLink,
                            Messages.getString("SettingDialog.dlg_open_web") + url, //$NON-NLS-1$
                            Messages.getString("SettingDialog.dlg_warn"), //$NON-NLS-1$
                            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    if (dialog == 0) {
                        Desktop.getDesktop().browse(new URI(url));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        return labelWithLink;
    }

    public static JLabel createSimpleLinkLabel(String text, MouseAdapter adapter) {
        JLabel labelWithLink = new JLabel(
                Messages.getString("UIUilt.linkheader") + text + Messages.getString("UIUilt.linkfooter")); //$NON-NLS-1$ //$NON-NLS-2$
        labelWithLink.addMouseListener(adapter);
        return labelWithLink;
    }

    public static GridBagConstraints createGridBagConstraints(int fill, int anchor, Insets insets, int gridx, int gridy) {
        GridBagConstraints gbc_panelGroup = new GridBagConstraints();
        if (fill != -1) {
            gbc_panelGroup.fill = fill;
        }
        if (anchor != -1) {
            gbc_panelGroup.anchor = anchor;
        }
        if (insets != null) {
            gbc_panelGroup.insets = insets;
        }
        if (gridx != -1) {
            gbc_panelGroup.gridx = gridx;
        }
        if (gridy != -1) {
            gbc_panelGroup.gridy = gridy;
        }

        return gbc_panelGroup;

    }

    public static GridBagLayout createGridBagLayout(int[] columnWidths, int[] rowHeights, double[] columnWeights,
            double[] rowWeights) {
        GridBagLayout gbl_panelGroup = new GridBagLayout();
        gbl_panelGroup.columnWidths = columnWidths;
        gbl_panelGroup.rowHeights = rowHeights;
        gbl_panelGroup.columnWeights = columnWeights;
        gbl_panelGroup.rowWeights = rowWeights;

        return gbl_panelGroup;
    }
}
