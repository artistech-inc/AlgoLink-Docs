/*
 * Copyright 2015 ArtisTech, Inc.
 */
package com.artistech.algolink.orgs.noise;

import com.artistech.algolink.core.ConfigBase;
import com.artistech.algolink.core.Organization;
import com.artistech.algolink.core.Population;
import com.artistech.algolink.annotations.OrgMeta;
import com.artistech.algolink.core.Communication;
import com.artistech.algolink.core.Entity;
import com.artistech.algolink.exceptions.CommunicationException;
import com.artistech.utils.Random;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;

/**
 *
 * @author matta
 */
@OrgMeta(description = "Background Noise",
        displayName = "Random Personal Comms",
        version = "1.0",
        type = OrgMeta.OrgTypes.SOCIETAL)
public final class NoiseOrganization extends Organization {

    protected final NoiseConfig _config;
    private Population _pop;
    private static final NoiseContent NOISE_CONTENT;

    /**
     * Static Constructor.
     */
    static {
        NOISE_CONTENT = new NoiseContent();
    }

    /**
     * Hidden Constructor.
     */
    protected NoiseOrganization() {
        this(new NoiseConfig());
    }

    /**
     * Constructor.
     *
     * @param config
     */
    protected NoiseOrganization(NoiseConfig config) {
        super(config);
        _config = config;
    }

    /**
     * Get the organizational configuration.
     *
     * @return
     */
    @Override
    protected final ConfigBase getOrgConfig() {
        return _config;
    }

    /**
     * Get the max size of this organization.
     *
     * This is used to tell the population how large it must be. For a size of
     * 0, it lets the population remain whatever > 0 size is specified.
     *
     * @return
     */
    @Override
    public int getMaxSize() {
        return 0;
    }

    /**
     * Grab and maintain a reference to the population.
     *
     * @param pop
     * @param time
     */
    @Override
    protected void populateHelper(Population pop, Calendar time) {
        _pop = pop;
    }

    /**
     * Get the members of the organization.
     *
     * The members of the organization are all members of the population.
     *
     * @return
     */
    @Override
    public java.util.Collection<Entity> getMembers() {
        return _pop.getMembers();
    }

    /**
     * Tick and generate communications.
     *
     * @param time
     * @return
     */
    @Override
    public Collection<Communication> tick(Calendar time) {
        double chance = _config.getCommsPerMinute();
        final ArrayList<Communication> comms = new ArrayList<>();

        while (chance >= 1.0) {
            try {
                Communication comm = createCommunication(time);
                comms.add(comm);
            } catch (CommunicationException ex) {
                Logger.getLogger(NoiseOrganization.class.getName()).log(Level.SEVERE, null, ex);
            }
            chance -= 1.0;
        }
        double rand = Random.nextDouble();
        if (rand < chance) {
            try {
                Communication comm = createCommunication(time);
                comms.add(comm);
            } catch (CommunicationException ex) {
                Logger.getLogger(NoiseOrganization.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return comms;
    }

    private Communication createCommunication(Calendar time) throws CommunicationException {
        ImmutablePair<Entity, Entity> randomEntityPair = _pop.getRandomEntityPair();
        int seconds = Random.nextInt(com.artistech.utils.TimeUtils.SECONDS_IN_MINUTE);
        Calendar t = (Calendar) time.clone();
        t.add(Calendar.SECOND, seconds);
        Communication comm = new Communication(randomEntityPair.getKey(), randomEntityPair.getValue(), t, this);

        byte[] load = null;
        Collection<NoiseMessageData> data;
        switch (_config.getMessageContentType()) {
            case Text:
                data = NOISE_CONTENT.getTextData();
                break;
            case Image:
                data = NOISE_CONTENT.getImageData();
                break;
            case Voice:
                data = NOISE_CONTENT.getVoiceData();
                break;
            case None:
            default:
                data = null;
                break;
        }
        if (data != null) {
            NoiseMessageData messageData = Random.getRandom(data);
            if (messageData.getSize() != null) {
                load = new byte[messageData.getSize()];
            } else if (messageData.getMessage() != null) {
                load = messageData.getMessage().getBytes();
            } else if (messageData.getUri() != null) {
                try (InputStream stream = messageData.getUri().toURL().openStream();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                    IOUtils.copy(stream, baos);
                    load = baos.toByteArray();
                } catch (MalformedURLException ex) {
                    Logger.getLogger(NoiseOrganization.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(NoiseOrganization.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        if (load == null) {
            load = new byte[_config.getLoadSize()];
        }
        comm.setCommunicationMessage(new NoiseMessage(load));

        return comm;
    }
}
