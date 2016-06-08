/*
 * Copyright 2015-2016 ArtisTech, Inc.
 */
package com.artistech.clock;

import java.util.Calendar;

/**
 * Interface for being able to listen to the clock.
 *
 * @author matta
 */
public interface ClockListener {

    /**
     * Fires every second before work().
     *
     * @param time
     */
    void secondPreWork(Calendar time);

    /**
     * Fires every minute before work().
     *
     * @param time
     */
    void minutePreWork(Calendar time);

    /**
     * Fires every hour before work().
     *
     * @param time
     */
    void hourPreWork(Calendar time);

    /**
     * Fires at midnight before work().
     *
     * @param time
     */
    void dayPreWork(Calendar time);

    /**
     * Handle tick().
     *
     * @param time
     */
    void tick(Calendar time);

    /**
     * Fires ever second after tick().
     *
     * @param time
     */
    void secondPostWork(Calendar time);

    /**
     * Fires every minute after tick().
     *
     * @param time
     */
    void minutePostWork(Calendar time);

    /**
     * Fires every hour after tick().
     *
     * @param time
     */
    void hourPostWork(Calendar time);

    /**
     * Fires at midnight after tick().
     *
     * @param time
     */
    void dayPostWork(Calendar time);
}
