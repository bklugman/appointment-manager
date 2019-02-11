package com.bklugman.appointment.manager.scheduler;

import io.dropwizard.lifecycle.Managed;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * runs {@link AppointmentScheduler} at a fixed schedule
 */
public class ManagedAppointmentCreator implements Managed {
    private final ScheduledExecutorService scheduledExecutorService;
    private final AppointmentScheduler appointmentScheduler;
    private final long delay;

    /**
     * @param scheduledExecutorService the executor service to use.
     * @param appointmentScheduler     the service to create appointments.
     * @param delay                    the delay between runs in seconds.
     */
    public ManagedAppointmentCreator(ScheduledExecutorService scheduledExecutorService, AppointmentScheduler appointmentScheduler, long delay) {
        this.scheduledExecutorService = scheduledExecutorService;
        this.appointmentScheduler = appointmentScheduler;
        this.delay = delay;
    }

    /**
     * runs the appointment scheduler with a fixed schedule based on the provided delay
     */
    @Override
    public void start() {
        scheduledExecutorService.scheduleWithFixedDelay(appointmentScheduler, 1, delay, TimeUnit.SECONDS);
    }

    /**
     * shuds down the executor service
     */
    @Override
    public void stop() {
        scheduledExecutorService.shutdown();
    }
}
