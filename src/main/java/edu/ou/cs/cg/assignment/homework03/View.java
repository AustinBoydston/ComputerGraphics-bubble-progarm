//******************************************************************************
// Copyright (C) 2016-2019 University of Oklahoma Board of Trustees.
//******************************************************************************
// Last modified: Wed Feb 27 17:34:13 2019 by Chris Weaver
//******************************************************************************
// Major Modification History:
//
// 20160209 [weaver]:	Original file.
// 20190203 [weaver]:	Updated to JOGL 2.3.2 and cleaned up.
// 20190227 [weaver]:	Updated to use model and asynchronous event handling.
//
//******************************************************************************
// Notes:
//
//******************************************************************************

package edu.ou.cs.cg.assignment.homework03;

//import java.lang.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Point2D;
import java.text.DecimalFormat;
import java.util.Random;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.awt.TextRenderer;
import com.jogamp.opengl.util.gl2.GLUT;

import edu.ou.cs.cg.utilities.Utilities;

//******************************************************************************

/**
 * The <CODE>View</CODE> class.<P>
 *
 * @author  Chris Weaver
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

	//**********************************************************************
	// Public Class Members
	//**********************************************************************

	public static final GLUT			MYGLUT = new GLUT();
	public static final Random			RANDOM = new Random();

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

		//gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);	// Black background
	}

	private void	updatePipeline(GLAutoDrawable drawable)
	{
		GL2			gl = drawable.getGL().getGL2();
		GLU			glu = GLU.createGLU();
		Point2D.Double	origin = model.getOrigin();

		float			xmin = (float)(origin.x - 1.0);
		float			xmax = (float)(origin.x + 1.0);
		float			ymin = (float)(origin.y - 1.0);
		float			ymax = (float)(origin.y + 1.0);

		gl.glMatrixMode(GL2.GL_PROJECTION);		// Prepare for matrix xform
		gl.glLoadIdentity();						// Set to identity matrix
		glu.gluOrtho2D(xmin, xmax, ymin, ymax);	// 2D translate and scale
	}
	
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

	private void	drawMode(GLAutoDrawable drawable)
	{
		GL2		gl = drawable.getGL().getGL2();
		double[]	p = Utilities.mapViewToScene(gl, 0.5 * w, 0.5 * h, 0.0);
		double[]	q = Utilities.mapSceneToView(gl, 0.0, 0.0, 0.0);
		String		svc = ("View center in scene: [" + FORMAT.format(p[0]) +
						   " , " + FORMAT.format(p[1]) + "]");
		String		sso = ("Scene origin in view: [" + FORMAT.format(q[0]) +
						   " , " + FORMAT.format(q[1]) + "]");

		renderer.beginRendering(w, h);

		// Draw all text in yellow
		renderer.setColor(1.0f, 1.0f, 0.0f, 1.0f);

		Point2D.Double	cursor = model.getCursor();

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

		String count = "Number of bubles popped: " + model.getCount();
		
		renderer.draw(svc, 2, 16);
		renderer.draw(sso, 2, 30);
		renderer.draw(count, 2, 700);

		renderer.endRendering();
	}

	private void	drawMain(GL2 gl)
	{
	    setScreenProjection(gl);
		setColor(gl, 0, 0, 0);
	    drawBubble(gl);
	}

	private void   setColor(GL2 gl, int r, int g, int b, int a)
    {
        gl.glColor4f(r / 255.0f, g / 255.0f, b / 255.0f, a / 255.0f);
    }

    private void    setColor(GL2 gl, int r, int g, int b)
    {
        setColor(gl, r, g, b, 255);
    }
    
	private static final int       SIDES_BUBBLE = 18;
    private static final double BUBBLE_ANGLE = 2.0 * Math.PI / SIDES_BUBBLE;
    
	private void drawBubble(GL2 gl)
	{
	    
	    double theta = 0.20 * BUBBLE_ANGLE;
        int cx = 100;
        int cy = 100;
        int r = 20;
        int xSpawn = 400;
        int ySpawn = 300;
        Random randDir = new Random();
        
        //might delete later
        int[] temp = {0, 0, 0};
        
        //create new bubble
        if( counter % 60 == 0)
        {
            //                                      Select a random number between 0 and 3 inclusive to ditermine the direction
            model.createBubble(randDir.nextInt(200) + xSpawn , randDir.nextInt(200) + ySpawn, 20, temp,  randDir.nextInt(4));
        }
        
        
        //draw bubbles
        if(!model.getBubbleList().isEmpty())
        {
	        //iterate through the list of bubbles
	        for(int j = 0; j < model.getBubbleList().size(); j++)
	        {
	            cx = model.getBubbleList().get(j).getX();
	            cy = model.getBubbleList().get(j).getY();
	            r = model.getBubbleList().get(j).getRadius();
	            
			    // Fill the bubble with cyan
			    gl.glBegin(GL.GL_TRIANGLE_FAN);
			
			    setColor(gl, 0, 255, 255);            // Cyan
			    gl.glVertex2d(cx, cy);
			
			    for (int i=0; i<SIDES_BUBBLE+1; i++)      // 18 sides
			    {
			        gl.glVertex2d(cx + r * Math.cos(theta), cy + r * Math.sin(theta));
			        theta += BUBBLE_ANGLE;
			    }
			
			    gl.glEnd();
			    
			    
			    //update position of bubble
			    model.updatePosition(j, model.getBubbleList().get(j).getDirection());
			    
			    
			    //check if bubble is still in the frame
			    if(cx < 0 || cx > 1280 || cy < 0 || cy > 720)
			    {
			        //delete the bubble when it exits the frame
			        model.getBubbleList().remove(j);
			    }
	        }
        }
	    setColor(gl, 0, 0, 0);
	    
	}
}

//******************************************************************************
