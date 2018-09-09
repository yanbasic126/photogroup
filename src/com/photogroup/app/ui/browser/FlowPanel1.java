//============================================================================
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
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
//============================================================================
package com.photogroup.app.ui.browser;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JButton;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import javax.swing.JToolBar;
import javax.swing.JLabel;
import javax.swing.JTree;


/**
 * DOC yyan  class global comment. Detailled comment
 * <br/>
 *
 * $Id$
 *
 */
public class FlowPanel1 {

    private JFrame frame;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                try {
                    FlowPanel1 window = new FlowPanel1();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public FlowPanel1() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));
        
        JPanel panel = new JPanel();
        panel.setBorder(new LineBorder(new Color(0, 0, 0)));
        frame.getContentPane().add(panel);
        GridBagLayout gbl_panel = new GridBagLayout();
        gbl_panel.columnWidths = new int[]{0, 0};
        gbl_panel.rowHeights = new int[]{23, 0, 0, 0};
        gbl_panel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
        gbl_panel.rowWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
        panel.setLayout(gbl_panel);
        
        JPanel panel_1 = new JPanel();
        GridBagConstraints gbc_panel_1 = new GridBagConstraints();
        gbc_panel_1.insets = new Insets(0, 0, 5, 0);
        gbc_panel_1.anchor = GridBagConstraints.NORTHWEST;
        gbc_panel_1.gridx = 0;
        gbc_panel_1.gridy = 0;
        panel.add(panel_1, gbc_panel_1);
        GridBagLayout gbl_panel_1 = new GridBagLayout();
        gbl_panel_1.columnWidths = new int[]{0, 0, 0, 0};
        gbl_panel_1.rowHeights = new int[]{23, 0};
        gbl_panel_1.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
        gbl_panel_1.rowWeights = new double[]{0.0, Double.MIN_VALUE};
        panel_1.setLayout(gbl_panel_1);
        
        JButton button = new JButton("+");
        GridBagConstraints gbc_button = new GridBagConstraints();
        gbc_button.anchor = GridBagConstraints.WEST;
        gbc_button.insets = new Insets(0, 0, 0, 5);
        gbc_button.gridx = 0;
        gbc_button.gridy = 0;
        panel_1.add(button, gbc_button);
        
        JButton btnTitle = new JButton("title");
        GridBagConstraints gbc_btnTitle = new GridBagConstraints();
        gbc_btnTitle.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnTitle.insets = new Insets(0, 0, 0, 5);
        gbc_btnTitle.anchor = GridBagConstraints.NORTH;
        gbc_btnTitle.gridx = 1;
        gbc_btnTitle.gridy = 0;
        panel_1.add(btnTitle, gbc_btnTitle);
        
        JButton btnNewButton = new JButton("Rename");
        GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
        gbc_btnNewButton.gridx = 2;
        gbc_btnNewButton.gridy = 0;
        panel_1.add(btnNewButton, gbc_btnNewButton);
        
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        GridBagConstraints gbc_toolBar = new GridBagConstraints();
        gbc_toolBar.insets = new Insets(0, 0, 5, 0);
        gbc_toolBar.fill = GridBagConstraints.HORIZONTAL;
        gbc_toolBar.gridx = 0;
        gbc_toolBar.gridy = 1;
        panel.add(toolBar, gbc_toolBar);
        
        JButton btnNewButton_1 = new JButton("+");
        btnNewButton_1.setPreferredSize(new Dimension(30, 30));
        btnNewButton_1.setBorder(new LineBorder(new Color(0, 0, 0)));
        toolBar.add(btnNewButton_1);
        
        JLabel lblTitle = new JLabel("titletitletitletitletitletitle");
        toolBar.add(lblTitle);
        
        JButton btnRename = new JButton("Rename");
        toolBar.add(btnRename);
        
        JTree tree = new JTree();
        GridBagConstraints gbc_tree = new GridBagConstraints();
        gbc_tree.fill = GridBagConstraints.BOTH;
        gbc_tree.gridx = 0;
        gbc_tree.gridy = 2;
        panel.add(tree, gbc_tree);
    }

}
