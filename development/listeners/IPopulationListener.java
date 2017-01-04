/*
 * Copyright 2015-2016 ArtisTech, Inc.
 */
package com.artistech.algolink.listeners;

import com.artistech.algolink.core.Population;
import java.util.Calendar;

/**
 *
 * @author matta
 */
public interface IPopulationListener extends IGroupListener {

    void onPopulationCleared(Population sender, Calendar time);

    void onPopulationCreated(Population sender, Calendar time);

    void onPopulationModified(Population sender, Calendar time);
    
    void onEntitiesInitialized(Population sender, Calendar time);
    
    void onOrganizationsInitialized(Population sender, Calendar time);
}
