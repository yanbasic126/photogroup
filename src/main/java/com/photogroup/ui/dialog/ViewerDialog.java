package com.photogroup.ui.dialog;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.KeyStroke;

import com.photogroup.groupby.metadata.MetadataReader;
import com.photogroup.ui.util.UIUilt;

public class ViewerDialog extends JFrame {

    private static final long serialVersionUID = 1L;

    private JLabel labelImage;

    private boolean componentResizing = false;

    private ImageIcon photoImage;

    public ViewerDialog(File photo) {
        getContentPane().setBackground(Color.BLACK);
        setTitle(photo.getAbsolutePath());
        setIconImage(Toolkit.getDefaultToolkit().getImage(ViewerDialog.class.getResource("/icon/lemon_16.png")));
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
        getContentPane().setLayout(UIUilt.createGridBagLayout(new int[] { 148, 0 }, new int[] { 1, 0 },
                new double[] { 1.0, Double.MIN_VALUE }, new double[] { 1.0, Double.MIN_VALUE }));
        labelImage = new JLabel();
        getContentPane().add(labelImage, UIUilt.createGridBagConstraints(-1, -1, null, 0, 0));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Cancel"); //$NON-NLS-1$
        getRootPane().getActionMap().put("Cancel", new AbstractAction() { //$NON-NLS-1$

            private static final long serialVersionUID = 1L;

            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
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

        try {
            BufferedImage image = ImageIO.read(photo);
            // https://stackoverflow.com/questions/5905868/how-to-rotate-jpeg-images-based-on-the-orientation-metadata
            int orientation = MetadataReader.orientation(photo);
            if (orientation != 1) {
                // BufferedImage image = ImageIO.read(new File(path));
                AffineTransform t = new AffineTransform();
                int sw = image.getWidth();
                int sh = image.getHeight();
                switch (orientation) {
                case 1:
                    break;
                case 2: // Flip X
                    t.scale(-1.0, 1.0);
                    t.translate(-sw, 0);

                    break;
                case 3: // PI rotation
                    t.translate(sw, sh);
                    t.rotate(Math.PI);
                    break;
                case 4: // Flip Y
                    t.scale(1.0, -1.0);
                    t.translate(0, -sh);
                    break;
                case 5: // - PI/2 and Flip X
                    t.rotate(-Math.PI / 2);
                    t.scale(-1.0, 1.0);
                    break;
                case 6: // -PI/2 and -width
                    t.translate(sh, 0);
                    t.rotate(Math.PI / 2);
                    break;
                case 7: // PI/2 and Flip
                    t.scale(-1.0, 1.0);
                    t.translate(-sh, 0);
                    t.translate(0, sw);
                    t.rotate(3 * Math.PI / 2);
                    break;
                case 8: // PI / 2
                    t.translate(0, sw);
                    t.rotate(3 * Math.PI / 2);
                    break;
                default:
                    break;
                }
                BufferedImage bscaleImage = new BufferedImage(sw, sh, Image.SCALE_FAST);
                bscaleImage.getGraphics().drawImage(image, 0, 0, null);
                AffineTransformOp op = new AffineTransformOp(t, AffineTransformOp.TYPE_BICUBIC);
                BufferedImage destinationImage = op.createCompatibleDestImage(bscaleImage,
                        (image.getType() == BufferedImage.TYPE_BYTE_GRAY) ? image.getColorModel() : null);
                // Graphics2D g = destinationImage.createGraphics();
                // g.setBackground(Color.WHITE);
                // g.clearRect(0, 0, destinationImage.getWidth(), destinationImage.getHeight());
                destinationImage = op.filter(bscaleImage, destinationImage);
                photoImage = new ImageIcon(destinationImage);
            } else {
                photoImage = new ImageIcon(image);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class ResizeRender implements Runnable {

        @Override
        public void run() {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int imageWidth = photoImage.getImage().getWidth(null);
            int imageHeight = photoImage.getImage().getHeight(null);
            double contentWidth = (double) getContentPane().getBounds().getWidth() / (double) imageWidth;
            double contentHeight = (double) getContentPane().getBounds().getHeight() / (double) imageHeight;
            double ratio = Math.min(contentWidth, contentHeight);
            int scaledWidth = (int) (imageWidth * ratio);
            int scaledHeight = (int) (imageHeight * ratio);

            Image scaleImage = photoImage.getImage().getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_FAST);
            labelImage.setIcon(new ImageIcon(scaleImage));

            componentResizing = false;
        }
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        double titlebarHeight = getSize().getHeight();
        double baseW = 600;
        double baseH = 600;
        double w = (double) photoImage.getIconWidth();
        double h = (double) photoImage.getIconHeight();
        // Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        // double screenH = screenSize.getHeight();
        // double screenW = screenSize.getHeight();
        int scaledWidth = 0;
        int scaledHeight = 0;
        if (w > h) {
            scaledWidth = (int) (w / h * baseW - titlebarHeight);
            scaledHeight = (int) baseW;
            // [:-)]
            // void java.awt.Window.setSize(int width, int height)
        } else {
            // []
            scaledWidth = (int) (baseH);
            scaledHeight = (int) (h / w * baseH);
        }
        setSize(scaledWidth, scaledHeight);
    }
}
