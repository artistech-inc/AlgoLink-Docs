/*
 * Copyright 2015-2016 ArtisTech, Inc.
 */
package com.artistech.algolink.listeners;

import com.artistech.utils.EventArgs;
import com.artistech.algolink.events.CommunicationCanceledEventArgs;
import com.artistech.algolink.core.Communication;

/**
 * Communication listener events.
 *
 * @author matta
 */
public interface ICommunicationListener {

    /**
     * When the communication is sending.
     *
     * @param comm
     * @param args
     */
    void onCommunicationSending(Communication comm, EventArgs args);

    /**
     * When the communication is canceled.
     *
     * @param comm
     * @param args
     */
    void onCommunicationCanceled(Communication comm, CommunicationCanceledEventArgs args);

    /**
     * When there is an error.
     *
     * @param comm
     * @param args
     */
    void onCommunicationError(Communication comm, EventArgs args);

    /**
     * When the communication ends.
     *
     * @param comm
     * @param args
     */
    void onCommunicationEnd(Communication comm, EventArgs args);

    /**
     * When the duration changes.
     *
     * @param comm
     * @param args
     */
    void onDurationChanged(Communication comm, EventArgs args);

    /**
     * When sending begins. (for async).
     *
     * @param comm
     */
    void onBeginSend(Communication comm);
}
