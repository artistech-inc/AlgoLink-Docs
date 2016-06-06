/*
 * Copyright 2015-2016 ArtisTech, Inc.
 */
package com.artistech.algolink.web.commfilters;

import com.artistech.algolink.core.commfilters.ICommunicationReceived;
import com.artistech.algolink.core.Communication;
import com.artistech.algolink.core.DataBuilder;
import com.artistech.algolink.core.commfilters.CommunicationFilterResult;
import com.artistech.algolink.core.commfilters.ExternalCommFilter;
import com.artistech.algolink.core.commfilters.IFilterListener;
import com.artistech.algolink.web.PostMessage;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author matta
 */
public class WebCommFilter extends ExternalCommFilter implements ICommunicationReceived {

    private static final Logger LOGGER = Logger.getLogger(WebCommFilter.class.getName());
    private transient ExternalCommRecvThread _recv_thread;

    private boolean initialized = false;

    private void readObject(java.io.ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        _recv_thread = new ExternalCommRecvThread();
    }

    @Override
    public void initialize(DataBuilder db) {
        if (!initialized) {
            initialized = true;
            super.initialize(db);
            _recv_thread.start();
            ExternalCommFilter.addListener(this);
        }
    }

    public WebCommFilter() {
        _recv_thread = new ExternalCommRecvThread();
    }

    @Override
    public void onCommunicationStarted(String id, String communication, HashMap<String, Object> vals) {
        _recv_thread.addMessage(new PostMessage(id, communication, vals, PostMessage.ReceiveType.START));
    }

    @Override
    public void onCommunicationCanceled(String id, String communication, HashMap<String, Object> vals) {
        _recv_thread.addMessage(new PostMessage(id, communication, vals, PostMessage.ReceiveType.CANCEL));
    }

    @Override
    public void onCommunicationReceived(String id, String communication, HashMap<String, Object> vals) {
        _recv_thread.addMessage(new PostMessage(id, communication, vals, PostMessage.ReceiveType.RECEIVED));
    }

    @Override
    public void onCommunicationQueued(Communication comm, DataBuilder db) {
    }

    @Override
    protected void onCommunicationReceivedHelper(final String id, String communication, HashMap<String, Object> vals) {
        if (id == null) {
            return;
        }

        if (PROCESSED.contains(id)) {
            Logger.getLogger(WebCommFilter.class.getName()).log(Level.FINE, "Already Processed (receiving): {0}", new Object[]{id});
            return;
        }
        PROCESSED.add(id);
        Communication comm2 = null;
        IFilterListener filt = null;

        synchronized (COMMS) {
            for (Communication comm : COMMS.keySet()) {
                if (comm.getId().equals(id)) {
                    if (comm.getCommunicationMessage() != null && communication != null) {
                        comm.getCommunicationMessage().setMessage(communication);
                    }
                    comm.setPayLoad(vals);
                    comm2 = comm;
                    filt = COMMS.get(comm);
                    break;
                }
            }
            if (comm2 != null) {
                COMMS.remove(comm2);
            }
        }

        if (comm2 != null && filt != null) {
            boolean success = doSend(comm2);
            if (!success) {
                Logger.getLogger(WebCommFilter.class.getName()).log(Level.FINER, "Send Failed: {0}", new Object[]{comm2.getId()});
            }
            CommunicationFilterResult res = new CommunicationFilterResult(success, comm2, vals);
            filt.received(this, comm2, res);
        }
    }

    @Override
    protected void onCommunicationStartedHelper(String id, String communication, HashMap<String, Object> vals) {
        Communication comm2 = null;
        IFilterListener filt = null;

        synchronized (COMMS) {
            for (Communication comm : COMMS.keySet()) {
                if (comm.getId().equals(id)) {
                    comm2 = comm;
                    filt = COMMS.get(comm);
                    break;
                }
            }
        }

        if (comm2 != null) {
            preFiltStarted(comm2);
            if (filt != null) {
                filt.started(this, comm2);
            }
            postFiltStarted(comm2);
        }
    }

    protected void preFiltStarted(Communication comm) {
    }

    protected void postFiltStarted(Communication comm) {
    }

    @Override
    protected void onCommunicationCanceledHelper(String id, String communication, HashMap<String, Object> vals) {
        if (!PROCESSED.contains(id)) {
            PROCESSED.add(id);
        }
        Communication comm2 = null;
        IFilterListener filt = null;

        synchronized (COMMS) {
            for (Communication comm : COMMS.keySet()) {
                if (comm.getId().equals(id)) {
                    comm2 = comm;
                    filt = COMMS.get(comm);
                    break;
                }
            }
            if (comm2 != null) {
                COMMS.remove(comm2);
            }
        }

        if (comm2 != null && filt != null) {
            filt.canceled(this, comm2);
        }
    }

    @Override
    public boolean beginSendCommunication(final Communication comm, final IFilterListener handler) {
        synchronized (COMMS) {
            COMMS.put(comm, handler);
        }

        //
        comm.raiseOnBeginSend();
        ExternalCommFilter.queueCommunication(comm, _db);

        return true;
    }

    @Override
    public final CommunicationFilterResult sendCommunication(Communication comm) {
        LOGGER.log(Level.SEVERE, "must call begin send communication...");
        throw new UnsupportedOperationException("must call begin send communication...");
    }

    @Override
    public final boolean willSend(Communication comm) {
        return true;
    }

    @Override
    public void teardown() {
        synchronized (COMMS) {
            COMMS.clear();
        }
        synchronized (QUEUED_COMMS) {
            QUEUED_COMMS.clear();
        }
        ExternalCommFilter.removeListener(this);
        _recv_thread.askForTermination();
        super.teardown();
    }

    @Override
    public final boolean getRequiresAsyncCommStart() {
        return true;
    }
}
