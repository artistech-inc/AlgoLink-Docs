/*
 * Copyright 2015-2016 ArtisTech, Inc.
 */
package com.artistech.algolink.listeners;

import com.artistech.utils.EventArgs;
import com.artistech.algolink.events.CommunicationTransactionEventArgs;
import com.artistech.algolink.core.Communication;
import com.artistech.algolink.core.Entity;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Events for listening to an entity.
 *
 * These are extensively used by the EntityWrapper class.
 *
 * @author matta
 */
public interface IEntityListener {

    /**
     * Fire on communication sending.
     *
     * @param sender
     * @param comm
     * @param args
     */
    void onCommunicationSending(Entity sender, Communication comm, CommunicationTransactionEventArgs args);

    /**
     * Fire on communication receiving.
     *
     * @param receiver
     * @param comm
     * @param args
     */
    void onCommunicationReceiving(Entity receiver, Communication comm, CommunicationTransactionEventArgs args);

    /**
     * Fire on communication sent.
     *
     * @param sender
     * @param comm
     */
    void onCommunicationSent(Entity sender, Communication comm);

    /**
     * Fire on communication received.
     *
     * @param receiver
     * @param comm
     */
    void onCommunicationReceived(Entity receiver, Communication comm);

    /**
     * Fire when neutrality changes.
     *
     * @param sender
     * @param args
     */
    void onNeutralityChanged(Entity sender, EventArgs args);

    /**
     * Fire when the entity moves.
     *
     * @param sender
     * @param args
     */
    void onEntityMove(Entity sender, EventArgs args);

    /**
     * Fire when a message is received.
     *
     * @param sender
     * @param message
     * @param time
     * @param values
     */
    void onMessageReceived(Entity sender, String message, Calendar time, HashMap<String, Object> values);

//  TODO: Possible Future Implementation
//    default void onCommunicationError(Entity sender, Communication comm) {
//    }
}
