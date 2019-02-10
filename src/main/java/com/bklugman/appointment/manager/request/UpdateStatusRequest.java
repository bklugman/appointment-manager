package com.bklugman.appointment.manager.request;

import com.bklugman.appointment.manager.model.AppointmentStatus;
import com.google.common.annotations.VisibleForTesting;

import javax.validation.constraints.NotNull;

/**
 * a representation of the request made to update an appointment's
 * status.
 */
public class UpdateStatusRequest {
    @NotNull
    private AppointmentStatus status;

    /**
     * a convenience constructor for testing.
     *
     * @param status the status to set
     */
    @VisibleForTesting
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
