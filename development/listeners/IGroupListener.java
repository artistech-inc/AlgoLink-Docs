/*
 * Copyright 2015-2016 ArtisTech, Inc.
 */
package com.artistech.algolink.listeners;

import com.artistech.algolink.core.Entity;
import com.artistech.algolink.core.Group;
import java.util.Calendar;

/**
 * Events that are fired by a group.
 *
 * @author matta
 */
public interface IGroupListener {

    /**
     * Fire when an entity is added.
     *
     * @param sender
     * @param entity
     * @param time
     */
    void onEntityAdded(Group sender, Entity entity, Calendar time);

    /**
     * Fire when an entity is removed.
     *
     * @param sender
     * @param entity
     * @param time
     */
    void onEntityRemoved(Group sender, Entity entity, Calendar time);

    /**
     * Fire when a group is added.
     *
     * @param sender
     * @param group
     * @param time
     */
    void onGroupAdded(Group sender, Group group, Calendar time);

    /**
     * Fire when a group is removed.
     *
     * @param sender
     * @param group
     * @param time
     */
    void onGroupRemoved(Group sender, Group group, Calendar time);
}
