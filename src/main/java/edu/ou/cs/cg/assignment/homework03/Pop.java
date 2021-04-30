package edu.ou.cs.cg.assignment.homework03;

public class Pop
{

    private int x;
    private int y;
    private double radius;
    private int timer;
    
    //construction
    Pop(int x, int y, double r)
    {
        this.x = x;
        this.y = y;
        radius = r;
        timer = 1;
    }

    
    //getters and setters
    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public double getRadius()
    {
        return radius;
    }
    
    public int getTimer()
    {
    	return timer;
    }
    
    public void setRadius(double r)
    {
    	radius = r;
    }
    
    public void setTimer(int t)
    {
    	timer = t;
    } 
}
