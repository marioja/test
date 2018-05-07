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
import java.applet.*;
import java.util.*;

/**
 *
 * Applet allowing a comparison of different
 * treemap layout algorithms.
 * <P>
 * You can configure to applet to compare different
 * layouts via an HTML param, so reprogramming
 * isn't necessary. Here's an example:
 * <br>
 * <CODE>
 * <!-- If you change the number of layouts you compare, -->
 * <!-- you may need to change the width of the applet.  -->
 * <applet code="edu.umd.cs.treemap.demo.LayoutApplet.class" height=730 width=580>
 * 
 *     <!-- The "layouts" param takes a comma-separated list of the full java      -->
 *     <!-- class names of the various MapLayout implementations you wish to test. -->
 *     <!-- Whitespace in the list is ignored. -->
 *     
 *     <param name="layouts"
 *         value= "edu.umd.cs.treemap.SliceLayout, 
 *                 edu.umd.cs.treemap.SquarifiedLayout, 
 *                 edu.umd.cs.treemap.StripTreemap, 
 *                 edu.umd.cs.treemap.PivotBySplitSize">
 * </applet>
 * </CODE>
 * <br>
 *
 */
public class LayoutApplet extends Applet implements Runnable
{
    // Panels for each treemap algorithm.
    private TreemapPanel[] view;
    
    // Tree structure options.
    private String[] treeOptions={"20 items", "50 items", "100 items",
        "10 x 10", "8 x 8 x 8"};
    private int[] optionDepths={1,1,1,2,3};
    private int[] optionBreadths={20,50,100,10,8};
  
    // Tree data.
    private double[] sizes;
    int breadth=6, depth=2;
    private static final int RANDOM=0, SINE=1;
    private int motion=RANDOM;
    private double t=0;
    
    // Updating.
    private Thread changing;
    private boolean pausing=true;
    
    // GUI controls.
    private int SIDE=120; // map dimensions will be SIDExSIDE.
    private Label changeLabel=new Label("Type of change:");
    private Choice changeChoice=new Choice();
    private Choice treeChoice=new Choice();
    private Button newButton=new Button("Start over");
    private Button pauseButton=new Button("Start updating");
    private Checkbox logBox, sineBox;
    private Checkbox orderBox=new Checkbox("Color by order", false);
    private Choice numChoice=new Choice();
    private Font uiFont=new Font("Helvetica", Font.PLAIN, 11);
    

    public void init()
    {
        makeViews(); // Load in layout algorithms, create TreemapPanels.
        
        setLayout(null);
        setBackground(Color.white);
        int w=size().width, h=size().height;
        
        for (int i=0; i<treeOptions.length; i++)
            treeChoice.addItem(treeOptions[i]);
        CheckboxGroup c=new CheckboxGroup();
        logBox=new Checkbox("Random walk", c, true);
        sineBox=new Checkbox("Sine waves", c, false);
        
        add(logBox,280,30,100,25);
        add(sineBox,420,30,100,25);
        add(pauseButton,0,0,90,20);
        add(newButton,140,0,90,20);
        add(orderBox,0,30,100,25);
        add(changeLabel,280,0,100,25);
        add(treeChoice,140,30,100,20);
        
        resetTrees();
    }
    
    private void add(Component c, int x, int y, int w, int h)
    {
        add(c);
        c.reshape(x,y,w,h);
        c.setFont(uiFont);
    }
    
    // Read relevant layout algorithms from an HTML param.
    private void makeViews()
    {
        try
        {
            StringTokenizer t=new StringTokenizer(getParameter("layouts"),",");
            int n=t.countTokens();
            view=new TreemapPanel[n];
            for (int i=0; i<n; i++)
            {
                String name=t.nextToken().trim();
                MapLayout layout=(MapLayout)(Class.forName(name).newInstance());
                view[i]=new TreemapPanel(layout);
            }
        }
        catch (Exception e)
        {
            System.out.println("Trouble reading 'layouts' param.");
            e.printStackTrace();
        }
    }
    
    
    synchronized public void start()
    {
        changing=new Thread(this);
        changing.start();
    }
    
    synchronized public void stop()
    {
        changing=null;
    }
    
    public boolean action(Event e, Object arg)
    {
        if (e.target==treeChoice)
        {
            resetTrees();
            reset();
        }
        
        if (e.target==orderBox)
        {
            for (int i=0; i<view.length; i++)
            {
                view[i].setFillByOrder(orderBox.getState());
                view[i].redraw();
            }
            
        }
        if (e.target==newButton)
            reset();
        if (e.target==logBox)
        {
            motion=RANDOM;
            reset();
        }
        if (e.target==sineBox)
        {
            motion=SINE;
            reset();
        }
        if (e.target==pauseButton)
        {
            pauseButton.setLabel(pausing ? "Pause" : "Continue");
            pausing=!pausing;
        }
        return true;
    }
    
    synchronized private void reset()
    {
        if (motion==SINE)
            t=0;
        else
            createRandomSizes();
            
        for (int i=0; i<view.length; i++)
            view[i].reset();
    }
    
    private void createRandomSizes()
    {
        Random r=new Random();
        for (int i=0; i<sizes.length; i++)
            sizes[i]=Math.random();
        for (int j=0; j<100; j++)
            updateSizes();
    }
    
    public void run()
    {
       
        while (Thread.currentThread()==changing)
        {
            synchronized (this)
            {
                updateSizes();
                for (int i=0; i<view.length; i++)
                    view[i].updateLayout();
            }
            do
            {
                try {Thread.sleep(20);}
                catch (Exception e) {}
            } while (pausing);
            t++;
        }
    }
    
    private void updateSizes()
    {
        if (motion==RANDOM)
        {
            // Add random noise.
            double max=sizes[0]*.5, min=sizes[0]*2;
            int i;
            for (i=0; i<sizes.length; i++)
            {
                sizes[i]*=(1+.03*(Math.random()-.5));
                max=Math.max(sizes[i], max);
                min=Math.min(sizes[i], min);
            }
            // Normalize.
            double scale=1.0/min;
            for (i=0; i<sizes.length; i++)
            {
                sizes[i]=sizes[i]*scale;
            }
            
        }
        else
        {
            for (int i=0; i<sizes.length; i++)
                sizes[i]=1.1+Math.sin(i+t*.01);
        }
    }
    
    private void resetTrees()
    {
        int i=treeChoice.getSelectedIndex();
        resetTrees(optionBreadths[i], optionDepths[i]);
    }
    
    private void resetTrees(int breadth, int depth)
    {
        
        TreeModel[] model=makeTreeModels(breadth,depth);
        for (int i=0; i<view.length; i++)
        {
            view[i].setModel(model[i], breadth);
            add(view[i]);
            view[i].setBackground(Color.white);//new Color(0xeeeeee));
            view[i].reshape(i*(SIDE+20), 80, SIDE, 4*SIDE+140);
            view[i].redraw();
        }
    }
    
    private TreeModel[] makeTreeModels(int breadth, int depth)
    {
        int m=view.length;
        int i;
        TreeModel[] model=new TreeModel[m];
        
        // Use the same array for all trees so that we only
        // have to do data updates in one place.
        sizes=BalancedTree.makeArray(breadth, depth);
        
        for (i=0; i<m; i++)
            model[i]=new BalancedTree(sizes, breadth, depth);
        createRandomSizes();
        return model;
    }
}
