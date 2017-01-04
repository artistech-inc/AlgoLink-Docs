/*
 * Copyright 2015-2016 ArtisTech, Inc.
 */
package com.artistech.algolink;

/**
 * Interface for listening to the AlgoLink application. Used mostly for opening
 * and cleaning up files/resources.
 *
 * @author matta
 */
public interface IAlgoLinkApplicationListener {

    /**
     * Start/open required resources
     */
    void applicationStarted();

    /**
     * Stop/close requested resources
     */
    void applicationEnded();
}
