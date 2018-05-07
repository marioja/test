/**
 * Copyright (C) 2001 by University of Maryland, College Park, MD 20742, USA 
 * and Martin Wattenberg, w@bewitched.com
 * All rights reserved.
 * Authors: Benjamin B. Bederson and Martin Wattenberg
 * http://www.cs.umd.edu/hcil/treemaps
 */

package edu.umd.cs.treemap.test;

import edu.umd.cs.treemap.*;
import java.awt.*;
import java.awt.event.*;
import java.text.MessageFormat;

/**
 * A simple windowed application for testing map layouts.
 */
public class TreemapTest extends Frame {
    MapModel map;
    MapLayout algorithm;
    public static double[][] lists=new double[][] {{60, 10, 10, 20},{60, 20, 10, 10},{20, 10, 60, 10}};

    public TreemapTest(int listNo) {
	int w = 400;
	int h = 400;

	map = new MyMap(lists[listNo]);
	Mappable[] items = map.getItems();
	    
	algorithm = new SquarifiedLayout();
	algorithm.layout(map, new Rect(0, 0, w, h));

	setBounds(100, 100, w, h);
	setVisible(true);

			// Watch for the user closing the window so we can exit gracefully
	addWindowListener (new WindowAdapter () {
		public void windowClosing (WindowEvent e) {
		    System.exit(0);
		}
	    });
    }

    public void paint(Graphics g) {
	Mappable[] items = map.getItems();
	Rect rect;
	Color[] colors=new Color[] {Color.black, Color.red, Color.green, Color.magenta};

	g.setColor(Color.black);

	for (int i=0; i<items.length; i++) {
		g.setColor(colors[i]);
	    rect = items[i].getBounds();
	    int a=(int)rect.x;
	    int b=(int)rect.y;
	    int c=(int)(rect.x+rect.w)-a;
	    int d=(int)(rect.y+rect.h)-b;
	    if (b==0) b=0;
	    g.drawRect(a,b,c,d);
	    System.out.println(MessageFormat.format("drawRect({0},{1},{2},{3})", a, b, c, d));
	}
    }

    static public void main(String[] args) {
	new TreemapTest(Integer.parseInt(args[0]));
    }
}
