/*
 * Copyright 2015 ArtisTech, Inc.
 */
package com.artistech.algolink.orgs.consensus2;

import com.artistech.algolink.core.Communication;
import com.artistech.algolink.core.Entity;
import com.artistech.algolink.core.EntityWrapperBase;
import com.artistech.algolink.core.Group;
import com.artistech.algolink.orgs.consensus2.Consensus2Opinion.EntityOpinion;
import com.artistech.utils.Random;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author matta
 */
public final class Consensus2Entity extends EntityWrapperBase {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(Consensus2Entity.class.getName());

    /**
     * Opinion Vector.
     */
    private final Map<String, EntityOpinion> opinions;

    /**
     * Parent organization.
     */
    protected final Consensus2Organization _org;

    /**
     * Constructor.
     *
     * @param ent
     * @param org
     */
    protected Consensus2Entity(Entity ent, Consensus2Organization org) {
        super(ent, org);
        _org = org;
        opinions = new TreeMap<>();
    }

    /**
     * Get the opinions.
     *
     * @return
     */
    public Collection<EntityOpinion> getOpinions() {
        return opinions.values();
    }

    /**
     * Get a vector of the state opinion.
     *
     * @return
     */
    @JsonIgnore
    public ArrayList<Integer> getOpinionStateVector() {
        ArrayList<Integer> ret = new ArrayList<>();
        for (String key : opinions.keySet()) {
            EntityOpinion eo = opinions.get(key);
            ret.add(eo.getBelief());
        }
        return ret;
    }

    /**
     * Get a string representation of the state vector.
     *
     * @return
     */
    public String stateVector() {
        ArrayList<Integer> val = getOpinionStateVector();
        StringBuilder sb = new StringBuilder();
        sb.append("[ ");
        for (Integer x : val) {
            sb.append(x).append(" ");
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * Set a specified opinion value.
     *
     * @param key
     * @param value
     */
    protected void setOpinionValue(int key, int value) {
        int counter = 0;
        for (String key2 : opinions.keySet()) {
            if (counter == key) {
                EntityOpinion eo = opinions.get(key2);
                eo.setBelief(value);
                break;
            }
            counter += 1;
        }
    }

    /**
     * Get a specified opinion value.
     *
     * @param key
     * @return
     */
    public int getOpinionValue(int key) {
        int counter = 0;
        for (String key2 : opinions.keySet()) {
            if (counter == key) {
                EntityOpinion eo = opinions.get(key2);
                return eo.getBelief();
            }
            counter += 1;
        }
        return -1;
    }

    /**
     * Add an opinion.
     *
     * @param value
     */
    protected void addOpinion(final EntityOpinion value) {
        opinions.put(value.getId(), value);
        value.addListener(new OpinionListener() {

            @Override
            public void opinionChanged(EntityOpinion sender, int oldValue, int newValue) {
                if (!_org.isInitializing) {
                    LOGGER.log(Level.FINEST, "[{2}] {3} changed from {0} to {1}", new Object[]{oldValue, newValue, sender.getEntity().getEntity().getId(), sender.getId()});
                }
            }
        });
    }

    /**
     * Get the opinion.
     *
     * @param type
     * @return
     */
    public EntityOpinion getOpinion(Consensus2Opinion type) {
        for (EntityOpinion op : opinions.values()) {
            if (op.getType() == type) {
                return op;
            }
        }
        return null;
    }

    /**
     * Get the opinion.
     *
     * @param id
     * @return
     */
    public EntityOpinion getOpinion(String id) {
        return opinions.get(id);
    }

    /**
     * Handle a communication receive event.
     *
     * @param receiver
     * @param comm
     */
    @Override
    public void onCommunicationReceived(Entity receiver, Communication comm) {
        super.onCommunicationReceived(receiver, comm);
        LOGGER.log(Level.FINEST, "INDIVIDUAL RECEIVE: {0}", comm.getId());

        //handle possible opinion changing...
        if (comm.getCommunicationMessage() != null
                && Consensus2Message.class.isAssignableFrom(comm.getCommunicationMessage().getClass())) {
            Consensus2Message cm = (Consensus2Message) comm.getCommunicationMessage();
            ArrayList<Integer> vector1 = cm.getOpinionStateVector();
            ArrayList<Integer> vector2 = this.getOpinionStateVector();
            int sim = Consensus2Organization.similarityThreshold(vector1, vector2);

            //if the opinion vectors are similar enough, change
            //an opinion if there is an opinion of differeing value
            if (sim >= _org._config.getPhi()) {
                ArrayList<Integer> indexes = new ArrayList<>();
                for (int ii = 0; ii < vector1.size(); ii++) {
                    if (!vector1.get(ii).equals(vector2.get(ii))) {
                        indexes.add(ii);
                    }
                }
                if (!indexes.isEmpty()) {
                    int index = Random.getRandom(indexes);
                    this.setOpinionValue(index, vector1.get(index));
                } else {
                    LOGGER.log(Level.FINEST, "IDENTICAL OPINION VECTOR: {0}", getEntity().getId());
                    Set<GroupEdge> allEdges = _org.graph.getAllEdges(comm.getSender(), comm.getReceiver());
//                    if (allEdges != null) {
                        for (GroupEdge ge : allEdges) {
                            ge.setVisited(true);
                        }
//                    }
                }
            } else if (cm.getInfluenceeType() == Consensus2Message.InfluenceeType.GROUP) {
                //similarity vector not similar enough invoke change
                //we are currently influenced by a group, so re-wire the group.
                LOGGER.log(Level.FINEST, "REWIRE GROUP: {0}", getEntity().getId());
                LOGGER.log(Level.FINEST, "SIMILARITY THRESHOLD NOT HIGH ENOUGH (GROUP): {0}", getEntity().getId());

                for (Group g : _org.getGroups()) {
                    if (g.getId().equals(cm.getInfluencerGroupId())) {
                        Consensus2Group grp = (Consensus2Group) g;
                        ArrayList<Entity> toKeep = new ArrayList<>(grp.getMembers());
                        toKeep.remove(getEntity());
                        grp.rewireRequested(toKeep);
                        break;
                    }
                }
            } else if (cm.getInfluenceeType() == Consensus2Message.InfluenceeType.ENTITY) {
                //similarity vector not similar enough to invoke change
                //we are currently influenced by an individual, do nothing.
                LOGGER.log(Level.FINEST, "SIMILARITY THRESHOLD NOT HIGH ENOUGH (ENTITY): {0}", getEntity().getId());

                //if we are running where groups of size 2 are special, that means that
                //we want to treat this as a group encounter and if phi isn't high enough
                //we need to rewire.
                if (_org.doSize2GroupSpecialWork()) {
                    LOGGER.log(Level.FINEST, "REWIRE GROUP: {0}", getEntity().getId());

                    for (Group g : _org.getGroups()) {
                        if (g.getId().equals(cm.getInfluencerGroupId())) {
                            Consensus2Group grp = (Consensus2Group) g;
                            ArrayList<Entity> toKeep = new ArrayList<>(grp.getMembers());
                            toKeep.remove(getEntity());
                            grp.rewireRequested(toKeep);
                            break;
                        }
                    }
                }
            }
        }
    }
}
