package com.bklugman.appointment.manager.inject;

import com.bklugman.appointment.manager.dao.AppointmentDao;
import com.bklugman.appointment.manager.resource.AppointmentResource;
import org.hibernate.SessionFactory;

/**
 * a class for managing the dependency injection for the application.
 */
public class Injector {
    /**
     * get an instance of {@link AppointmentResource}.
     *
     * @param sessionFactory the hibernate session factory.
     * @return an instance of {@link AppointmentResource}.
     */
    public static AppointmentResource getAppointmentResource(final SessionFactory sessionFactory) {
        return new AppointmentResource(new AppointmentDao(sessionFactory));
    }

    private Injector() {
        // class should not be instantiated
    }
}
