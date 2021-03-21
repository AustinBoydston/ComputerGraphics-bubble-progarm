package edu.ou.cs.cg.assignment.homework03;

public class Bubble
{

    private int x;
    private int y;
    private int radius;
    private int[] color;
    private int direction;
    
    //construction
    Bubble(int x, int y, int r, int[] c, int dir)
    {
        this.x = x;
        this.y = y;
        radius = r;
        color = c;
        direction = dir;
    }

    
    //getters and setters
    public int getX()
    {
        return x;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public int getY()
    {
        return y;
    }

    public void setY(int y)
    {
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

    public int getDirection()
    {
        return direction;
    }

    public void setDirection(int direction)
    {
        this.direction = direction;
    }
    
    
    
}
