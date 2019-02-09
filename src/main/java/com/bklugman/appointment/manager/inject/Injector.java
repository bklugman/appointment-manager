package com.bklugman.appointment.manager.inject;

import com.bklugman.appointment.manager.config.AppointmentManagerConfig;
import com.bklugman.appointment.manager.resource.AppointmentResource;

/**
 * a class for managing the dependency injection for the application
 */
public class Injector {

    public static ApplicationScope createApplicationScope(final AppointmentManagerConfig config) {
        return new ApplicationScope();
    }

    public static AppointmentResource getAppointmentResource(final ApplicationScope applicationScope) {
        return new AppointmentResource();
    }

    private Injector() {
        // class should not be instantiated
    }
}
