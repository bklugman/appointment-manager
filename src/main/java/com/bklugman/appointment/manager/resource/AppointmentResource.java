package com.bklugman.appointment.manager.resource;

import com.bklugman.appointment.manager.dao.AppointmentDao;
import com.bklugman.appointment.manager.model.Appointment;
import com.bklugman.appointment.manager.request.UpdateStatusRequest;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.PATCH;
import org.eclipse.jetty.http.HttpStatus;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.function.Supplier;

/**
 * a class for managing the appointment resources
 */
@Path("v1/appointments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AppointmentResource {

    private final AppointmentDao appointmentDao;

    public AppointmentResource(final AppointmentDao appointmentDao) {
        this.appointmentDao = appointmentDao;
    }

    @POST
    @UnitOfWork
    public Response createAppointment(final Appointment appointmentToStore) {
        Appointment appointment = appointmentDao.createAppointment(appointmentToStore);
        return Response.status(HttpStatus.CREATED_201)
                .entity(appointment)
                .build();
    }

    @GET
    @UnitOfWork(readOnly = true)
    public List<Appointment> getAppointments(@QueryParam("startDate") final long startDate,
                                             @QueryParam("endDate") final long endDate) {
        return appointmentDao.getAppointmentsBetweenDates(startDate, endDate);
    }

    @GET
    @Path(("/{id}"))
    @UnitOfWork(readOnly = true)
    public Appointment getAppointment(@PathParam("id") final long appointmentId) {

        return getAppointmentWithId(appointmentId);
    }

    private Appointment getAppointmentWithId(@PathParam("id") long appointmentId) {
        return appointmentDao.getAppointmentById(appointmentId)
                .orElseThrow(getNotFoundExceptionSupplier(appointmentId));
    }

    private Supplier<NotFoundException> getNotFoundExceptionSupplier(@PathParam("id") long appointmentId) {
        return () -> new NotFoundException("No appointment exists with id=" + appointmentId);
    }

    @PATCH
    @Path(("/{id}"))
    @UnitOfWork
    public void updateAppointmentStatus(@PathParam("id") final long appointmentId, @NotNull final UpdateStatusRequest request) {
        Appointment appointment = getAppointmentWithId(appointmentId);
        appointmentDao.updateStatus(appointment, request.getStatus());

    }

    @DELETE
    @Path(("/{id}"))
    @UnitOfWork
    public void deleteAppointment(@PathParam("id") final long appointmentId) {
        Appointment appointment = getAppointmentWithId(appointmentId);
        appointmentDao.deleteAppointment(appointment);
    }

}
