/*
 * Copyright 2015-2016 ArtisTech, Inc.
 */
package com.artistech.algolink.listeners;

import com.artistech.algolink.core.DataBuilder;
import com.artistech.algolink.core.Population;
import java.util.Calendar;

/**
 * A databuilder listener.
 *
 * @author matta
 */
public interface IDataBuilderListener {

    /**
     * Fires when a tick is complete.
     *
     * @param db
     * @param pop
     * @param time
     */
    void tick(DataBuilder db, Population pop, Calendar time);

    /**
     * Fires when the simulation is complete.
     *
     * Useful for cleaning up resources or writing extra log files.
     *
     * @param db
     * @param pop
     * @param time
     */
    void simulationComplete(DataBuilder db, Population pop, Calendar time);

    /**
     * Fires when the simulation is starting.
     *
     * Useful for initializing extra log files.
     *
     * @param db
     * @param pop
     * @param time
     */
    void simulationStart(DataBuilder db, Population pop, Calendar time);
}
