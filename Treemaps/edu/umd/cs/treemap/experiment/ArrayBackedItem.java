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
 * A MapItem that takes its size from an array.
 * This object is handy when you want to change the sizes
 * of many MapItems at once with generic routines that
 * just operate on a list of doubles... you just operate
 * on the underlying array and the ArrayBackedItem 
 * will use your changes.
 */
public class ArrayBackedItem extends MapItem
{
    int index;
    double[] array;
    
    public ArrayBackedItem(double[] array, int index, int order)
    {
        this.index=index;
        this.array=array;
        setOrder(order);
    }

    public double getSize()
    {
        return array[index];
    }

    public void setSize(double s)
    {
        array[index]=s;
    }
}
