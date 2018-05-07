/**
 * Copyright (C) 2001 by University of Maryland, College Park, MD 20742, USA 
 * and Martin Wattenberg, w@bewitched.com
 * All rights reserved.
 * Authors: Benjamin B. Bederson and Martin Wattenberg
 * http://www.cs.umd.edu/hcil/treemaps
 */

package edu.umd.cs.treemap.experiment;

import edu.umd.cs.treemap.*;

/**
 * An BalancedTree provides an easy way to create a TreeModel
 * corresponding to a balanced tree
 * whose data is backed by an array. This class is used in both the
 * experiments and the applet demo.
 */
public class BalancedTree extends TreeModel
{
    private double[] array;
    
    public BalancedTree(int breadth, int depth)
    {
        this(makeArray(breadth, depth), breadth, depth);
    }
    
    public static double[] makeArray(int breadth, int depth)
    {
        return new double[countLeaves(breadth, depth)];
    }
    
    public BalancedTree(double[] array, int breadth, int depth)
    {
        this.array=array;
        int subSize=countLeaves(breadth, depth-1);
        for (int i=0; i<breadth; i++)
            addChild(submodel(breadth, depth-1, i*subSize, i));
    }
    
    private TreeModel submodel(int breadth, int depth, int startIndex, int order)
    {
        if (depth==0)
            return new TreeModel(new ArrayBackedItem(array, startIndex, order));
        TreeModel t=new TreeModel();
        int subSize=countLeaves(breadth, depth-1);
        for (int i=0; i<breadth; i++)
            t.addChild(submodel(breadth, depth-1, startIndex+i*subSize, i));
        return t;
    }
    
    private static int countLeaves(int breadth, int depth)
    {
        return (int)Math.pow(breadth, depth);
    }
    
    public double[] getArray()
    {
        return array;
    }
}
