/*
 * Copyright 2015 ArtisTech, Inc.
 */
package com.artistech.algolink.orgs.noise;

import com.artistech.algolink.core.CommunicationMessage;

/**
 *
 * @author matta
 */
public class NoiseMessage extends CommunicationMessage {

    private byte[] _load;

    public NoiseMessage() {
        this(new byte[]{});
    }

    public NoiseMessage(byte[] value) {
        _load = value;
    }

    public byte[] getLoad() {
        return _load;
    }

    public void setLoad(byte[] value) {
        _load = value;
    }
}
