package com.photogroup.ui;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.ProgressMonitor;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.TitledBorder;

import com.photogroup.PhotoGroup;
import com.photogroup.ui.dialog.GroupResultDialog;
import com.photogroup.util.FileUtil;

/*
 * Replaced with GroupBrowser
 */
@Deprecated
public class PhotoGroupWindow {

    private JFrame frmPhotoGroupGui;

    private JTextField textField;

    private JSpinner spinnerThreshold;

    private ButtonGroup btnGrpModel;

    private JComboBox comboBoxFormat;

    private JCheckBox chckbxGuess;

    private JCheckBox chckbxGPS;

    private JProgressBar progressBar;

    private JButton btnRun;

    private ProgressMonitor progressMonitor;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                try {
                    PhotoGroupWindow window = new PhotoGroupWindow();
                    window.frmPhotoGroupGui.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public PhotoGroupWindow() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - 700) / 2);
        int y = (int) ((dimension.getHeight() - 350) / 2);
        frmPhotoGroupGui = new JFrame();
        frmPhotoGroupGui.setTitle(Messages.getString("PhotoGroupWindow.0")); //$NON-NLS-1$
        frmPhotoGroupGui.setBounds(0, 0, 800, 400);
        // frmPhotoGroupGui.setBounds(x, y, 700, 350);
        frmPhotoGroupGui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmPhotoGroupGui.getContentPane().setLayout(new GridLayout(1, 0, 0, 0));

        JPanel panel = new JPanel();
        frmPhotoGroupGui.getContentPane().add(panel);
        GridBagLayout gbl_panel = new GridBagLayout();
        panel.setLayout(gbl_panel);

        JPanel panel_0 = new JPanel();
        GridBagConstraints gbc_panel_0 = new GridBagConstraints();
        gbc_panel_0.anchor = GridBagConstraints.WEST;
        gbc_panel_0.insets = new Insets(0, 0, 0, 0);
        panel.add(panel_0, gbc_panel_0);
        GridBagLayout gbl_panel_0 = new GridBagLayout();
        gbl_panel_0.columnWidths = new int[] { 50, 50, 50 };
        gbl_panel_0.rowHeights = new int[] { 30 };
        panel_0.setLayout(gbl_panel_0);

        JButton btnPreview = new JButton(Messages.getString("PhotoGroupWindow.1")); //$NON-NLS-1$
        btnPreview.setEnabled(false);
        panel_0.add(btnPreview);

        btnRun = new JButton(Messages.getString("PhotoGroupWindow.2"));
        GridBagConstraints gbc_btnRun = new GridBagConstraints();
        panel_0.add(btnRun);
        btnRun.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String errMsg = validateBeforeRun();
                if (errMsg == null) {
                    doRun();
                } else {
                    // popup error
                    JOptionPane.showMessageDialog(frmPhotoGroupGui, errMsg, "Run", JOptionPane.ERROR_MESSAGE);
                }
            }

        });

        JButton btnOpenReport = new JButton(Messages.getString("PhotoGroupWindow.3")); //$NON-NLS-1$
        btnOpenReport.setEnabled(false);
        panel_0.add(btnOpenReport);

        JPanel panel_1 = new JPanel();
        GridBagConstraints gbc_panel_1 = new GridBagConstraints();
        gbc_panel_1.anchor = GridBagConstraints.WEST;
        gbc_panel_1.insets = new Insets(0, 0, 0, 0);
        gbc_panel_1.gridy = 1;
        panel.add(panel_1, gbc_panel_1);
        GridBagLayout gbl_panel_1 = new GridBagLayout();
        gbl_panel_1.columnWidths = new int[] { 50, 50, 80 };
        gbl_panel_1.rowHeights = new int[] { 30 };
        panel_1.setLayout(gbl_panel_1);

        JLabel lblNewLabel = new JLabel(Messages.getString("PhotoGroupWindow.4")); //$NON-NLS-1$
        panel_1.add(lblNewLabel);

        textField = new JTextField();
        textField.setColumns(40);
        panel_1.add(textField);

        JButton btnBrowse = new JButton(Messages.getString("PhotoGroupWindow.5")); //$NON-NLS-1$
        btnBrowse.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                if (!textField.getText().isEmpty()) {
                    File choicedDir = new File(textField.getText());
                    if (choicedDir.exists()) {
                        chooser.setCurrentDirectory(choicedDir);
                    }
                }
                chooser.setDialogTitle(Messages.getString("PhotoGroupWindow.6")); //$NON-NLS-1$
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.setAcceptAllFileFilterUsed(false);
                if (chooser.showOpenDialog(frmPhotoGroupGui) == JFileChooser.APPROVE_OPTION) {
                    textField.setText(chooser.getSelectedFile().getAbsolutePath());
                }
            }

        });
        panel_1.add(btnBrowse);

        JPanel panel_2 = new JPanel();
        panel_2.setBorder(new TitledBorder(Messages.getString("PhotoGroupWindow.7"))); //$NON-NLS-1$
        GridBagLayout gbl_panel_2 = new GridBagLayout();
        gbl_panel_2.columnWidths = new int[] { 5, 0, 0, 0, 5 };
        gbl_panel_2.rowHeights = new int[] { 0, 30, 30, 30, 30, 30, 30, 0 };
        gbl_panel_2.columnWeights = new double[] { 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
        gbl_panel_2.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
        panel_2.setLayout(gbl_panel_2);

        JLabel lblNewLabel_1 = new JLabel(Messages.getString("PhotoGroupWindow.8")); //$NON-NLS-1$
        GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
        gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel_1.gridx = 1;
        gbc_lblNewLabel_1.gridy = 1;
        panel_2.add(lblNewLabel_1, gbc_lblNewLabel_1);

        SpinnerModel model = new SpinnerNumberModel(1, 1, 100, 1);
        spinnerThreshold = new JSpinner(model);
        spinnerThreshold.setValue(1);
        GridBagConstraints gbc_spinner = new GridBagConstraints();
        gbc_spinner.anchor = GridBagConstraints.WEST;
        gbc_spinner.insets = new Insets(0, 0, 5, 5);
        gbc_spinner.gridx = 2;
        gbc_spinner.gridy = 1;
        panel_2.add(spinnerThreshold, gbc_spinner);

        JLabel lblNewLabel_2 = new JLabel(Messages.getString("PhotoGroupWindow.9")); //$NON-NLS-1$
        GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
        gbc_lblNewLabel_2.anchor = GridBagConstraints.WEST;
        gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 0);
        gbc_lblNewLabel_2.gridx = 3;
        gbc_lblNewLabel_2.gridy = 1;
        panel_2.add(lblNewLabel_2, gbc_lblNewLabel_2);

        JLabel lblNewLabel_3 = new JLabel(Messages.getString("PhotoGroupWindow.10")); //$NON-NLS-1$
        GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
        gbc_lblNewLabel_3.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel_3.gridx = 1;
        gbc_lblNewLabel_3.gridy = 2;
        panel_2.add(lblNewLabel_3, gbc_lblNewLabel_3);

        comboBoxFormat = new JComboBox();
        comboBoxFormat.setModel(new DefaultComboBoxModel(new String[] { "YYYY.M.d", "YYYY.MM.dd", "M.d", "MM.dd" })); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        comboBoxFormat.setEditable(true);
        GridBagConstraints gbc_comboBox = new GridBagConstraints();
        gbc_comboBox.anchor = GridBagConstraints.WEST;
        gbc_comboBox.insets = new Insets(0, 0, 5, 5);
        gbc_comboBox.gridx = 2;
        gbc_comboBox.gridy = 2;
        panel_2.add(comboBoxFormat, gbc_comboBox);

        JLabel lblNewLabel_4 = new JLabel(Messages.getString("PhotoGroupWindow.15")); //$NON-NLS-1$
        GridBagConstraints gbc_lblNewLabel_4 = new GridBagConstraints();
        gbc_lblNewLabel_4.anchor = GridBagConstraints.WEST;
        gbc_lblNewLabel_4.insets = new Insets(0, 0, 5, 0);
        gbc_lblNewLabel_4.gridx = 3;
        gbc_lblNewLabel_4.gridy = 2;
        panel_2.add(lblNewLabel_4, gbc_lblNewLabel_4);

        JLabel lblNewLabel_5 = new JLabel(Messages.getString("PhotoGroupWindow.16")); //$NON-NLS-1$
        GridBagConstraints gbc_lblNewLabel_5 = new GridBagConstraints();
        gbc_lblNewLabel_5.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel_5.gridx = 1;
        gbc_lblNewLabel_5.gridy = 3;
        panel_2.add(lblNewLabel_5, gbc_lblNewLabel_5);

        JPanel panel_4 = new JPanel();
        GridBagConstraints gbc_panel_4 = new GridBagConstraints();
        gbc_panel_4.anchor = GridBagConstraints.WEST;
        gbc_panel_4.insets = new Insets(0, 0, 5, 5);
        gbc_panel_4.gridx = 2;
        gbc_panel_4.gridy = 3;
        panel_2.add(panel_4, gbc_panel_4);
        panel_4.setLayout(new GridLayout(1, 1, 0, 0));

        JRadioButton rdbtn1 = new JRadioButton("1"); //$NON-NLS-1$
        panel_4.add(rdbtn1);

        JRadioButton rdbtn2 = new JRadioButton("2"); //$NON-NLS-1$
        panel_4.add(rdbtn2);

        JRadioButton rdbtn3 = new JRadioButton("3"); //$NON-NLS-1$
        rdbtn3.setSelected(true);
        panel_4.add(rdbtn3);

        btnGrpModel = new ButtonGroup();
        btnGrpModel.add(rdbtn1);
        btnGrpModel.add(rdbtn2);
        btnGrpModel.add(rdbtn3);

        JLabel lblNewLabel_6 = new JLabel(Messages.getString("PhotoGroupWindow.20")); //$NON-NLS-1$
        GridBagConstraints gbc_lblNewLabel_6 = new GridBagConstraints();
        gbc_lblNewLabel_6.anchor = GridBagConstraints.WEST;
        gbc_lblNewLabel_6.insets = new Insets(0, 0, 5, 0);
        gbc_lblNewLabel_6.gridx = 3;
        gbc_lblNewLabel_6.gridy = 3;
        panel_2.add(lblNewLabel_6, gbc_lblNewLabel_6);

        JLabel lblNewLabel_7 = new JLabel(Messages.getString("PhotoGroupWindow.21")); //$NON-NLS-1$
        GridBagConstraints gbc_lblNewLabel_7 = new GridBagConstraints();
        gbc_lblNewLabel_7.fill = GridBagConstraints.VERTICAL;
        gbc_lblNewLabel_7.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel_7.gridx = 1;
        gbc_lblNewLabel_7.gridy = 4;
        panel_2.add(lblNewLabel_7, gbc_lblNewLabel_7);

        chckbxGuess = new JCheckBox();
        chckbxGuess.setSelected(true);
        GridBagConstraints gbc_chckbxNewCheckBox = new GridBagConstraints();
        gbc_chckbxNewCheckBox.anchor = GridBagConstraints.WEST;
        gbc_chckbxNewCheckBox.fill = GridBagConstraints.VERTICAL;
        gbc_chckbxNewCheckBox.insets = new Insets(0, 0, 5, 5);
        gbc_chckbxNewCheckBox.gridx = 2;
        gbc_chckbxNewCheckBox.gridy = 4;
        panel_2.add(chckbxGuess, gbc_chckbxNewCheckBox);

        JLabel lblNewLabel_8 = new JLabel(Messages.getString("PhotoGroupWindow.22")); //$NON-NLS-1$
        GridBagConstraints gbc_lblNewLabel_8 = new GridBagConstraints();
        gbc_lblNewLabel_8.anchor = GridBagConstraints.WEST;
        gbc_lblNewLabel_8.fill = GridBagConstraints.VERTICAL;
        gbc_lblNewLabel_8.insets = new Insets(0, 0, 5, 0);
        gbc_lblNewLabel_8.gridx = 3;
        gbc_lblNewLabel_8.gridy = 4;
        panel_2.add(lblNewLabel_8, gbc_lblNewLabel_8);

        JLabel lblNewLabel_9 = new JLabel(Messages.getString("PhotoGroupWindow.23")); //$NON-NLS-1$
        GridBagConstraints gbc_lblNewLabel_9 = new GridBagConstraints();
        gbc_lblNewLabel_9.fill = GridBagConstraints.VERTICAL;
        gbc_lblNewLabel_9.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel_9.gridx = 1;
        gbc_lblNewLabel_9.gridy = 5;
        panel_2.add(lblNewLabel_9, gbc_lblNewLabel_9);

        chckbxGPS = new JCheckBox();
        chckbxGPS.setSelected(true);
        GridBagConstraints gbc_chckbxNewCheckBox_1 = new GridBagConstraints();
        gbc_chckbxNewCheckBox_1.anchor = GridBagConstraints.WEST;
        gbc_chckbxNewCheckBox_1.fill = GridBagConstraints.VERTICAL;
        gbc_chckbxNewCheckBox_1.insets = new Insets(0, 0, 5, 5);
        gbc_chckbxNewCheckBox_1.gridx = 2;
        gbc_chckbxNewCheckBox_1.gridy = 5;
        panel_2.add(chckbxGPS, gbc_chckbxNewCheckBox_1);

        JLabel lblNewLabel_10 = new JLabel(Messages.getString("PhotoGroupWindow.24")); //$NON-NLS-1$
        GridBagConstraints gbc_lblNewLabel_10 = new GridBagConstraints();
        gbc_lblNewLabel_10.anchor = GridBagConstraints.WEST;
        gbc_lblNewLabel_10.fill = GridBagConstraints.VERTICAL;
        gbc_lblNewLabel_10.insets = new Insets(0, 0, 5, 0);
        gbc_lblNewLabel_10.gridx = 3;
        gbc_lblNewLabel_10.gridy = 5;
        panel_2.add(lblNewLabel_10, gbc_lblNewLabel_10);

        JLabel lblNewLabel_11 = new JLabel(Messages.getString("PhotoGroupWindow.25")); //$NON-NLS-1$
        GridBagConstraints gbc_lblNewLabel_11 = new GridBagConstraints();
        gbc_lblNewLabel_11.fill = GridBagConstraints.VERTICAL;
        gbc_lblNewLabel_11.insets = new Insets(0, 0, 0, 5);
        gbc_lblNewLabel_11.gridx = 1;
        gbc_lblNewLabel_11.gridy = 6;
        panel_2.add(lblNewLabel_11, gbc_lblNewLabel_11);

        JCheckBox chckbxNewCheckBox_2 = new JCheckBox();
        chckbxNewCheckBox_2.setSelected(true);
        GridBagConstraints gbc_chckbxNewCheckBox_2 = new GridBagConstraints();
        gbc_chckbxNewCheckBox_2.anchor = GridBagConstraints.WEST;
        gbc_chckbxNewCheckBox_2.fill = GridBagConstraints.VERTICAL;
        gbc_chckbxNewCheckBox_2.insets = new Insets(0, 0, 0, 5);
        gbc_chckbxNewCheckBox_2.gridx = 2;
        gbc_chckbxNewCheckBox_2.gridy = 6;
        panel_2.add(chckbxNewCheckBox_2, gbc_chckbxNewCheckBox_2);

        JLabel lblNewLabel_12 = new JLabel(Messages.getString("PhotoGroupWindow.26")); //$NON-NLS-1$
        GridBagConstraints gbc_lblNewLabel_12 = new GridBagConstraints();
        gbc_lblNewLabel_12.anchor = GridBagConstraints.WEST;
        gbc_lblNewLabel_12.fill = GridBagConstraints.VERTICAL;
        gbc_lblNewLabel_12.gridx = 3;
        gbc_lblNewLabel_12.gridy = 6;
        panel_2.add(lblNewLabel_12, gbc_lblNewLabel_12);
        GridBagConstraints gbc_panel_2 = new GridBagConstraints();
        gbc_panel_2.anchor = GridBagConstraints.NORTH;
        gbc_panel_2.fill = GridBagConstraints.HORIZONTAL;
        gbc_panel_2.insets = new Insets(0, 0, 5, 0);
        gbc_panel_2.gridx = 0;
        gbc_panel_2.gridy = 2;
        panel.add(panel_2, gbc_panel_2);

        JPanel panel_3 = new JPanel();
        panel_3.setLayout(new GridLayout(0, 1, 0, 0));

        progressBar = new JProgressBar();
        progressBar.setValue(0);
        panel_3.add(progressBar);
        GridBagConstraints gbc_panel_3 = new GridBagConstraints();
        gbc_panel_3.fill = GridBagConstraints.BOTH;
        gbc_panel_3.gridx = 0;
        gbc_panel_3.gridy = 3;
        panel.add(panel_3, gbc_panel_3);
    }

    protected String validateBeforeRun() {
        String errorMsg = null;
        if (textField.getText().isEmpty()) {
            errorMsg = "Error! Please select a folder.";
        } else {
            if (!new File(textField.getText()).exists()) {
                errorMsg = "Error! fild does not exist.";
            }
        }
        return errorMsg;
    }

    private void doRun() {
        btnRun.setEnabled(false);
        // List<String> args = new ArrayList<String>();
        // args.add("--path");
        // args.add(textField.getText());
        // args.add("--threshold");
        // args.add(spinnerThreshold.getValue().toString());
        // args.add("--module");
        Enumeration<AbstractButton> radios = btnGrpModel.getElements();
        int model = 1;
        do {
            if (radios.nextElement().isSelected()) {
                break;
            }
            model++;
        } while (radios.hasMoreElements());
        final int model2 = model;
        // args.add(String.valueOf(model));
        // args.add("--format");
        // args.add(comboBoxFormat.getSelectedItem().toString());
        // if (chckbxGuess.isSelected()) {
        // args.add("--guess");
        // }
        // if (chckbxGPS.isSelected()) {
        // args.add("--gps");
        // }
        int threshold = Integer.parseInt(spinnerThreshold.getValue().toString());

        HashMap<String, List<File>> photoGroup = new HashMap<String, List<File>>();

        PhotoGroup groupThread = new PhotoGroup(photoGroup, textField.getText(), threshold, model2,
                comboBoxFormat.getSelectedItem().toString(), chckbxGuess.isSelected(), chckbxGPS.isSelected(), false);
        ExecutorService exe = Executors.newFixedThreadPool(1);

        new Thread(new Runnable() {

            @Override
            public void run() {
                while (!exe.isTerminated()) {
                    int precent = Integer.parseInt(groupThread.processPrecent.toString());
                    progressBar.setValue(precent);
                }
                btnRun.setEnabled(true);
                if ("OK".equals(showResultDialog(photoGroup))) {
                    FileUtil.movePhotos(textField.getText(), photoGroup);
                }
            }
        }).start();

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

    private String showResultDialog(Map<String, List<File>> groups) {
        GroupResultDialog dialog = new GroupResultDialog(groups);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
        return dialog.action;
    }

}
