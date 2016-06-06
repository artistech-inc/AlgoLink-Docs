/*
 * Copyright 2015 ArtisTech, Inc.
 */
package com.artistech.algolink.orgs.noise;

import com.artistech.algolink.core.MessageData;
import java.io.Serializable;
import java.net.URI;

/**
 *
 * @author matta
 */
public class NoiseMessageData implements Serializable, MessageData {

    private Integer size;
    private String msg;
    private URI uri;

    public NoiseMessageData() {
        size = null;
        msg = null;
        uri = null;
    }

    @Override
    public Integer getSize() {
        return size;
    }

    @Override
    public void setSize(Integer value) {
        size = value;
    }

    @Override
    public String getMessage() {
        return msg;
    }

    @Override
    public void setMessage(String value) {
        msg = value;
    }

    @Override
    public URI getUri() {
        return uri;
    }

    @Override
    public void setUri(URI value) {
        uri = value;
    }
}
