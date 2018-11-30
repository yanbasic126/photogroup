package com.photogroup.ui.dialog;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.KeyStroke;

public class ViewerDialog extends JFrame implements ActionListener {

    private JLabel labelImage;

    private boolean componentResizing = false;

    ImageIcon png;

    public ViewerDialog(String path) {
        setTitle(path);
        setIconImage(Toolkit.getDefaultToolkit().getImage(AboutAndUpdateDialog.class.getResource("/icon/lemon_16.png")));
        png = new ImageIcon(path);
        setSize(100, 0);
        this.addComponentListener(new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent e) {
                if (png != null) {

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
            double a = (double) getContentPane().getBounds().getWidth() / (double) png.getImage().getWidth(null);
            double b = (double) getContentPane().getBounds().getHeight() / (double) png.getImage().getHeight(null);
            double ratio = Math.min(a, b);

            int scaledWidth = (int) (png.getImage().getWidth(null) * ratio);
            int scaledHeight = (int) (png.getImage().getHeight(null) * ratio);

            Image scaleImage = png.getImage().getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_FAST);
            labelImage.setIcon(new ImageIcon(scaleImage));

            componentResizing = false;
        }

    }

    public static void main(String[] args) {
        ViewerDialog vd = new ViewerDialog("D:\\yyi.talendbj.esb\\picture\\test\\IMG_1224 - Copy - Copy (2).JPG");
        vd.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        double titlebarHeight = getSize().getHeight();
        double baseHeight = 600;
        double w = (double) png.getIconWidth();
        double h = (double) png.getIconHeight();
        setSize((int) (w / h * (baseHeight - titlebarHeight)), (int) baseHeight);
    }
}
