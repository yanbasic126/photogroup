package com.photogroup.ui.browser;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultCaret;

import com.drew.imaging.ImageProcessingException;
import com.photogroup.exception.ExceptionHandler;
import com.photogroup.groupby.PhotoGroup;
import com.photogroup.groupby.metadata.MetadataReader;
import com.photogroup.ui.Messages;
import com.photogroup.ui.SettingStore;
import com.photogroup.ui.dialog.AboutAndUpdateDialog;
import com.photogroup.ui.dialog.SettingDialog;
import com.photogroup.ui.dialog.ViewerDialog;
import com.photogroup.ui.layout.WrapLayout;
import com.photogroup.ui.widget.JTextAreaLog;
import com.photogroup.ui.widget.JTextFieldAddress;
import com.photogroup.update.UpdateManager;
import com.photogroup.util.FileUtil;
import com.photogroup.util.ImageUtil;

public class GroupBrowser {

    private static final int PHOTO_GAP = 1;

    private static final int PREVIEW_SIZE = 168;

    private static final List<String> SYSTEM_LOG_OUTPUT = Collections.synchronizedList(new ArrayList<String>());

    private static final LineBorder IMAGE_PANEL_BORDER = new LineBorder(UIManager.getColor("Panel.background"), 2);

    private static final LineBorder IMAGE_PANEL_SELECTED_BORDER = new LineBorder(UIManager.getColor("Table.selectionBackground"),
            2);

    private static boolean systemLogThreadStart = false;

    private HashMap<String, List<File>> photoGroup;

    private PrintStream systemOutRedirect;

    private UpdateManager updateManager;

    private JFrame frameGroupBrowser;

    private JTextFieldAddress textFieldFolder;

    private JProgressBar progressBar;

    private JPanel panelGroupAll;

    private ImageIcon documentEmptyImageIcon;

    private ImageIcon downIcon;

    private ImageIcon upIcon;

    private ImageIcon renameIcon;

    private ImageIcon lemonIcon;

    private ImageIcon lemonSmallIcon;

    private ImageIcon debugIcon;

    private ImageIcon profileIcon;

    private ImageIcon settingIcon;

    private ImageIcon saveIcon;

    private ImageIcon folderIcon;

    private JButton btnProfile;

    private JButton btnSave;

    private JTextAreaLog txtDebugLog;

    private JPanel panelDebugLog;

    private JScrollPane scrollPaneDebug;

    private JPanel panelDebug;

    private JPanel panelStatus;

    private JPanel panelSelectedImage;

    private List<JPanel> panelFlowList = new ArrayList<JPanel>();

