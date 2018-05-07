/**
 * Copyright (C) 2001 by University of Maryland, College Park, MD 20742, USA 
 * and Martin Wattenberg, w@bewitched.com
 * All rights reserved.
 * Authors: Benjamin B. Bederson and Martin Wattenberg
 * http://www.cs.umd.edu/hcil/treemaps
 */

package edu.umd.cs.treemap.experiment;

import edu.umd.cs.treemap.*;

import java.awt.*;
import java.applet.*;
import java.util.*;

/**
 * This class is a console application that
 * performs experiments and generates statistics for various
 * algorithms. With the parameters given, and the
 * simple but inefficient implementation of some of
 * the layout algorithms, it takes several
 * hours on a fast-ish machine.
 * <P>
 * To do: make this take options from the command line.
 */
public class LayoutExperiment
{
    // STEPS= number of data change steps in each trial.
    // TRIALS= number of trials.
    final static int STEPS=100, TRIALS=100;
    
    // Layout algorithms to test.
    static MapLayout[] algorithms=
    {
        new SliceLayout(),
        new SquarifiedLayout(),
        new StripTreemap(),
        new BinaryTreeLayout(),
        new PivotByMiddle(),
        new PivotBySize(),
        new PivotBySplitSize()
    };
    
    public static void main(String[] args)
    {
        performExperiments();
    }
    
    static void performExperiments()
    {
        // Print header.
        System.out.println("===================");
        System.out.println("LayoutExperiment:");
        System.out.println("Started: "+new Date());
        
        // argument order: steps, trials, breadth, depth, seed, distribution.
        perform(STEPS,TRIALS,20,1,1,Experiment.GAUSSIAN_INITIAL);
        perform(STEPS,TRIALS,20,1,1,Experiment.ZIPF_INITIAL);
        perform(STEPS,TRIALS,100,1,1,Experiment.GAUSSIAN_INITIAL);
        perform(STEPS,TRIALS,100,1,1,Experiment.ZIPF_INITIAL);
        perform(STEPS,TRIALS,8,3,1,Experiment.GAUSSIAN_INITIAL);
        perform(STEPS,TRIALS,8,3,1,Experiment.ZIPF_INITIAL);
        
        // Print footer.
        System.out.println("==================");
        System.out.println("Finished: "+new Date());
    }
    
    static void perform(int steps, int trials, int breadth, int depth, 
                        long seed, int distribution)
    {
        // Print info to file and console.
        // The System.err printing is so that you
        // can watch the program's progress.
        
        // Print info about the parameters of the current experiment.
        System.out.println("===================");
        System.out.println((distribution==0 ? "Gaussian" : "Zipf") +" distribution");
        System.err.println("---"+(distribution==0 ? "Gaussian" : "Zipf") +" distribution");
        System.out.println("Tree breadth: "+breadth);
        System.err.println("---Tree breadth: "+breadth);
        System.out.println("Tree depth:   "+depth);
        System.err.println("---Tree depth:   "+depth);
        System.out.println("trials:       "+trials);
        System.out.println("steps:        "+steps);
        System.out.println("seed:         "+seed);
        System.out.println("--------------\n\n");
        
        // Run the experiment for each algorithm and
        // print the results.
        for (int i=0; i<algorithms.length; i++)
        {
            MapLayout t=algorithms[i];
            System.err.println(t.getName());
            System.out.println(t.getName());
            Experiment experiment=new Experiment();
            Result result=
                experiment.run(t, steps, trials, breadth, depth, seed, distribution);
            System.out.println(result);
            System.out.println("\n........\n");
//            try {Thread.sleep(1000*60);} catch (Exception e) {} // my laptop got really hot, so I added this line...
        }
    }
   
}
