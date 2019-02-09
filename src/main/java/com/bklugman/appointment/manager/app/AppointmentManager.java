package com.bklugman.appointment.manager.app;

import com.bklugman.appointment.manager.config.AppointmentManagerConfig;
import com.bklugman.appointment.manager.inject.ApplicationScope;
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
 * the application class for the appointment manager.
 * This class will add all of the REST resources to the application server.
 */
public class AppointmentManager extends Application<AppointmentManagerConfig> {

    public static void main(String[] argv) throws Exception {
        new AppointmentManager().run(argv);
    }

    private final HibernateBundle<AppointmentManagerConfig> hibernateBundle = new HibernateBundle<AppointmentManagerConfig>(Appointment.class) {
        @Override
        public PooledDataSourceFactory getDataSourceFactory(AppointmentManagerConfig configuration) {
            return configuration.getDataSourceFactory();
        }
    };

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

    @Override
    public void run(AppointmentManagerConfig configuration, Environment environment) {
        final ApplicationScope applicationScope = Injector.createApplicationScope(hibernateBundle.getSessionFactory());

        environment.jersey().register(Injector.getAppointmentResource(applicationScope));
    }
}
