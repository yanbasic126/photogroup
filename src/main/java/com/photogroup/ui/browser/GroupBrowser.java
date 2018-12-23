package com.photogroup.ui.browser;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
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
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
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
import javax.swing.text.DefaultCaret;

import com.drew.imaging.ImageProcessingException;
import com.photogroup.exception.ExceptionHandler;
import com.photogroup.groupby.PhotoGroup;
import com.photogroup.groupby.metadata.MetadataReader;
import com.photogroup.setting.HistoryDirectory;
import com.photogroup.ui.Messages;
import com.photogroup.ui.SettingStore;
import com.photogroup.ui.dialog.AboutAndUpdateDialog;
import com.photogroup.ui.dialog.SettingDialog;
import com.photogroup.ui.layout.WrapLayout;
import com.photogroup.ui.util.UIUilt;
import com.photogroup.ui.widget.FileListAccessory;
import com.photogroup.ui.widget.JTextAreaLog;
import com.photogroup.ui.widget.JTextFieldAddress;
import com.photogroup.util.FileUtil;
import com.photogroup.util.ImageUtil;

public class GroupBrowser {

    private static final int MENU_DIR_LEN = 30;

    private static final int PHOTO_GAP = 1;

    private static final int PREVIEW_SIZE = 168;

    private static final List<String> SYSTEM_LOG_OUTPUT = Collections.synchronizedList(new ArrayList<String>());

    private static final LineBorder IMAGE_PANEL_BORDER = new LineBorder(UIManager.getColor("Panel.background"), 2); //$NON-NLS-1$

    private static final LineBorder IMAGE_PANEL_SELECTED_BORDER = new LineBorder(UIManager.getColor("Table.selectionBackground"), //$NON-NLS-1$
            2);

    private static final WrapLayout WRAP_LAYOUT_FLOW = new WrapLayout();

    private static boolean systemLogThreadStart = false;

    private HashMap<String, List<File>> photoGroup;

    private JFrame frameGroupBrowser;

    private JTextFieldAddress textFieldFolder;

    private JProgressBar progressBar;

    private JPanel panelGroupAll;

    private ImageIcon documentEmptyImageIcon;

    private ImageIcon downIcon;

    private ImageIcon upIcon;

    // private ImageIcon renameIcon;

    private ImageIcon lemonIcon;

    private ImageIcon lemonSmallIcon;

    // private ImageIcon debugIcon;

    private ImageIcon profileIcon;

    private ImageIcon settingIcon;

    private ImageIcon saveIcon;

    private ImageIcon folderIcon;

    private JButton btnProfile;

    private JButton btnSave;

    private JTextAreaLog txtDebugLog;

    private JPanel panelDebug;

    private JPanel panelSelectedImage;

    private List<JPanel> panelFlowList = new ArrayList<JPanel>();

    private List<JButton> btnExpandList = new ArrayList<JButton>();

    private Map<JTextField, String> textFieldTitleMap = new HashMap<JTextField, String>();

    private JMenuBar menuBar;

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
        PrintStream systemOutRedirect = new PrintStream(new ByteArrayOutputStream() {

            @Override
            public synchronized void write(byte[] b, int off, int len) {
                SYSTEM_LOG_OUTPUT.add(new String(b, off, len));
            }
        });
        System.setErr(systemOutRedirect);
        System.setOut(systemOutRedirect);

