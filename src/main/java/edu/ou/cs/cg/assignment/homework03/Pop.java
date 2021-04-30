package edu.ou.cs.cg.assignment.homework03;

// This class contains the variables and methods for a popping animation object implemented in our
// project for creating and keeping track of popping animations.
public class Pop
{

    private int x;						// x coordinate of pop origin
    private int y;						// y coordinate of pop origin
    private double radius;				// radius used for drawing
    private int timer;					// timer of frames spent active
    private float[] color;				// float[] for material color
    
    // Constructor
    Pop(int x, int y, double r, float[] c)
    {
        this.x = x;
        this.y = y;
        radius = r;
        timer = 1;
        color = c;
    }

    // Getters and setters
    
    // Returns the x coordinate
    public int getX()
    {
        return x;
    }

    // Return the y coordinate
    public int getY()
    {
        return y;
    }

    // Returns the radius
    public double getRadius()
    {
        return radius;
    }
    
    // Returns the timer
    public int getTimer()
    {
    	return timer;
    }
    
    // Changes the radius
    public void setRadius(double r)
    {
    	radius = r;
    }
    
    // Changes the timer
    public void setTimer(int t)
    {
    	timer = t;
    } 
    
    // Returns material color
    public float[] getColor()
    {
    	return color;
    }
}