    private List<JButton> btnExpandList = new ArrayList<JButton>();

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);
        // Font systemFont = new JLabel().getFont();
        // FontUIResource fontRes = new FontUIResource(systemFont.getFontName(), systemFont.getStyle(),
        // systemFont.getSize() + 2);
        // for (Enumeration<Object> keys = UIManager.getDefaults().keys(); keys.hasMoreElements();) {
        // Object key = keys.nextElement();
        // Object value = UIManager.get(key);
        // if (value instanceof FontUIResource) {
        // UIManager.put(key, fontRes);
        // }
        // }
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                try {
                    // UIManager.setLookAndFeel(new SubstanceGraphiteLookAndFeel());
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    GroupBrowser window = new GroupBrowser();
                    window.frameGroupBrowser.setVisible(true);
                } catch (Exception e) {
                    ExceptionHandler.logError(e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public GroupBrowser() {
        systemOutRedirect = new PrintStream(new ByteArrayOutputStream() {

            @Override
            public synchronized void write(byte[] b, int off, int len) {
                SYSTEM_LOG_OUTPUT.add(new String(b, off, len));
            }
        });
        System.setErr(systemOutRedirect);
        System.setOut(systemOutRedirect);

        try {
            downIcon = ImageUtil.getImageFromSystemResource("icon/down_16.png");
            upIcon = ImageUtil.getImageFromSystemResource("icon/up_16.png");
            // renameIcon = ImageUtil.getImageIconFromSystemResource("icon/Blue_tag.png");
            lemonIcon = ImageUtil.getImageFromSystemResource("icon/lemon_32.png");
            lemonSmallIcon = ImageUtil.getImageFromSystemResource("icon/lemon_16.png");
            debugIcon = ImageUtil.getImageFromSystemResource("icon/debug_16.png");
            profileIcon = ImageUtil.getImageFromSystemResource("icon/profile_32.png");
            settingIcon = ImageUtil.getImageFromSystemResource("icon/settings_32.png");
            saveIcon = ImageUtil.getImageFromSystemResource("icon/save_32.png");
            folderIcon = ImageUtil.getImageFromSystemResource("icon/folder_16.png");

            // documentEmptyImageIcon
            BufferedImage scaleImage = new BufferedImage(PREVIEW_SIZE, PREVIEW_SIZE, Image.SCALE_FAST);
            scaleImage.getGraphics().drawImage(ImageUtil.getImageFromSystemResource("icon/file_64.png").getImage(), 52, 52, null);
            documentEmptyImageIcon = new ImageIcon(scaleImage);
        } catch (IOException e) {
            e.printStackTrace();
            ExceptionHandler.logError(e.getMessage());
        }
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frameGroupBrowser = new JFrame();
        frameGroupBrowser.setBounds(100, 100, (PREVIEW_SIZE + PHOTO_GAP * 2) * 5 + 40, 700);
        frameGroupBrowser.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameGroupBrowser.setTitle("Lemon Photo");
        frameGroupBrowser.setIconImage(lemonIcon.getImage());
        frameGroupBrowser.setLocationByPlatform(true);
        JMenuBar menuBar = new JMenuBar();
        frameGroupBrowser.setJMenuBar(menuBar);

        JMenu mnWindowMenu = new JMenu("Window");
        menuBar.add(mnWindowMenu);
        // mnWindowMenu.setAccelerator(KeyStroke.getKeyStroke('W', InputEvent.ALT_MASK));
        JMenuItem mntmCollapseItem = new JMenuItem("Collapse All");
        mntmCollapseItem.setIcon(upIcon);
        mnWindowMenu.add(mntmCollapseItem);
        mntmCollapseItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                for (JButton btnExpand : btnExpandList) {
                    btnExpand.setIcon(downIcon);
                }
                for (JPanel panelFlow : panelFlowList) {
                    panelFlow.setVisible(false);
                }
            }
        });

        JMenuItem mntmExpandItem = new JMenuItem("Expand All");
        mntmExpandItem.setIcon(downIcon);
        mnWindowMenu.add(mntmExpandItem);
        mntmExpandItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                for (JButton btnExpand : btnExpandList) {
                    btnExpand.setIcon(upIcon);
                }
                for (JPanel panelFlow : panelFlowList) {
                    panelFlow.setVisible(true);
                }
            }
        });

        JMenu mnHelpMenu = new JMenu("Help");
        menuBar.add(mnHelpMenu);
        // mnHelpMenu.setAccelerator(KeyStroke.getKeyStroke('H', InputEvent.ALT_MASK));

        JMenuItem mntmHelpItem = new JMenuItem("Help");
        mnHelpMenu.add(mntmHelpItem);

        JMenuItem mntmLogItem = new JMenuItem("Show Logs");
        // mntmLogItem.setIcon(debugIcon);
        mnHelpMenu.add(mntmLogItem);
        mntmLogItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (panelDebug.isVisible()) {
                    panelDebug.setVisible(false);
                    mntmLogItem.setText("Show Logs");
                } else {
                    panelDebug.setVisible(true);
                    mntmLogItem.setText("Hide Logs");
                }
            }
        });

        JMenuItem mntmAboutItem = new JMenuItem("About");
        // mntmAboutItem.setIcon(lemonSmallIcon);
        mnHelpMenu.add(mntmAboutItem);
        mntmAboutItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                showAboutDialog();
            }
        });

        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 0, 0 };
        gridBagLayout.rowHeights = new int[] { 0, 0, 1, 0, 0 };
        gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
        gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 1.0, 0.0, 0.0 };
        frameGroupBrowser.getContentPane().setLayout(gridBagLayout);

        JPanel panelToolbar = new JPanel();
        GridBagConstraints gbc_panelToolbar = new GridBagConstraints();
        gbc_panelToolbar.anchor = GridBagConstraints.NORTH;
        gbc_panelToolbar.insets = new Insets(0, 0, 5, 0);
        gbc_panelToolbar.fill = GridBagConstraints.HORIZONTAL;
        gbc_panelToolbar.gridx = 0;
        gbc_panelToolbar.gridy = 0;
        frameGroupBrowser.getContentPane().add(panelToolbar, gbc_panelToolbar);
        GridBagLayout gbl_panelToolbar = new GridBagLayout();
        gbl_panelToolbar.columnWidths = new int[] { 0, 0, 0 };
        gbl_panelToolbar.rowHeights = new int[] { 0, 0 };
        gbl_panelToolbar.columnWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
        gbl_panelToolbar.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
        panelToolbar.setLayout(gbl_panelToolbar);

        JToolBar toolBarSetting = new JToolBar();
        toolBarSetting.setFloatable(false);
        GridBagConstraints gbc_toolBarSetting = new GridBagConstraints();
        gbc_toolBarSetting.insets = new Insets(0, 0, 0, 5);
        gbc_toolBarSetting.gridx = 0;
        gbc_toolBarSetting.gridy = 0;
        panelToolbar.add(toolBarSetting, gbc_toolBarSetting);

        btnProfile = new JButton("Profile");
        btnProfile.setIcon(profileIcon);

        btnProfile.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                profile();
            }
        });

        JButton btnSetting = new JButton("Setting");
        btnSetting.setIcon(settingIcon);

        btnSetting.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                SettingDialog dialog = new SettingDialog();
                dialog.setVisible(true);
            }
        });

        btnSave = new JButton("Save");
        btnSave.setIcon(saveIcon);
        btnSave.setEnabled(false);
        btnSave.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (photoGroup == null) {
                    // popup error
                    JOptionPane.showMessageDialog(frameGroupBrowser, "Error! Nothing to save!", "Save",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    doSave();
                }
            }
        });

        // JToggleButton btnLogs = new JToggleButton("Log");
        // btnLogs.addActionListener(new ActionListener() {
        //
        // public void actionPerformed(ActionEvent e) {
        // panelDebug.setVisible(!panelDebug.isVisible());
        // }
        // });

        // try {
        // BufferedImage bufferedImage = ImageIO.read(ClassLoader.getSystemResource("icon/debug_32.png"));
        // btnLogs.setIcon(new ImageIcon(bufferedImage));
        // } catch (IOException e) {
        // e.printStackTrace();
        // ExceptionHandler.logError(e.getMessage());
        // }

        JSeparator separator_1 = new JSeparator();
        separator_1.setOrientation(SwingConstants.VERTICAL);
        toolBarSetting.add(separator_1);

        toolBarSetting.add(btnProfile);
        toolBarSetting.add(btnSave);

        JSeparator separator = new JSeparator();
        separator.setOrientation(SwingConstants.VERTICAL);
        toolBarSetting.add(separator);
        toolBarSetting.add(btnSetting);
        // toolBarSetting.add(btnLogs);

        JPanel panelAddress = new JPanel();
        GridBagConstraints gbc_panelAddress = new GridBagConstraints();
        gbc_panelAddress.anchor = GridBagConstraints.NORTH;
        gbc_panelAddress.insets = new Insets(0, 0, 5, 0);
        gbc_panelAddress.fill = GridBagConstraints.HORIZONTAL;
        gbc_panelAddress.gridx = 0;
        gbc_panelAddress.gridy = 1;
        frameGroupBrowser.getContentPane().add(panelAddress, gbc_panelAddress);
        GridBagLayout gbl_panelAddress = new GridBagLayout();
        gbl_panelAddress.columnWidths = new int[] { 86, 0, 0 };
        gbl_panelAddress.rowHeights = new int[] { 23, 0 };
        gbl_panelAddress.columnWeights = new double[] { 1.0, 0.0, Double.MIN_VALUE };
        gbl_panelAddress.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
        panelAddress.setLayout(gbl_panelAddress);

        // JLabel lblFolder = new JLabel("Photo folder ");
        // GridBagConstraints gbc_lblFolder = new GridBagConstraints();
        // gbc_lblFolder.anchor = GridBagConstraints.WEST;
        // gbc_lblFolder.insets = new Insets(0, 0, 0, 5);
        // gbc_lblFolder.gridx = 0;
        // gbc_lblFolder.gridy = 0;
        // panelAddress.add(lblFolder, gbc_lblFolder);

        JButton btnFolder = new JButton();
        btnFolder.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String checkFolder = checkFolder();
                    if (checkFolder == null) {
                        File folder = new File(textFieldFolder.getText());
                        Desktop.getDesktop().open(folder);
                    } else {
                        JOptionPane.showMessageDialog(frameGroupBrowser, checkFolder, "Folder", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                    ExceptionHandler.logError(ex.getMessage());
                }
            }

        });
        btnFolder.setIcon(folderIcon);
        GridBagConstraints gbc_btnFolder = new GridBagConstraints();
        gbc_btnFolder.insets = new Insets(0, 0, 0, 5);
        gbc_btnFolder.gridx = 1;
        gbc_btnFolder.gridy = 0;

        int size = (int) btnFolder.getPreferredSize().getHeight();
        btnFolder.setPreferredSize(new Dimension(size, size));
        panelAddress.add(btnFolder, gbc_btnFolder);

        JButton btnBrowse = new JButton("Browse...");
        int width = (int) btnBrowse.getPreferredSize().getWidth();
        btnBrowse.setPreferredSize(new Dimension(width, size));
        btnBrowse.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                if (!textFieldFolder.getText().isEmpty()) {
                    File choicedDir = new File(textFieldFolder.getText());
                    if (choicedDir.exists()) {
                        chooser.setCurrentDirectory(choicedDir);
                    }
                }
                chooser.setDialogTitle(Messages.getString("PhotoGroupWindow.6")); //$NON-NLS-1$
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.setAcceptAllFileFilterUsed(false);
                if (chooser.showOpenDialog(frameGroupBrowser) == JFileChooser.APPROVE_OPTION) {
                    textFieldFolder.setText(chooser.getSelectedFile().getAbsolutePath());
                }
            }

        });
        GridBagConstraints gbc_btnBrowse = new GridBagConstraints();
        gbc_btnBrowse.anchor = GridBagConstraints.NORTHWEST;
        gbc_btnBrowse.gridx = 2;
        gbc_btnBrowse.gridy = 0;
        panelAddress.add(btnBrowse, gbc_btnBrowse);

        textFieldFolder = new JTextFieldAddress();
        textFieldFolder.setText("");
        int twidth = (int) textFieldFolder.getPreferredSize().getWidth();
        textFieldFolder.setPreferredSize(new Dimension(twidth, size));
        textFieldFolder.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (10 == e.getKeyCode()) {
                    profile();
                }
            }
        });
        GridBagConstraints gbc_textFieldFolder = new GridBagConstraints();
        gbc_textFieldFolder.fill = GridBagConstraints.HORIZONTAL;
        gbc_textFieldFolder.insets = new Insets(0, 0, 0, 5);
        gbc_textFieldFolder.gridx = 0;
        gbc_textFieldFolder.gridy = 0;
        panelAddress.add(textFieldFolder, gbc_textFieldFolder);
        textFieldFolder.setColumns(10);

        JPanel panelBrowser = new JPanel();
        GridBagConstraints gbc_panelBrowser = new GridBagConstraints();
        gbc_panelBrowser.insets = new Insets(0, 0, 5, 0);
        gbc_panelBrowser.fill = GridBagConstraints.BOTH;
        gbc_panelBrowser.gridx = 0;
        gbc_panelBrowser.gridy = 2;
        frameGroupBrowser.getContentPane().add(panelBrowser, gbc_panelBrowser);
        GridBagLayout gbl_panelBrowser = new GridBagLayout();
        gbl_panelBrowser.columnWidths = new int[] { 2, 0 };
        gbl_panelBrowser.rowHeights = new int[] { 2, 0 };
        gbl_panelBrowser.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
        gbl_panelBrowser.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
        panelBrowser.setLayout(gbl_panelBrowser);

        panelGroupAll = new JPanel();
        panelGroupAll.setBackground(new Color(255, 255, 255));
        GridBagConstraints gbc_panelGroupAll = new GridBagConstraints();
        gbc_panelGroupAll.anchor = GridBagConstraints.NORTH;
        gbc_panelGroupAll.insets = new Insets(0, 0, 0, 5);
        gbc_panelGroupAll.fill = GridBagConstraints.BOTH;
        gbc_panelGroupAll.gridx = 0;
        gbc_panelGroupAll.gridy = 0;
        GridBagLayout gbl_panelGroupAll = new GridBagLayout();
        gbl_panelGroupAll.columnWidths = new int[] { 0 };
        gbl_panelGroupAll.rowHeights = new int[] { 0 };
        gbl_panelGroupAll.columnWeights = new double[] { 1.0 };
        gbl_panelGroupAll.rowWeights = new double[] { 0.0, 1.0 };
        panelGroupAll.setLayout(gbl_panelGroupAll);

        JScrollPane scrollPane = new JScrollPane(panelGroupAll);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        GridBagConstraints gbc_scrollPane = new GridBagConstraints();
        gbc_scrollPane.fill = GridBagConstraints.BOTH;
        gbc_scrollPane.gridx = 0;
        gbc_scrollPane.gridy = 0;
        // panelBrowser.add(panelGroupAll, gbc_panelGroupAll);
        panelBrowser.add(scrollPane, gbc_scrollPane);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        panelStatus = new JPanel();
        GridBagConstraints gbc_panelStatus = new GridBagConstraints();
        gbc_panelStatus.insets = new Insets(0, 0, 5, 0);
        gbc_panelStatus.anchor = GridBagConstraints.NORTH;
        gbc_panelStatus.fill = GridBagConstraints.HORIZONTAL;
        gbc_panelStatus.gridx = 0;
        gbc_panelStatus.gridy = 3;
        frameGroupBrowser.getContentPane().add(panelStatus, gbc_panelStatus);
        GridBagLayout gbl_panelStatus = new GridBagLayout();
        gbl_panelStatus.columnWidths = new int[] { 269 };
        gbl_panelStatus.rowHeights = new int[] { 14, 0 };
        gbl_panelStatus.columnWeights = new double[] { 1.0 };
        gbl_panelStatus.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
        panelStatus.setLayout(gbl_panelStatus);

        progressBar = new JProgressBar();
        progressBar.setValue(0);
        GridBagConstraints gbc_progressBar = new GridBagConstraints();
        gbc_progressBar.fill = GridBagConstraints.BOTH;
        gbc_progressBar.gridx = 0;
        gbc_progressBar.gridy = 0;
        panelStatus.add(progressBar, gbc_progressBar);

        panelDebug = new JPanel();
        GridBagConstraints gbc_panel_2_1 = new GridBagConstraints();
        gbc_panel_2_1.anchor = GridBagConstraints.NORTH;
        gbc_panel_2_1.fill = GridBagConstraints.HORIZONTAL;
        gbc_panel_2_1.gridx = 0;
        gbc_panel_2_1.gridy = 4;
        // panel_2.setVisible(false);
        frameGroupBrowser.getContentPane().add(panelDebug, gbc_panel_2_1);
        gbc_panel_2_1 = new GridBagConstraints();
        gbc_panel_2_1.insets = new Insets(0, 0, 5, 0);
        gbc_panel_2_1.anchor = GridBagConstraints.NORTHWEST;
        gbc_panel_2_1.gridx = 1;
        gbc_panel_2_1.gridy = 0;
        GridBagLayout gbl_panelDebug = new GridBagLayout();
        gbl_panelDebug.columnWidths = new int[] { 600, 0 };
        gbl_panelDebug.rowHeights = new int[] { 0 };
        gbl_panelDebug.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
        gbl_panelDebug.rowWeights = new double[] { Double.MIN_VALUE };
        panelDebug.setLayout(gbl_panelDebug);

        panelDebugLog = new JPanel();
        // panelDebug.add(panel_2, gbc_panel_2_1);
        GridBagLayout gbl_panelDebugLog = new GridBagLayout();
        gbl_panelDebugLog.columnWidths = new int[] { 0, 0 };
        gbl_panelDebugLog.rowHeights = new int[] { 0, 0 };
        gbl_panelDebugLog.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
        gbl_panelDebugLog.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
        panelDebugLog.setLayout(gbl_panelDebugLog);

        scrollPaneDebug = new JScrollPane(panelDebugLog);
        scrollPaneDebug.setPreferredSize(new Dimension(100, 160));
        GridBagConstraints gbc_scrollPaneDebug = new GridBagConstraints();
        gbc_scrollPaneDebug.fill = GridBagConstraints.BOTH;
        gbc_scrollPaneDebug.gridx = 0;
        gbc_scrollPaneDebug.gridy = 0;
        txtDebugLog = new JTextAreaLog();
        txtDebugLog.setText("");
        txtDebugLog.setRows(10);
        DefaultCaret caret = (DefaultCaret) txtDebugLog.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        GridBagConstraints gbc_txtDebugLog = new GridBagConstraints();
        gbc_txtDebugLog.anchor = GridBagConstraints.SOUTH;
        gbc_txtDebugLog.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtDebugLog.gridx = 0;
        gbc_txtDebugLog.gridy = 0;
        panelDebugLog.add(txtDebugLog, gbc_txtDebugLog);
        panelDebug.add(scrollPaneDebug, gbc_scrollPaneDebug);
        scrollPaneDebug.getVerticalScrollBar().setUnitIncrement(16);
        panelDebug.setVisible(false);

    }

    protected void showAboutDialog() {
        AboutAndUpdateDialog dialog = new AboutAndUpdateDialog();
        dialog.setLocationRelativeTo(frameGroupBrowser);

        dialog.setVisible(true);
    }

    private void createImageGroup(Entry<String, List<File>> oneGroup) {
        JPanel panelGroup1 = new JPanel();
        // panelGroup1.setBorder(new LineBorder(new Color(0, 0, 0)));
        GridBagConstraints gbc_panelGroup1 = new GridBagConstraints();
        gbc_panelGroup1.fill = GridBagConstraints.HORIZONTAL;
        gbc_panelGroup1.anchor = GridBagConstraints.NORTH;
        gbc_panelGroup1.insets = new Insets(0, 0, 5, 0);
        gbc_panelGroup1.gridx = 0;
        // gbc_panelGroup1.gridy = 0;
        panelGroupAll.add(panelGroup1, gbc_panelGroup1);

        GridBagLayout gbl_panelGroup3 = new GridBagLayout();
        gbl_panelGroup3.columnWidths = new int[] { 0, 0 };
        gbl_panelGroup3.rowHeights = new int[] { 0, 0 };
        gbl_panelGroup3.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
        gbl_panelGroup3.rowWeights = new double[] { 0.0, 0.0 };

        panelGroup1.setLayout(gbl_panelGroup3);

        // JButton btnDate_1 = new JButton(oneGroup.getKey() + " (" + oneGroup.getValue().size() + ")");
        // btnDate_1.setHorizontalAlignment(AbstractButton.LEFT);
        // GridBagConstraints gbc_btnDate_1 = new GridBagConstraints();
        // gbc_btnDate_1.fill = GridBagConstraints.HORIZONTAL;
        // gbc_btnDate_1.gridx = 0;
        // gbc_btnDate_1.gridy = 0;
        // panelGroup1.add(btnDate_1, gbc_btnDate_1);

        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        GridBagConstraints gbc_toolBar = new GridBagConstraints();
        gbc_toolBar.fill = GridBagConstraints.HORIZONTAL;
        gbc_toolBar.gridx = 0;
        gbc_toolBar.gridy = 0;
        panelGroup1.add(toolBar, gbc_toolBar);

        JButton btnExpand = new JButton();
        btnExpand.setIcon(upIcon);
        toolBar.add(btnExpand);

        // JLabel lblTitle = new JLabel(oneGroup.getKey() + " (" + oneGroup.getValue().size() + ")");
        // toolBar.add(lblTitle);

        JTextField textFieldTitle = new JTextField(oneGroup.getKey());
        textFieldTitle.setToolTipText(oneGroup.getKey());
        textFieldTitle.setName(oneGroup.getKey());
        // textFieldTitle.setEditable(false);
        toolBar.add(textFieldTitle);
        textFieldTitle.setBorder(new EmptyBorder(0, 0, 0, 0));

        JButton btnRename = new JButton("Rename");
        // btnRename.setIcon(renameIcon);
        btnRename.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (!textFieldTitle.getToolTipText().equals(textFieldTitle.getText())) {
                    String originName = textFieldTitle.getToolTipText();
                    photoGroup.put(textFieldTitle.getText(), photoGroup.get(originName));
                    photoGroup.remove(originName);
                    textFieldTitle.setToolTipText(textFieldTitle.getText());
                }
            }
        });

        toolBar.add(btnRename);

        JPanel panelFlow = new JPanel();
        panelFlow.setBorder(new EmptyBorder(0, 0, 0, 0));
        WrapLayout wl_panelFlow = new WrapLayout();
        wl_panelFlow.setAlignment(FlowLayout.LEFT);
        wl_panelFlow.setHgap(PHOTO_GAP);
        wl_panelFlow.setVgap(PHOTO_GAP);
        panelFlow.setLayout(wl_panelFlow);
        // panelFlow.setBorder(new LineBorder(new Color(0, 0, 0)));
        panelFlow.setBackground(new Color(255, 255, 255));
        // FlowLayout flowLayout = (FlowLayout) panelFlow.getLayout();
        // flowLayout.setAlignment(FlowLayout.LEFT);
        GridBagConstraints gbc_panelFlow = new GridBagConstraints();
        gbc_panelFlow.fill = GridBagConstraints.HORIZONTAL;
        gbc_panelFlow.gridx = 0;
        gbc_panelFlow.gridy = 1;
        panelGroup1.add(panelFlow, gbc_panelFlow);

        btnExpand.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (panelFlow.isVisible()) {
                    btnExpand.setIcon(downIcon);
                    panelFlow.setVisible(false);
                } else {
                    btnExpand.setIcon(upIcon);
                    panelFlow.setVisible(true);
                }
            }
        });

        panelFlowList.add(panelFlow);
        btnExpandList.add(btnExpand);

        textFieldTitle.getDocument().addDocumentListener(new DocumentListener() {

            public void changedUpdate(DocumentEvent e) {
                // whatever you want
            }

            public void removeUpdate(DocumentEvent e) {
                // whatever you want
            }

            public void insertUpdate(DocumentEvent e) {
                // whatever you want
                // String originName = textFieldTitle.getToolTipText();
                // photoGroup.put(textFieldTitle.getText(), photoGroup.get(originName));
                // photoGroup.remove(originName);
                // textFieldTitle.setToolTipText(textFieldTitle.getText());
            }
        });

        for (File photo : oneGroup.getValue()) {
            addImagePanel(panelFlow, photo);
        }

        // panelFlow.doLayout();
        // return panelFlow;
    }

    private void addImagePanel(JPanel panelFlow, File photo) {
        JPanel panel = new JPanel();
        panel.setBorder(IMAGE_PANEL_BORDER);
        GridBagLayout gbl_panel = new GridBagLayout();
        gbl_panel.columnWidths = new int[] { 0 };
        gbl_panel.rowHeights = new int[] { 23, 23 };
        gbl_panel.columnWeights = new double[] { 0.0 };
        gbl_panel.rowWeights = new double[] { 0.0, 0.0 };
        panel.setLayout(gbl_panel);
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
            ImageIcon icon = new ImageIcon(resizeImageToPreviewIPhone(bufferedImage));
            labelImg.setIcon(icon);
        } else {
            buffered = false;
            labelImg.setIcon(documentEmptyImageIcon);
        }
        final boolean isImage = buffered;
        panel.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == 1) {
                    if (e.getClickCount() == 1) {
                        if (panelSelectedImage != null) {
                            panelSelectedImage.setBorder(IMAGE_PANEL_BORDER);
                        }
                        panel.setBorder(IMAGE_PANEL_SELECTED_BORDER);
                        panelSelectedImage = panel;
                    } else if (e.getClickCount() == 2) {
                        openToPreview(photo, isImage);
                    }
                } else if (e.getButton() == 2) {
                    if (e.getClickCount() == 1) {
                        openToPreview(photo, isImage);
                    }
                }
            }
        });
        GridBagConstraints gbc_label = new GridBagConstraints();
        gbc_label.anchor = GridBagConstraints.NORTHWEST;
        gbc_label.gridx = 0;
        gbc_label.gridy = 0;
        panel.add(labelImg, gbc_label);

        JLabel labelName = new JLabel(photo.getName());
        // labelName.setBorder(new EmptyBorder(0, 0, 0, 0));
        labelName.setToolTipText(photo.getName());
        int height = labelName.getHeight();
        labelName.setPreferredSize(new Dimension(PREVIEW_SIZE, 20));
        GridBagConstraints gbc_labelName = new GridBagConstraints();
        gbc_labelName.anchor = GridBagConstraints.NORTHWEST;
        gbc_labelName.gridx = 0;
        gbc_labelName.gridy = 1;
        panel.add(labelName, gbc_labelName);

    }

    private BufferedImage resizeImageToPreview(BufferedImage originalImage) {
        int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        int x = (width > height) ? PREVIEW_SIZE : width / height * PREVIEW_SIZE;
        int y = (width > height) ? height / width * PREVIEW_SIZE : PREVIEW_SIZE;

        BufferedImage resizedImage = new BufferedImage(x, y, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, x, y, null);
        g.dispose();
        return resizedImage;
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

    protected String validateBeforeRun() {
        String errorMsg = checkFolder();
        return errorMsg;
    }

    private String checkFolder() {
        String errorMsg = null;
        if (textFieldFolder.getText().isEmpty()) {
            errorMsg = "Error! Please select a folder.";
        } else {
            if (!new File(textFieldFolder.getText()).exists()) {
                errorMsg = "Error! Folder '" + textFieldFolder.getText() + "' does not exist.";
            }
        }
        return errorMsg;
    }

    private void profile() {
        String errMsg = validateBeforeRun();
        if (errMsg == null) {
            doProfile();
        } else {
            // popup error
            JOptionPane.showMessageDialog(frameGroupBrowser, errMsg, "Profile", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void doProfile() {
        btnProfile.setEnabled(false);
        photoGroup = new HashMap<String, List<File>>();

        PhotoGroup groupThread = new PhotoGroup(photoGroup, textFieldFolder.getText(),
                SettingStore.getSettingStore().getThreshold(), SettingStore.getSettingStore().getModule(),
                SettingStore.getSettingStore().getFormat(), SettingStore.getSettingStore().isGuess(),
                SettingStore.getSettingStore().isGps(), SettingStore.getSettingStore().isReport(),
                SettingStore.getSettingStore().isIncludeSubFolder());
        ExecutorService exe = Executors.newFixedThreadPool(1);
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (!exe.isTerminated()) {
                    // EventQueue.invokeLater(new Runnable() {
                    //
                    // public void run() {
                    int precent = Integer.parseInt(groupThread.processPrecent.toString());
                    progressBar.setValue(precent);
                    // }
                    // });
                }
                panelGroupAll.removeAll();
                Iterator<?> it = photoGroup.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, List<File>> pair = (Entry<String, List<File>>) it.next();
                    createImageGroup(pair);
                }
                double[] rowWeights;
                if (photoGroup.size() > 1) {
                    rowWeights = new double[photoGroup.size()];
                    for (int i = 0; i < rowWeights.length - 1; i++) {
                        rowWeights[i] = 0.0;
                    }
                    rowWeights[rowWeights.length - 1] = 1.0;
                } else {
                    rowWeights = new double[] { 1.0 };
                }
                GridBagLayout panelGroupAllGridBagLayout = (GridBagLayout) panelGroupAll.getLayout();
                panelGroupAllGridBagLayout.rowWeights = rowWeights;
                panelGroupAll.setVisible(false);
                panelGroupAll.setVisible(true);
                btnProfile.setEnabled(true);
                if (photoGroup.size() > 0) {
                    btnSave.setEnabled(true);
                }
                progressBar.setValue(100);
                // EventQueue.invokeLater(new Runnable() {
                //
                // public void run() {
                // }
                // });
                // if ("OK".equals(showResultDialog(photoGroup))) {
                // FileUtil.movePhotos(threshold, textField.getText(), photoGroup);
                // }
            }
        }).start();

        if (!systemLogThreadStart) {
            systemLogThreadStart = true;
            new Thread(new Runnable() {

                @Override
                public void run() {
                    while (true) {
                        if (txtDebugLog != null && panelDebug.isVisible() && !SYSTEM_LOG_OUTPUT.isEmpty()) {
                            StringBuffer sb = new StringBuffer(txtDebugLog.getText());
                            for (String s : SYSTEM_LOG_OUTPUT) {
                                sb.append(s);
                            }
                            txtDebugLog.setText(sb.toString());
                            SYSTEM_LOG_OUTPUT.clear();
                        }
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }

        exe.execute(groupThread);
        exe.shutdown();
        // new Thread(new Runnable() {
        //
        // @Override
        // public void run() {
        // btnRun.setText("Running");
        // btnRun.setEnabled(false);
        // Map<String, List<File>> groups = group.getPhotoGroup();
        // try {
        // GroupResultDialog dialog = new GroupResultDialog(groups);
        // dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        // dialog.setVisible(true);
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        // FileUtil.movePhotos(threshold, textField.getText(), groups);
        // btnRun.setEnabled(true);
        // btnRun.setText("Run");
        // }
        // }).start();
    }

    private void doSave() {
        FileUtil.movePhotos(textFieldFolder.getText(), photoGroup);
        btnSave.setEnabled(false);
    }

    private void openToPreview(File file, boolean isImage) {
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
}
