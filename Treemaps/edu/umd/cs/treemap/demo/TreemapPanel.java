/**
 * Copyright (C) 2001 by University of Maryland, College Park, MD 20742, USA 
 * and Martin Wattenberg, w@bewitched.com
 * All rights reserved.
 * Authors: Benjamin B. Bederson and Martin Wattenberg
 * http://www.cs.umd.edu/hcil/treemaps
 */

package edu.umd.cs.treemap.demo;

import edu.umd.cs.treemap.*;
import edu.umd.cs.treemap.experiment.*;

import java.awt.*;

/**
 * A panel containing a map display and moving graphs
 * various treemap metrics.
 */
class TreemapPanel extends Panel
{
    private MapDisplay view;
    private TreeModel model;
    private MapLayout algorithm;
    private Rect bounds;
    private Label title, changeLabel, aspectLabel, readabilityLabel;
    private MovingGraph changeHistogram=new MovingGraph();
    private MovingGraph aspectHistogram=new MovingGraph();
    private MovingGraph readabilityHistogram=new MovingGraph();
    private boolean first=true;
    private Color histogramColor=Color.lightGray;
    private Font labelFont=new Font("Helvetica", Font.PLAIN, 11);
    private Font titleFont=new Font("Helvetica", Font.BOLD, 11);
    private int steps=0;
    private double change=0, aspect=0, readability=0;
    
    TreemapPanel(MapLayout algorithm)
    {
        this.algorithm=algorithm;
        bounds=new Rect(0,0,1,1);
        view=new MapDisplay(bounds);
        title=new Label(algorithm.getName());//, Label.CENTER);
        title.setFont(titleFont);
        changeLabel=new Label("Change");
        aspectLabel=new Label("Avg. Aspect");
        readabilityLabel=new Label("Readability");
        add(changeLabel);
        changeLabel.setFont(labelFont);
        add(aspectLabel);
        aspectLabel.setFont(labelFont);
        add(readabilityLabel);
        readabilityLabel.setFont(labelFont);
        
        setLayout(null);
        add(title);
        add(view);
        add(changeHistogram);
        changeHistogram.setBackground(histogramColor);
        changeHistogram.setYMax(40);
        add(aspectHistogram);
        aspectHistogram.setBackground(histogramColor);
        aspectHistogram.setYMax(4);
        add(readabilityHistogram);
        readabilityHistogram.setBackground(histogramColor);
        readabilityHistogram.setYMax(1);
    }
    
    void setFillByOrder(boolean fillByOrder)
    {
        view.setFillByOrder(fillByOrder);
    }
    
    public void setBackground(Color c)
    {
        super.setBackground(c);
        title.setBackground(c);
        changeLabel.setBackground(c);
        aspectLabel.setBackground(c);
    }
    
    synchronized public void redraw()
    {
        view.redraw();
        repaint();
    }
    
    public void reshape(int x, int y, int w, int h)
    {
        super.reshape(x,y,w,h);
        int s=Math.min(w,h);
        bounds=new Rect(0,0,s,s);
        view.setRealBounds(bounds);
        view.reshape(0,20,s,s);
        title.reshape(0,0,w,20);
 
        aspectHistogram.reshape(0,s+60,s,s);
        aspectLabel.reshape(0,s+40,s,20);
        changeHistogram.reshape(0,2*s+100,s,s);
        changeLabel.reshape(0,2*s+80,s,20);
        readabilityHistogram.reshape(0,3*s+140,s,s);
        readabilityLabel.reshape(0,3*s+120,s,20);

        updateLayout();
        
    }
    
    synchronized void reset()
    {
        first=true;
        steps=0;
        change=0;
        aspect=0;
        readability=0;
        changeHistogram.clear();
        aspectHistogram.clear();
        readabilityHistogram.clear();
    }
    
    synchronized void setModel(TreeModel model, int breadth)
    {
        this.model=model;
        if (view!=null)
            view.setItems(model.getTreeItems(), breadth);
    }
    
    synchronized void updateLayout()
    {
        if (model==null || view==null) return;
        
        LayoutDifference measure=new LayoutDifference();
        Mappable[] leaves=model.getTreeItems();
        measure.recordLayout(leaves);
        model.layout(algorithm, bounds);
        view.redraw();
        
        
        double d=measure.averageDistance(leaves);
        if (!first)
            changeHistogram.nextValue(d);
        first=false;
        double a=LayoutCalculations.averageAspectRatio(leaves);
        aspectHistogram.nextValue(a);
        double r=LayoutCalculations.getReadability(model);
        readabilityHistogram.nextValue(r);
        
        steps++;
        change+=d;
        changeLabel.setText("Change = "+format(change/steps));
        aspect+=a;
        aspectLabel.setText("Avg. Aspect = "+format(aspect/steps));
        readability+=r;
        readabilityLabel.setText("Avg. Readability = "+format(readability/steps));
    }
    

    final String format(double q)
    {
        double r=Math.round(100*q)/100.0;
        String m=String.valueOf(r);
        // deal with floating point weirdness.
        int n=m.indexOf('.');
        if (n>=0 && m.length()-n>3) m=m.substring(0,n+3);
        if (m.endsWith(".0")) m=m.substring(0,m.length()-2);

        return m;
    }
    
}
