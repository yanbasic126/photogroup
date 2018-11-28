package com.photogroup.ui.browser;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.plaf.synth.SynthLookAndFeel;

/**
 * DOC yyan class global comment. Detailled comment <br/>
 *
 * $Id$
 *
 */
public class FlowPanel1 {

    private JFrame frame;

    private JTextField textField;

    private JTextField txtYfuyfyukfy;

    private JPanel panelDebug;

    private JTextArea txtDebugLog;

    private JPanel panelDebugLog;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        if (null instanceof FlowPanel1) {
            System.out.println(11);
        } else {
            System.out.println(22);
        }
        // UIManager.put("ScrollBar.thumb", Color.black);

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
        try {
            SynthLookAndFeel laf = new SynthLookAndFeel();
            laf.load(getClass().getClassLoader().getResourceAsStream("dark_synth_theme.xml"), this.getClass());
            UIManager.setLookAndFeel(laf);
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 776, 560);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));

        JMenuBar menuBar = new JMenuBar();
        frame.getContentPane().add(menuBar);

        JMenu mnNewMenu = new JMenu("New menu");
        menuBar.add(mnNewMenu);

        JMenuItem mntmNewMenuItem = new JMenuItem("New menu item");
        mntmNewMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
            }
        });
        mnNewMenu.add(mntmNewMenuItem);

        JPanel panel = new JPanel();
        frame.getContentPane().add(panel);
        GridBagLayout gbl_panel = new GridBagLayout();
        gbl_panel.columnWidths = new int[] { 0, 0 };
        gbl_panel.rowHeights = new int[] { 23, 0, 0, 10, 0 };
        gbl_panel.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
        gbl_panel.rowWeights = new double[] { 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE };
        panel.setLayout(gbl_panel);

        JPanel panel_1 = new JPanel();
        GridBagConstraints gbc_panel_1 = new GridBagConstraints();
        gbc_panel_1.fill = GridBagConstraints.HORIZONTAL;
        gbc_panel_1.insets = new Insets(0, 0, 5, 0);
        gbc_panel_1.anchor = GridBagConstraints.NORTH;
        gbc_panel_1.gridx = 0;
        gbc_panel_1.gridy = 0;
        panel.add(panel_1, gbc_panel_1);
        GridBagLayout gbl_panel_1 = new GridBagLayout();
        gbl_panel_1.columnWidths = new int[] { 22, 0, 128, 41, 0 };
        gbl_panel_1.rowHeights = new int[] { 23, 0 };
        gbl_panel_1.columnWeights = new double[] { 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE };
        gbl_panel_1.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
        panel_1.setLayout(gbl_panel_1);

        JButton button = new JButton("+");
        GridBagConstraints gbc_button = new GridBagConstraints();
        gbc_button.insets = new Insets(0, 0, 0, 5);
        gbc_button.gridx = 0;
        gbc_button.gridy = 0;
        panel_1.add(button, gbc_button);

        JButton btnTitle = new JButton("title");
        GridBagConstraints gbc_btnTitle = new GridBagConstraints();
        gbc_btnTitle.insets = new Insets(0, 0, 0, 5);
        gbc_btnTitle.anchor = GridBagConstraints.NORTH;
        gbc_btnTitle.gridx = 1;
        gbc_btnTitle.gridy = 0;
        panel_1.add(btnTitle, gbc_btnTitle);

        textField = new JTextField();
        GridBagConstraints gbc_textField = new GridBagConstraints();
        gbc_textField.insets = new Insets(0, 0, 0, 5);
        gbc_textField.gridx = 2;
        gbc_textField.gridy = 0;
        panel_1.add(textField, gbc_textField);
        textField.setColumns(10);

        JButton btnNewButton = new JButton("Rename");
        GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
        gbc_btnNewButton.gridx = 3;
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
        toolBar.add(btnNewButton_1);

        JLabel lblTitle = new JLabel("titletitletitletitletitletitle");
        toolBar.add(lblTitle);

        txtYfuyfyukfy = new JTextField();
        txtYfuyfyukfy.setText("yfuyfyukfy");
        toolBar.add(txtYfuyfyukfy);
        txtYfuyfyukfy.setColumns(10);
        // txtYfuyfyukfy.setBorder(new EmptyBorder(0, 0, 0, 0));

        JButton btnRename = new JButton("Rename");
        btnRename.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                panelDebug.setVisible(!panelDebug.isVisible());
                txtDebugLog.setRows(10);
                panelDebug.doLayout();

                panelDebugLog.doLayout();
            }
        });
        toolBar.add(btnRename);

        JPanel panel_3 = new JPanel();
        GridBagConstraints gbc_panel_3 = new GridBagConstraints();
        gbc_panel_3.insets = new Insets(0, 0, 5, 0);
        gbc_panel_3.fill = GridBagConstraints.BOTH;
        gbc_panel_3.gridx = 0;
        gbc_panel_3.gridy = 2;
        panel.add(panel_3, gbc_panel_3);
        GridBagLayout gbl_panel_3 = new GridBagLayout();
        gbl_panel_3.columnWidths = new int[] { 79, 0 };
        gbl_panel_3.rowHeights = new int[] { 66, 0 };
        gbl_panel_3.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
        gbl_panel_3.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
        panel_3.setLayout(gbl_panel_3);

        JPanel panel_4 = new JPanel();
        JScrollPane scrollPane_1 = new JScrollPane(panel_4);
        GridBagLayout gbl_panel_4 = new GridBagLayout();
        gbl_panel_4.columnWidths = new int[] { 0 };
        gbl_panel_4.rowHeights = new int[] { 0, 0, 0 };
        gbl_panel_4.columnWeights = new double[] { 1.0 };
        gbl_panel_4.rowWeights = new double[] { 0.0, 1.0, 1.0 };
        panel_4.setLayout(gbl_panel_4);

        JPanel panel_2 = new JPanel();
        GridBagConstraints gbc_panel_2 = new GridBagConstraints();
        gbc_panel_2.fill = GridBagConstraints.BOTH;
        gbc_panel_2.gridx = 0;
        gbc_panel_2.gridy = 0;
        panel_4.add(panel_2, gbc_panel_2);

        JButton btnNewButton_2 = new JButton("New button");
        panel_2.add(btnNewButton_2);

        JTree tree = new JTree();
        panel_2.add(tree);
        GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
        gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
        gbc_scrollPane_1.gridx = 0;
        gbc_scrollPane_1.gridy = 0;
        panel_3.add(scrollPane_1, gbc_scrollPane_1);

        panelDebug = new JPanel();
        GridBagConstraints gbc_panel_2_1 = new GridBagConstraints();
        gbc_panel_2_1.anchor = GridBagConstraints.NORTH;
        gbc_panel_2_1.fill = GridBagConstraints.HORIZONTAL;
        gbc_panel_2_1.gridx = 0;
        gbc_panel_2_1.gridy = 3;
        // panel_2.setVisible(false);
        panel.add(panelDebug, gbc_panel_2_1);
        gbc_panel_2_1 = new GridBagConstraints();
        gbc_panel_2_1.insets = new Insets(0, 0, 5, 0);
        gbc_panel_2_1.anchor = GridBagConstraints.NORTHWEST;
        gbc_panel_2_1.gridx = 1;
        gbc_panel_2_1.gridy = 0;
        GridBagLayout gbl_panelDebug = new GridBagLayout();
        gbl_panelDebug.columnWidths = new int[] { 756, 0 };
        gbl_panelDebug.rowHeights = new int[] { 100, 0 };
        gbl_panelDebug.columnWeights = new double[] { 0.0, Double.MIN_VALUE };
        gbl_panelDebug.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
        panelDebug.setLayout(gbl_panelDebug);

        panelDebugLog = new JPanel();
        // panelDebug.add(panel_2, gbc_panel_2_1);
        GridBagLayout gbl_panelDebugLog = new GridBagLayout();
        gbl_panelDebugLog.columnWidths = new int[] { 0, 0 };
        gbl_panelDebugLog.rowHeights = new int[] { 0, 0 };
        gbl_panelDebugLog.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
        gbl_panelDebugLog.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
        panelDebugLog.setLayout(gbl_panelDebugLog);

        JScrollPane scrollPaneDebug = new JScrollPane(panelDebugLog);
        scrollPaneDebug.setPreferredSize(new Dimension(100, 100));

        txtDebugLog = new JTextArea();
        txtDebugLog.setText(
                "package com.photogroup.ui.browser;\r\n\r\nimport java.awt.Color;\r\nimport java.awt.Dimension;\r\nimport java.awt.EventQueue;\r\nimport java.awt.GridBagConstraints;\r\nimport java.awt.GridBagLayout;\r\nimport java.awt.GridLayout;\r\nimport java.awt.Insets;\r\n\r\nimport javax.swing.JButton;\r\nimport javax.swing.JFrame;\r\nimport javax.swing.JLabel;\r\nimport javax.swing.JPanel;\r\nimport javax.swing.JTextField;\r\nimport javax.swing.JToolBar;\r\nimport javax.swing.JTree;\r\nimport javax.swing.border.EmptyBorder;\r\nimport javax.swing.border.LineBorder;\r\nimport java.awt.event.ActionListener;\r\nimport java.awt.event.ActionEvent;\r\nimport javax.swing.JTextArea;\r\nimport javax.swing.JTextPane;\r\nimport javax.swing.JScrollPane;\r\n\r\n\r\n/**\r\n * DOC yyan  class global comment. Detailled comment\r\n * <br/>\r\n *\r\n * $Id$\r\n *\r\n */\r\npublic class FlowPanel1 {\r\n\r\n    private JFrame frame;\r\n    private JTextField textField;\r\n    private JTextField txtYfuyfyukfy;\r\n    private JPanel panelDebug;\r\n    private JTextArea textArea;\r\n    private JPanel panel_2;\r\n\r\n    /**\r\n     * Launch the application.\r\n     */\r\n    public static void main(String[] args) {\r\n        if(null instanceof FlowPanel1) {\r\n            System.out.println(11);\r\n        }else {\r\n            System.out.println(22);\r\n        }\r\n        EventQueue.invokeLater(new Runnable() {\r\n\r\n            public void run() {\r\n                try {\r\n                    FlowPanel1 window = new FlowPanel1();\r\n                    window.frame.setVisible(true);\r\n                } catch (Exception e) {\r\n                    e.printStackTrace();\r\n                }\r\n            }\r\n        });\r\n    }\r\n\r\n    /**\r\n     * Create the application.\r\n     */\r\n    public FlowPanel1() {\r\n        initialize();\r\n    }\r\n\r\n    /**\r\n     * Initialize the contents of the frame.\r\n     */\r\n    private void initialize() {\r\n        frame = new JFrame();\r\n        frame.setBounds(100, 100, 450, 612);\r\n        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);\r\n        frame.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));\r\n        \r\n        JPanel panel = new JPanel();\r\n        panel.setBorder(new LineBorder(new Color(0, 0, 0)));\r\n        frame.getContentPane().add(panel);\r\n        GridBagLayout gbl_panel = new GridBagLayout();\r\n        gbl_panel.columnWidths = new int[]{0, 0};\r\n        gbl_panel.rowHeights = new int[]{23, 0, 0, 20, 0};\r\n        gbl_panel.columnWeights = new double[]{1.0, Double.MIN_VALUE};\r\n        gbl_panel.rowWeights = new double[]{0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};\r\n        panel.setLayout(gbl_panel);\r\n        \r\n        JPanel panel_1 = new JPanel();\r\n        GridBagConstraints gbc_panel_1 = new GridBagConstraints();\r\n        gbc_panel_1.fill = GridBagConstraints.HORIZONTAL;\r\n        gbc_panel_1.insets = new Insets(0, 0, 5, 0);\r\n        gbc_panel_1.anchor = GridBagConstraints.NORTH;\r\n        gbc_panel_1.gridx = 0;\r\n        gbc_panel_1.gridy = 0;\r\n        panel.add(panel_1, gbc_panel_1);\r\n        GridBagLayout gbl_panel_1 = new GridBagLayout();\r\n        gbl_panel_1.columnWidths = new int[]{22, 0, 128, 41, 0};\r\n        gbl_panel_1.rowHeights = new int[]{23, 0};\r\n        gbl_panel_1.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};\r\n        gbl_panel_1.rowWeights = new double[]{0.0, Double.MIN_VALUE};\r\n        panel_1.setLayout(gbl_panel_1);\r\n        \r\n        JButton button = new JButton(\"+\");\r\n        GridBagConstraints gbc_button = new GridBagConstraints();\r\n        gbc_button.insets = new Insets(0, 0, 0, 5);\r\n        gbc_button.gridx = 0;\r\n        gbc_button.gridy = 0;\r\n        panel_1.add(button, gbc_button);\r\n        \r\n        JButton btnTitle = new JButton(\"title\");\r\n        GridBagConstraints gbc_btnTitle = new GridBagConstraints();\r\n        gbc_btnTitle.insets = new Insets(0, 0, 0, 5);\r\n        gbc_btnTitle.anchor = GridBagConstraints.NORTH;\r\n        gbc_btnTitle.gridx = 1;\r\n        gbc_btnTitle.gridy = 0;\r\n        panel_1.add(btnTitle, gbc_btnTitle);\r\n        \r\n        textField = new JTextField();\r\n        GridBagConstraints gbc_textField = new GridBagConstraints();\r\n        gbc_textField.insets = new Insets(0, 0, 0, 5);\r\n        gbc_textField.gridx = 2;\r\n        gbc_textField.gridy = 0;\r\n        panel_1.add(textField, gbc_textField);\r\n        textField.setColumns(10);\r\n        \r\n        JButton btnNewButton = new JButton(\"Rename\");\r\n        GridBagConstraints gbc_btnNewButton = new GridBagConstraints();\r\n        gbc_btnNewButton.gridx = 3;\r\n        gbc_btnNewButton.gridy = 0;\r\n        panel_1.add(btnNewButton, gbc_btnNewButton);\r\n        \r\n        JToolBar toolBar = new JToolBar();\r\n        toolBar.setFloatable(false);\r\n        GridBagConstraints gbc_toolBar = new GridBagConstraints();\r\n        gbc_toolBar.insets = new Insets(0, 0, 5, 0);\r\n        gbc_toolBar.fill = GridBagConstraints.HORIZONTAL;\r\n        gbc_toolBar.gridx = 0;\r\n        gbc_toolBar.gridy = 1;\r\n        panel.add(toolBar, gbc_toolBar);\r\n        \r\n        JButton btnNewButton_1 = new JButton(\"+\");\r\n        btnNewButton_1.setPreferredSize(new Dimension(30, 30));\r\n        btnNewButton_1.setBorder(new LineBorder(new Color(0, 0, 0)));\r\n        toolBar.add(btnNewButton_1);\r\n        \r\n        JLabel lblTitle = new JLabel(\"titletitletitletitletitletitle\");\r\n        toolBar.add(lblTitle);\r\n        \r\n        txtYfuyfyukfy = new JTextField();\r\n        txtYfuyfyukfy.setText(\"yfuyfyukfy\");\r\n        toolBar.add(txtYfuyfyukfy);\r\n        txtYfuyfyukfy.setColumns(10);\r\n        txtYfuyfyukfy.setBorder(new EmptyBorder(0, 0, 0, 0));\r\n        \r\n        JButton btnRename = new JButton(\"Rename\");\r\n        btnRename.addActionListener(new ActionListener() {\r\n            public void actionPerformed(ActionEvent e) {\r\n                panelDebug.setVisible(!panelDebug.isVisible());\r\n                textArea.setRows(10);\r\n                panelDebug.doLayout();\r\n                \r\n                panel_2.doLayout();\r\n            }\r\n        });\r\n        toolBar.add(btnRename);\r\n        \r\n        JTree tree = new JTree();\r\n        GridBagConstraints gbc_tree = new GridBagConstraints();\r\n        gbc_tree.insets = new Insets(0, 0, 5, 0);\r\n        gbc_tree.fill = GridBagConstraints.BOTH;\r\n        gbc_tree.gridx = 0;\r\n        gbc_tree.gridy = 2;\r\n        panel.add(tree, gbc_tree);\r\n        \r\n        panelDebug = new JPanel();\r\n        GridBagConstraints gbc_panel_2_1 = new GridBagConstraints();\r\n        gbc_panel_2_1.anchor = GridBagConstraints.SOUTH;\r\n        gbc_panel_2_1.fill = GridBagConstraints.HORIZONTAL;\r\n        gbc_panel_2_1.gridx = 0;\r\n        gbc_panel_2_1.gridy = 3;\r\n//        panel_2.setVisible(false);\r\n        panel.add(panelDebug, gbc_panel_2_1);\r\n        GridBagLayout gbl_panelDebug = new GridBagLayout();\r\n        gbl_panelDebug.columnWidths = new int[]{210, 0};\r\n        gbl_panelDebug.rowHeights = new int[]{10, 0, 0};\r\n        gbl_panelDebug.columnWeights = new double[]{1.0, Double.MIN_VALUE};\r\n        gbl_panelDebug.rowWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};\r\n        panelDebug.setLayout(gbl_panelDebug);\r\n        \r\n        panel_2 = new JPanel();\r\n        gbc_panel_2_1 = new GridBagConstraints();\r\n        gbc_panel_2_1.insets = new Insets(0, 0, 5, 0);\r\n        gbc_panel_2_1.anchor = GridBagConstraints.NORTHWEST;\r\n        gbc_panel_2_1.gridx = 1;\r\n        gbc_panel_2_1.gridy = 0;\r\n        panelDebug.add(panel_2, gbc_panel_2_1);\r\n        GridBagLayout gbl_panel_2 = new GridBagLayout();\r\n        gbl_panel_2.columnWidths = new int[]{0, 0};\r\n        gbl_panel_2.rowHeights = new int[]{0, 0};\r\n        gbl_panel_2.columnWeights = new double[]{1.0, Double.MIN_VALUE};\r\n        gbl_panel_2.rowWeights = new double[]{1.0, Double.MIN_VALUE};\r\n        panel_2.setLayout(gbl_panel_2);\r\n        \r\n        JScrollPane scrollPane = new JScrollPane(panel_2);\r\n        \r\n        textArea = new JTextArea();\r\n        textArea.setRows(10);\r\n        GridBagConstraints gbc_textArea = new GridBagConstraints();\r\n        gbc_textArea.fill = GridBagConstraints.BOTH;\r\n        gbc_textArea.gridx = 0;\r\n        gbc_textArea.gridy = 0;\r\n        panel_2.add(textArea, gbc_textArea);\r\n        GridBagConstraints gbc_scrollPane = new GridBagConstraints();\r\n        gbc_scrollPane.fill = GridBagConstraints.BOTH;\r\n        gbc_scrollPane.gridx = 0;\r\n        gbc_scrollPane.gridy = 1;\r\n        panelDebug.add(scrollPane, gbc_scrollPane);\r\n    }\r\n\r\n}\r\n");
        txtDebugLog.setRows(2);
        GridBagConstraints gbc_txtDebugLog = new GridBagConstraints();
        gbc_txtDebugLog.fill = GridBagConstraints.BOTH;
        gbc_txtDebugLog.gridx = 0;
        gbc_txtDebugLog.gridy = 0;
        panelDebugLog.add(txtDebugLog, gbc_txtDebugLog);
        GridBagConstraints gbc_scrollPaneDebug = new GridBagConstraints();
        gbc_scrollPaneDebug.anchor = GridBagConstraints.NORTH;
        gbc_scrollPaneDebug.fill = GridBagConstraints.HORIZONTAL;
        gbc_scrollPaneDebug.gridx = 0;
        gbc_scrollPaneDebug.gridy = 0;
        panelDebug.add(scrollPaneDebug, gbc_scrollPaneDebug);

    }

}
