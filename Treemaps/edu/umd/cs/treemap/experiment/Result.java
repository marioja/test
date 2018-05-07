/**
 * Copyright (C) 2001 by University of Maryland, College Park, MD 20742, USA 
 * and Martin Wattenberg, w@bewitched.com
 * All rights reserved.
 * Authors: Benjamin B. Bederson and Martin Wattenberg
 * http://www.cs.umd.edu/hcil/treemaps
 */

package edu.umd.cs.treemap.experiment;

/**
 *
 * Object containing results from an experiment.
 *
 */
class Result
{
    double aspect, change, readability;
    String crlf="\r\n";
    
    Result() {this(0,0,0);}
    
    Result(double aspect, double change, double readability)
    {
        this.aspect=aspect;
        this.change=change;
        this.readability=readability;
    }
    
    public String toString()
    {
        return "aspect="+aspect+crlf+"change="+change+crlf+"readability="+readability+crlf;
    }
}
