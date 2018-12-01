package com.photogroup.ui.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import com.photogroup.ui.Messages;
import com.photogroup.ui.SettingStore;
import com.photogroup.util.ImageUtil;

public class SettingDialog extends JDialog {

    private final JPanel contentPanel = new JPanel();

    private JSpinner spinnerThreshold;

    private ButtonGroup btnGrpModel;

    private JComboBox comboBoxFormat;

    private JCheckBox chckbxGuess;

    private JCheckBox chckbxGPS;

    private JCheckBox chckbxSubfolder;

    private ImageIcon lemonIcon;

    // private JCheckBox chckbxReport;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            SettingDialog dialog = new SettingDialog();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the dialog.
     */
    public SettingDialog() {

        setTitle(Messages.getString("SettingDialog.this.title")); //$NON-NLS-1$
        try {
            lemonIcon = ImageUtil.getImageFromSystemResource("icon/lemon_32.png");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        setIconImage(lemonIcon.getImage());
        setModal(true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 830, 400);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        GridBagLayout gbl_contentPanel = new GridBagLayout();
        gbl_contentPanel.columnWidths = new int[] { 0, 0 };
        gbl_contentPanel.rowHeights = new int[] { 0, 0 };
        gbl_contentPanel.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
        gbl_contentPanel.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
        contentPanel.setLayout(gbl_contentPanel);
        {
            JPanel panel = new JPanel();
            GridBagConstraints gbc_panel = new GridBagConstraints();
            gbc_panel.fill = GridBagConstraints.BOTH;
            gbc_panel.gridx = 0;
            gbc_panel.gridy = 0;
            contentPanel.add(panel, gbc_panel);

            JPanel panel_2 = new JPanel();
            panel_2.setBorder(new TitledBorder(Messages.getString("PhotoGroupWindow.7"))); //$NON-NLS-1$
            GridBagLayout gbl_panel_2 = new GridBagLayout();
            gbl_panel_2.columnWidths = new int[] { 0, 0, 0, 5 };
            gbl_panel_2.rowHeights = new int[] { 30, 30, 30, 30, 30, 30, 0, 0 };
            gbl_panel_2.columnWeights = new double[] { 0.0, 0.0, 1.0, Double.MIN_VALUE };
            gbl_panel_2.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
            panel_2.setLayout(gbl_panel_2);

            JLabel lblNewLabel_1 = new JLabel(Messages.getString("PhotoGroupWindow.8")); //$NON-NLS-1$
            GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
            gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
            gbc_lblNewLabel_1.gridx = 0;
            gbc_lblNewLabel_1.gridy = 0;
            panel_2.add(lblNewLabel_1, gbc_lblNewLabel_1);

            SpinnerModel model = new SpinnerNumberModel(1, 1, 100, 1);
            spinnerThreshold = new JSpinner(model);
            spinnerThreshold.setValue(SettingStore.getSettingStore().getThreshold());
            GridBagConstraints gbc_spinner = new GridBagConstraints();
            gbc_spinner.anchor = GridBagConstraints.WEST;
            gbc_spinner.insets = new Insets(0, 0, 5, 5);
            gbc_spinner.gridx = 1;
            gbc_spinner.gridy = 0;
            panel_2.add(spinnerThreshold, gbc_spinner);

            JLabel lblNewLabel_2 = new JLabel(Messages.getString("PhotoGroupWindow.9")); //$NON-NLS-1$
            GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
            gbc_lblNewLabel_2.anchor = GridBagConstraints.WEST;
            gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 0);
            gbc_lblNewLabel_2.gridx = 2;
            gbc_lblNewLabel_2.gridy = 0;
            panel_2.add(lblNewLabel_2, gbc_lblNewLabel_2);

            JLabel lblNewLabel_3 = new JLabel(Messages.getString("PhotoGroupWindow.10")); //$NON-NLS-1$
            GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
            gbc_lblNewLabel_3.insets = new Insets(0, 0, 5, 5);
            gbc_lblNewLabel_3.gridx = 0;
            gbc_lblNewLabel_3.gridy = 1;
            panel_2.add(lblNewLabel_3, gbc_lblNewLabel_3);

            comboBoxFormat = new JComboBox();
            comboBoxFormat.setModel(new DefaultComboBoxModel(new String[] { "YYYY.M.d", "YYYY.MM.dd", "M.d", "MM.dd" })); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
            comboBoxFormat.setEditable(true);
            comboBoxFormat.setSelectedItem(SettingStore.getSettingStore().getFormat());
            GridBagConstraints gbc_comboBox = new GridBagConstraints();
            gbc_comboBox.anchor = GridBagConstraints.WEST;
            gbc_comboBox.insets = new Insets(0, 0, 5, 5);
            gbc_comboBox.gridx = 1;
            gbc_comboBox.gridy = 1;
            panel_2.add(comboBoxFormat, gbc_comboBox);

            JLabel lblNewLabel_4 = new JLabel(Messages.getString("PhotoGroupWindow.15")); //$NON-NLS-1$
            GridBagConstraints gbc_lblNewLabel_4 = new GridBagConstraints();
            gbc_lblNewLabel_4.anchor = GridBagConstraints.WEST;
            gbc_lblNewLabel_4.insets = new Insets(0, 0, 5, 0);
            gbc_lblNewLabel_4.gridx = 2;
            gbc_lblNewLabel_4.gridy = 1;
            panel_2.add(lblNewLabel_4, gbc_lblNewLabel_4);

            JLabel lblNewLabel_5 = new JLabel(Messages.getString("PhotoGroupWindow.16")); //$NON-NLS-1$
            GridBagConstraints gbc_lblNewLabel_5 = new GridBagConstraints();
            gbc_lblNewLabel_5.insets = new Insets(0, 0, 5, 5);
            gbc_lblNewLabel_5.gridx = 0;
            gbc_lblNewLabel_5.gridy = 2;
            panel_2.add(lblNewLabel_5, gbc_lblNewLabel_5);

            JPanel panel_4 = new JPanel();
            GridBagConstraints gbc_panel_4 = new GridBagConstraints();
            gbc_panel_4.anchor = GridBagConstraints.WEST;
            gbc_panel_4.insets = new Insets(0, 0, 5, 5);
            gbc_panel_4.gridx = 1;
            gbc_panel_4.gridy = 2;
            panel_2.add(panel_4, gbc_panel_4);
            panel_4.setLayout(new GridLayout(1, 1, 0, 0));

            JRadioButton rdbtn1 = new JRadioButton("1"); //$NON-NLS-1$
            panel_4.add(rdbtn1);

            JRadioButton rdbtn2 = new JRadioButton("2"); //$NON-NLS-1$
            panel_4.add(rdbtn2);

            JRadioButton rdbtn3 = new JRadioButton("3"); //$NON-NLS-1$
            panel_4.add(rdbtn3);

            int module = SettingStore.getSettingStore().getModule();
            if (module == 1) {
                rdbtn1.setSelected(true);
            } else if (module == 2) {
                rdbtn2.setSelected(true);
            } else if (module == 3) {
                rdbtn3.setSelected(true);
            }
            btnGrpModel = new ButtonGroup();
            btnGrpModel.add(rdbtn1);
            btnGrpModel.add(rdbtn2);
            btnGrpModel.add(rdbtn3);

            JLabel lblNewLabel_6 = new JLabel(Messages.getString("PhotoGroupWindow.20")); //$NON-NLS-1$
            GridBagConstraints gbc_lblNewLabel_6 = new GridBagConstraints();
            gbc_lblNewLabel_6.anchor = GridBagConstraints.WEST;
            gbc_lblNewLabel_6.insets = new Insets(0, 0, 5, 0);
            gbc_lblNewLabel_6.gridx = 2;
            gbc_lblNewLabel_6.gridy = 2;
            panel_2.add(lblNewLabel_6, gbc_lblNewLabel_6);

            JLabel lblNewLabel_7 = new JLabel(Messages.getString("PhotoGroupWindow.21")); //$NON-NLS-1$
            GridBagConstraints gbc_lblNewLabel_7 = new GridBagConstraints();
            gbc_lblNewLabel_7.fill = GridBagConstraints.VERTICAL;
            gbc_lblNewLabel_7.insets = new Insets(0, 0, 5, 5);
            gbc_lblNewLabel_7.gridx = 0;
            gbc_lblNewLabel_7.gridy = 3;
            panel_2.add(lblNewLabel_7, gbc_lblNewLabel_7);

            chckbxGuess = new JCheckBox();
            chckbxGuess.setSelected(SettingStore.getSettingStore().isGuess());
            GridBagConstraints gbc_chckbxNewCheckBox = new GridBagConstraints();
            gbc_chckbxNewCheckBox.anchor = GridBagConstraints.WEST;
            gbc_chckbxNewCheckBox.fill = GridBagConstraints.VERTICAL;
            gbc_chckbxNewCheckBox.insets = new Insets(0, 0, 5, 5);
            gbc_chckbxNewCheckBox.gridx = 1;
            gbc_chckbxNewCheckBox.gridy = 3;
            panel_2.add(chckbxGuess, gbc_chckbxNewCheckBox);

            JLabel lblNewLabel_8 = new JLabel(Messages.getString("PhotoGroupWindow.22")); //$NON-NLS-1$
            GridBagConstraints gbc_lblNewLabel_8 = new GridBagConstraints();
            gbc_lblNewLabel_8.anchor = GridBagConstraints.WEST;
            gbc_lblNewLabel_8.fill = GridBagConstraints.VERTICAL;
            gbc_lblNewLabel_8.insets = new Insets(0, 0, 5, 0);
            gbc_lblNewLabel_8.gridx = 2;
            gbc_lblNewLabel_8.gridy = 3;
            panel_2.add(lblNewLabel_8, gbc_lblNewLabel_8);

            JLabel lblNewLabel_9 = new JLabel(Messages.getString("PhotoGroupWindow.23")); //$NON-NLS-1$
            GridBagConstraints gbc_lblNewLabel_9 = new GridBagConstraints();
            gbc_lblNewLabel_9.fill = GridBagConstraints.VERTICAL;
            gbc_lblNewLabel_9.insets = new Insets(0, 0, 5, 5);
            gbc_lblNewLabel_9.gridx = 0;
            gbc_lblNewLabel_9.gridy = 4;
            panel_2.add(lblNewLabel_9, gbc_lblNewLabel_9);

            chckbxGPS = new JCheckBox();
            chckbxGPS.setSelected(SettingStore.getSettingStore().isGps());
            GridBagConstraints gbc_chckbxNewCheckBox_1 = new GridBagConstraints();
            gbc_chckbxNewCheckBox_1.anchor = GridBagConstraints.WEST;
            gbc_chckbxNewCheckBox_1.fill = GridBagConstraints.VERTICAL;
            gbc_chckbxNewCheckBox_1.insets = new Insets(0, 0, 5, 5);
            gbc_chckbxNewCheckBox_1.gridx = 1;
            gbc_chckbxNewCheckBox_1.gridy = 4;
            panel_2.add(chckbxGPS, gbc_chckbxNewCheckBox_1);

            JLabel lblNewLabel_10 = new JLabel(Messages.getString("PhotoGroupWindow.24")); //$NON-NLS-1$
            GridBagConstraints gbc_lblNewLabel_10 = new GridBagConstraints();
            gbc_lblNewLabel_10.anchor = GridBagConstraints.WEST;
            gbc_lblNewLabel_10.fill = GridBagConstraints.VERTICAL;
            gbc_lblNewLabel_10.insets = new Insets(0, 0, 5, 0);
            gbc_lblNewLabel_10.gridx = 2;
            gbc_lblNewLabel_10.gridy = 4;
            panel_2.add(lblNewLabel_10, gbc_lblNewLabel_10);

            JLabel lblNewLabel_11 = new JLabel(Messages.getString("PhotoGroupWindow.25")); //$NON-NLS-1$
            GridBagConstraints gbc_lblNewLabel_11 = new GridBagConstraints();
            gbc_lblNewLabel_11.fill = GridBagConstraints.VERTICAL;
            gbc_lblNewLabel_11.insets = new Insets(0, 0, 5, 5);
            gbc_lblNewLabel_11.gridx = 0;
            gbc_lblNewLabel_11.gridy = 5;
            panel_2.add(lblNewLabel_11, gbc_lblNewLabel_11);

            // chckbxReport = new JCheckBox();
            // chckbxReport.setSelected(SettingStore.getSettingStore().isReport());
            // GridBagConstraints gbc_chckbxReport = new GridBagConstraints();
            // gbc_chckbxReport.anchor = GridBagConstraints.WEST;
            // gbc_chckbxReport.fill = GridBagConstraints.VERTICAL;
            // gbc_chckbxReport.insets = new Insets(0, 0, 0, 5);
            // gbc_chckbxReport.gridx = 1;
            // gbc_chckbxReport.gridy = 5;
            // panel_2.add(chckbxReport, gbc_chckbxReport);

            JLabel lblNewLabel_12 = new JLabel(Messages.getString("PhotoGroupWindow.26")); //$NON-NLS-1$
            GridBagConstraints gbc_lblNewLabel_12 = new GridBagConstraints();
            gbc_lblNewLabel_12.insets = new Insets(0, 0, 5, 0);
            gbc_lblNewLabel_12.anchor = GridBagConstraints.WEST;
            gbc_lblNewLabel_12.fill = GridBagConstraints.VERTICAL;
            gbc_lblNewLabel_12.gridx = 2;
            gbc_lblNewLabel_12.gridy = 5;
            panel_2.add(lblNewLabel_12, gbc_lblNewLabel_12);
            GridBagConstraints gbc_panel_2 = new GridBagConstraints();
            gbc_panel_2.anchor = GridBagConstraints.NORTH;
            gbc_panel_2.fill = GridBagConstraints.HORIZONTAL;
            gbc_panel_2.insets = new Insets(0, 0, 5, 0);
            gbc_panel_2.gridx = 0;
            gbc_panel_2.gridy = 2;
            panel.add(panel_2, gbc_panel_2);

            JLabel lblIsSubLabel = new JLabel(Messages.getString("SettingDialog.lblIsSubLabel.text")); //$NON-NLS-1$
            GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
            gbc_lblNewLabel.anchor = GridBagConstraints.SOUTH;
            gbc_lblNewLabel.insets = new Insets(0, 0, 0, 5);
            gbc_lblNewLabel.gridx = 0;
            gbc_lblNewLabel.gridy = 6;
            panel_2.add(lblIsSubLabel, gbc_lblNewLabel);

            chckbxSubfolder = new JCheckBox(""); //$NON-NLS-1$
            GridBagConstraints gbc_chckbxSubfolderBox = new GridBagConstraints();
            gbc_chckbxSubfolderBox.anchor = GridBagConstraints.WEST;
            gbc_chckbxSubfolderBox.insets = new Insets(0, 0, 0, 5);
            gbc_chckbxSubfolderBox.gridx = 1;
            gbc_chckbxSubfolderBox.gridy = 6;
            panel_2.add(chckbxSubfolder, gbc_chckbxSubfolderBox);

            JLabel lblSubfolderLabel = new JLabel(Messages.getString("SettingDialog.lblSubfolderLabel.text")); //$NON-NLS-1$
            GridBagConstraints gbc_lblNewLabel_13 = new GridBagConstraints();
            gbc_lblNewLabel_13.anchor = GridBagConstraints.WEST;
            gbc_lblNewLabel_13.gridx = 2;
            gbc_lblNewLabel_13.gridy = 6;
            panel_2.add(lblSubfolderLabel, gbc_lblNewLabel_13);

            JPanel panel_3 = new JPanel();
            panel_3.setLayout(new GridLayout(0, 1, 0, 0));
        }
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                JButton okButton = new JButton("OK");
                okButton.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        SettingStore.getSettingStore().setThreshold(Integer.parseInt(spinnerThreshold.getValue().toString()));

                        Enumeration<AbstractButton> radios = btnGrpModel.getElements();
                        int model = 1;
                        do {
                            if (radios.nextElement().isSelected()) {
                                break;
                            }
                            model++;
                        } while (radios.hasMoreElements());
                        SettingStore.getSettingStore().setModule(model);
                        SettingStore.getSettingStore().setFormat(comboBoxFormat.getSelectedItem().toString());
                        SettingStore.getSettingStore().setGuess(chckbxGuess.isSelected());
                        SettingStore.getSettingStore().setGps(chckbxGPS.isSelected());
                        // SettingStore.getSettingStore().setReport(chckbxReport.isSelected());
                        SettingStore.getSettingStore().setIncludeSubFolder(chckbxSubfolder.isSelected());
                        dispose();
                    }
                });
                okButton.setActionCommand("OK");
                buttonPane.add(okButton);
                getRootPane().setDefaultButton(okButton);
            }
            {
                JButton cancelButton = new JButton("Cancel");
                cancelButton.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        dispose();
                    }
                });
                cancelButton.setActionCommand("Cancel");
                buttonPane.add(cancelButton);
            }
        }
    }

}
