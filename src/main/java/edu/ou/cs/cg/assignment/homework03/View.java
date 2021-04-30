//******************************************************************************
// Copyright (C) 2016-2019 University of Oklahoma Board of Trustees.
//******************************************************************************
// Last modified:  Fri Apr 30 2021 by Team 7
//******************************************************************************
// Major Modification History:
//
// 20160209 [weaver]:	Original file.
// 20190203 [weaver]:	Updated to JOGL 2.3.2 and cleaned up.
// 20190227 [weaver]:	Updated to use model and asynchronous event handling.
// 20210430 [team7]:	Final edits to project.
//
//******************************************************************************
// Notes: This documents contains structure of code used and given for use
//        during the semester for our homeworks and were adopted for usage in
//        our project. All the non-generic code unique to our project will
//        have comments to explain its purpose.
//******************************************************************************

package edu.ou.cs.cg.assignment.homework03;

//import java.lang.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;
import javax.swing.*;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.*;
import com.jogamp.opengl.glu.*;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.awt.TextRenderer;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.texture.*;

import edu.ou.cs.cg.utilities.Utilities;

//******************************************************************************

/**
 * The <CODE>View</CODE> class.<P>
 *
 * @author  Team 7
 * @version %I%, %G%
 */
public final class View
	implements GLEventListener
{
	//**********************************************************************
	// Private Class Members
	//**********************************************************************

	private static final int			DEFAULT_FRAMES_PER_SECOND = 60;
	private static final DecimalFormat	FORMAT = new DecimalFormat("0.000");
	// A constant for the sides of a bubble
    private static final int       		SIDES_BUBBLE = 18;
    // A constant for the angle per side on a bubble
    private static final double 		BUBBLE_ANGLE = 2.0 * Math.PI / SIDES_BUBBLE;

	//**********************************************************************
	// Public Class Members
	//**********************************************************************

	public static final GLUT			MYGLUT = new GLUT();
	public static final Random			RANDOM = new Random();   	// Random generator
	
	public static final String			RSRC = "images/";			// Location of textures
	private static final String[]		FILENAMES = 				// Texture name storage
		{
				"bubble.jpg",			// Bubble texture
				"underwater.jpg"		// Background water texture
		};

	//**********************************************************************
	// Private Members
	//**********************************************************************

	// State (internal) variables
	private final GLJPanel				canvas;
	private int						w;			// Canvas width
	private int						h;			// Canvas height

	private TextRenderer				renderer;

	private final FPSAnimator			animator;
	private int						counter;	// Frame counter

	private final Model				model;
	
	private Texture[]				textures;

	private final KeyHandler			keyHandler;
	private final MouseHandler			mouseHandler;

	//**********************************************************************
	// Constructors and Finalizer
	//**********************************************************************

	public View(GLJPanel canvas)
	{
		this.canvas = canvas;

		// Initialize rendering
		counter = 0;
		canvas.addGLEventListener(this);

		// Initialize model (scene data and parameter manager)
		model = new Model(this);

		// Initialize controller (interaction handlers)
		keyHandler = new KeyHandler(this, model);
		mouseHandler = new MouseHandler(this, model);

		// Initialize animation
		animator = new FPSAnimator(canvas, DEFAULT_FRAMES_PER_SECOND);
		animator.start();
	}

	//**********************************************************************
	// Getters and Setters
	//**********************************************************************

	public GLJPanel	getCanvas()
	{
		return canvas;
	}

	public int	getWidth()
	{
		return w;
	}

	public int	getHeight()
	{
		return h;
	}

	//**********************************************************************
	// Override Methods (GLEventListener)
	//**********************************************************************

	public void	init(GLAutoDrawable drawable)
	{
		w = drawable.getSurfaceWidth();
		h = drawable.getSurfaceHeight();

		renderer = new TextRenderer(new Font("Monospaced", Font.PLAIN, 12),
									true, true);

		initPipeline(drawable);
		initTextures(drawable);
	}

	public void	dispose(GLAutoDrawable drawable)
	{
		renderer = null;
	}

	public void	display(GLAutoDrawable drawable)
	{
		updatePipeline(drawable);

		update(drawable);
		render(drawable);
	}

	public void	reshape(GLAutoDrawable drawable, int x, int y, int w, int h)
	{
		this.w = w;
		this.h = h;
	}

	//**********************************************************************
	// Private Methods (Rendering)
	//**********************************************************************

	private void	update(GLAutoDrawable drawable)
	{
		counter++;									// Advance animation counter
	}

	private void	render(GLAutoDrawable drawable)
	{
		GL2	gl = drawable.getGL().getGL2();

		gl.glClear(GL.GL_COLOR_BUFFER_BIT);		// Clear the buffer

		// Draw the scene
		drawMain(gl);								// Draw main content
		drawMode(drawable);						// Draw mode text

		gl.glFlush();								// Finish and display
	}

	//**********************************************************************
	// Private Methods (Pipeline)
	//**********************************************************************

	private void	initPipeline(GLAutoDrawable drawable)
	{
		GL2	gl = drawable.getGL().getGL2();
		
		// Enable lighting
		gl.glEnable(GL2.GL_LIGHTING);
		gl.glEnable(GL2.GL_NORMALIZE);
		
		// Setup for light parameters
        float SHINE_ALL_DIRECTIONS = 1;
        float[] lightPos = {640, 360, 30, SHINE_ALL_DIRECTIONS};
        float[] lightColorAmbient = {0.5f, 0.5f, 0.5f, 1f};
        float[] lightColorSpecular = {0.8f, 0.8f, 0.8f, 1f};

        // Set light parameters.
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, lightPos, 0);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_AMBIENT, lightColorAmbient, 0);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPECULAR, lightColorSpecular, 0);

        // Enable light source
        gl.glEnable(GL2.GL_LIGHT1);

		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);	// Black background
	}
	
	// Method adopted from Homework 6 to locate and store texture objects
	private void	initTextures(GLAutoDrawable drawable)
	{
		GL2 gl = drawable.getGL().getGL2();
		
		// Initialize texture storage array
		textures = new Texture[FILENAMES.length];
		
		// Loop that finds all the files with textures based on the size of our name storage
		for (int i = 0; i < FILENAMES.length; i++)
		{
			// Process of using the given location and names to search for a file else throw exception
			try
			{
				URL url = View.class.getResource(RSRC + FILENAMES[i]);
				
				if (url != null)
				{
					textures[i] = TextureIO.newTexture(url, false, TextureIO.JPG);
					
					textures[i].setTexParameteri(gl, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR);
					textures[i].setTexParameteri(gl, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
					textures[i].setTexParameteri(gl, GL2.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP_TO_EDGE);
					textures[i].setTexParameteri(gl, GL2.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP_TO_EDGE);
				}
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
				System.exit(1);;
			}
		}
	}

	// Contains redundant information
	private void	updatePipeline(GLAutoDrawable drawable)
	{
		GL2			gl = drawable.getGL().getGL2();
		GLU			glu = GLU.createGLU();
		Point2D.Double	origin = model.getOrigin();

		//float			xmin = (float)(origin.x - 1.0);
		//float			xmax = (float)(origin.x + 1.0);
		//float			ymin = (float)(origin.y - 1.0);
		//float			ymax = (float)(origin.y + 1.0);

		//gl.glMatrixMode(GL2.GL_PROJECTION);		// Prepare for matrix xform
		//gl.glLoadIdentity();						// Set to identity matrix
		//glu.gluOrtho2D(xmin, xmax, ymin, ymax);	// 2D translate and scale
	}
	
	// Method of setting the size and location of the screen projection, based
	// on a format of 1280x720 pixel size by default
	private void    setScreenProjection(GL2 gl)
	{
        GLU glu = GLU.createGLU();

        gl.glMatrixMode(GL2.GL_PROJECTION);     // Prepare for matrix xform
        gl.glLoadIdentity();                        // Set to identity matrix
        glu.gluOrtho2D(0.0f, 1280.0f, 0.0f, 720.0f);// 2D translate and scale
    }

	//**********************************************************************
	// Private Methods (Scene)
	//**********************************************************************

	// Method for rendering text on the scene
	private void	drawMode(GLAutoDrawable drawable)
	{
		GL2		gl = drawable.getGL().getGL2();
		
		// Unused material
		//double[]	p = Utilities.mapViewToScene(gl, 0.5 * w, 0.5 * h, 0.0);
		//double[]	q = Utilities.mapSceneToView(gl, 0.0, 0.0, 0.0);
		//String		svc = ("View center in scene: [" + FORMAT.format(p[0]) +
		//				   " , " + FORMAT.format(p[1]) + "]");
		//String		sso = ("Scene origin in view: [" + FORMAT.format(q[0]) +
		//				   " , " + FORMAT.format(q[1]) + "]");

		renderer.beginRendering(w, h);

		// Draw all text in white
		renderer.setColor(1.0f, 010f, 1.0f, 1.0f);

		Point2D.Double	cursor = model.getCursor();

		// Prints cursor location, currently unused
		/*
		if (cursor != null)
		{
			String		sx = FORMAT.format(new Double(cursor.x));
			String		sy = FORMAT.format(new Double(cursor.y));
			String		s = "Pointer at (" + sx + "," + sy + ")";

			renderer.draw(s, 2, 2);
		}
		else
		{
			renderer.draw("No Pointer", 2, 2);
		}
		*/

		// Output for the number of total bubbles popped which is stored in Model
		String count = "Number of bubles popped: " + model.getCount();
		
		//renderer.draw(svc, 2, 16);
		//renderer.draw(sso, 2, 30);
		renderer.draw(count, 2, 700);				// Prints in the top left

		renderer.endRendering();
	}

	// Method containing the main drawing methods for rendering
	private void	drawMain(GL2 gl)
	{
		// Orientates the screen projection as specified
	    setScreenProjection(gl);
	    // Sets the background texture
	    background(gl);
	    // Call for setting color to black for default, mostly cautionary
		setColor(gl, 0, 0, 0);
		// Method for drawing the generated bubbles
	    drawBubble(gl);
	    // Method for drawing the popping animations
	    drawPopped(gl);
	}

	// Method for ease of use with glColor
	private void   setColor(GL2 gl, int r, int g, int b, int a)
    {
        gl.glColor4f(r / 255.0f, g / 255.0f, b / 255.0f, a / 255.0f);
    }

	// Method for ease of use with glColor
    private void    setColor(GL2 gl, int r, int g, int b)
    {
        setColor(gl, r, g, b, 255);
    }
    
    // Method for generating background texure
    private void background(GL2 gl)
    {
    	// Setup for choosing the background water texture and allowing for enabling/
    	// binding
    	Texture tex = textures[1];
    	tex.enable(gl);
    	tex.bind(gl);
    	
    	// Begin drawing, uses quads
    	gl.glBegin(GL2.GL_QUADS);
    	
    	// Gets the normalized coordinates of the texture
    	TextureCoords coords = tex.getImageTexCoords();
    	
    	// Parameters for setting up material
    	float[] emit = new float[] {0.5f, 0.5f, 0.5f, 1.0f};
	    float[] shine = new float[] {0.0f, 1.0f, 1.0f, 0.5f};
	    float[] amb = new float[] {0.0f, 1.0f, 1.0f, 1.0f};
	    float[] spec = new float[] {0.0f, 1.0f, 1.0f, 0.5f};
	    float[] dif = new float[] {0.0f, 1.0f, 1.0f, 1.0f};
	    gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL2.GL_EMISSION, emit, 0);
	    gl.glMaterialfv(GL.GL_FRONT, GL2.GL_SHININESS, shine, 0);
	    //gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL2.GL_AMBIENT, amb, 0);
	    gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, dif, 0);
	    //gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, spec, 0);
    	
    	//setColor(gl, 0, 0, 140);								// Color for debugging
	    // Assign the bottom left texture coord to the respective vertex of the quad
    	gl.glTexCoord2f(coords.left(), coords.bottom());
    	gl.glVertex2f(0.0f,0.0f);
    	
    	//setColor(gl, 0, 0, 140);
    	// Assign the top left texture coord to the respective vertex of the quad
    	gl.glTexCoord2f(coords.left(), coords.top());
    	gl.glVertex2f(0.0f, 720.0f);
    	
    	//setColor(gl, 255, 255, 255);
    	// Assign the top right texture coord to the respective vertex of the quad
    	gl.glTexCoord2f(coords.right(), coords.top());
    	gl.glVertex2f(1280.0f, 720.0f);
    	
    	//setColor(gl, 255, 255, 255);
    	// Assign the bottom right texture coord to the respective vertex of the quad
    	gl.glTexCoord2f(coords.right(), coords.bottom());
    	gl.glVertex2f(1280.0f, 0.0f);
    	
    	gl.glEnd();

    	tex.disable(gl);
    }
    
    // Method adopted from homework06 to simulate natural cascade effects for bubble
    // generation
    public static int randomCascade(double d)
	{
		int	n = 0;

		while (RANDOM.nextDouble() > d)
			n++;

		return n;
	}
   
    // Method for generating stored bubbles
	private void drawBubble(GL2 gl)
	{
	    // paramters used for creating/generating a bubble
	    double theta = BUBBLE_ANGLE;		// Angle for one side of a bubble
        int cx = 100;						// Initialization of x coordinate 
        int cy = 100;						// Initialization of y coordinate
        int r = 20;							// Radius of bubbles
        int xSpawn = 400;					// Initial spawning coordinate for x
        int ySpawn = 300;					// Initial spawning coordinate for y
        int dirx = 0;						// Initialization of x rate of change
        int diry = 0;						// Initialization of y rate of change
        
        // Create new bubble every 1 second (60 frames)
        if( counter % 60 == 0)
        {
        	for (int l = 0; l < randomCascade(0.8) + 1; l++)
        	{
	        	// Boolean for determining x direction
	        	if (RANDOM.nextBoolean() == true)
	            	dirx = 1;
	            else
	            	dirx = -1;
	        	
	        	// Boolean for determining y direction
	        	if (RANDOM.nextBoolean() == true)
	        		diry = 1;
	        	else
	        		diry = -1;
	        	
	            // Create a new bubble using random numbers between 0 and 200 for the intial position
	        	// of spawn, the specified radius above, and random numbers between 1 and 3 for
	        	// determining rate of change of each coordinate with dirx and diry being the decider
	        	// for inversing the value
	            model.createBubble(RANDOM.nextInt(200) + xSpawn , RANDOM.nextInt(200) + ySpawn, 
	            		r, dirx*(RANDOM.nextInt(2)+1), diry*(RANDOM.nextInt(2)+1));
        	}
        }
        
        //draw bubbles if there are any stored in the list in Model
        if(!model.getBubbleList().isEmpty())
        {
	        //iterate through the list of bubbles
	        for(int j = 0; j < model.getBubbleList().size(); j++)
	        {
	        	// Setup for parameters of coordinates and radius
	            cx = model.getBubbleList().get(j).getX();
	            cy = model.getBubbleList().get(j).getY();
	            r = model.getBubbleList().get(j).getRadius();
	            
	            // Begin drawing
			    gl.glBegin(GL2.GL_POLYGON);
			
			    //setColor(gl, 0, 255, 255);            // Cyan, unused
			    
			    // Setup for material properties, bubbles colored with cyan
			    float[] emit = new float[] {0.0f, 1.0f, 1.0f, 1.0f};
			    float[] shine = new float[] {0.0f, 1.0f, 1.0f, 0.5f};
			    float[] amb = new float[] {0.0f, 1.0f, 1.0f, 1.0f};
			    float[] spec = new float[] {0.0f, 1.0f, 1.0f, 0.5f};
			    float[] dif = new float[] {0.0f, 1.0f, 1.0f, 1.0f};
			    gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL2.GL_EMISSION, emit, 0);
			    gl.glMaterialfv(GL.GL_FRONT, GL2.GL_SHININESS, shine, 0);
			    //gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL2.GL_AMBIENT, amb, 0);
			    gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, dif, 0);
			    //gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, spec, 0);
			    
			    // Locate the appropriate texture and prepare for binding
			    Texture tex = textures[0];
		    	tex.enable(gl);
		    	tex.bind(gl);
		    	TextureCoords coords = tex.getImageTexCoords();
		    	
			    // Draw the bubble by using a polygon with many sides
			    for (int i=0; i<SIDES_BUBBLE+1; i++)      // 18 sides
			    {
			    	float xtex = 0.5f*(((float) Math.cos(theta))+1.0f);
			    	float ytex = 0.5f*(((float) Math.sin(theta))+1.0f);
			    	gl.glTexCoord2f(xtex, ytex);
			        gl.glVertex2d(cx + r * Math.cos(theta), cy + r * Math.sin(theta));
			        theta += BUBBLE_ANGLE;
			    }
			
			    gl.glEnd();
			    
			    tex.disable(gl);
			    
			    
			    // Update position of bubble
			    model.updatePosition(j);
			    
			    // Check for overlap
			    for (int i = 0; i < model.getBubbleList().size(); i++)
			    {
			    	// Don't check the same location
			    	if ( i != j)
			    	{
			    		// Setup for parameters of importance
			    		// Save the curren bubble, a, and the bubble specified by the loop, b
				    	Bubble a = model.getBubbleList().get(j);
				    	Bubble b = model.getBubbleList().get(i);
				    	// Radii of both bubbles
				    	int arad = a.getRadius();
				    	int brad = b.getRadius();
				    	// Coordinates for both bubbles
				    	double ax = (double) a.getX();
				    	double ay = (double) a.getY();
				    	double bx = (double) b.getX();
				    	double by = (double) b.getY();
				    	// Rate of change for both bubbles
				    	int adx = a.getDirectionX();
				    	int ady = a.getDirectionY();
				    	int bdx = b.getDirectionX();
				    	int bdy = b.getDirectionY();
				    	
				    	// Calculate the distance between two point
				    	double dist =  Math.sqrt(((ax - bx)*(ax - bx)) + ((ay - by)*(ay - by)));
				    	
				    	// If the distance bewteen the two bubble origins is less than both radii
				    	// combined, bubbles overlap
				    	if (dist < (double) (arad + brad))
				    	{
				    		// Bubble a is larger or equal to, therefore it absorbs b
				    		if (arad >= brad)
				    		{
				    			// Set a new radius, a combination of both original radii
				    			// of a and b, to bubble a
				    			model.getBubbleList().get(j).setRadius(arad + brad);
				    			// Set a new rate of change by combining both bubbles' rates for
				    			// bubble a
				    			model.getBubbleList().get(j).setDirection(adx + bdx, ady + bdy);
				    			// Remove bubble b from the list
				    			model.removeBubble(i);
				    		}
				    		// Bubble b is larger and absorbs a
				    		else if (brad > arad)
				    		{
				    			// Set a new radius, a combination of both original radii
				    			// of a and b, to bubble b
				    			model.getBubbleList().get(i).setRadius(arad + brad);
				    			// Set a new rate of change by combining both bubbles' rates for
				    			// bubble b
				    			model.getBubbleList().get(i).setDirection(adx + bdx, ady + bdy);
				    			// Remove bubble a from the list
				    			model.removeBubble(j);
				    		}
				    	}
			    	}
			    }
			    
			    //check if bubble is still in the frame
			    if(cx < 0 || cx > 1280 || cy < 0 || cy > 720)
			    {
			        //delete the bubble when it exits the frame
			        model.getBubbleList().remove(j);
			    }
	        }
        }
	}
	
	// Method that draws the stored popping animations for popped bubbles
	private void drawPopped(GL2 gl)
	{
		// Generate animations if there exists any
		if (!model.getPoppedList().isEmpty())
		{
			// Iterate through the list
			for (int i = 0; i < model.getPoppedList().size(); i++)
			{
				// Setup for parameters of the animation
				double cx = (double) model.getPoppedList().get(i).getX();
				double cy = (double) model.getPoppedList().get(i).getY();
				int timer = model.getPoppedList().get(i).getTimer();
				double r = model.getPoppedList().get(i).getRadius();
				
				// Setup for the material of the animation
				float[] emit = new float[] {0.0f, 1.0f, 1.0f, 1.0f};
			    float[] shine = new float[] {0.0f, 1.0f, 1.0f, 0.5f};
			    float[] amb = new float[] {0.0f, 1.0f, 1.0f, 1.0f};
			    float[] spec = new float[] {0.0f, 1.0f, 1.0f, 0.5f};
			    float[] dif = new float[] {0.0f, 1.0f, 1.0f, 1.0f};
			    gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL2.GL_EMISSION, emit, 0);
			    gl.glMaterialfv(GL.GL_FRONT, GL2.GL_SHININESS, shine, 0);
			    //gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL2.GL_AMBIENT, amb, 0);
			    gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, dif, 0);
			    //gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, spec, 0);
			    
				// Quadratic equation used to account for gravity
				double gy = 0.1*timer*timer;
				
				// Quadratic equation used to account for gravity with initial upward velocity
				double gyu = 0;
				if (timer > 1)
					gyu = 0.1*timer*timer - 2*timer;
				
				//
				// Water droplets
				// 4 water droplets are generated using a mixture of triangle and half circle
				// structure that appear on different sides of the popped bubble star done later
				//
				
				// Angle for the half circles used in the droplets
				double theta = BUBBLE_ANGLE/20;
				
				// First droplet (right side of bubble)
				// Each y value has a timer amount subtracted in each frame to the y coordinate
				// to cause the drop to move from its horizontal to a vertical position as well 
				// as the value gy which is the quadratic value for gravity velocity increase 
				// as well as a value of timer*2 for a constant horizontal movement from popping 
				// out the side.
				gl.glBegin(GL2.GL_POLYGON);
				
				gl.glVertex2d(cx + timer*2 + timer, cy - gy);					// Peak vertex point
				gl.glVertex2d(cx + 20 + timer*2, cy + 10 - timer - gy);			// Vertex before half circle
				// Iterate through the half circle with 19 sides
				for (int j=0; j<20; j++)
				{
					gl.glVertex2d((cx+20 + timer*2) + 10 * Math.cos(theta), cy - gy - timer + 10 * Math.sin(theta));
		        	theta += BUBBLE_ANGLE;
				}
				gl.glVertex2d(cx + 20 + timer*2, cy - 10 - timer - gy);			// Last vertex after half circle
				
				gl.glEnd();
				
				// Second droplet (bottom side of bubble)
				// The simplest of the drops with no movement except for the downwards
				// quadratic of gravity.
				gl.glBegin(GL2.GL_POLYGON);
				
				gl.glVertex2d(cx, cy - gy);					// Peak vertex point
				gl.glVertex2d(cx + 10, cy - 20 - gy);		// Vertex before half circle
				// Iterate through the half circle with 19 sides
				for (int j=0; j<20; j++)
				{
					gl.glVertex2d((cx) + 10 * Math.cos(theta), (cy-20) - gy + 10 * Math.sin(theta));
		        	theta += BUBBLE_ANGLE;
				}
				gl.glVertex2d(cx -10, cy - 20 - gy);		// Last vertex after half circle
				
				gl.glEnd();
				
				// Third droplet (left side of bubble)
				// Similar format as the first drop, with a timer on the y coordinate and a constant
				// rate of change on the x coordinate to achieve the same effect.
				gl.glBegin(GL2.GL_POLYGON);
				
				gl.glVertex2d(cx - timer*2 - timer, cy - gy);				// Peak vertex point
				gl.glVertex2d(cx - 20 - timer*2, cy + 10 - timer - gy);		// Vertex before half circle
				// Iterate through the half circle with 19 sides
				for (int j=0; j<20; j++)
				{
					gl.glVertex2d((cx-20 - timer*2) + 10 * Math.cos(theta), cy - timer - gy + 10 * Math.sin(theta));
		        	theta += BUBBLE_ANGLE;
				}
				gl.glVertex2d(cx - 20 - timer*2, cy - timer - 10 - gy);		// Last vertex after half circle
				
				gl.glEnd();
				
				// Fourth Droplet (upside of the bubble)
				// This drop also has no x change like the second, and incorporates double
				// the effect of the second quadratic for gravity to simulate the orientation
				// shift of the drop and its decreasing rise from the pop.
				gl.glBegin(GL2.GL_POLYGON);
				
				gl.glVertex2d(cx, cy - gyu);					// Peak vertex point
				gl.glVertex2d(cx + 10, cy + 21 - 2*gyu);		// Vertex before half circle
				// Iterate through the half circle with 19 sides
				for (int j=0; j<20; j++)
				{
					gl.glVertex2d((cx) + 10 * Math.cos(theta), (cy + 21) - 2*gyu + 10 * Math.sin(theta));
		        	theta += BUBBLE_ANGLE;
				}
				gl.glVertex2d(cx -10, cy + 21 - 2*gyu);			// Last vertex after half circle
				
				gl.glEnd();
				
				// Star shape
				// A star-like shape is used to represent the general mass of burst that
				// occurs when a bubble pops, very much in a cartoon-ish method. This star
				// has 6 points and scales with the radius of the bubble. The star appears
				// for only 5 frames to simulate a quick pop followed by the droplets. The
				// method for generating a star is adopted from a similar one in the
				// homeworks.
				
				// If the timer is 5 frames or less
				if (timer < 6)
				{
					// Setup for parameters
					double beta = 0.5*Math.PI;
					int sides = 6;
					double delta = Math.PI/sides;
					
					// Begin drawing star
					gl.glBegin(GL.GL_TRIANGLE_FAN);
					
					gl.glVertex2d(cx, cy);					// Center of bubble to serve as the start
					// Iterate through each point
					for (int k = 0; k < sides; k++)
					{
						gl.glVertex2d(cx + (40.0*(r/20.0))*Math.cos(beta), cy + (40.0*(r/20.0))*Math.sin(beta));
						beta += delta;
						
						gl.glVertex2d(cx + (16.0*(r/20.0))*Math.cos(beta), cy + (16.0*(r/20.0))*Math.sin(beta));
						beta += delta;
					}
					gl.glVertex2d(cx + (40.0*(r/20.0))*Math.cos(beta), cy + (40.0*(r/20.0))*Math.sin(beta));
					
					gl.glEnd();
				}
				
				// Increment to timer to track the time animation has spent active
				timer++;
				model.getPoppedList().get(i).setTimer(timer);
				
				// If the animation has been active for 20 frames, remove
				if (timer > 21)
					model.removePopped(i);
			}
		}
	}
}

//******************************************************************************
