/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.janezfeldin.Display;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 *
 * @author Janez Feldin
 */
public class ImageWindow extends JFrame
{

    ImageScreen screen;
//    Canvas canvas;

    public ImageWindow()
    {
        screen = new ImageScreen();
        Canvas canvas = new Canvas();
        canvas.setSize(screen.getScreeDimension());
        canvas.setIgnoreRepaint(true);
        canvas.requestFocus();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        this.setResizable(false);
        this.add(canvas);

        this.pack();

        canvas.createBufferStrategy(2);
        screen.setBuffer(canvas.getBufferStrategy());

        screen.start();

        this.setIgnoreRepaint(true);
    }

    public static void main(String args[])
    {
        new ImageWindow();
    }

    public void showImage(String url) throws IOException
    {
        BufferedImage image = ImageIO.read(new File(url));
        screen.showImage(image);
//        canvas.setSize(screen.getScreeDimension());
        this.setSize(screen.getScreeDimension());
    }

    public void showImage(BufferedImage image)
    {
        screen.showImage(image);
//        canvas.setSize(screen.getScreeDimension());
        this.setSize(screen.getScreeDimension());
    }

    public void setFrameRateLimit(double limit)
    {
        screen.setFrameRateLimit(limit);
    }

    public double getFrameRateLimit()
    {
        return screen.getFrameRateLimit();
    }
}
