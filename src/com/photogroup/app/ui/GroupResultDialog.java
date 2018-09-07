// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2013 Talend – www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
// ============================================================================
package com.photogroup.app.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * DOC yyan class global comment. Detailled comment <br/>
 *
 * $Id$
 *
 */
public class GroupResultDialog extends JDialog {

    /**
     * 
     */
    private static final long serialVersionUID = 3873535103100400277L;

    private final JPanel contentPanel = new JPanel();
    
    public String action;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            GroupResultDialog dialog = new GroupResultDialog(null);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the dialog.
     */
    public GroupResultDialog(Map<String, List<File>> groups) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - 600) / 2);
        int y = (int) ((dimension.getHeight() - 500) / 2);
        setModalityType(ModalityType.APPLICATION_MODAL);
        setBounds(0, 0, 600, 500);
//        setBounds(x, y, 600, 500);
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

            JTree tree = new PhotoJTree(groups);
            tree.setEditable(true);

            GridBagConstraints gbc_tree = new GridBagConstraints();
            gbc_tree.fill = GridBagConstraints.BOTH;
            gbc_tree.gridx = 0;
            gbc_tree.gridy = 0;

            JScrollPane scrollPane = new JScrollPane(tree);
            contentPanel.add(scrollPane, gbc_tree);
        }
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                JButton okButton = new JButton("OK");
                okButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        action = "OK";
                        dispose();
                    }
                });
                okButton.setActionCommand("OK");
                buttonPane.add(okButton);
                getRootPane().setDefaultButton(okButton);
            }
            {
                JButton cancelButton = new JButton("Cancel");
                cancelButton.setActionCommand("Cancel");
                cancelButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        action = "Cancel";
                        dispose();
                    }
                });
                buttonPane.add(cancelButton);
            }
        }
        {
            JScrollPane scrollPane = new JScrollPane();
            getContentPane().add(scrollPane, BorderLayout.NORTH);
        }
    }

    class PhotoJTree extends JTree {

        private Map<String, List<File>> groups;

        private DefaultTreeModel dtModel;

        public PhotoJTree(Map<String, List<File>> groups) {
            this.groups = groups;
            dtModel = new DefaultTreeModel(builtTreeNode());
            this.setModel(dtModel);
            setCellRenderer(new PhotoCellRenderer());
        }

        /**
         * DOC yyan Comment method "builtTreeNode".
         */
        private DefaultMutableTreeNode builtTreeNode() {

            DefaultMutableTreeNode dmtNode = new DefaultMutableTreeNode("ROOT");

            Iterator<?> it = groups.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, List<File>> pair = (Entry<String, List<File>>) it.next();
                DefaultMutableTreeNode folderNode = new DefaultMutableTreeNode(pair.getKey());
                for (File photo : pair.getValue()) {
                    DefaultMutableTreeNode imageNode = new DefaultMutableTreeNode(photo);
                    folderNode.add(imageNode);
                }
                dmtNode.add(folderNode);
            }

            return dmtNode;
        }
    }

    class PhotoCellRenderer implements TreeCellRenderer {

        private JLabel label;

        PhotoCellRenderer() {
            label = new JLabel();
        }

        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf,
                int row, boolean hasFocus) {
            Object obj = ((DefaultMutableTreeNode) value).getUserObject();
            if (obj instanceof File) {
                File image = (File) obj;
                // ImageIcon icon = new ImageIcon(image.getAbsolutePath());
                // label.setIcon(icon);
                label.setText(image.getName());
            } else {
                // label.setIcon(null);
                label.setText("" + value);
            }
            return label;
        }
    }
}
