/*
 * Copyright 2015 ArtisTech, Inc.
 */
package com.artistech.algolink.orgs.noise;

import com.artistech.algolink.core.MessageContent;
import com.artistech.utils.PropertiesInitializer;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author matta
 */
public class NoiseContent extends PropertiesInitializer implements MessageContent<NoiseMessageData> {
    private ArrayList<NoiseMessageData> texts;
    private ArrayList<NoiseMessageData> images;
    private ArrayList<NoiseMessageData> voice;

    @Override
    protected void initializeHelper() {
        super.initializeHelper(); //To change body of generated methods, choose Tools | Templates.
        texts = new ArrayList<>();
        images = new ArrayList<>();
        voice = new ArrayList<>();
    }
    
    @Override
    public Collection<NoiseMessageData> getTextData() {
        return texts;
    }
    
    @Override
    public void setTextData(Collection<NoiseMessageData> value) {
        texts.clear();
        texts.addAll(value);
    }
    
    @Override
    public Collection<NoiseMessageData> getImageData() {
        return images;
    }
    
    @Override
    public void setImageData(Collection<NoiseMessageData> value) {
        images.clear();
        images.addAll(value);
    }
    
    @Override
    public Collection<NoiseMessageData> getVoiceData() {
        return voice;
    }
    
    @Override
    public void setVoiceData(Collection<NoiseMessageData> value) {
        voice.clear();
        voice.addAll(value);
    }

}
