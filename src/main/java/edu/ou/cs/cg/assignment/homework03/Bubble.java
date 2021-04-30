package edu.ou.cs.cg.assignment.homework03;

public class Bubble
{

    private int x;
    private int y;
    private int radius;
    private int[] color;
    private int dx;
    private int dy;
    
    //construction
    Bubble(int x, int y, int r, int[] c, int dx, int dy)
    {
        this.x = x;
        this.y = y;
        radius = r;
        color = c;
        this.dx = dx;
        this.dy = dy;
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

    public int[] getColor()
    {
        return color;
    }

    public void setColor(int[] color)
    {
        this.color = color;
    }

    public int getDirectionX()
    {
        return dx;
    }
    
    public int getDirectionY()
    {
    	return dy;
    }
    
    public void setDirection(int dx, int dy)
    {
    	this.dx = dx;
    	this.dy = dy;
    }
    
    
}
