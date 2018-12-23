package com.photogroup.ui.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
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
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;

import com.photogroup.ui.Messages;
import com.photogroup.ui.SettingStore;
import com.photogroup.ui.util.UIUilt;
import com.photogroup.util.ImageUtil;

public class SettingDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    private static final String[] FORMAT_TYPES = new String[] { "yyyy.M.d", "yyyy.MM.dd", "M.d", "MM.dd" };

    private final JPanel contentPanel = new JPanel();

    private JSpinner spinnerThreshold;

    private ButtonGroup btnGrpModel;

    private JComboBox<String> comboBoxFormat;

    private JCheckBox chckbxGuess;

    private JCheckBox chckbxGPS;

    private JCheckBox chckbxSubfolder;

    private JCheckBox chckbxThumbnail;

    private JTextField textFieldBaidu;

    private JTextField textFieldBing;

    private JTextField textFieldGoogle;

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
            ImageIcon lemonIcon = ImageUtil.getImageFromSystemResource(Messages.getString("SettingDialog.0")); //$NON-NLS-1$
            setIconImage(lemonIcon.getImage());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        setModal(true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 900, 400);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(UIUilt.createGridBagLayout(new int[] { 0, 0 }, new int[] { 0, 0 },
                new double[] { 1.0, Double.MIN_VALUE }, new double[] { 1.0, Double.MIN_VALUE }));
        {

            JTabbedPane tabUIPane = new JTabbedPane();
            tabUIPane.setTabPlacement(JTabbedPane.LEFT);

            JPanel groupingPanel = new JPanel();

            tabUIPane.add(Messages.getString("SettingDialog.group_tab"), groupingPanel); //$NON-NLS-1$
            contentPanel.add(tabUIPane, UIUilt.createGridBagConstraints(-1, GridBagConstraints.NORTHWEST, null, -1, -1));

            JPanel keysPanel = new JPanel();
            tabUIPane.add(Messages.getString("SettingDialog.key_tab"), keysPanel); //$NON-NLS-1$
            keysPanel.setLayout(UIUilt.createGridBagLayout(new int[] { 0, 526, 0, 0 }, new int[] { 0, 0, 0, 0, 0 },
                    new double[] { 0.0, 0.0, 1.0, Double.MIN_VALUE }, new double[] { 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE }));

            JLabel lblBaidu = new JLabel(Messages.getString("SettingDialog.lblBaidu.text_1")); //$NON-NLS-1$
            keysPanel.add(lblBaidu, UIUilt.createGridBagConstraints(-1, -1, new Insets(0, 0, 5, 5), 0, 0));

            textFieldBaidu = new JTextField();
            textFieldBaidu.setText(SettingStore.getSettingStore().getBaiduKey());
            keysPanel.add(textFieldBaidu,
                    UIUilt.createGridBagConstraints(GridBagConstraints.HORIZONTAL, -1, new Insets(0, 0, 5, 5), 1, 0));
            textFieldBaidu.setColumns(10);

            JLabel lblForBaiduAdress = UIUilt.createSimpleLinkLabel(Messages.getString("SettingDialog.lblForChineseAdress.text"), //$NON-NLS-1$
                    Messages.getString("SettingDialog.lblBaidu.url")); //$NON-NLS-1$
            keysPanel.add(lblForBaiduAdress,
                    UIUilt.createGridBagConstraints(-1, GridBagConstraints.WEST, new Insets(0, 0, 5, 0), 2, 0));

            JLabel lblBing = new JLabel(Messages.getString("SettingDialog.lblBing.text")); //$NON-NLS-1$
            keysPanel.add(lblBing, UIUilt.createGridBagConstraints(-1, -1, new Insets(0, 0, 5, 5), 0, 1));

            textFieldBing = new JTextField();
            textFieldBing.setText(SettingStore.getSettingStore().getBingKey());
            keysPanel.add(textFieldBing,
                    UIUilt.createGridBagConstraints(GridBagConstraints.HORIZONTAL, -1, new Insets(0, 0, 5, 5), 1, 1));
            textFieldBing.setColumns(10);

            JLabel lblBingAddress = UIUilt.createSimpleLinkLabel(Messages.getString("SettingDialog.lblEnglishAddress.text"), //$NON-NLS-1$
                    Messages.getString("SettingDialog.lblBing.url")); //$NON-NLS-1$
            keysPanel.add(lblBingAddress,
                    UIUilt.createGridBagConstraints(-1, GridBagConstraints.WEST, new Insets(0, 0, 5, 0), 2, 1));

            JLabel lblGoogle = new JLabel(Messages.getString("SettingDialog.lblGoogle.text")); //$NON-NLS-1$
            keysPanel.add(lblGoogle, UIUilt.createGridBagConstraints(-1, -1, new Insets(0, 0, 5, 5), 0, 2));

            textFieldGoogle = new JTextField();
            textFieldGoogle.setText(SettingStore.getSettingStore().getGoogleKey());
            keysPanel.add(textFieldGoogle,
                    UIUilt.createGridBagConstraints(GridBagConstraints.HORIZONTAL, -1, new Insets(0, 0, 5, 5), 1, 2));
            textFieldGoogle.setColumns(10);

            JLabel lblNewLabel = new JLabel(Messages.getString("SettingDialog.lblNewLabel.text")); //$NON-NLS-1$
            keysPanel.add(lblNewLabel,
                    UIUilt.createGridBagConstraints(-1, GridBagConstraints.WEST, new Insets(0, 0, 5, 0), 2, 2));

            JPanel panel_2 = new JPanel();
            // panel_2.setBorder(new TitledBorder(Messages.getString("PhotoGroupWindow.7"))); //$NON-NLS-1$
            panel_2.setLayout(UIUilt.createGridBagLayout(new int[] { 0, 0, 0, 5 },
                    new int[] { 30, 30, 30, 30, 30, 30, 30, 30, 30 }, new double[] { 0.0, 0.0, 1.0, Double.MIN_VALUE },
                    new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE }));

            JLabel lblNewLabel_1 = new JLabel(Messages.getString("PhotoGroupWindow.8")); //$NON-NLS-1$
            panel_2.add(lblNewLabel_1, UIUilt.createGridBagConstraints(-1, -1, new Insets(0, 0, 5, 5), 0, 0));

            SpinnerModel model = new SpinnerNumberModel(1, 1, 100, 1);
            spinnerThreshold = new JSpinner(model);
            spinnerThreshold.setValue(SettingStore.getSettingStore().getThreshold());
            panel_2.add(spinnerThreshold,
                    UIUilt.createGridBagConstraints(-1, GridBagConstraints.WEST, new Insets(0, 0, 5, 5), 1, 0));

            JLabel lblNewLabel_2 = new JLabel(Messages.getString("PhotoGroupWindow.9")); //$NON-NLS-1$
            panel_2.add(lblNewLabel_2,
                    UIUilt.createGridBagConstraints(-1, GridBagConstraints.WEST, new Insets(0, 0, 5, 0), 2, 0));

            JLabel lblNewLabel_3 = new JLabel(Messages.getString("PhotoGroupWindow.10")); //$NON-NLS-1$
            panel_2.add(lblNewLabel_3, UIUilt.createGridBagConstraints(-1, -1, new Insets(0, 0, 5, 5), 0, 1));

            comboBoxFormat = new JComboBox<String>();
            comboBoxFormat.setModel(new DefaultComboBoxModel<String>(FORMAT_TYPES));
            comboBoxFormat.setEditable(true);
            comboBoxFormat.setSelectedItem(SettingStore.getSettingStore().getFormat());
            panel_2.add(comboBoxFormat,
                    UIUilt.createGridBagConstraints(-1, GridBagConstraints.WEST, new Insets(0, 0, 5, 5), 1, 1));

            JLabel lblNewLabel_4 = new JLabel(Messages.getString("PhotoGroupWindow.15")); //$NON-NLS-1$
            panel_2.add(lblNewLabel_4,
                    UIUilt.createGridBagConstraints(-1, GridBagConstraints.WEST, new Insets(0, 0, 5, 0), 2, 1));

            JLabel lblNewLabel_5 = new JLabel(Messages.getString("PhotoGroupWindow.16")); //$NON-NLS-1$
            panel_2.add(lblNewLabel_5, UIUilt.createGridBagConstraints(-1, -1, new Insets(0, 0, 5, 5), 0, 2));

            JPanel panel_4 = new JPanel();
            panel_2.add(panel_4, UIUilt.createGridBagConstraints(-1, GridBagConstraints.WEST, new Insets(0, 0, 5, 5), 1, 2));
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
            panel_2.add(lblNewLabel_6,
                    UIUilt.createGridBagConstraints(-1, GridBagConstraints.WEST, new Insets(0, 0, 5, 0), 2, 2));

            JLabel lblNewLabel_7 = new JLabel(Messages.getString("PhotoGroupWindow.21")); //$NON-NLS-1$
            panel_2.add(lblNewLabel_7,
                    UIUilt.createGridBagConstraints(GridBagConstraints.VERTICAL, -1, new Insets(0, 0, 5, 5), 0, 3));
            chckbxGuess = new JCheckBox();
            chckbxGuess.setSelected(SettingStore.getSettingStore().isGuess());
            panel_2.add(chckbxGuess, UIUilt.createGridBagConstraints(GridBagConstraints.VERTICAL, GridBagConstraints.WEST,
                    new Insets(0, 0, 5, 5), 1, 3));

            JLabel lblNewLabel_8 = new JLabel(Messages.getString("PhotoGroupWindow.22")); //$NON-NLS-1$
            panel_2.add(lblNewLabel_8, UIUilt.createGridBagConstraints(GridBagConstraints.VERTICAL, GridBagConstraints.WEST,
                    new Insets(0, 0, 5, 0), 2, 3));

            JLabel lblNewLabel_9 = new JLabel(Messages.getString("PhotoGroupWindow.23")); //$NON-NLS-1$
            panel_2.add(lblNewLabel_9,
                    UIUilt.createGridBagConstraints(GridBagConstraints.VERTICAL, -1, new Insets(0, 0, 5, 5), 0, 4));

            chckbxGPS = new JCheckBox();
            chckbxGPS.setSelected(SettingStore.getSettingStore().isGps());
            panel_2.add(chckbxGPS, UIUilt.createGridBagConstraints(GridBagConstraints.VERTICAL, GridBagConstraints.WEST,
                    new Insets(0, 0, 5, 5), 1, 4));

            JLabel lblNewLabel_10 = new JLabel(Messages.getString("PhotoGroupWindow.24")); //$NON-NLS-1$
            panel_2.add(lblNewLabel_10, UIUilt.createGridBagConstraints(GridBagConstraints.VERTICAL, GridBagConstraints.WEST,
                    new Insets(0, 0, 5, 0), 2, 4));

            JLabel lblNewLabel_11 = new JLabel(Messages.getString("PhotoGroupWindow.25")); //$NON-NLS-1$
            panel_2.add(lblNewLabel_11,
                    UIUilt.createGridBagConstraints(GridBagConstraints.VERTICAL, -1, new Insets(0, 0, 5, 5), 0, 5));

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
            panel_2.add(lblNewLabel_12, UIUilt.createGridBagConstraints(GridBagConstraints.VERTICAL, GridBagConstraints.WEST,
                    new Insets(0, 0, 5, 0), 2, 5));
            groupingPanel.add(panel_2, UIUilt.createGridBagConstraints(GridBagConstraints.HORIZONTAL, GridBagConstraints.NORTH,
                    new Insets(0, 0, 5, 0), 0, 2));

            JLabel lblIsSubLabel = new JLabel(Messages.getString("SettingDialog.lblIsSubLabel.text")); //$NON-NLS-1$
            panel_2.add(lblIsSubLabel,
                    UIUilt.createGridBagConstraints(-1, GridBagConstraints.SOUTH, new Insets(0, 0, 5, 5), 0, 6));

            chckbxSubfolder = new JCheckBox(""); //$NON-NLS-1$
            chckbxSubfolder.setSelected(SettingStore.getSettingStore().isIncludeSubFolder());
            panel_2.add(chckbxSubfolder,
                    UIUilt.createGridBagConstraints(-1, GridBagConstraints.WEST, new Insets(0, 0, 5, 5), 1, 6));

            JLabel lblSubfolderLabel = new JLabel(Messages.getString("SettingDialog.lblSubfolderLabel.text")); //$NON-NLS-1$
            panel_2.add(lblSubfolderLabel,
                    UIUilt.createGridBagConstraints(-1, GridBagConstraints.WEST, new Insets(0, 0, 5, 0), 2, 6));

            JLabel lblUsethumbnail = new JLabel(Messages.getString("SettingDialog.lblUsethumbnail.text")); //$NON-NLS-1$
            panel_2.add(lblUsethumbnail, UIUilt.createGridBagConstraints(-1, -1, new Insets(0, 0, 0, 5), 0, 7));

            chckbxThumbnail = new JCheckBox();
            chckbxThumbnail.setSelected(SettingStore.getSettingStore().isUseThumbnail());
            panel_2.add(chckbxThumbnail,
                    UIUilt.createGridBagConstraints(-1, GridBagConstraints.WEST, new Insets(0, 0, 0, 5), 1, 7));

            JLabel lblShowThumnail = new JLabel(Messages.getString("SettingDialog.lblShowThumnail.text")); //$NON-NLS-1$
            panel_2.add(lblShowThumnail, UIUilt.createGridBagConstraints(-1, GridBagConstraints.WEST, null, 2, 7));

            JPanel panel_3 = new JPanel();
            panel_3.setLayout(new GridLayout(0, 1, 0, 0));
        }
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                JButton okButton = new JButton(Messages.getString("SettingDialog.btn_ok")); //$NON-NLS-1$
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
                        SettingStore.getSettingStore().setUseThumbnail(chckbxThumbnail.isSelected());
                        SettingStore.getSettingStore().setBaiduKey(textFieldBaidu.getText());
                        SettingStore.getSettingStore().setBingKey(textFieldBing.getText());
                        SettingStore.getSettingStore().setGoogleKey(textFieldGoogle.getText());
                        try {
                            SettingStore.saveSettings();
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        dispose();
                    }
                });
                okButton.setActionCommand(Messages.getString("SettingDialog.btn_ok")); //$NON-NLS-1$
                buttonPane.add(okButton);
                getRootPane().setDefaultButton(okButton);
            }
            {
                JButton cancelButton = new JButton(Messages.getString("SettingDialog.btn_cancel")); //$NON-NLS-1$
                cancelButton.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        dispose();
                    }
                });
                cancelButton.setActionCommand(Messages.getString("SettingDialog.btn_cancel")); //$NON-NLS-1$
                buttonPane.add(cancelButton);
            }
        }
    }

}
