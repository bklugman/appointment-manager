package com.bklugman.appointment.manager.scheduler;

import com.bklugman.appointment.manager.dao.AppointmentDao;
import com.bklugman.appointment.manager.model.Appointment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * a test class for {@link AppointmentScheduler}
 */
class AppointmentSchedulerTest {

    private AppointmentDao mockDao = mock(AppointmentDao.class);
    private Random mockRandom = mock(Random.class);
    private AppointmentScheduler scheduler;

    @BeforeEach
    void initializeScheduler() {
        long duration = 10000;
        double price = 50.0;
        String doctorsName = "Dr. foo";
        scheduler = new AppointmentScheduler(mockDao, mockRandom, doctorsName, price, duration);
    }

    @Test
    void shouldDoNothingWhenRandomReturnsLessThan5() {
        when(mockRandom.nextInt(10)).thenReturn(4);
        scheduler.run();

        verify(mockDao, never()).createAppointment(any());
    }

    @Test
    void shouldCreateAppointmentWhenRandomReturns5OrGreater() {
        when(mockRandom.nextInt(10)).thenReturn(5);

        scheduler.run();
        verify(mockDao).createAppointment(any(Appointment.class));
    }
}
