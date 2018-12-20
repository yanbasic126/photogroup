package com.photogroup.ui.util;

import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.photogroup.ui.Messages;

public class UIUilt {

    public static JLabel createSimpleLinkLabel(String text, String url) {
        JLabel labelWithLink = new JLabel(Messages.getString("UIUilt.linkheader") + text + Messages.getString("UIUilt.linkfooter")); //$NON-NLS-1$ //$NON-NLS-2$
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
}
