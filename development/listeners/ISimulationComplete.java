/*
 * Copyright 2015-2016 ArtisTech, Inc.
 */
package com.artistech.algolink.listeners;

import com.artistech.algolink.core.DataBuilder;
import com.artistech.algolink.core.Population;
import java.util.Calendar;

/**
 *
 * @author matta
 */
public interface ISimulationComplete {

    void simulationComplete(DataBuilder db, Population pop, Calendar time);
}
