/**
 * Copyright (C) 2001 by University of Maryland, College Park, MD 20742, USA 
 * and Martin Wattenberg, w@bewitched.com
 * All rights reserved.
 * Authors: Benjamin B. Bederson and Martin Wattenberg
 * http://www.cs.umd.edu/hcil/treemaps
 */

package edu.umd.cs.treemap.demo;

import java.awt.*;

/**
 * This code was written by Martin Wattenberg and has long been in the public domain.
 * <P>
 * The BufferPanel is a panel that paints itself
 * using an offscreen buffer, but only redraws the offscreen buffer
 * when explicitly told to do so.
 * <P>
 * Caching the offscreen image is useful for situations
 * where rendering the offscreen buffer takes a long time
 * and re-rendering each time repaint() would be inefficient.
 * <P>
 * The key public method for this panel is <code>redraw()</code>,
 * which forces an update of the offscreen buffer and then draws
 * to the screen.
 * <P>
 * The key protected method, which subclasses should override,
 * is <code>drawOffscreen(Graphics g)</code> where all
 * rendering code should be placed.
 */
public abstract class BufferPanel extends Panel
{
	private Image offscreenImage; 
	private Graphics offscreen;   

	protected void clearBuffer()
	{
		offscreen.setColor(getBackground());
		offscreen.fillRect(0,0,size().width,size().height);
	}
	
	protected abstract void drawOffscreen(Graphics g);

    public Graphics getBufferGraphics() 
    {
	    return offscreen;
    }
    
    public void paint(Graphics g) 
    {
	    if (offscreenImage != null)
		    g.drawImage(offscreenImage, 0, 0, null);
    }

    public void redraw() 
    {
	    if (offscreen != null) 
	    {
	        offscreen.setFont(getFont());
		    drawOffscreen(offscreen);
		    repaint();
	    }
    }
    

    public void reshape(int x, int y, int w, int h) 
    {
	    super.reshape(x, y, w, h);
	    offscreenImage = createImage(w, h);
	    offscreen = offscreenImage.getGraphics();
	    drawOffscreen(offscreen);
    }

    public void setBackground(Color c) 
    {
	    super.setBackground(c);
	    redraw();
    }
    
    public void setFont(Font f) 
    {
	    super.setFont(f);
	    redraw();
    }

    public void update(Graphics g) 
    {
	    paint(g);
    }
}
