package com.photogroup.ui.widget;

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JPanel;

public class FileListAccessory extends JPanel implements PropertyChangeListener {

    private static final int PREFERRED_WIDTH = 125;

    private static final int PREFERRED_HEIGHT = 100;

    File file = null;

    JList list;

    public FileListAccessory(JFileChooser chooser) {
        chooser.addPropertyChangeListener(this);
        setPreferredSize(new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT));
        list = new JList();
        list.setModel(new AbstractListModel() {

            String[] values = new String[] { "1", "2", "3" };

            public int getSize() {
                return values.length;
            }

            public Object getElementAt(int index) {
                return values[index];
            }
        });
        add(list);
    }

    public void propertyChange(PropertyChangeEvent e) {
        boolean update = false;
        String prop = e.getPropertyName();

        // If the directory changed, don't show an image.
        if (JFileChooser.DIRECTORY_CHANGED_PROPERTY.equals(prop)) {
            file = (File) e.getNewValue();
            update = true;
            List<String> recursiveFiles = new ArrayList<String>();
            for (File f : file.listFiles()) {
                recursiveFiles.add(f.getName());
            }
            list.setListData(recursiveFiles.toArray());
            repaint();
        }

    }
}