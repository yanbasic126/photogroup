package com.photogroup.ui.browser;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultCaret;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.metadata.exif.ExifThumbnailDirectory;
import com.photogroup.PhotoGroup;
import com.photogroup.exception.ExceptionHandler;
import com.photogroup.ui.Messages;
import com.photogroup.ui.SettingStore;
import com.photogroup.ui.dialog.SettingDialog;
import com.photogroup.ui.layout.WrapLayout;
import com.photogroup.util.FileUtil;

public class GroupBrowser {

    private JFrame frameGroupBrowser;

    private JTextField textFieldFolder;

    private JProgressBar progressBar;

    private JPanel panelGroupAll;

    private static int PREVIEW_SIZE = 168;

    private ImageIcon documentEmptyImageIcon;

    private ImageIcon upIcon;

    private ImageIcon downIcon;

    private ImageIcon renameIcon;

    private JButton btnOpen;

    private HashMap<String, List<File>> photoGroup;

    private JTextArea txtDebugLog;

    private JPanel panelDebugLog;

    private JScrollPane scrollPaneDebug;

    private JPanel panelDebug;

    private JPanel panelStatus;

    private static final List<String> systemLogOutput = Collections.synchronizedList(new ArrayList<String>());

    private static boolean systemLogThreadStart = false;

