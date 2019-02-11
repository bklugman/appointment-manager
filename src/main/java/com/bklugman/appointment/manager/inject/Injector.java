package com.bklugman.appointment.manager.inject;

import com.bklugman.appointment.manager.config.AppointmentManagerConfig;
import com.bklugman.appointment.manager.dao.AppointmentDao;
import com.bklugman.appointment.manager.resource.AppointmentResource;
import com.bklugman.appointment.manager.scheduler.AppointmentScheduler;
import com.bklugman.appointment.manager.scheduler.ManagedAppointmentCreator;
import io.dropwizard.hibernate.UnitOfWorkAwareProxyFactory;
import org.hibernate.SessionFactory;

import java.util.Random;
import java.util.concurrent.Executors;

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
        return new AppointmentResource(getAppointmentDao(sessionFactory));
    }

    private static AppointmentDao getAppointmentDao(SessionFactory sessionFactory) {
        return new AppointmentDao(sessionFactory);
    }

    /**
     * get an instance of {@link ManagedAppointmentCreator}.
     *
     * @param sessionFactory the hibernate session factory.
     * @param config         the applications config.
     * @return an instance of {@link ManagedAppointmentCreator}.
     */
    public static ManagedAppointmentCreator managedAppointmentCreator(final SessionFactory sessionFactory, final AppointmentManagerConfig config) {
        AppointmentScheduler scheduler = getAppointmentScheduler(sessionFactory, config);
        return new ManagedAppointmentCreator(Executors.newScheduledThreadPool(2), scheduler, config.getAppointmentCreation().getDelay());
    }

    private static AppointmentScheduler getAppointmentScheduler(SessionFactory sessionFactory, AppointmentManagerConfig config) {
        AppointmentDao appointmentDao = getAppointmentDao(sessionFactory);

        return new UnitOfWorkAwareProxyFactory("background - appointment creation", sessionFactory)
                .create(AppointmentScheduler.class, new Class[]{AppointmentDao.class, Random.class, String.class, double.class, long.class},
                        getAppointmentSchedulerConstructorArguments(config, appointmentDao));
    }

    private static Object[] getAppointmentSchedulerConstructorArguments(AppointmentManagerConfig config, AppointmentDao appointmentDao) {
        return new Object[]{appointmentDao, new Random(), config.getAppointmentCreation().getDoctorsName(),
                config.getAppointmentCreation().getPrice(), config.getAppointmentCreation().getDuration()};
    }

    private Injector() {
        // class should not be instantiated
    }
}
