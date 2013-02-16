/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.janezfeldin.Display;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

/**
 *
 * @author Janez Feldin
 */
public class ImageScreen implements Runnable
{
    private long timeBetweenFrames = 50;
    private double frameRateLimit = 20;
    private BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_3BYTE_BGR);
    
    
    private BufferStrategy strat;
    private Graphics2D g;
    private boolean running = false;
    private Thread screen;
    private Dimension screen_size;
    
    public ImageScreen()
    {
        screen_size = new Dimension(100,100);
    }
    
    public void setTimeBetweenFrames(long milSec)
    {
        timeBetweenFrames = milSec;
        frameRateLimit = (double)1000/frameRateLimit;
    }
    
    public long getTimeBetweenFrames()
    {
        return timeBetweenFrames;
    }
    
    public void setFrameRateLimit(double limit)
    {
        frameRateLimit = limit;
        timeBetweenFrames = (long)((double)1000/limit);
    }
    public double getFrameRateLimit()
    {
        return frameRateLimit;
    }

    public void setScreeDimension(Dimension size)
    {
        screen_size = size;
    }
    public Dimension getScreeDimension()
    {
        return screen_size;
    }
    
    public void showImage(BufferedImage image)
    {
        this.image = image;
        this.screen_size = new Dimension(image.getWidth(),image.getHeight());
    }

    @Override
    public void run()
    {
        while (running)
        {
            g = (Graphics2D) strat.getDrawGraphics();

            renderImageScreen();

            g.dispose();
            strat.show();

            try
            {
                screen.sleep(timeBetweenFrames);
            }
            catch (InterruptedException ex)
            {
                ex.printStackTrace();
            }

        }
    }

    /*public static Image loadImage(String file)
    {
        Image image = null;
        try
        {
            image = ImageIO.read(ImageScreen.class.getResource(file));
        }
        catch (Exception ex)
        {
            System.out.println(ex.toString());
            ex.printStackTrace();
        }
        return image;
    }*/

    public void setBuffer(BufferStrategy strat)
    {
        this.strat = strat;
    }

    public void renderImageScreen()
    {
        g.drawImage(image, 0, 0,null);
    }

    public void start()
    {
        if (!running)
        {
            running = true;
            screen = new Thread(this);
            screen.start();
        }

    }

    public void stop()
    {
        if (running)
        {
            running = false;
            try
            {
                screen.join();
            }
            catch (InterruptedException ex)
            {
                System.out.println(ex.toString());
                System.exit(0);
            }
        }
    }
}
