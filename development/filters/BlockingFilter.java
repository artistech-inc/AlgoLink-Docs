/*
 * Copyright 2015-2016 ArtisTech, Inc.
 */
package com.artistech.algolink.core.commfilters;

import com.artistech.algolink.CommunicationCancellerEnum;
import com.artistech.algolink.core.Communication;
import com.artistech.algolink.core.CommunicationFilter;
import com.artistech.algolink.core.Entity;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Filter that will only send if the receiver and sender are not busy.
 *
 * @author matta
 */
public class BlockingFilter extends CommunicationFilter {

    private static final Logger LOGGER = Logger.getLogger(BlockingFilter.class.getName());
    private final ArrayList<Entity> busy = new ArrayList<>();

    /**
     * Send the communication.
     *
     * @param comm
     * @return
     */
    @Override
    public synchronized CommunicationFilterResult sendCommunication(Communication comm) {
        String meta = "";
        CommunicationCancellerEnum by = CommunicationCancellerEnum.NEITHER;
        if (willSend(comm)) {
            busy.add(comm.getSender());
            busy.add(comm.getReceiver());
            return new CommunicationFilterResult(super.doSend(comm), comm, "SUCCESS");
        } else if (busy.contains(comm.getSender())) {
            by = CommunicationCancellerEnum.SENDER;
            meta = "Cannot complete communication, Sender is busy";
        } else if (busy.contains(comm.getReceiver())) {
            by = CommunicationCancellerEnum.RECEIVER;
            meta = "Cannot complete communication, Receiver is busy";
        }
        super.doCancel(comm, by);
        LOGGER.log(Level.FINE, meta);
        return new CommunicationFilterResult(false, comm, meta);
    }

    /**
     * Handle on end communication.
     *
     * @param comm
     */
    @Override
    public synchronized void endCommunication(Communication comm) {
        busy.remove(comm.getSender());
        busy.remove(comm.getReceiver());
    }

    /**
     * Return if the communication will send.
     *
     * @param comm
     * @return
     */
    @Override
    public synchronized boolean willSend(Communication comm) {
        return !busy.contains(comm.getSender()) && !busy.contains(comm.getReceiver());
    }

    /**
     * Clean up resources.
     */
    @Override
    public void teardown() {
        super.teardown();
        busy.clear();
    }
}
