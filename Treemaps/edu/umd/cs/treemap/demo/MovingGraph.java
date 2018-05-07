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
 * Draw an animated graph of a time series.
 */
class MovingGraph extends BufferPanel
{
    private double yMax=100;
    private int resolution=10;
    private int step=0;
    private double max, min;

    void clear()
    {
        clearBuffer();
        step=0;
    }
    
    void setYMax(double yMax)
    {
        this.yMax=yMax;
    }
    
    protected void drawOffscreen(Graphics g)
    {
        int h=size().height;
        int w=size().width;
        g.copyArea(0,0,w,h,-1,0);
        g.setColor(Color.black);
        
        int y1=h-1-(int)(h*min/yMax);
        int y2=h-1-(int)(h*max/yMax);
        g.setColor(Color.gray);
        g.drawLine(w-1,h-1,w-1,y2);
        g.setColor(getBackground());
        g.drawLine(w-1,y2-1,w-1,0);
    }
    
    synchronized void nextValue(double v)
    {
        // This "step" and "resolution" code
        // lets you do a crude kind of sub-sampling.
        // Right now the histogram just draws the max of each "step" values
        // it receives, which is useful for seeing thin spikes.
        if (++step<resolution)
        {
            if (step==1)
            {
                max=v; min=v; return;
            }
            max=Math.max(v,max);
            min=Math.min(v,min);
            return;
        }
        step=0;
        redraw();
    }
}
