package edu.ou.cs.cg.assignment.homework03;

// This class contains the variables and methods for a bubble object implemented in our
// project for creating and storing bubble information.
public class Bubble
{

    private int x;				// x coordinate
    private int y;				// y coordinate
    private int radius;			// radius of the bubble
    private int dx;				// rate of change for x coordinate
    private int dy;				// rate of change for y coordinate
    
    // constructor
    Bubble(int x, int y, int r, int dx, int dy)
    {
        this.x = x;
        this.y = y;
        radius = r;
        this.dx = dx;
        this.dy = dy;
    }

    //getters and setters
    
    // returns the x coordinate
    public int getX()
    {
        return x;
    }

    // returns the y coordinate
    public int getY()
    {
        return y;
    }

    // changes the current coordinates
    public void setPos(int x, int y)
    {
    	this.x = x;
        this.y = y;
    }

    // returns the radius
    public int getRadius()
    {
        return radius;
    }

    // changes the radius
    public void setRadius(int radius)
    {
        this.radius = radius;
    }

    // returns the rate of change for x
    public int getDirectionX()
    {
        return dx;
    }
    
    // returns the rate of change for y
    public int getDirectionY()
    {
    	return dy;
    }
    
    // changes the rate of direction
    public void setDirection(int dx, int dy)
    {
    	this.dx = dx;
    	this.dy = dy;
    }
    
    
}
