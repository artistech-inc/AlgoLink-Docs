/*
 * Copyright 2015 ArtisTech, Inc.
 */
package com.artistech.algolink.orgs.noise;

import com.l2fprod.common.annotations.Categorization;
import com.artistech.algolink.core.OrgConfigBase;
import com.artistech.algolink.core.Organization;
import com.artistech.algolink.core.media.CommunicationMediumType;
import com.artistech.algolink.json.ColorDeserializer;
import com.artistech.algolink.json.ColorSerializer;
import com.artistech.algolink.json.CommMediumTypeDeserializer;
import com.artistech.algolink.json.CommMediumTypeSerializer;
import com.artistech.annotations.EnumeratedType;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Calendar;

/**
 *
 * @author matta
 */
public class NoiseConfig extends OrgConfigBase {

    private java.awt.Color _color;
    private double _comms_per_minute;
    private CommunicationMediumType _medium;
    private boolean _stressTest;
    private int _loadSize;
    private MessageDataType _messageDataType;
    
    public NoiseConfig() {
    }

    @Categorization(category = "Organization Config")
    public double getCommsPerMinute() {
        return _comms_per_minute;
    }

    @Categorization(category = "Organization Config")
    public void setCommsPerMinute(double value) {
        _comms_per_minute = value;
    }

    @EnumeratedType
    @JsonSerialize(using = CommMediumTypeSerializer.class, as = CommunicationMediumType.class)
    @Categorization(category = "Organization Config")
    public CommunicationMediumType getCommunicationMediumType() {
        return _medium;
    }

    @EnumeratedType
    @JsonDeserialize(using = CommMediumTypeDeserializer.class, as = CommunicationMediumType.class)
    @Categorization(category = "Organization Config")
    public void setCommunicationMediumType(CommunicationMediumType value) {
        _medium = value;
    }

    @Categorization(category = "Stress Test")
    public boolean getStressTest() {
        return _stressTest;
    }

    @Categorization(category = "Stress Test")
    public void setStressTest(boolean value) {
        _stressTest = value;
    }

    @Override
    protected void initializeHelper() {
        super.initializeHelper();
        _color = java.awt.Color.BLACK;
    }

    @JsonDeserialize(using = ColorDeserializer.class, as = java.awt.Color.class)
    @Categorization(category = "Communication Colors")
    public java.awt.Color getColor() {
        return _color;
    }

    @JsonSerialize(using = ColorSerializer.class, as = java.awt.Color.class)
    @Categorization(category = "Communication Colors")
    public void setColor(java.awt.Color value) {
        _color = value;
    }

    @Override
    public Organization createOrganization(Calendar time) {
        return new NoiseOrganization(this);
    }
    
    public int getLoadSize() {
        return _loadSize;
    }
    
    public void setLoadSize(int value) {
        _loadSize = value;
    }

    public MessageDataType getMessageContentType() {
        return _messageDataType;
    }
    
    public void setMessageContentType(MessageDataType value) {
        _messageDataType = value;
    }
}
