package edu.ou.cs.cg.assignment.homework03;

// This class contains the variables and methods for a popping animation object implemented in our
// project for creating and keeping track of popping animations.
public class Pop
{

    private int x;						// x coordinate of pop origin
    private int y;						// y coordinate of pop origin
    private double radius;				// radius used for drawing
    private int timer;					// timer of frames spent active
    
    // constructor
    Pop(int x, int y, double r)
    {
        this.x = x;
        this.y = y;
        radius = r;
        timer = 1;
    }

    // getters and setters
    
    // returns the x coordinate
    public int getX()
    {
        return x;
    }

    // return the y coordinate
    public int getY()
    {
        return y;
    }

    // returns the radius
    public double getRadius()
    {
        return radius;
    }
    
    // returns the timer
    public int getTimer()
    {
    	return timer;
    }
    
    // changes the radius
    public void setRadius(double r)
    {
    	radius = r;
    }
    
    // changes the timer
    public void setTimer(int t)
    {
    	timer = t;
    } 
}
