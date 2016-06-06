/*
 * Copyright 2015-2016 ArtisTech, Inc.
 */
package com.artistech.algolink.core.commfilters;

import com.artistech.algolink.core.Communication;
import com.artistech.algolink.core.CommunicationFilter;
import com.artistech.algolink.core.Configurable;
import com.artistech.utils.EventArgs;
import com.artistech.utils.PropertiesInitializer;
import java.util.logging.Logger;

/**
 * Communication filter that randomly drops communications some specified
 * percent of the time.
 *
 * @author matta
 */
public class PercentDropFilter extends CommunicationFilter implements Configurable {

    private static final Logger LOGGER = Logger.getLogger(PercentDropFilter.class.getName());
    private PercentDropConfig _config = new PercentDropConfig();

    /**
     * Constructor.
     */
    public PercentDropFilter() {
        this(new PercentDropConfig());
    }

    /**
     * Constructor.
     *
     * @param config
     */
    public PercentDropFilter(PercentDropConfig config) {
        _config = config;
    }

    /**
     * Get the configuration.
     *
     * @return
     */
    @Override
    public PropertiesInitializer getConfig() {
        return _config;
    }

    /**
     * Set the configuration.
     *
     * @param value
     */
    @Override
    public void setConfig(PropertiesInitializer value) {
        if (value != null && PercentDropConfig.class.isAssignableFrom(value.getClass())) {
            _config = (PercentDropConfig) value;
        }
    }

    /**
     * Send the communication.
     *
     * @param comm
     * @return
     */
    @Override
    public CommunicationFilterResult sendCommunication(Communication comm) {
        super.doSend(comm);
        if (willSend(comm)) {
            return new CommunicationFilterResult(true, comm, "SUCCESS");
        } else {
            comm.setSent(false);
            comm.raiseOnError(new EventArgs());
            return new CommunicationFilterResult(false, comm, "DROPPED");
        }
    }

    /**
     * Will the communication send.
     *
     * A random value is pulled and if this value is >= the percent drop, it
     * sends.
     *
     * @param comm
     * @return
     */
    @Override
    public boolean willSend(Communication comm) {
        double decider = com.artistech.utils.Random.nextDouble();
        return decider >= _config.getPercentDrop();
    }

}
