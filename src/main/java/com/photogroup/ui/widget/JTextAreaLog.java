package com.photogroup.ui.widget;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

public class JTextAreaLog extends JTextArea implements MouseListener {

    private static final long serialVersionUID = -2308615404205560110L;

    private JPopupMenu pop = null;

    private JMenuItem copy = null;

    private JMenuItem paste = null;

    private JMenuItem cut = null;

    private JMenuItem pasteAndProfile = null;

    private JMenuItem delete = null;

    private JMenuItem selectAll = null;

    public JTextAreaLog() {
        super();
        init();
    }

    private void init() {
        this.addMouseListener(this);
        pop = new JPopupMenu();
        pop.add(cut = new JMenuItem("Cut"));
        pop.add(copy = new JMenuItem("Copy"));
        pop.add(paste = new JMenuItem("Paste"));
        pasteAndProfile = new JMenuItem("Paste & Profile");
        // pop.add(pasteAndProfile = new JMenuItem("Paste & Profile"));
        pop.add(delete = new JMenuItem("Clear"));
        pop.add(new JSeparator());
        pop.add(selectAll = new JMenuItem("Select All"));

        copy.setAccelerator(KeyStroke.getKeyStroke('C', InputEvent.CTRL_MASK));
        paste.setAccelerator(KeyStroke.getKeyStroke('V', InputEvent.CTRL_MASK));
        cut.setAccelerator(KeyStroke.getKeyStroke('X', InputEvent.CTRL_MASK));
        delete.setAccelerator(KeyStroke.getKeyStroke('D', InputEvent.CTRL_MASK));
        selectAll.setAccelerator(KeyStroke.getKeyStroke('A', InputEvent.CTRL_MASK));

        cut.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                action(e);
            }
        });
        copy.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                action(e);
            }
        });
        paste.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                action(e);
            }
        });
        pasteAndProfile.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                action(e);
            }
        });
        delete.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                action(e);
            }
        });
        selectAll.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                action(e);
            }
        });
        this.add(pop);
    }

    public void action(ActionEvent e) {
        String str = e.getActionCommand();
        if (str.equals(copy.getText())) {
            this.copy();
        } else if (str.equals(paste.getText())) {
            this.paste();
        } else if (str.equals(cut.getText())) {
            this.cut();
        } else if (str.equals(pasteAndProfile.getText())) {
            this.paste();
        } else if (str.equals(delete.getText())) {
            this.setText("");
        } else if (str.equals(selectAll.getText())) {
            this.selectAll();
        }
    }

    public JPopupMenu getPop() {
        return pop;
    }

    public void setPop(JPopupMenu pop) {
        this.pop = pop;
    }

    public boolean isClipboardString() {
        boolean b = false;
        Clipboard clipboard = this.getToolkit().getSystemClipboard();
        Transferable content = clipboard.getContents(this);
        try {
            if (content.getTransferData(DataFlavor.stringFlavor) instanceof String) {
                b = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }

    public boolean isCanCopy() {
        boolean b = false;
        int start = this.getSelectionStart();
        int end = this.getSelectionEnd();
        if (start != end)
            b = true;
        return b;
    }

    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            copy.setEnabled(isCanCopy());
            paste.setEnabled(isClipboardString());
            cut.setEnabled(isCanCopy());
            pop.show(this, e.getX(), e.getY());
        }
    }

    public void mouseClicked(MouseEvent e) {
        // do nothing
    }

    public void mouseEntered(MouseEvent e) {
        // do nothing
    }

    public void mouseExited(MouseEvent e) {
        // do nothing
    }

    public void mouseReleased(MouseEvent e) {
        // do nothing
    }

}
