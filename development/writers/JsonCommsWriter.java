/*
 * Copyright 2015-2016 ArtisTech, Inc.
 */
package com.artistech.algolink.writers;

import com.artistech.algolink.annotations.WriterMeta;
import com.artistech.algolink.core.Communication;
import com.artistech.algolink.core.DataBuilder;
import com.artistech.algolink.core.Population;
import com.artistech.algolink.events.CommunicationEventArgs;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Serialize communications to a JSON file.
 *
 * @author matta
 */
@WriterMeta(category = WriterMeta.WriterTypes.Stream,
        outputFileExtension = ".comms_log.json.gz",
        displayName = "Json Comms Writer",
        threadSafe = true)
public class JsonCommsWriter extends IAlgoLinkWriterImpl {

    private java.io.OutputStreamWriter _writer;
    private boolean _had_previous;
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Constructor.
     */
    public JsonCommsWriter() {
    }

    /**
     * On tick.
     *
     * no-op.
     *
     * @param db
     * @param pop
     * @param time
     */
    @Override
    public void tick(DataBuilder db, Population pop, Calendar time) {
    }

    /**
     * On simulation complete, close up the file.
     *
     * @param db
     * @param pop
     * @param time
     */
    @Override
    public void simulationComplete(DataBuilder db, Population pop, Calendar time) {
        if (_writer != null) {
            try {
                _writer.write("]");
                _writer.flush();
                _writer.close();
                _writer = null;
            } catch (IOException ex) {
                Logger.getLogger(JsonCommsWriter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * On simulation start, initialize the file.
     *
     * @param db
     * @param pop
     * @param time
     */
    @Override
    public void simulationStart(DataBuilder db, Population pop, Calendar time) {
        _had_previous = false;
        try {
            _writer = new OutputStreamWriter(new java.util.zip.GZIPOutputStream(new java.io.FileOutputStream(db.getOutputFilePrefix() + getOutputFilePostfix())));
            _writer.write("[");
        } catch (IOException ex) {
            Logger.getLogger(JsonCommsWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * On communication.
     *
     * no-op.
     *
     * @param sender
     * @param e
     */
    @Override
    public void onCommunication(Object sender, CommunicationEventArgs e) {
    }

    /**
     * On communication end.
     *
     * Print JSON communication data.
     *
     * @param sender
     * @param e
     */
    @Override
    public void onCommunicationEnd(Object sender, CommunicationEventArgs e) {
        if (_writer != null) {
            String nl = System.getProperty("line.separator");
            for (Communication c : e.getCommunications()) {
                try {
                    if (_had_previous) {
                        _writer.write(", " + nl);
                    }
                    _had_previous = true;
                    _writer.write(MAPPER.writeValueAsString(c));
                } catch (IOException ex) {
                    Logger.getLogger(JsonCommsWriter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
