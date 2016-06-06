/*
 * Copyright 2015-2016 ArtisTech, Inc.
 */
package com.artistech.algolink.serializers;

import com.artistech.algolink.annotations.WriterMeta;
import com.artistech.algolink.core.DataBuilder;
import com.artistech.algolink.core.Population;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Serialize the organization to JSON.
 *
 * @author matta
 */
@WriterMeta(category = WriterMeta.WriterTypes.Serialization,
        outputFileExtension = ".pop.json.gz",
        displayName = "Json Serializer")
public class JsonSerializer implements IAlgoLinkSerializer {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Serialize.
     *
     * @param db
     * @param pop
     */
    @Override
    public void serialize(DataBuilder db, Population pop) {
        HashMap<String, Object> to_serialize = new HashMap<>();
        to_serialize.put("DataBuilderConfig", db.getConfig());
        to_serialize.put("Population", pop);
        MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
        try (java.util.zip.GZIPOutputStream writer = new java.util.zip.GZIPOutputStream(new FileOutputStream(db.getOutputFilePrefix() + ".pop.json.gz"))) {
            MAPPER.writeValue(writer, to_serialize);
        } catch (IOException ex) {
            Logger.getLogger(JsonSerializer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Get the display name.
     *
     * @return
     */
    @Override
    public String displayName() {
        WriterMeta ann = this.getClass().getAnnotation(WriterMeta.class);
        return ann.displayName();
    }

    /**
     * Get the extension (".pop.json.gz").
     *
     * @return
     */
    @Override
    public String getOutputFilePostfix() {
        WriterMeta meta = this.getClass().getAnnotation(WriterMeta.class);
        return meta.outputFileExtension();
    }
}