        try {
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

            // documentEmptyImageIcon
            BufferedImage scaleImage = new BufferedImage(PREVIEW_SIZE, PREVIEW_SIZE, Image.SCALE_FAST);
            scaleImage.getGraphics().drawImage(ImageUtil.getImageFromSystemResource("icon/file_64.png").getImage(), 52, 52, null); //$NON-NLS-1$
            documentEmptyImageIcon = new ImageIcon(scaleImage);
        } catch (IOException e) {
            e.printStackTrace();
            ExceptionHandler.logError(e.getMessage());
        }
        createWrapLayout();
        initialize();
    }

    private void createWrapLayout() {
        WRAP_LAYOUT_FLOW.setAlignment(FlowLayout.LEFT);
        WRAP_LAYOUT_FLOW.setHgap(PHOTO_GAP);
        WRAP_LAYOUT_FLOW.setVgap(PHOTO_GAP);
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        createFrame();
        createMenu();
        createToolbar();
        createAddressBar();
        createBrowserPanel();
        createDebugPanel();
    }

    private void createDebugPanel() {
        panelDebug = new JPanel();

        // panel_2.setVisible(false);
        frameGroupBrowser.getContentPane().add(panelDebug,
                UIUilt.createGridBagConstraints(GridBagConstraints.HORIZONTAL, GridBagConstraints.NORTH, null, 0, 4));

        panelDebug.setLayout(UIUilt.createGridBagLayout(new int[] { 600, 0 }, new int[] { 0 },
                new double[] { 1.0, Double.MIN_VALUE }, new double[] { Double.MIN_VALUE }));

        JPanel panelDebugLog = new JPanel();
        // panelDebug.add(panel_2, gbc_panel_2_1);
        panelDebugLog.setLayout(UIUilt.createGridBagLayout(new int[] { 0, 0 }, new int[] { 0, 0 },
                new double[] { 1.0, Double.MIN_VALUE }, new double[] { 1.0, Double.MIN_VALUE }));

        JScrollPane scrollPaneDebug = new JScrollPane(panelDebugLog);
        scrollPaneDebug.setPreferredSize(new Dimension(100, 160));
        txtDebugLog = new JTextAreaLog();
        txtDebugLog.setText(""); //$NON-NLS-1$
        txtDebugLog.setRows(10);
        DefaultCaret caret = (DefaultCaret) txtDebugLog.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        panelDebugLog.add(txtDebugLog,
                UIUilt.createGridBagConstraints(GridBagConstraints.HORIZONTAL, GridBagConstraints.SOUTH, null, 0, 0));

        panelDebug.add(scrollPaneDebug, UIUilt.createGridBagConstraints(GridBagConstraints.BOTH, -1, null, 0, 0));
        scrollPaneDebug.getVerticalScrollBar().setUnitIncrement(16);
        panelDebug.setVisible(false);
    }

    private void createBrowserPanel() {
        JPanel panelBrowser = new JPanel();
        frameGroupBrowser.getContentPane().add(panelBrowser,
                UIUilt.createGridBagConstraints(GridBagConstraints.BOTH, -1, new Insets(0, 0, 5, 0), 0, 2));
        panelBrowser.setLayout(UIUilt.createGridBagLayout(new int[] { 2, 0 }, new int[] { 2, 0 },
                new double[] { 1.0, Double.MIN_VALUE }, new double[] { 1.0, Double.MIN_VALUE }));

        panelGroupAll = new JPanel();
        panelGroupAll.setBackground(Color.WHITE);
        panelGroupAll.setLayout(
                UIUilt.createGridBagLayout(new int[] { 0 }, new int[] { 0 }, new double[] { 1.0 }, new double[] { 0.0, 1.0 }));

        JScrollPane scrollPane = new JScrollPane(panelGroupAll);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        // panelBrowser.add(panelGroupAll, gbc_panelGroupAll);
        panelBrowser.add(scrollPane, UIUilt.createGridBagConstraints(GridBagConstraints.BOTH, -1, null, 0, 0));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        JPanel panelStatus = new JPanel();
        frameGroupBrowser.getContentPane().add(panelStatus, UIUilt.createGridBagConstraints(GridBagConstraints.HORIZONTAL,
                GridBagConstraints.NORTH, new Insets(0, 0, 5, 0), 0, 3));
        panelStatus.setLayout(UIUilt.createGridBagLayout(new int[] { 269 }, new int[] { 14, 0 }, new double[] { 1.0 },
                new double[] { 0.0, Double.MIN_VALUE }));

        progressBar = new JProgressBar();
        progressBar.setValue(0);
        panelStatus.add(progressBar, UIUilt.createGridBagConstraints(GridBagConstraints.BOTH, -1, null, 0, 0));
    }

    private void createAddressBar() {
        JPanel panelAddress = new JPanel();
        frameGroupBrowser.getContentPane().add(panelAddress, UIUilt.createGridBagConstraints(GridBagConstraints.HORIZONTAL,
                GridBagConstraints.NORTH, new Insets(0, 0, 5, 0), 0, 1));
        panelAddress.setLayout(UIUilt.createGridBagLayout(new int[] { 86, 0, 0 }, new int[] { 23, 0 },
                new double[] { 1.0, 0.0, Double.MIN_VALUE }, new double[] { 0.0, Double.MIN_VALUE }));

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
                GroupBrowserHelper.openFolder(textFieldFolder.getText());
            }
        });
        btnFolder.setIcon(folderIcon);
        int size = (int) btnFolder.getPreferredSize().getHeight();
        btnFolder.setPreferredSize(new Dimension(size, size));
        panelAddress.add(btnFolder, UIUilt.createGridBagConstraints(-1, -1, new Insets(0, 0, 0, 5), 1, 0));

        JButton btnBrowse = new JButton(Messages.getString("GroupBrowser.browse")); //$NON-NLS-1$
        int width = (int) btnBrowse.getPreferredSize().getWidth();
        btnBrowse.setPreferredSize(new Dimension(width, size));
        btnBrowse.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                browseFolder();
            }
        });
        panelAddress.add(btnBrowse, UIUilt.createGridBagConstraints(-1, GridBagConstraints.NORTHWEST, null, 2, 0));

        textFieldFolder = new JTextFieldAddress();
        textFieldFolder.setText(""); //$NON-NLS-1$
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
        panelAddress.add(textFieldFolder,
                UIUilt.createGridBagConstraints(GridBagConstraints.HORIZONTAL, -1, new Insets(0, 0, 0, 5), 0, 0));
        textFieldFolder.setColumns(10);
    }

    private void browseFolder() {
        UIManager.put("FileChooser.readOnly", Boolean.TRUE);
        JFileChooser chooser = new JFileChooser();
        if (!textFieldFolder.getText().isEmpty()) {
            File choicedDir = new File(textFieldFolder.getText());
            if (choicedDir.exists()) {
                chooser.setCurrentDirectory(choicedDir);
            }
        }
        chooser.setDialogTitle(Messages.getString("PhotoGroupWindow.6")); //$NON-NLS-1$
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAccessory(new FileListAccessory(chooser));
        chooser.setAcceptAllFileFilterUsed(false);
        if (chooser.showOpenDialog(frameGroupBrowser) == JFileChooser.APPROVE_OPTION) {
            textFieldFolder.setText(chooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void createToolbar() {
        JPanel panelToolbar = new JPanel();
        frameGroupBrowser.getContentPane().add(panelToolbar, UIUilt.createGridBagConstraints(GridBagConstraints.HORIZONTAL,
                GridBagConstraints.NORTH, new Insets(0, 0, 5, 0), 0, 0));
        panelToolbar.setLayout(UIUilt.createGridBagLayout(new int[] { 0, 0, 0 }, new int[] { 0, 0 },
                new double[] { 0.0, 0.0, Double.MIN_VALUE }, new double[] { 0.0, Double.MIN_VALUE }));

        JToolBar toolBarSetting = new JToolBar();
        toolBarSetting.setFloatable(false);
        panelToolbar.add(toolBarSetting, UIUilt.createGridBagConstraints(-1, -1, new Insets(0, 0, 0, 5), 0, 0));

        btnProfile = new JButton(Messages.getString("GroupBrowser.profile")); //$NON-NLS-1$
        btnProfile.setIcon(profileIcon);

        btnProfile.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                profile();
            }
        });

        JButton btnSetting = new JButton(Messages.getString("GroupBrowser.seetings")); //$NON-NLS-1$
        btnSetting.setIcon(settingIcon);

        btnSetting.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                SettingDialog dialog = new SettingDialog();
                dialog.setVisible(true);
            }
        });

        btnSave = new JButton(Messages.getString("GroupBrowser.save")); //$NON-NLS-1$
        btnSave.setIcon(saveIcon);
        btnSave.setEnabled(false);
        btnSave.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (photoGroup == null) {
                    // popup error
                    JOptionPane.showMessageDialog(frameGroupBrowser, Messages.getString("GroupBrowser.save_error"), //$NON-NLS-1$
                            Messages.getString("GroupBrowser.save"), //$NON-NLS-1$
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
    }

    private void createFrame() {
        frameGroupBrowser = new JFrame();
        int calWidth = (PREVIEW_SIZE + PHOTO_GAP * 2) * 5 + 60;
        int calHeight = (int) (calWidth * 0.8);
        frameGroupBrowser.setBounds(100, 100, calWidth, calHeight);
        frameGroupBrowser.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameGroupBrowser.setTitle(Messages.getString("GroupBrowser.title")); //$NON-NLS-1$
        frameGroupBrowser.setIconImage(lemonIcon.getImage());
        frameGroupBrowser.setLocationByPlatform(true);
        frameGroupBrowser.getContentPane().setLayout(UIUilt.createGridBagLayout(new int[] { 0, 0 }, new int[] { 0, 0, 1, 0, 0 },
                new double[] { 1.0, Double.MIN_VALUE }, new double[] { 0.0, 0.0, 1.0, 0.0, 0.0 }));
    }

    private void createMenu() {
        menuBar = new JMenuBar();
        frameGroupBrowser.setJMenuBar(menuBar);

        createMenuFile();

        JMenu mnWindowMenu = new JMenu(Messages.getString("GroupBrowser.menu_window")); //$NON-NLS-1$
        menuBar.add(mnWindowMenu);
        mnWindowMenu.setMnemonic(KeyEvent.VK_W);
        // mnWindowMenu.setAccelerator(KeyStroke.getKeyStroke('W', InputEvent.ALT_MASK));
        JMenuItem mntmCollapseItem = new JMenuItem(Messages.getString("GroupBrowser.menu_coll_all")); //$NON-NLS-1$
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

        JMenuItem mntmExpandItem = new JMenuItem(Messages.getString("GroupBrowser.menu_exp_all")); //$NON-NLS-1$
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

        JMenu mnHelpMenu = new JMenu(Messages.getString("GroupBrowser.menu_help")); //$NON-NLS-1$
        menuBar.add(mnHelpMenu);
        // mnHelpMenu.setAccelerator(KeyStroke.getKeyStroke('H', InputEvent.ALT_MASK));
        mnHelpMenu.setMnemonic(KeyEvent.VK_H);
        JMenuItem mntmHelpItem = new JMenuItem(Messages.getString("GroupBrowser.menu_help")); //$NON-NLS-1$
        mnHelpMenu.add(mntmHelpItem);

        mntmHelpItem.setMnemonic(KeyEvent.VK_H);

        JMenuItem mntmLogItem = new JMenuItem(Messages.getString("GroupBrowser.menu_show_log")); //$NON-NLS-1$
        // mntmLogItem.setIcon(debugIcon);
        mnHelpMenu.add(mntmLogItem);
        mntmLogItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (panelDebug.isVisible()) {
                    panelDebug.setVisible(false);
                    mntmLogItem.setText(Messages.getString("GroupBrowser.menu_show_log")); //$NON-NLS-1$
                } else {
                    panelDebug.setVisible(true);
                    mntmLogItem.setText(Messages.getString("GroupBrowser.menu_hide_log")); //$NON-NLS-1$
                }
            }
        });

        JMenuItem mntmAboutItem = new JMenuItem(Messages.getString("GroupBrowser.menu_about")); //$NON-NLS-1$
        mntmAboutItem.setIcon(lemonSmallIcon);
        mntmAboutItem.setMnemonic(KeyEvent.VK_A);
        mnHelpMenu.add(mntmAboutItem);
        mntmAboutItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                showAboutDialog();
            }
        });
    }

    private void createMenuFile() {
        JMenu mnFileMenu = new JMenu(Messages.getString("GroupBrowser.fie")); //$NON-NLS-1$
        menuBar.add(mnFileMenu, 0);
        mnFileMenu.setMnemonic(KeyEvent.VK_F);
        JMenuItem mntmOpenItem = new JMenuItem(Messages.getString("GroupBrowser.open_folder")); //$NON-NLS-1$
        mnFileMenu.add(mntmOpenItem);

        mntmOpenItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                browseFolder();
            }
        });

        Set<String> directories = HistoryDirectory.INSTANCE.listDirectory();
        if (directories.size() > 0) {
            mnFileMenu.addSeparator();
        }
        for (String dir : directories) {
            String shortDir = dir.length() < MENU_DIR_LEN ? dir
                    : dir.substring(0, MENU_DIR_LEN / 2) + "..." + dir.substring(dir.length() - MENU_DIR_LEN / 2, dir.length());
            JMenuItem mntmDirItem = new JMenuItem(shortDir);
            mntmDirItem.setToolTipText(dir);
            mnFileMenu.add(mntmDirItem);
            mntmDirItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    textFieldFolder.setText(dir);
                }
            });
        }
        if (directories.size() > 0) {
            mnFileMenu.addSeparator();
            JMenuItem mntmClearHistoryItem = new JMenuItem("Clear History");
            mnFileMenu.add(mntmClearHistoryItem);
            mntmClearHistoryItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    HistoryDirectory.INSTANCE.clearHistory();
                    refreshMenuFile();
                }
            });
            mnFileMenu.addSeparator();
        }

        JMenuItem mntmExitItem = new JMenuItem(Messages.getString("GroupBrowser.exit")); //$NON-NLS-1$
        mnFileMenu.add(mntmExitItem);
        mntmExitItem.setMnemonic(KeyEvent.VK_X);
        mntmExitItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                frameGroupBrowser.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frameGroupBrowser.dispose();
            }
        });
    }

    protected void showAboutDialog() {
        AboutAndUpdateDialog dialog = new AboutAndUpdateDialog();
        dialog.setLocationRelativeTo(frameGroupBrowser);

        dialog.setVisible(true);
    }

    private void createImageGroup(String title, List<File> files) {
        JPanel panelGroup1 = new JPanel();
        // panelGroup1.setBorder(new LineBorder(new Color(0, 0, 0)));
        panelGroupAll.add(panelGroup1, UIUilt.createGridBagConstraints(GridBagConstraints.HORIZONTAL, GridBagConstraints.NORTH,
                new Insets(0, 0, 5, 0), 0, -1));

        panelGroup1.setLayout(UIUilt.createGridBagLayout(new int[] { 0, 0 }, new int[] { 0, 0 },
                new double[] { 1.0, Double.MIN_VALUE }, new double[] { 0.0, 0.0 }));

        // JButton btnDate_1 = new JButton(oneGroup.getKey() + " (" + oneGroup.getValue().size() + ")");
        // btnDate_1.setHorizontalAlignment(AbstractButton.LEFT);
        // GridBagConstraints gbc_btnDate_1 = new GridBagConstraints();
        // gbc_btnDate_1.fill = GridBagConstraints.HORIZONTAL;
        // gbc_btnDate_1.gridx = 0;
        // gbc_btnDate_1.gridy = 0;
        // panelGroup1.add(btnDate_1, gbc_btnDate_1);

        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);

        panelGroup1.add(toolBar, UIUilt.createGridBagConstraints(GridBagConstraints.HORIZONTAL, -1, null, 0, 0));

        JButton btnExpand = new JButton();
        btnExpand.setIcon(upIcon);
        toolBar.add(btnExpand);

        // JLabel lblTitle = new JLabel(oneGroup.getKey() + " (" + oneGroup.getValue().size() + ")");
        // toolBar.add(lblTitle);

        JTextField textFieldTitle = new JTextField(title);
        // textFieldTitle.setToolTipText(title);
        // textFieldTitle.setName(title);
        // textFieldTitle.setEditable(false);
        toolBar.add(textFieldTitle);
        textFieldTitle.setBorder(new EmptyBorder(0, 0, 0, 0));

        textFieldTitleMap.put(textFieldTitle, title);

        // JButton btnRename = new JButton(Messages.getString("GroupBrowser.btn_rename")); //$NON-NLS-1$
        // // btnRename.setIcon(renameIcon);
        // btnRename.addActionListener(new ActionListener() {
        //
        // public void actionPerformed(ActionEvent e) {
        // if (!textFieldTitle.getToolTipText().equals(textFieldTitle.getText())) {
        // String originName = textFieldTitle.getToolTipText();
        // photoGroup.put(textFieldTitle.getText(), photoGroup.get(originName));
        // photoGroup.remove(originName);
        // textFieldTitle.setToolTipText(textFieldTitle.getText());
        // }
        // }
        // });
        //
        // toolBar.add(btnRename);

        JPanel panelFlow = new JPanel();
        panelFlow.setBorder(new EmptyBorder(0, 0, 0, 0));
        panelFlow.setLayout(WRAP_LAYOUT_FLOW);
        // panelFlow.setBorder(new LineBorder(new Color(0, 0, 0)));
        panelFlow.setBackground(Color.WHITE);
        // FlowLayout flowLayout = (FlowLayout) panelFlow.getLayout();
        // flowLayout.setAlignment(FlowLayout.LEFT);

        panelGroup1.add(panelFlow, UIUilt.createGridBagConstraints(GridBagConstraints.HORIZONTAL, -1, null, 0, 1));

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

        // textFieldTitle.getDocument().addDocumentListener(new DocumentListener() {
        //
        // public void changedUpdate(DocumentEvent e) {
        // // whatever you want
        // }
        //
        // public void removeUpdate(DocumentEvent e) {
        // // whatever you want
        // }
        //
        // public void insertUpdate(DocumentEvent e) {
        // // whatever you want
        // // String originName = textFieldTitle.getToolTipText();
        // // photoGroup.put(textFieldTitle.getText(), photoGroup.get(originName));
        // // photoGroup.remove(originName);
        // // textFieldTitle.setToolTipText(textFieldTitle.getText());
        // }
        // });

        for (File photo : files) {
            addImagePanel(panelFlow, photo);
        }

        // panelFlow.doLayout();
        // return panelFlow;
    }

    private void addImagePanel(JPanel panelFlow, File photo) {
        JPanel panel = new JPanel();
        panel.setBorder(IMAGE_PANEL_BORDER);
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
            labelImg.setIcon(documentEmptyImageIcon);
        }
        final boolean isImage = buffered;
        panel.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == 1 && e.getClickCount() == 1) {
                    if (panelSelectedImage != null) {
                        panelSelectedImage.setBorder(IMAGE_PANEL_BORDER);
                    }
                    panel.setBorder(IMAGE_PANEL_SELECTED_BORDER);
                    panelSelectedImage = panel;
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

    // private BufferedImage resizeImageToPreview(BufferedImage originalImage) {
    // int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
    //
    // int width = originalImage.getWidth();
    // int height = originalImage.getHeight();
    // int x = (width > height) ? PREVIEW_SIZE : width / height * PREVIEW_SIZE;
    // int y = (width > height) ? height / width * PREVIEW_SIZE : PREVIEW_SIZE;
    //
    // BufferedImage resizedImage = new BufferedImage(x, y, type);
    // Graphics2D g = resizedImage.createGraphics();
    // g.drawImage(originalImage, 0, 0, x, y, null);
    // g.dispose();
    // return resizedImage;
    // }

    private void profile() {
        String errMsg = GroupBrowserHelper.checkFolder(textFieldFolder.getText());
        if (errMsg == null) {
            doProfile();
            HistoryDirectory.INSTANCE.addDirectory(textFieldFolder.getText());
            refreshMenuFile();
        } else {
            // popup error
            JOptionPane.showMessageDialog(frameGroupBrowser, errMsg, Messages.getString("GroupBrowser.profile"), //$NON-NLS-1$
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshMenuFile() {
        menuBar.remove(0);
        createMenuFile();
    }

    private void doProfile() {
        btnProfile.setEnabled(false);
        photoGroup = new HashMap<String, List<File>>();

        PhotoGroup groupThread = new PhotoGroup(photoGroup, textFieldFolder.getText(),
                /* SettingStore.getSettingStore().getThreshold(), */ SettingStore.getSettingStore().getModule(),
                SettingStore.getSettingStore().getFormat(), SettingStore.getSettingStore().isGuess(),
                SettingStore.getSettingStore().isGps(), /* SettingStore.getSettingStore().isReport(), */
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
                final List<String> sortTitles = GroupBrowserHelper.getGroupTitlesByDate(photoGroup);

                for (String title : sortTitles) {
                    createImageGroup(title, photoGroup.get(title));
                }
                layoutGroupPanel();
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

        startLogMonitor();

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

    private void startLogMonitor() {
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
    }

    private void doSave() {
        // update photo group
        Set<Entry<JTextField, String>> set = textFieldTitleMap.entrySet();
        for (Entry<JTextField, String> entry : set) {
            JTextField textFieldTitle = entry.getKey();
            String newTitle = textFieldTitle.getText();
            String oldTitle = entry.getValue();
            if (!newTitle.equals(oldTitle)) {
                photoGroup.put(newTitle, photoGroup.get(oldTitle));
                photoGroup.remove(oldTitle);
            }
        }

        FileUtil.movePhotos(textFieldFolder.getText(), photoGroup);
        btnSave.setEnabled(false);
    }

    private void layoutGroupPanel() {
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
    }

}
