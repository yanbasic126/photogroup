package com.photogroup.ui.dialog;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.photogroup.ui.util.UIUilt;

public class ChangeGroupDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    private final JPanel contentPanel = new JPanel();

    private JTable groupJTable;

    private String selectedName;

    /**
     * Create the dialog.
     */
    public ChangeGroupDialog(String[] groupList) {
        setModal(true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 450, 500);
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 432, 0 };
        gridBagLayout.rowHeights = new int[] { 218, 35, 0 };
        gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
        gridBagLayout.rowWeights = new double[] { 1.0, 0.0, Double.MIN_VALUE };
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
        gbl_contentPanel.rowHeights = new int[] { 0 };
        gbl_contentPanel.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
        gbl_contentPanel.rowWeights = new double[] { 1.0 };
        contentPanel.setLayout(gbl_contentPanel);
        {
            JPanel listPanel = new JPanel();
            GridBagConstraints gbc_ListPanel = new GridBagConstraints();
            gbc_ListPanel.fill = GridBagConstraints.BOTH;
            gbc_ListPanel.gridx = 0;
            gbc_ListPanel.gridy = 0;
            contentPanel.add(listPanel, gbc_ListPanel);
            GridBagLayout gbl_ListPanel = new GridBagLayout();
            gbl_ListPanel.columnWidths = new int[] { 0, 0 };
            gbl_ListPanel.rowHeights = new int[] { 0, 0 };
            gbl_ListPanel.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
            gbl_ListPanel.rowWeights = new double[] { 0.0, 1.0 };
            listPanel.setLayout(gbl_ListPanel);

            {
                JPanel descriptionPanel = new JPanel();
                GridBagConstraints gbc_panel = new GridBagConstraints();
                gbc_panel.insets = new Insets(0, 0, 5, 0);
                gbc_panel.fill = GridBagConstraints.BOTH;
                gbc_panel.gridx = 0;
                gbc_panel.gridy = 0;
                listPanel.add(descriptionPanel, gbc_panel);
                JLabel lblText = new JLabel("Select a group from list or");
                GridBagConstraints gbc_lblText = new GridBagConstraints();
                gbc_lblText.insets = new Insets(0, 0, 5, 0);
                gbc_lblText.anchor = GridBagConstraints.WEST;
                gbc_lblText.gridx = 0;
                gbc_lblText.gridy = 0;
                descriptionPanel.add(lblText, gbc_lblText);

                JLabel lblCreate = UIUilt.createSimpleLinkLabel("Create", new MouseAdapter() {

                    public void mouseClicked(MouseEvent e) {
                        groupJTable.changeSelection(groupJTable.getRowCount() - 1, 0, false, false);
                    }
                });
                descriptionPanel.add(lblCreate);

                JLabel lblANewGroup = new JLabel("a New Group");
                descriptionPanel.add(lblANewGroup);
            }

            groupJTable = new JTable();
            String[][] nameList = new String[groupList.length + 1][1];
            for (int i = 0; i < groupList.length; i++) {
                nameList[i][0] = groupList[i];
            }
            nameList[groupList.length][0] = "";
            groupJTable.setModel(new DefaultTableModel(nameList, new String[] { "Name" }) {

                private static final long serialVersionUID = 1L;

                @Override
                public boolean isCellEditable(int row, int column) {
                    return row == getRowCount() - 1 ? true : false;
                }
            });
            groupJTable.setTableHeader(null);
            groupJTable.setFillsViewportHeight(true);
            groupJTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            GridBagConstraints gbc_GroupList = new GridBagConstraints();
            gbc_GroupList.insets = new Insets(0, 0, 5, 0);
            gbc_GroupList.fill = GridBagConstraints.BOTH;
            gbc_GroupList.gridx = 0;
            gbc_GroupList.gridy = 1;

            JScrollPane scrollPane = new JScrollPane(groupJTable);

            GridBagConstraints gbc_scrollPane = new GridBagConstraints();
            gbc_scrollPane.fill = GridBagConstraints.BOTH;
            gbc_scrollPane.gridx = 0;
            gbc_scrollPane.gridy = 1;
            listPanel.add(scrollPane, gbc_scrollPane);
        }
        {

            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));

            GridBagConstraints gbc_buttonPane = new GridBagConstraints();
            gbc_buttonPane.anchor = GridBagConstraints.NORTH;
            gbc_buttonPane.fill = GridBagConstraints.HORIZONTAL;
            gbc_buttonPane.gridx = 0;
            gbc_buttonPane.gridy = 1;

            getContentPane().add(buttonPane, gbc_buttonPane);
            {
                JButton okButton = new JButton("OK");
                okButton.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        selectedName = groupJTable.getModel().getValueAt(groupJTable.getSelectedRow(), 0).toString();
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

    public String getSelectedName() {
        return selectedName;
    }
}
