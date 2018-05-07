/**
 * Copyright (C) 2001 by University of Maryland, College Park, MD 20742, USA 
 * and Martin Wattenberg, w@bewitched.com
 * All rights reserved.
 * Authors: Benjamin B. Bederson and Martin Wattenberg
 * http://www.cs.umd.edu/hcil/treemaps
 */

package edu.umd.cs.treemap.test;

import edu.umd.cs.treemap.*;

/**
 * This implementation of MapModel lets you
 * put in your own hard-coded sizes.
 */
public class TestMap implements MapModel 
{
    static double[] testSize=
    {
        1,1,1,1,1,1,1,1,1,1,1,.001,1,1,1,1,1,1,1
        
    };
    
    Mappable[] items;
    
    public TestMap()
    {
        this(testSize);
    }
    
    public TestMap(double[] size)
    {
        int n=size.length;
        items=new MapItem[n];
        for (int i=0; i<n; i++)
        {
            items[i]=new MapItem(size[i],i);
        }
    }


    public Mappable[] getItems() 
    {
	    return items;
    }
}
