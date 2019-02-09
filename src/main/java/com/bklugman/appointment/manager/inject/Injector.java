package com.bklugman.appointment.manager.inject;

import com.bklugman.appointment.manager.dao.AppointmentDao;
import com.bklugman.appointment.manager.resource.AppointmentResource;
import org.hibernate.SessionFactory;

/**
 * a class for managing the dependency injection for the application
 */
public class Injector {

    public static ApplicationScope createApplicationScope(final SessionFactory sessionFactory) {
        return new ApplicationScope(sessionFactory);
    }

    public static AppointmentResource getAppointmentResource(final ApplicationScope applicationScope) {
        return new AppointmentResource(new AppointmentDao(applicationScope.getSessionFactory()));
    }

    private Injector() {
        // class should not be instantiated
    }
}
