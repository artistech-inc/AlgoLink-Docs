/*
 * Copyright 2015-2016 ArtisTech, Inc.
 */
package com.artistech.algolink.json;

import com.artistech.utils.TypeHandler;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.util.Calendar;

/**
 * Serialize a Calendar to String.
 *
 * @author matta
 */
public class CalendarSerializer extends com.fasterxml.jackson.databind.JsonSerializer<java.util.Calendar> {

    @Override
    public void serialize(Calendar t, JsonGenerator jg, SerializerProvider sp) throws IOException, JsonProcessingException {
        jg.writeObject(TypeHandler.dateToString(t));
    }

}
