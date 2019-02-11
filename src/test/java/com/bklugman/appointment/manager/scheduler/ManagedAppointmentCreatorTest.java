package com.bklugman.appointment.manager.scheduler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

/**
 * a test class for {@link ManagedAppointmentCreator}
 */
class ManagedAppointmentCreatorTest {
    private AppointmentScheduler mockAppointmentScheduler = mock(AppointmentScheduler.class);
    private ScheduledExecutorService mockExecutorService = mock(ScheduledExecutorService.class);
    private final long delay = 30;

    private ManagedAppointmentCreator managedAppointmentCreator;

    @BeforeEach
    void initializeAppointmentCreator() {
        reset(mockExecutorService, mockAppointmentScheduler);
        managedAppointmentCreator = new ManagedAppointmentCreator(mockExecutorService, mockAppointmentScheduler, delay);
    }

    @Test
    void shouldStartExecutorServiceWhenStartIsCalled() {
        managedAppointmentCreator.start();
        verify(mockExecutorService).scheduleWithFixedDelay(mockAppointmentScheduler, 1, delay, TimeUnit.SECONDS);
    }

    @Test
    void shouldShutDownExecutorServiceWhenStopIsCalled() {
        managedAppointmentCreator.stop();
        verify(mockExecutorService).shutdown();
    }
}
