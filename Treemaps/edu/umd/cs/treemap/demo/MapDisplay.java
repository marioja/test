/**
 * Copyright (C) 2001 by University of Maryland, College Park, MD 20742, USA 
 * and Martin Wattenberg, w@bewitched.com
 * All rights reserved.
 * Authors: Benjamin B. Bederson and Martin Wattenberg
 * http://www.cs.umd.edu/hcil/treemaps
 */

package edu.umd.cs.treemap.demo;

import edu.umd.cs.treemap.*;
import java.awt.*;

/**
 *
 * Draw, using a an offscreen buffer, a collection of map items.
 *
 */
class MapDisplay extends BufferPanel
{
    private Rect bounds;
    private Mappable[] items;
    private Color outlineColor=Color.black;
    private Color[] fillColor;
    private int thickness=1;
    private int breadth;
    private boolean fillByOrder;
    
    
    MapDisplay(Rect bounds)
    {
        this.bounds=bounds;
        fillColor=new Color[256];
        for (int i=0; i<256; i++)
            fillColor[255-i]=new Color(i,i,i);
    }
    
    
    void setFillByOrder(boolean fillByOrder)
    {
        this.fillByOrder=fillByOrder;
    }
    

    // "breadth" is used to color by order.
    void setItems(Mappable[] items, int breadth)
    {
        this.items=items;
        this.breadth=breadth;
    }
    
    void setRealBounds(Rect bounds)
    {
        this.bounds=bounds;
    }

    protected void drawOffscreen(Graphics g)
    {
        clearBuffer();
        if (items==null) return;
        int w=size().width-1, h=size().height-1;
        for (int i=0; i<items.length; i++)
        {
            Mappable m=items[i];
            Rect r=m.getBounds();
            int x=(int)Math.round(w*r.x/bounds.w);
            int width=(int)Math.round(w*(r.x+r.w)/bounds.w)-x;
            int y=(int)Math.round(h*r.y/bounds.h);
            int height=(int)Math.round(h*(r.y+r.h)/bounds.h)-y;
            g.setColor(fillByOrder ? 
                    fillColor[(int)((128*m.getOrder())/breadth)] :
                    Color.white);
            g.fillRect(x,y,width,height);
            g.setColor(outlineColor);
            for (int j=0; j<thickness; j++)
                g.drawRect(x+j,y+j,width-2*j,height-2*j);
        }  
        Toolkit.getDefaultToolkit().sync();
    }
}
