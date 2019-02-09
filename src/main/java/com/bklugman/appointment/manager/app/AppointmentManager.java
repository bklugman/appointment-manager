package com.bklugman.appointment.manager.app;

import com.bklugman.appointment.manager.config.AppointmentManagerConfig;
import com.bklugman.appointment.manager.inject.ApplicationScope;
import com.bklugman.appointment.manager.inject.Injector;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
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

    @Override
    public void initialize(Bootstrap<AppointmentManagerConfig> bootstrap) {
        bootstrap.addBundle(new MigrationsBundle<AppointmentManagerConfig>() {
            @Override
            public DataSourceFactory getDataSourceFactory(AppointmentManagerConfig configuration) {
                return configuration.getDataSourceFactory();
            }
        });
    }

    @Override
    public void run(AppointmentManagerConfig configuration, Environment environment) throws Exception {
        final ApplicationScope applicationScope = Injector.createApplicationScope(configuration);

        environment.jersey().register(Injector.getAppointmentResource(applicationScope));
    }
}
