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
import java.awt.Component;
import java.awt.event.*;
import java.awt.geom.Point2D;
import edu.ou.cs.cg.utilities.Utilities;

//******************************************************************************

/**
 * The <CODE>KeyHandler</CODE> class.<P>
 *
 * @author  Team 7
 * @version %I%, %G%
 */
public final class KeyHandler extends KeyAdapter
{
	//**********************************************************************
	// Private Members
	//**********************************************************************

	// State (internal) variables
	private final View		view;
	private final Model	model;

	//**********************************************************************
	// Constructors and Finalizer
	//**********************************************************************

	public KeyHandler(View view, Model model)
	{
		this.view = view;
		this.model = model;

		Component	component = view.getCanvas();

		component.addKeyListener(this);
	}

	//**********************************************************************
	// Override Methods (KeyListener)
	//**********************************************************************

	public void		keyPressed(KeyEvent e)
	{
		Point2D.Double	p = model.getOrigin();
		double			a = (Utilities.isShiftDown(e) ? 0.01 : 0.1);

		switch (e.getKeyCode())
		{
			// Current numpad and arrow actions not applicable
			case KeyEvent.VK_NUMPAD5:
				p.x = 0.0;	p.y = 0.0;	break;

			case KeyEvent.VK_NUMPAD4:
			case KeyEvent.VK_LEFT:
				p.x -= a;		p.y += 0.0;	break;

			case KeyEvent.VK_NUMPAD6:
			case KeyEvent.VK_RIGHT:
				p.x += a;		p.y += 0.0;	break;

			case KeyEvent.VK_NUMPAD2:
			case KeyEvent.VK_DOWN:
				p.x += 0.0;	p.y -= a;		break;

			case KeyEvent.VK_NUMPAD8:
			case KeyEvent.VK_UP:
				p.x += 0.0;	p.y += a;		break;

			case KeyEvent.VK_NUMPAD1:
				p.x -= a;		p.y -= a;		break;

			case KeyEvent.VK_NUMPAD7:
				p.x -= a;		p.y += a;		break;

			case KeyEvent.VK_NUMPAD3:
				p.x += a;		p.y -= a;		break;

			case KeyEvent.VK_NUMPAD9:
				p.x += a;		p.y += a;		break;
				
			// Toggles colorful bubbles
			case KeyEvent.VK_C:
				model.toggleColorful(); break;
			
			// Funcitonality to allow for popping with space press when cursor
			// is above the bubble
			case KeyEvent.VK_SPACE: model.pop(); break;
		}

		model.setOriginInSceneCoordinates(p);
	}
}

//******************************************************************************
