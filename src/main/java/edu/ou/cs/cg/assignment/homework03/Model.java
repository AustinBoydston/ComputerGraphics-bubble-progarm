//******************************************************************************
// Copyright (C) 2019 University of Oklahoma Board of Trustees.
//******************************************************************************
// Last modified: Wed Feb 27 17:32:08 2019 by Chris Weaver
//******************************************************************************
// Major Modification History:
//
// 20190227 [weaver]:	Original file.
//
//******************************************************************************
//
// The model manages all of the user-adjustable variables utilized in the scene.
// (You can store non-user-adjustable scene data here too, if you want.)
//
// For each variable that you want to make interactive:
//
//   1. Add a member of the right type
//   2. Initialize it to a reasonable default value in the constructor.
//   3. Add a method to access a copy of the variable's current value.
//   4. Add a method to modify the variable.
//
// Concurrency management is important because the JOGL and the Java AWT run on
// different threads. The modify methods use the GLAutoDrawable.invoke() method
// so that all changes to variables take place on the JOGL thread. Because this
// happens at the END of GLEventListener.display(), all changes will be visible
// to the View.update() and render() methods in the next animation cycle.
//
//******************************************************************************

package edu.ou.cs.cg.assignment.homework03;

//import java.lang.*;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLRunnable;

import edu.ou.cs.cg.utilities.Utilities;

//******************************************************************************

/**
 * The <CODE>Model</CODE> class.
 *
 * @author Chris Weaver
 * @version %I%, %G%
 */
public final class Model
{
    // **********************************************************************
    // Private Members
    // **********************************************************************

    // State (internal) variables
    private final View view;

    // Model variables
    private Point2D.Double origin; // Current origin coords
    private Point2D.Double cursor; // Current cursor coords
    private boolean colorful; // Show rainbow version?
    private int popCount;

    private ArrayList<Bubble> bubbles = new ArrayList<Bubble>();
    private ArrayList<Pop> popped = new ArrayList<Pop>();

    // **********************************************************************
    // Constructors and Finalizer
    // **********************************************************************

    public Model(View view)
    {
        this.view = view;

        // Initialize user-adjustable variables (with reasonable default values)
        origin = new Point2D.Double(0.0, 0.0);
        cursor = null;
        colorful = false;
        popCount = 0;
    }

    // **********************************************************************
    // Public Methods (Access Variables)
    // **********************************************************************
    
    //return the list of bubbles
    public ArrayList<Bubble> getBubbleList()
    {
        return bubbles;
    }
    
    public ArrayList<Pop> getPoppedList()
    {
    	return popped;
    }

    public Point2D.Double getOrigin()
    {
        return new Point2D.Double(origin.x, origin.y);
    }

    public Point2D.Double getCursor()
    {
        if (cursor == null)
            return null;
        else
            return new Point2D.Double(cursor.x, cursor.y);
    }

    public boolean getColorful()
    {
        return colorful;
    }
    
    public int getCount()
    {
    	return popCount;
    }

    // **********************************************************************
    // Public Methods (Modify Variables)
    // **********************************************************************

    public void createPopped(int x, int y, int r)
    {
    	view.getCanvas().invoke(false, new BasicUpdater() {
    		public void update(GL2 gl)
    		{
    			popped.add(new Pop(x, y, r));
    		}
    	});;
    }
    
    public void createBubble(int x, int y, int r, int[] c, int dx, int dy)
    {
    	view.getCanvas().invoke(false, new BasicUpdater() {
    		public void update(GL2 gl)
    		{
    			 bubbles.add(new Bubble(x, y, r, c, dx, dy));
    		}
    	});;
    }
    
    public void updatePosition(int index)
    {
    	view.getCanvas().invoke(false, new BasicUpdater() {
    		public void update(GL2 gl)
    		{
    			int x = bubbles.get(index).getX();
    	        int y = bubbles.get(index).getY();
    	        int dx = bubbles.get(index).getDirectionX();
    	        int dy = bubbles.get(index).getDirectionY();
    	        
    	        //update position based on direction
    	        x = x + dx;
    	        y = y + dy;
    	        bubbles.get(index).setPos(x, y);
    		}
    	});;
    }
    
    public void pop()
    {
    	view.getCanvas().invoke(false, new BasicUpdater() {
    		public void update(GL2 gl)
    		{
    			if (!bubbles.isEmpty())
    	        {
    	            for (int i = 0; i < bubbles.size(); i++)
    	            {
    	                if (cursor.x < bubbles.get(i).getX() + bubbles.get(i).getRadius()
    	                        && cursor.x > bubbles.get(i).getX() - bubbles.get(i).getRadius())
    	                {
    	                    if (cursor.y < bubbles.get(i).getY() + bubbles.get(i).getRadius()
    	                            && cursor.y > bubbles.get(i).getY() - bubbles.get(i).getRadius())
    	                    {
    	                        bubbles.remove(i);
    	                        popCount++;
    	                    }
    	                }
    	            }
    	        }
    		}
    	});;
    }
    
    public void setOriginInSceneCoordinates(Point2D.Double q)
    {
        view.getCanvas().invoke(false, new BasicUpdater()
        {
            public void update(GL2 gl)
            {
                origin = new Point2D.Double(q.x, q.y);
            }
        });
        ;
    }

    public void setOriginInViewCoordinates(Point q)
    {
        view.getCanvas().invoke(false, new ViewPointUpdater(q)
        {
            public void update(double[] p)
            {
                origin = new Point2D.Double(p[0], p[1]);
            }
        });
        ;
    }

    public void setCursorInViewCoordinates(Point q)
    {
        view.getCanvas().invoke(false, new ViewPointUpdater(q)
        {
            public void update(double[] p)
            {
                cursor = new Point2D.Double(p[0], p[1]);
            }
        });
        ;
    }

    public void turnCursorOff()
    {
        view.getCanvas().invoke(false, new BasicUpdater()
        {
            public void update(GL2 gl)
            {
                cursor = null;
            }
        });
        ;
    }

    public void toggleColorful()
    {
        view.getCanvas().invoke(false, new BasicUpdater()
        {
            public void update(GL2 gl)
            {
                colorful = !colorful;
            }
        });
        ;
    }

    // **********************************************************************
    // Inner Classes
    // **********************************************************************

    // Convenience class to simplify the implementation of most updaters.
    private abstract class BasicUpdater implements GLRunnable
    {
        public final boolean run(GLAutoDrawable drawable)
        {
            GL2 gl = drawable.getGL().getGL2();

            update(gl);

            return true; // Let animator take care of updating the display
        }

        public abstract void update(GL2 gl);
    }

    // Convenience class to simplify updates in cases in which the input is a
    // single point in view coordinates (integers/pixels).
    private abstract class ViewPointUpdater extends BasicUpdater
    {
        private final Point q;

        public ViewPointUpdater(Point q)
        {
            this.q = q;
        }

        public final void update(GL2 gl)
        {
            int h = view.getHeight();
            double[] p = Utilities.mapViewToScene(gl, q.x, h - q.y, 0.0);

            update(p);
        }

        public abstract void update(double[] p);
    }
}

//******************************************************************************
