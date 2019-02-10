package com.bklugman.appointment.manager.inject;

import com.bklugman.appointment.manager.dao.AppointmentDao;
import com.bklugman.appointment.manager.resource.AppointmentResource;
import com.bklugman.appointment.manager.resource.AppointmentSchedulerResource;
import org.hibernate.SessionFactory;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * a class for managing the dependency injection for the application.
 */
public class Injector {
    private static final Random RANDOM = new Random();

    /**
     * get an instance of {@link AppointmentResource}.
     *
     * @param sessionFactory the hibernate session factory.
     * @return an instance of {@link AppointmentResource}.
     */
    public static AppointmentResource getAppointmentResource(final SessionFactory sessionFactory) {
        return new AppointmentResource(getAppointmentDao(sessionFactory));
    }

    private static AppointmentDao getAppointmentDao(SessionFactory sessionFactory) {
        return new AppointmentDao(sessionFactory);
    }

    /**
     * get an instance of {@link AppointmentSchedulerResource}
     *
     * @param sessionFactory the hibernate session factory.
     * @return an instance of {@link AppointmentSchedulerResource}
     */
    public static AppointmentSchedulerResource getAppointmentSchedulerResource(final SessionFactory sessionFactory) {
        AppointmentDao appointmentDao = getAppointmentDao(sessionFactory);
        return new AppointmentSchedulerResource(appointmentDao, () -> (int) TimeUnit.HOURS.toMillis(1));
    }

    private Injector() {
        // class should not be instantiated
    }
}
