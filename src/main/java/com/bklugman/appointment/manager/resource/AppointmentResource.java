package com.bklugman.appointment.manager.resource;

import io.dropwizard.jersey.PATCH;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * a class for managing the appointment resources
 */
@Path("v1/appointments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AppointmentResource {

    @POST
    public Response createAppointment() {
        return null; // TODO
    }

    @GET
    public Response getAppointments(@QueryParam("startDate") final long startDate
            , @QueryParam("endDate") final long endDate) {
        return null; // TODO
    }

    @GET
    @Path(("/{id}"))
    public Response getAppointment(@PathParam("id") final String appointmentId) {
        return null; // TODO
    }

    @PATCH
    @Path(("/{id}"))
    public Response updateAppointmentStatus(@PathParam("id") final String appointmentId) {
        return null; // TODO
    }

    @DELETE
    @Path(("/{id}"))
    public Response deleteAppointment(@PathParam("id") final String appointmentId) {
        return null; // TODO
    }

}
