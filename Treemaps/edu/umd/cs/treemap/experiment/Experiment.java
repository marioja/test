/**
 * Copyright (C) 2001 by University of Maryland, College Park, MD 20742, USA 
 * and Martin Wattenberg, w@bewitched.com
 * All rights reserved.
 * Authors: Benjamin B. Bederson and Martin Wattenberg
 * http://www.cs.umd.edu/hcil/treemaps
 */

package edu.umd.cs.treemap.experiment;

import edu.umd.cs.treemap.*;

import java.util.*;

/**
 * Code for running a Monte Carlo experiment
 * to measure efficacy of a treemap layout algorithm.
 * Used in the LayoutExperiment application.
 */
class Experiment
{
    final static int GAUSSIAN_INITIAL=0, ZIPF_INITIAL=1;
    private int steps;
    private int trials;
    private int breadth;
    private int distribution;
    private int depth;
    private long seed;
    private double change, aspect;
    private double[] sizes;
    private Rect bounds=new Rect(0,0,100,100);
    
    // For each experiment, run the given number of trials
    // and report the average of the results.
    Result run(MapLayout t, int steps, int trials, 
                            int breadth, int depth, long seed, int distribution)
    {
        this.steps=steps;
        this.trials=trials;
        this.breadth=breadth;
        this.depth=depth;
        this.seed=seed;
        if (distribution>=0 && distribution<2)
            this.distribution=distribution;
        else
            throw new IllegalArgumentException("Unknown distribution: "+distribution);
        
        Random random=new Random(seed);
        Result result=new Result();
        TreeModel tree=makeModel();
        
        for (int i=0; i<trials; i++)
        {
            Result r=runTrial(t, tree, random);
            result.aspect+=r.aspect;
            result.change+=r.change;
            result.readability+=r.readability;
        }
        
        result.aspect/=trials;
        result.change/=trials;
        result.readability/=trials;
        
        return result;
    }
    
    // For each trial, run the given number of steps
    // and report the average of the results.
    Result runTrial(MapLayout algorithm, TreeModel model, Random random)
    {
        // initialize sizes.
        for (int i=0; i<sizes.length; i++)
        {
            if (distribution==GAUSSIAN_INITIAL)
                sizes[i]=Math.exp(random.nextGaussian());
            else
                sizes[i]=1/(1+sizes.length*Math.random());
        }
        
        // Do initial layout for first baseline.
        updateLayout(model, null, algorithm); // layout once so can measure diff right away.
        
        // Now do requested number of steps of updating.
        Result result=new Result();
        for (int i=0; i<steps; i++)
        {
            updateData(random);
            updateLayout(model, result, algorithm);
        }

        result.aspect/=steps;
        result.change/=steps;
        result.readability/=steps;
        return result;
    }
    
    // Make a balanced tree model for experiments.
    TreeModel makeModel()
    {
        BalancedTree tree=new BalancedTree(breadth, depth);
        sizes=tree.getArray();
        return tree;
    }
    
    // Update data with given random number generator.
    void updateData(Random random)
    {
        // Add random noise.
        int i;
        for (i=0; i<sizes.length; i++)
        {
            sizes[i]*=Math.exp(.05*random.nextGaussian());
        }
    }
    
    // Update the given model with the given algorithm,
    // adding the results in the given Result object.
    void updateLayout(TreeModel model, Result result, MapLayout algorithm)
    {
        LayoutDifference measure=new LayoutDifference();
        Mappable[] leaves=model.getTreeItems();
        measure.recordLayout(leaves);
        model.layout(algorithm, bounds);
        if (result==null) return;
        result.change+=measure.averageDistance(leaves);
        result.aspect+=LayoutCalculations.averageAspectRatio(leaves);
        result.readability+=LayoutCalculations.getReadability(model);
    }
    
    
}
