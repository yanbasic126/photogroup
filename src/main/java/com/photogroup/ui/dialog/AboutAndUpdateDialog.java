package com.photogroup.ui.dialog;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.photogroup.ui.Messages;
import com.photogroup.ui.util.ResourceUtil;
import com.photogroup.ui.util.UIUilt;
import com.photogroup.update.UpdateManager;
import com.photogroup.util.FileUtil;

public class AboutAndUpdateDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    private static final String LICENSE_URL = "https://raw.githubusercontent.com/yanbasic126/photogroup/master/LICENSE";

    private static final String LICENST_TYPE = "License";

    private static final String GIT_REPO = "https://github.com/yanbasic126/photogroup";

    private static final Color LEMON_COLOR = new Color(243, 205, 63);

    private final JPanel contentPanel = new JPanel();

    private UpdateManager updateManager;

    private JButton btnUpdateAndDownload;

    private JLabel labelSize;

    public AboutAndUpdateDialog() {
        setResizable(false);
        setTitle("About " + Messages.getString("GroupBrowser.title"));
        setIconImage(Toolkit.getDefaultToolkit().getImage(AboutAndUpdateDialog.class.getResource("/icon/lemon_16.png")));
        setModal(true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 407, 271);
        getContentPane().setLayout(UIUilt.createGridBagLayout(new int[] { 432, 0 }, new int[] { 228, 0 },
                new double[] { 0.0, Double.MIN_VALUE }, new double[] { 0.0, Double.MIN_VALUE }));
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, UIUilt.createGridBagConstraints(GridBagConstraints.BOTH, -1, null, 0, 0));
        contentPanel.setLayout(UIUilt.createGridBagLayout(new int[] { 0, 0 }, new int[] { 0, 0 },
                new double[] { 1.0, Double.MIN_VALUE }, new double[] { 1.0, Double.MIN_VALUE }));
        {
            JPanel panelParent = new JPanel();
            contentPanel.add(panelParent, UIUilt.createGridBagConstraints(GridBagConstraints.BOTH, -1, null, 0, 0));
            panelParent.setLayout(UIUilt.createGridBagLayout(new int[] { 0, 0, 0, 0, 0 }, new int[] { 0, 0, 0 },
                    new double[] { 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE }, new double[] { 0.0, 1.0, Double.MIN_VALUE }));
            {
                JLabel label = new JLabel("        ");
                panelParent.add(label, UIUilt.createGridBagConstraints(-1, -1, new Insets(0, 0, 5, 5), 0, 0));
            }
            {
                JLabel label = new JLabel("        ");
                panelParent.add(label, UIUilt.createGridBagConstraints(-1, -1, new Insets(0, 0, 0, 5), 0, 1));
            }
            {
                JPanel panelImage = new JPanel();
                panelParent.add(panelImage,
                        UIUilt.createGridBagConstraints(GridBagConstraints.BOTH, -1, new Insets(0, 0, 0, 5), 1, 1));
                panelImage.setLayout(UIUilt.createGridBagLayout(new int[] { 32, 0 }, new int[] { 32, 0 },
                        new double[] { 0.0, Double.MIN_VALUE }, new double[] { 1.0, Double.MIN_VALUE }));
                {
                    JLabel lblNewLabel = new JLabel();
                    lblNewLabel.setIcon(ResourceUtil.lemonIcon);
                    panelImage.add(lblNewLabel, UIUilt.createGridBagConstraints(-1, GridBagConstraints.NORTHWEST, null, 0, 0));
                    lblNewLabel.addMouseListener(new MouseAdapter() {

                        public void mouseClicked(MouseEvent e) {
                            if (lblNewLabel.getLocation().y == 0) {
                                new Thread(new Runnable() {

                                    @Override
                                    public void run() {
                                        double height = lblNewLabel.getParent().getHeight() - 72;
                                        for (double h = 1; h < height; h++) {
                                            lblNewLabel.setLocation(0, (int) h);
                                            int sleep = (int) ((height - h) / 2);
                                            try {
                                                Thread.sleep(sleep);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }).start();
                            }
                        }
                    });
                }
            }
            {
                JPanel panelInfo = new JPanel();
                panelParent.add(panelInfo, UIUilt.createGridBagConstraints(GridBagConstraints.BOTH, -1, null, 3, 1));
                panelInfo.setLayout(UIUilt.createGridBagLayout(new int[] { 0, 0 }, new int[] { 0, 0, 0, 0, 0, 0, 0 },
                        new double[] { 1.0, Double.MIN_VALUE }, new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE }));
                {
                    JLabel lblLemonPhoto = new JLabel(Messages.getString("GroupBrowser.title"));
                    lblLemonPhoto.setForeground(LEMON_COLOR);
                    lblLemonPhoto.setFont(new Font("Tahoma", Font.BOLD, 32));
                    panelInfo.add(lblLemonPhoto, UIUilt.createGridBagConstraints(-1, -1, new Insets(0, 0, 5, 0), 0, 0));
                }
                {
                    JPanel panelDownload = new JPanel();
                    panelInfo.add(panelDownload,
                            UIUilt.createGridBagConstraints(GridBagConstraints.BOTH, -1, new Insets(0, 0, 5, 0), 0, 2));
                    panelDownload.setLayout(UIUilt.createGridBagLayout(new int[] { 79, 127, 12, 0 }, new int[] { 25, 0 },
                            new double[] { 0.0, 0.0, 1.0, Double.MIN_VALUE }, new double[] { 0.0, Double.MIN_VALUE }));
                    {
                        {
                            JLabel lblCurversion = new JLabel("CurVersion");
                            panelDownload.add(lblCurversion,
                                    UIUilt.createGridBagConstraints(-1, -1, new Insets(0, 0, 5, 0), 1, 0));
                            lblCurversion.setText(FileUtil.getBuildVersion());
                        }
                        btnUpdateAndDownload = new JButton("Checking Update");
                        panelDownload.add(btnUpdateAndDownload,
                                UIUilt.createGridBagConstraints(-1, GridBagConstraints.NORTHWEST, new Insets(0, 0, 0, 5), 1, 1));
                        btnUpdateAndDownload.setEnabled(false);
                        btnUpdateAndDownload.addActionListener(new ActionListener() {

                            public void actionPerformed(ActionEvent e) {
                                JFileChooser chooser = new JFileChooser();
                                // chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
                                chooser.setSelectedFile(
                                        new File(System.getProperty("user.dir") + "/" + updateManager.getFilename()));
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
                        labelSize = new JLabel();
                        panelDownload.add(labelSize, UIUilt.createGridBagConstraints(-1, GridBagConstraints.WEST, null, 2, 1));
                    }
                }
                {
                    JLabel lblInformation = new JLabel("Move photos by taken date and address.");
                    panelInfo.add(lblInformation, UIUilt.createGridBagConstraints(-1, -1, new Insets(0, 0, 5, 0), 0, 3));
                }
                {
                    JLabel lblUrl = UIUilt.createSimpleLinkLabel(GIT_REPO, GIT_REPO);
                    panelInfo.add(lblUrl, UIUilt.createGridBagConstraints(-1, -1, new Insets(0, 0, 5, 0), 0, 4));
                }
                {
                    JLabel lblLicense = UIUilt.createSimpleLinkLabel(LICENST_TYPE, LICENSE_URL);
                    panelInfo.add(lblLicense, UIUilt.createGridBagConstraints(-1, -1, null, 0, 5));
                }
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
            new Thread(new DownloadSizeMonitor(targetPath)).start();
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

    class DownloadSizeMonitor implements Runnable {

        private String targetPath;

        public DownloadSizeMonitor(String targetPath) {
            this.targetPath = targetPath;
        }

        @Override
        public void run() {
            File target = new File(targetPath);
            while (btnUpdateAndDownload.getText().equals("Downloading...")) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (target.exists()) {
                    String fileSize = FileUtil.getPrintSize(target.length());
                    labelSize.setText(fileSize);
                }
            }
            labelSize.setText("");
        }
    }
}
