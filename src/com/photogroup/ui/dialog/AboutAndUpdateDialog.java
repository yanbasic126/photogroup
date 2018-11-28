package com.photogroup.ui.dialog;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.photogroup.update.UpdateManager;
import com.photogroup.util.FileUtil;

public class AboutAndUpdateDialog extends JDialog {

    private final JPanel contentPanel = new JPanel();

    private UpdateManager updateManager;

    private JButton btnUpdateAndDownload;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            AboutAndUpdateDialog dialog = new AboutAndUpdateDialog();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the dialog.
     */
    public AboutAndUpdateDialog() {
        setTitle("About Lemon Photo");
        setIconImage(Toolkit.getDefaultToolkit().getImage(AboutAndUpdateDialog.class.getResource("/icon/lemon_16.png")));
        setModal(true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 432, 0 };
        gridBagLayout.rowHeights = new int[] { 228, 25, 0, 0 };
        gridBagLayout.columnWeights = new double[] { 0.0, Double.MIN_VALUE };
        gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
        getContentPane().setLayout(gridBagLayout);
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        GridBagConstraints gbc_contentPanel = new GridBagConstraints();
        gbc_contentPanel.fill = GridBagConstraints.BOTH;
        gbc_contentPanel.insets = new Insets(0, 0, 5, 0);
        gbc_contentPanel.gridx = 0;
        gbc_contentPanel.gridy = 0;
        getContentPane().add(contentPanel, gbc_contentPanel);
        GridBagLayout gbl_contentPanel = new GridBagLayout();
        gbl_contentPanel.columnWidths = new int[] { 0, 0 };
        gbl_contentPanel.rowHeights = new int[] { 0, 0 };
        gbl_contentPanel.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
        gbl_contentPanel.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
        contentPanel.setLayout(gbl_contentPanel);
        {
            JPanel panelParent = new JPanel();
            GridBagConstraints gbc_panelParent = new GridBagConstraints();
            gbc_panelParent.fill = GridBagConstraints.BOTH;
            gbc_panelParent.gridx = 0;
            gbc_panelParent.gridy = 0;
            contentPanel.add(panelParent, gbc_panelParent);
            GridBagLayout gbl_panelParent = new GridBagLayout();
            gbl_panelParent.columnWidths = new int[] { 0, 0, 0, 0, 0, 0 };
            gbl_panelParent.rowHeights = new int[] { 0, 0, 0 };
            gbl_panelParent.columnWeights = new double[] { 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE };
            gbl_panelParent.rowWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
            panelParent.setLayout(gbl_panelParent);
            {
                JLabel label = new JLabel("        ");
                GridBagConstraints gbc_label = new GridBagConstraints();
                gbc_label.insets = new Insets(0, 0, 5, 5);
                gbc_label.gridx = 0;
                gbc_label.gridy = 0;
                panelParent.add(label, gbc_label);
            }
            {
                JLabel label = new JLabel("        ");
                GridBagConstraints gbc_label = new GridBagConstraints();
                gbc_label.insets = new Insets(0, 0, 0, 5);
                gbc_label.gridx = 0;
                gbc_label.gridy = 1;
                panelParent.add(label, gbc_label);
            }
            {
                JPanel panelImage = new JPanel();
                GridBagConstraints gbc_panelImage = new GridBagConstraints();
                gbc_panelImage.insets = new Insets(0, 0, 0, 5);
                gbc_panelImage.fill = GridBagConstraints.BOTH;
                gbc_panelImage.gridx = 1;
                gbc_panelImage.gridy = 1;
                panelParent.add(panelImage, gbc_panelImage);
                GridBagLayout gbl_panelImage = new GridBagLayout();
                gbl_panelImage.columnWidths = new int[] { 32, 0 };
                gbl_panelImage.rowHeights = new int[] { 32, 0 };
                gbl_panelImage.columnWeights = new double[] { 0.0, Double.MIN_VALUE };
                gbl_panelImage.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
                panelImage.setLayout(gbl_panelImage);
                {
                    JLabel lblNewLabel = new JLabel("");
                    lblNewLabel.setIcon(new ImageIcon(AboutAndUpdateDialog.class.getResource("/icon/lemon_32.png")));
                    GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
                    gbc_lblNewLabel.anchor = GridBagConstraints.NORTHWEST;
                    gbc_lblNewLabel.gridx = 0;
                    gbc_lblNewLabel.gridy = 0;
                    panelImage.add(lblNewLabel, gbc_lblNewLabel);
                }
            }
            {
                JPanel panelInfo = new JPanel();
                GridBagConstraints gbc_panelInfo = new GridBagConstraints();
                gbc_panelInfo.insets = new Insets(0, 0, 0, 5);
                gbc_panelInfo.fill = GridBagConstraints.BOTH;
                gbc_panelInfo.gridx = 3;
                gbc_panelInfo.gridy = 1;
                panelParent.add(panelInfo, gbc_panelInfo);
                GridBagLayout gbl_panelInfo = new GridBagLayout();
                gbl_panelInfo.columnWidths = new int[] { 0, 0 };
                gbl_panelInfo.rowHeights = new int[] { 0, 0, 0, 0, 0, 0 };
                gbl_panelInfo.columnWeights = new double[] { 0.0, Double.MIN_VALUE };
                gbl_panelInfo.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
                panelInfo.setLayout(gbl_panelInfo);
                {
                    JLabel lblLemonPhoto = new JLabel("Lemon Photo");
                    lblLemonPhoto.setFont(new Font("Tahoma", Font.BOLD, 32));
                    GridBagConstraints gbc_lblLemonPhoto = new GridBagConstraints();
                    gbc_lblLemonPhoto.insets = new Insets(0, 0, 5, 0);
                    gbc_lblLemonPhoto.gridx = 0;
                    gbc_lblLemonPhoto.gridy = 0;
                    panelInfo.add(lblLemonPhoto, gbc_lblLemonPhoto);
                }
                {
                    JLabel lblCurversion = new JLabel("CurVersion");
                    GridBagConstraints gbc_lblCurversion = new GridBagConstraints();
                    gbc_lblCurversion.insets = new Insets(0, 0, 5, 0);
                    gbc_lblCurversion.gridx = 0;
                    gbc_lblCurversion.gridy = 1;
                    panelInfo.add(lblCurversion, gbc_lblCurversion);
                    lblCurversion.setText(FileUtil.getBuildVersion());
                }
                {
                    btnUpdateAndDownload = new JButton("Checking Update");
                    GridBagConstraints gbc_btnDownload = new GridBagConstraints();
                    gbc_btnDownload.insets = new Insets(0, 0, 5, 0);
                    gbc_btnDownload.gridx = 0;
                    gbc_btnDownload.gridy = 2;
                    panelInfo.add(btnUpdateAndDownload, gbc_btnDownload);
                    btnUpdateAndDownload.setEnabled(false);
                    btnUpdateAndDownload.addActionListener(new ActionListener() {

                        public void actionPerformed(ActionEvent e) {
                            JFileChooser chooser = new JFileChooser();
                            // chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
                            chooser.setSelectedFile(new File(
                                    System.getProperty("user.dir") + "/lemonphoto-" + updateManager.getLatestVersion() + ".jar"));
                            chooser.setDialogTitle("Download"); //$NON-NLS-1$
                            chooser.setDialogType(JFileChooser.SAVE_DIALOG);
                            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                            chooser.setAcceptAllFileFilterUsed(false);
                            if (chooser.showOpenDialog(contentPanel) == JFileChooser.APPROVE_OPTION) {
                                new Thread(new UpdateDownloader(chooser.getSelectedFile().getAbsolutePath())).start();
                            }
                        }
                    });
                }
                {
                    JLabel lblInformation = new JLabel("Move photos by taken date and address.");
                    GridBagConstraints gbc_lblInformation = new GridBagConstraints();
                    gbc_lblInformation.insets = new Insets(0, 0, 5, 0);
                    gbc_lblInformation.anchor = GridBagConstraints.WEST;
                    gbc_lblInformation.gridx = 0;
                    gbc_lblInformation.gridy = 3;
                    panelInfo.add(lblInformation, gbc_lblInformation);
                }
                {
                    JLabel lblUrl = new JLabel("https://github.com/yanbasic126/photogroup");
                    GridBagConstraints gbc_lblUrl = new GridBagConstraints();
                    gbc_lblUrl.gridx = 0;
                    gbc_lblUrl.gridy = 4;
                    panelInfo.add(lblUrl, gbc_lblUrl);
                }
            }
        }
        {
            JPanel buttonPane = new JPanel();
            GridBagConstraints gbc_buttonPane = new GridBagConstraints();
            gbc_buttonPane.insets = new Insets(0, 0, 5, 0);
            gbc_buttonPane.anchor = GridBagConstraints.NORTH;
            gbc_buttonPane.fill = GridBagConstraints.HORIZONTAL;
            gbc_buttonPane.gridx = 0;
            gbc_buttonPane.gridy = 1;
            getContentPane().add(buttonPane, gbc_buttonPane);
            GridBagLayout gbl_buttonPane = new GridBagLayout();
            gbl_buttonPane.columnWidths = new int[] { 53, 0 };
            gbl_buttonPane.rowHeights = new int[] { 25, 0 };
            gbl_buttonPane.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
            gbl_buttonPane.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
            buttonPane.setLayout(gbl_buttonPane);
            {
                JButton okButton = new JButton("OK");
                okButton.setActionCommand("OK");
                GridBagConstraints gbc_okButton = new GridBagConstraints();
                gbc_okButton.anchor = GridBagConstraints.NORTH;
                gbc_okButton.gridx = 0;
                gbc_okButton.gridy = 0;
                buttonPane.add(okButton, gbc_okButton);
                getRootPane().setDefaultButton(okButton);
            }
        }

        new Thread(new UpdateChecker()).start();
    }

    class UpdateChecker implements Runnable {

        @Override
        public void run() {
            if (updateManager == null) {
                updateManager = new UpdateManager();
            }
            String latestVersion = updateManager.getLatestVersion();
            if (FileUtil.getBuildVersion().compareTo(latestVersion) < 0) {
                btnUpdateAndDownload.setText(latestVersion);
                btnUpdateAndDownload.setEnabled(true);
            } else {
                btnUpdateAndDownload.setText("Up to Date");
            }
        }
    }

    class UpdateDownloader implements Runnable {

        private String targetPath;

        public UpdateDownloader(String targetPath) {
            this.targetPath = targetPath;
        }

        @Override
        public void run() {
            if (updateManager == null) {
                return;
            }
            String text = btnUpdateAndDownload.getText();
            btnUpdateAndDownload.setEnabled(false);
            btnUpdateAndDownload.setText("Downloading...");
            try {
                FileUtil.download(updateManager.getDownloadURL(), targetPath);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                btnUpdateAndDownload.setEnabled(true);
                btnUpdateAndDownload.setText(text);
            }
        }
    }
}
