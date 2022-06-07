package com.sasi.gwmaker.graphwalker;

/**
 * Factory is a factory pattern class to generate GraphWalker objects.
 *
 * @TODO Extended in the future
 */
public class GraphWalkerFactory {

    /**
     * @return A new graphwalker object.
     */
    public static GraphWalker newGraphWalker(){
        return new GraphWalker();
    }

}
