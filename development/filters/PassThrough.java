/*
 * Copyright 2015-2016 ArtisTech, Inc.
 */
package com.artistech.algolink.core.commfilters;

import com.artistech.algolink.core.CommunicationFilter;
import com.artistech.algolink.core.Communication;

/**
 * Default communication filter. All communications are successfully
 * transmitted.
 *
 * @author matta
 */
public class PassThrough extends CommunicationFilter {

    /**
     * Send the communication.  Always success.
     * @param comm
     * @return 
     */
    @Override
    public CommunicationFilterResult sendCommunication(Communication comm) {
        boolean sent = super.doSend(comm);
        return new CommunicationFilterResult(sent, comm, "SUCCESS");
    }

    /**
     * Will the communication send.  Always true.
     * @param comm
     * @return 
     */
    @Override
    public final boolean willSend(Communication comm) {
        return true;
    }

}
