package com.progen.wigdets;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.apache.log4j.Logger;

public class ScreenCapture {

    public static Logger logger = Logger.getLogger(ScreenCapture.class);
    String imgPath = null;
    File imageFile;

    public ScreenCapture(String userId, String path, String tempId) throws InterruptedException {
        try {
            Robot robot = new Robot();
            imgPath = path;
            //Thread.sleep(2000);
            Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
            BufferedImage bufferedImage = robot.createScreenCapture(new Rectangle(0, 145, 1400, 705));

            imageFile = new File("screenshotnnn.jpg");
            ImageIO.write(bufferedImage, "jpg", imageFile);
            DecreaseImageSize m = new DecreaseImageSize(path);
            m.createimage(path);
        } catch (AWTException e) {
            logger.error("Exception: ", e);
        } catch (IOException e) {
            logger.error("Exception: ", e);
        }
    }

    public String returnPath(String userId, String path, String tempId) throws IOException, InterruptedException {
        FileInputStream in = null;
        FileOutputStream out = null;
        try {
            in = new FileInputStream("screenshotnnn.jpg");
            ////////////////////////////////////////////////////////////////////////////////////.println.println("path is:: " + path + "\\web\\images\\screenshot" + tempId + ".jpg");
            //java.io.File f = new java.io.File("QueryDesigner");
            //System.
            //////////////////////////////////////////////////////////////////////////////////////.println.println("f is:: "+f.listFiles().length);
            out = new FileOutputStream(new File(path + "\\web\\images\\screenshot" + tempId + ".jpg"));

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        } catch (Exception exp) {
            ////////////////////////////////////////////////////////////////////////////////////.println.println("exception in file stream is:: ");
            logger.error("Exception: ", exp);
        } finally {
            in.close();
            out.close();
        }
        //Thread.sleep(3000);
        imgPath = imageFile.getAbsolutePath();
        return imgPath;
    }
}

class DecreaseImageSize extends JFrame {

    DecreaseImageSize(String path) {
        add(BorderLayout.CENTER, new ImagePanel(path));
        setSize(230, 180);
    }

    public void cut(String path) throws IOException {

        javax.swing.JFrame f = null;
        FileInputStream in = new FileInputStream("screenshotnnn.jpg");

//        JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(in);
//
//        BufferedImage bufImage = decoder.decodeAsBufferedImage();
//        BufferedImage cropped = bufImage.getSubimage(10, 30, 215, 140);
//        ImageIO.write(cropped, "jpeg", new File("screenshotnnn.jpg"));
    }

    public static void createimage(String path) throws RasterFormatException, IOException {
        DecreaseImageSize jrframe = new DecreaseImageSize(path);
        jrframe.setVisible(true);


        BufferedImage image = new BufferedImage(jrframe.getWidth(), jrframe.getHeight(), BufferedImage.TYPE_INT_RGB);

        Graphics2D g = image.createGraphics();
        jrframe.paint(g);

        g.dispose();
        ImageIO.write(image, "jpeg", new File("screenshotnnn.jpg"));

        jrframe.setVisible(false);

        jrframe.cut(path);

        jrframe.dispose();


    }
}

class ImagePanel extends JPanel {

    public static Logger logger = Logger.getLogger(ImagePanel.class);
    Image img = Toolkit.getDefaultToolkit().createImage("screenshotnnn.jpg");
    private int SCALE_FAST;

    ImagePanel(String path) {

        MediaTracker mt = new MediaTracker(this);

        mt.addImage(img, 0);

        try {
            mt.waitForAll();
        } catch (InterruptedException e) {
            logger.error("Exception: ", e);
        }
    }

    protected void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;
        int newW = 230; //(int)(originalImage.getWidth() * xScaleFactor);
        int newH = 180; //(int)(originalImage.getHeight() * yScaleFactor);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        super.paintComponent(g);

        g2.drawImage(img, 0, 0, newW, newH, null);

    }

    public void buildThumbNail() {
        Runtime run = Runtime.getRuntime();

    }
}
