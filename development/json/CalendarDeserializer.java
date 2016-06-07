/*
 * Copyright 2015-2016 ArtisTech, Inc.
 */
package com.artistech.algolink.json;

import com.artistech.utils.TypeHandler;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Deserialize a string to a calendar object.
 *
 * @author matta
 */
public class CalendarDeserializer extends com.fasterxml.jackson.databind.JsonDeserializer<java.util.Calendar> {

    @Override
    public Calendar deserialize(JsonParser jp, DeserializationContext dc) throws IOException, JsonProcessingException {
        ObjectCodec oc = jp.getCodec();
        JsonNode node = oc.readTree(jp);
        String s = node.asText();
        try {
            return TypeHandler.calendarFromString(s);
        } catch (ParseException ex) {
            Logger.getLogger(CalendarDeserializer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Calendar.getInstance();
    }

}
