//******************************************************************************
// Copyright (C) 2016-2019 University of Oklahoma Board of Trustees.
//******************************************************************************
// Last modified: Fri Apr 30 2021 by Team 7
//******************************************************************************
// Major Modification History:
//
// 20160225 [weaver]:	Original file.
// 20190227 [weaver]:	Updated to use model and asynchronous event handling.
// 20210430 [team7]:	Final edits to project.
//
//******************************************************************************
// Notes: This documents contains structure of code used and given for use
//        during the semester for our homeworks and were adopted for usage in
//        our project. All the non-generic code unique to our project will
//        have comments to explain its purpose.
//
//******************************************************************************

package edu.ou.cs.cg.assignment.homework03;

//import java.lang.*;
import java.awt.*;
import java.awt.event.*;
import edu.ou.cs.cg.utilities.Utilities;

//******************************************************************************

/**
 * The <CODE>MouseHandler</CODE> class.
 * <P>
 *
 * @author Team 7
 * @version %I%, %G%
 */
public final class MouseHandler extends MouseAdapter
{
    // **********************************************************************
    // Private Members
    // **********************************************************************

    // State (internal) variables
    private final View view;
    private final Model model;

    // **********************************************************************
    // Constructors and Finalizer
    // **********************************************************************

    public MouseHandler(View view, Model model)
    {
        this.view = view;
        this.model = model;

        Component component = view.getCanvas();

        component.addMouseListener(this);
        component.addMouseMotionListener(this);
        component.addMouseWheelListener(this);
    }

    // **********************************************************************
    // Override Methods (MouseListener)
    // **********************************************************************

    public void mouseClicked(MouseEvent e)
    {
    	// Trigger pop attempt when clicking the mouse
    	model.pop();
    }

    public void mouseEntered(MouseEvent e)
    {
        model.setCursorInViewCoordinates(e.getPoint());
    }

    public void mouseExited(MouseEvent e)
    {
        model.turnCursorOff();
    }

    public void mousePressed(MouseEvent e)
    {
    	// Trigger pop attempt when clicking the mouse
    	model.pop();
    }

    public void mouseReleased(MouseEvent e)
    {
    }

    // **********************************************************************
    // Override Methods (MouseMotionListener)
    // **********************************************************************

    public void mouseDragged(MouseEvent e)
    {
        model.setCursorInViewCoordinates(e.getPoint());
    }

    public void mouseMoved(MouseEvent e)
    {
        model.setCursorInViewCoordinates(e.getPoint());
    }

    // **********************************************************************
    // Override Methods (MouseWheelListener)
    // **********************************************************************

    public void mouseWheelMoved(MouseWheelEvent e)
    {
    }
}

//******************************************************************************
