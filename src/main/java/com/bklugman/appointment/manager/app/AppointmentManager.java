package com.bklugman.appointment.manager.app;

import com.bklugman.appointment.manager.config.AppointmentManagerConfig;
import com.bklugman.appointment.manager.inject.Injector;
import com.bklugman.appointment.manager.model.Appointment;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.db.PooledDataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * The main class for the appointment manager application.
 * This class will also add all of the http resources to the application server.
 */
public class AppointmentManager extends Application<AppointmentManagerConfig> {

    /**
     * creates the appointment-manager application and runs it.
     *
     * @param argv the program arguments.
     */
    public static void main(String[] argv) throws Exception {
        new AppointmentManager().run(argv);
    }

    private final HibernateBundle<AppointmentManagerConfig> hibernateBundle = new HibernateBundle<AppointmentManagerConfig>(Appointment.class) {
        @Override
        public PooledDataSourceFactory getDataSourceFactory(AppointmentManagerConfig configuration) {
            return configuration.getDataSourceFactory();
        }
    };

    /**
     * Add any required bundles to the application.
     *
     * @param bootstrap {@inheritDoc}
     */
    @Override
    public void initialize(Bootstrap<AppointmentManagerConfig> bootstrap) {
        bootstrap.addBundle(new MigrationsBundle<AppointmentManagerConfig>() {
            @Override
            public DataSourceFactory getDataSourceFactory(AppointmentManagerConfig configuration) {
                return configuration.getDataSourceFactory();
            }
        });
        bootstrap.addBundle(hibernateBundle);
    }

    /**
     * Adds the required resources to the application's jersey environment.
     *
     * @param configuration {@inheritDoc}
     * @param environment   {@inheritDoc}
     */
    @Override
    public void run(AppointmentManagerConfig configuration, Environment environment) {
        environment.jersey().register(Injector.getAppointmentResource(hibernateBundle.getSessionFactory()));
        environment.jersey().register(Injector.getAppointmentSchedulerResource(hibernateBundle.getSessionFactory()));
    }
}
