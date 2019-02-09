package com.bklugman.appointment.manager.app;

import com.bklugman.appointment.manager.config.AppointmentManagerConfig;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class AppointmentManager extends Application<AppointmentManagerConfig> {

    @Override
    public void initialize(Bootstrap<AppointmentManagerConfig> bootstrap) {
        // nothing yet
    }

    @Override
    public void run(AppointmentManagerConfig configuration, Environment environment) throws Exception {

    }
}
