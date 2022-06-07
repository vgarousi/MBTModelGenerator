package com.sasi.gwmaker.graphwalker.generator;

import org.graphwalker.core.condition.EdgeCoverage;
import org.graphwalker.core.generator.RandomPath;

/**
 * Extended Random Path Generator(RPG).
 *
 * This class extends RandomPath Generator and creates a 100% Edge Coverage
 * RPG by default without any extra variables passed into the constructor.
 *
 * Basically a shell class that doesn't do much work.
 */
public class XRPathGenerator extends RandomPath {

    /**
     * To be used in the MBT .json file.
     */
    public String pathJsonName = "random(edge_coverage(100))";

    /**
     * Constructor:
     * Create a new RandomPath Generator with a 100% Edge Coverage.
     */
    public XRPathGenerator() {
        super(new EdgeCoverage(100));
    }

}
