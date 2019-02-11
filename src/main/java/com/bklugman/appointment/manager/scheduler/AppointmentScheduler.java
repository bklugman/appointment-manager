package com.bklugman.appointment.manager.scheduler;

import com.bklugman.appointment.manager.dao.AppointmentDao;
import com.bklugman.appointment.manager.model.Appointment;
import com.bklugman.appointment.manager.model.AppointmentStatus;
import io.dropwizard.hibernate.UnitOfWork;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * a class for randomly creating appointments at random times
 */
public class AppointmentScheduler implements Runnable {
    private final AppointmentDao appointmentDao;
    private final Random random;
    private final String doctorsName;
    private final double price;
    private final long duration;

    /**
     * @param appointmentDao the appointment dao to use.
     * @param random         the random number generator to use.
     * @param doctorsName    the doctors name for each appointment created.
     * @param price          the price of each appointment created.
     * @param duration       the duration of each appointment created.
     */
    public AppointmentScheduler(final AppointmentDao appointmentDao, Random random, String doctorsName, double price, long duration) {
        this.appointmentDao = appointmentDao;
        this.random = random;
        this.doctorsName = doctorsName;
        this.price = price;
        this.duration = duration;
    }

    /**
     * this method has a 50% chance of creating an appointment when run.
     * When it creates an appointment, the appointment date will be random.
     */
    @Override
    @UnitOfWork
    public void run() {
        if (random.nextInt(10) >= 5) {
            createAppointment();
        }
    }

    private void createAppointment() {
        Appointment appointment = getAppointment();
        appointmentDao.createAppointment(appointment);
    }

    private Appointment getAppointment() {
        int hoursOffsetBound = (int) TimeUnit.HOURS.toMillis(1);
        Date appointmentDate = new Date(System.currentTimeMillis() + random.nextInt(hoursOffsetBound));
        return new Appointment(
                null,
                appointmentDate,
                duration,
                doctorsName,
                AppointmentStatus.AVAILABLE,
                price
        );
    }
}
