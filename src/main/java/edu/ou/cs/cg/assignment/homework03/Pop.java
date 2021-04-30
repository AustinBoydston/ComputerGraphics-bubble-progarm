package edu.ou.cs.cg.assignment.homework03;

public class Pop
{

    private int x;
    private int y;
    private int radius;
    private int timer;
    
    //construction
    Pop(int x, int y, int r)
    {
        this.x = x;
        this.y = y;
        radius = r;
        timer = 30;
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

    public void setPos(int x, int y)
    {
    	this.x = x;
        this.y = y;
    }

    public int getRadius()
    {
        return radius;
    }

    public void setRadius(int radius)
    {
        this.radius = radius;
    }
    
    
    
}
