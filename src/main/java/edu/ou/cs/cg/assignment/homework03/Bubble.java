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
    private float[] emit;		// material color
    
    // Constructor
    Bubble(int x, int y, int r, int dx, int dy, float[] emit)
    {
        this.x = x;
        this.y = y;
        radius = r;
        this.dx = dx;
        this.dy = dy;
        this.emit = emit;
    }

    // Getters and setters
    
    // Returns the x coordinate
    public int getX()
    {
        return x;
    }

    // Returns the y coordinate
    public int getY()
    {
        return y;
    }

    // Changes the current coordinates
    public void setPos(int x, int y)
    {
    	this.x = x;
        this.y = y;
    }

    // Returns the radius
    public int getRadius()
    {
        return radius;
    }

    // Changes the radius
    public void setRadius(int radius)
    {
        this.radius = radius;
    }

    // Returns the rate of change for x
    public int getDirectionX()
    {
        return dx;
    }
    
    // Returns the rate of change for y
    public int getDirectionY()
    {
    	return dy;
    }
    
    // Changes the rate of direction
    public void setDirection(int dx, int dy)
    {
    	this.dx = dx;
    	this.dy = dy;
    }
    
    // Returns the color float[] for materials
    public float[] getColor()
    {
    	return emit;
    }
}
