package com.bklugman.appointment.manager.request;

import com.bklugman.appointment.manager.model.AppointmentStatus;

public class UpdateStatusRequest {
    private AppointmentStatus status;

    public UpdateStatusRequest(final AppointmentStatus status) {
        this.status = status;
    }

    private UpdateStatusRequest() {
        // needed for jackson serialization
    }

    public AppointmentStatus getStatus() {
        return status;
    }
}