    private PrintStream systemOutRedirect;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {

            public void run() {
                try {
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
                systemLogOutput.add(new String(b, off, len));
            }
        });
        System.setErr(systemOutRedirect);
        System.setOut(systemOutRedirect);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
            ExceptionHandler.logError(e.getMessage());
        }
        try {
            BufferedImage documentEmptyImage = ImageIO.read(ClassLoader.getSystemResource("icon/document_empty_64.png"));
            documentEmptyImageIcon = new ImageIcon(documentEmptyImage);
            upIcon = new ImageIcon(ImageIO.read(ClassLoader.getSystemResource("icon/Up.png")));
            downIcon = new ImageIcon(ImageIO.read(ClassLoader.getSystemResource("icon/Down.png")));
            renameIcon = new ImageIcon(ImageIO.read(ClassLoader.getSystemResource("icon/Blue_tag.png")));
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
        frameGroupBrowser.setBounds(100, 100, 700, 500);
        frameGroupBrowser.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameGroupBrowser.setTitle("Photo Group");
        JMenuBar menuBar = new JMenuBar();
        frameGroupBrowser.setJMenuBar(menuBar);

        JMenu mnHelpMenu = new JMenu("Help");
        menuBar.add(mnHelpMenu);

        JMenuItem mntmHelpItem = new JMenuItem("Help");
        mnHelpMenu.add(mntmHelpItem);

        JMenuItem mntmAboutItem = new JMenuItem("About");
        mnHelpMenu.add(mntmAboutItem);
        String buildVersion = FileUtil.getBuildVersion();
        if (buildVersion != null) {
            JMenuItem mntmVersionItem = new JMenuItem(buildVersion);
            mnHelpMenu.add(mntmVersionItem);
        }
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

        btnOpen = new JButton("Profile");

        try {
            BufferedImage bufferedImage = ImageIO.read(ClassLoader.getSystemResource("icon/publish.png"));
            btnOpen.setIcon(new ImageIcon(bufferedImage));
        } catch (IOException e) {
            e.printStackTrace();
            ExceptionHandler.logError(e.getMessage());
        }

        btnOpen.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String errMsg = validateBeforeRun();
                if (errMsg == null) {
                    doRun();
                } else {
                    // popup error
                    JOptionPane.showMessageDialog(frameGroupBrowser, errMsg, "Profile", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton btnSetting = new JButton("Setting");

        try {
            BufferedImage bufferedImage = ImageIO.read(ClassLoader.getSystemResource("icon/settings.png"));
            btnSetting.setIcon(new ImageIcon(bufferedImage));
        } catch (IOException e) {
            ExceptionHandler.logError(e.getMessage());
            e.printStackTrace();
        }

        btnSetting.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                SettingDialog dialog = new SettingDialog();
                dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                dialog.setVisible(true);
            }
        });

        JButton btnMove = new JButton("Save");

        try {
            BufferedImage bufferedImage = ImageIO.read(ClassLoader.getSystemResource("icon/upcoming_work.png"));
            btnMove.setIcon(new ImageIcon(bufferedImage));
        } catch (IOException e) {
            e.printStackTrace();
            ExceptionHandler.logError(e.getMessage());
        }

        btnMove.addActionListener(new ActionListener() {

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

        JToggleButton btnLogs = new JToggleButton("Log");
        btnLogs.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                panelDebug.setVisible(!panelDebug.isVisible());
            }
        });

        try {
            BufferedImage bufferedImage = ImageIO.read(ClassLoader.getSystemResource("icon/featured.png"));
            btnLogs.setIcon(new ImageIcon(bufferedImage));
        } catch (IOException e) {
            e.printStackTrace();
            ExceptionHandler.logError(e.getMessage());
        }

        JSeparator separator_1 = new JSeparator();
        separator_1.setOrientation(SwingConstants.VERTICAL);
        toolBarSetting.add(separator_1);

        toolBarSetting.add(btnOpen);
        toolBarSetting.add(btnMove);

        JSeparator separator = new JSeparator();
        separator.setOrientation(SwingConstants.VERTICAL);
        toolBarSetting.add(separator);
        toolBarSetting.add(btnSetting);
        toolBarSetting.add(btnLogs);

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
                    File folder = new File(textFieldFolder.getText());
                    if (folder.exists()) {
                        Desktop.getDesktop().open(new File(textFieldFolder.getText()));
                    } else {
                        JOptionPane.showMessageDialog(frameGroupBrowser, "Folder doesn't exist.", "Folder",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                    ExceptionHandler.logError(ex.getMessage());
                }
            }

        });
        try {
            BufferedImage bufferedImage = ImageIO.read(ClassLoader.getSystemResource("icon/Folder.png"));
            btnFolder.setIcon(new ImageIcon(bufferedImage));
        } catch (IOException e) {
            e.printStackTrace();
            ExceptionHandler.logError(e.getMessage());
        }
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

        textFieldFolder = new JTextField();
        textFieldFolder.setText("");
        int twidth = (int) textFieldFolder.getPreferredSize().getWidth();
        textFieldFolder.setPreferredSize(new Dimension(twidth, size));
        textFieldFolder.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (10 == e.getKeyCode()) {
                    doRun();
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
        gbl_panelGroupAll.rowWeights = new double[] { 0.0 };
        panelGroupAll.setLayout(gbl_panelGroupAll);

        JScrollPane scrollPane = new JScrollPane(panelGroupAll);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        GridBagConstraints gbc_scrollPane = new GridBagConstraints();
        gbc_scrollPane.fill = GridBagConstraints.BOTH;
        gbc_scrollPane.gridx = 0;
        gbc_scrollPane.gridy = 0;
        // panelBrowser.add(panelGroupAll, gbc_panelGroupAll);
        panelBrowser.add(scrollPane, gbc_scrollPane);

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
        txtDebugLog = new JTextArea();
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
        panelDebug.setVisible(false);

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
        btnExpand.setIcon(downIcon);
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
                String originName = textFieldTitle.getName();
                photoGroup.put(textFieldTitle.getText(), photoGroup.get(originName));
                photoGroup.remove(originName);
                textFieldTitle.setName(textFieldTitle.getText());
            }
        });

        toolBar.add(btnRename);

        JPanel panelFlow = new JPanel();
        WrapLayout wl_panelFlow = new WrapLayout();
        wl_panelFlow.setAlignment(FlowLayout.LEFT);
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
                    btnExpand.setIcon(upIcon);
                    panelFlow.setVisible(false);
                } else {
                    btnExpand.setIcon(downIcon);
                    panelFlow.setVisible(true);
                }
            }
        });

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
        panel.setBorder(new LineBorder(new Color(0, 0, 0)));
        GridBagLayout gbl_panel = new GridBagLayout();
        gbl_panel.columnWidths = new int[] { 0 };
        gbl_panel.rowHeights = new int[] { 23, 23 };
        gbl_panel.columnWeights = new double[] { 0.0 };
        gbl_panel.rowWeights = new double[] { 0.0, 0.0 };
        panel.setLayout(gbl_panel);
        panelFlow.add(panel);

        JLabel labelImg = new JLabel();
        labelImg.setPreferredSize(new Dimension(PREVIEW_SIZE, PREVIEW_SIZE));
        ExifThumbnailDirectory thumbDirectory = null;
        BufferedImage bufferedImage = null;
        try {
            thumbDirectory = JpegMetadataReader.readMetadata(photo).getFirstDirectoryOfType(ExifThumbnailDirectory.class);
        } catch (JpegProcessingException | IOException e) {
            e.printStackTrace();
            ExceptionHandler.logError(photo.getAbsolutePath() + "|" + e.getMessage());
        }

        try {
            if (thumbDirectory != null && thumbDirectory.getThumbnailData() != null) {
                InputStream thumbByteStream = new ByteArrayInputStream(thumbDirectory.getThumbnailData());
                // bufferedImage = ImageIO.read(thumbByteStream);
                bufferedImage = ImageIO.read(photo);
            } else {
                bufferedImage = ImageIO.read(photo);
            }
        } catch (IOException e) {
            e.printStackTrace();
            ExceptionHandler.logError(photo.getAbsolutePath() + "|" + e.getMessage());
        }

        if (bufferedImage != null) {
            ImageIcon icon = new ImageIcon(resizeImageToPreviewIPhone(bufferedImage));
            labelImg.setIcon(icon);
        } else {
            labelImg.setIcon(documentEmptyImageIcon);
        }

        GridBagConstraints gbc_label = new GridBagConstraints();
        gbc_label.anchor = GridBagConstraints.NORTHWEST;
        gbc_label.gridx = 0;
        gbc_label.gridy = 0;
        panel.add(labelImg, gbc_label);

        JLabel labelName = new JLabel(photo.getName());
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
        String errorMsg = null;
        if (textFieldFolder.getText().isEmpty()) {
            errorMsg = "Error! Please select a folder.";
        } else {
            if (!new File(textFieldFolder.getText()).exists()) {
                errorMsg = "Error! File does not exist.";
            }
        }
        return errorMsg;
    }

    private void doRun() {
        btnOpen.setEnabled(false);
        photoGroup = new HashMap<String, List<File>>();

        PhotoGroup groupThread = new PhotoGroup(photoGroup, textFieldFolder.getText(),
                SettingStore.getSettingStore().getThreshold(), SettingStore.getSettingStore().getModule(),
                SettingStore.getSettingStore().getFormat(), SettingStore.getSettingStore().isGuess(),
                SettingStore.getSettingStore().isGps(), SettingStore.getSettingStore().isReport());
        ExecutorService exe = Executors.newFixedThreadPool(1);
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (!exe.isTerminated()) {
                    int precent = Integer.parseInt(groupThread.processPrecent.toString());
                    progressBar.setValue(precent);
                }
                panelGroupAll.removeAll();
                Iterator<?> it = photoGroup.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, List<File>> pair = (Entry<String, List<File>>) it.next();
                    createImageGroup(pair);
                }

                panelGroupAll.setVisible(false);
                panelGroupAll.setVisible(true);
                btnOpen.setEnabled(true);
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
                        if (txtDebugLog != null && panelDebug.isVisible() && !systemLogOutput.isEmpty()) {
                            StringBuffer sb = new StringBuffer(txtDebugLog.getText());
                            for (String s : systemLogOutput) {
                                sb.append(s);
                            }
                            txtDebugLog.setText(sb.toString());
                            systemLogOutput.clear();
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
    }
}
