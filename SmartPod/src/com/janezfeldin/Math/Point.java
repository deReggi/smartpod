/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.janezfeldin.Math;

/**
 *
 * @author Janez Feldin
 */
public class Point
{
    public double x;
    public double y;
    
    public Point()
    {
        x = 0.0;
        y = 0.0;
    }
    
    public Point(Point p)
    {
        x = p.x;
        y = p.y;
    }
    
    public Point(double x,double y)
    {
        this.x = x;
        this.y = y;
    }
    
    public double getX()
    {
        return x;
    }
    
    public void setX(double x)
    {
        this.x = x;
    }
    
    public double getY()
    {
        return y;
    }
    
    public void setY(double y)
    {
        this.y = y;
    }
    
    /**
     * Returns the distance between two points. 
     */
    public double getDistanceTo(Point p)
    {
        return Math.sqrt(p.x*p.x+p.y*p.y);
    }
    
    /**
     * Returns the distance in horizontal direction between two points. 
     */
    public double getDistanceX(Point p)
    {
        return Math.abs(Math.pow(p.x-x,2));
    }
    
    /**
     * Returns the distance in vertical direction between two points. 
     */
    public double getDistanceY(Point p)
    {
        return Math.abs(Math.pow(p.y-y,2));
    }
    
    @Override
    public String toString()
    {
        return "x: "+x+"\ty: "+y;
    }
}
