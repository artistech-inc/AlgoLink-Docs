/*
 * Copyright 2015-2016 ArtisTech, Inc.
 */
package com.artistech.algolink.listeners;

import com.artistech.algolink.events.CommunicationEventArgs;

/**
 * A communication listener that is registered through the DataBuilder.
 *
 * @author matta
 */
public interface IOnCommunicationListener {

    /**
     * Fire when a communication starts.
     *
     * @param sender
     * @param e
     */
    void onCommunication(Object sender, CommunicationEventArgs e);

    /**
     * Fire when a communication is complete.
     *
     * @param sender
     * @param e
     */
    void onCommunicationEnd(Object sender, CommunicationEventArgs e);
}
