package com.photogroup.ui.dialog;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.KeyStroke;

public class ViewerDialog extends JFrame implements ActionListener {

    private JLabel labelImage;

    private boolean componentResizing = false;

    ImageIcon photoImage;

    public ViewerDialog(String path) {
        getContentPane().setBackground(new Color(0, 0, 0));
        setTitle(path);
        setIconImage(Toolkit.getDefaultToolkit().getImage(AboutAndUpdateDialog.class.getResource("/icon/lemon_16.png")));
        getContentPane().addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if ((e.getButton() == 1 && e.getClickCount() == 2) || (e.getButton() == 2 && e.getClickCount() == 1)) {
                    if (getExtendedState() == JFrame.MAXIMIZED_BOTH) {
                        setExtendedState(0);
                    } else {
                        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
                    }
                    return;
                }
            }
        });
        photoImage = new ImageIcon(path);
        setSize(100, 0);
        this.addComponentListener(new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent e) {
                if (photoImage != null) {

                    if (!componentResizing) {
                        new Thread(new ResizeRender()).start();
                    }
                    componentResizing = true;
                }
            }
        });
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 148, 0 };
        gridBagLayout.rowHeights = new int[] { 1, 0 };
        gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
        gridBagLayout.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
        getContentPane().setLayout(gridBagLayout);
        labelImage = new JLabel();
        GridBagConstraints gbc_labelImage = new GridBagConstraints();
        gbc_labelImage.gridx = 0;
        gbc_labelImage.gridy = 0;
        getContentPane().add(labelImage, gbc_labelImage);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Cancel"); //$NON-NLS-1$
        getRootPane().getActionMap().put("Cancel", new AbstractAction() { //$NON-NLS-1$

            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    class ResizeRender implements Runnable {

        @Override
        public void run() {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            double a = (double) getContentPane().getBounds().getWidth() / (double) photoImage.getImage().getWidth(null);
            double b = (double) getContentPane().getBounds().getHeight() / (double) photoImage.getImage().getHeight(null);
            double ratio = Math.min(a, b);

            int scaledWidth = (int) (photoImage.getImage().getWidth(null) * ratio);
            int scaledHeight = (int) (photoImage.getImage().getHeight(null) * ratio);

            Image scaleImage = photoImage.getImage().getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_FAST);
            labelImage.setIcon(new ImageIcon(scaleImage));

            componentResizing = false;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        double titlebarHeight = getSize().getHeight();
        double baseHeight = 600;
        double w = (double) photoImage.getIconWidth();
        double h = (double) photoImage.getIconHeight();
        setSize((int) (w / h * (baseHeight - titlebarHeight)), (int) baseHeight);
    }
}
