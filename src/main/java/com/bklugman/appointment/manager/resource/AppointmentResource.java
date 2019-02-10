package com.bklugman.appointment.manager.resource;

import com.bklugman.appointment.manager.dao.AppointmentDao;
import com.bklugman.appointment.manager.model.Appointment;
import com.bklugman.appointment.manager.request.UpdateStatusRequest;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.PATCH;
import org.eclipse.jetty.http.HttpStatus;

import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

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

    /**
     * create a new appointment.
     *
     * @param appointmentToStore the new appointment to create.
     * @return a response with the newly created appointment.
     */
    @POST
    @UnitOfWork
    public Response createAppointment(final Appointment appointmentToStore) {
        Appointment appointment = appointmentDao.createAppointment(appointmentToStore);
        return Response.status(HttpStatus.CREATED_201)
                .entity(appointment)
                .build();
    }

    /**
     * fetches all appointments between [startDate, endDate].
     *
     * @param startDate the start date.
     * @param endDate   the end date.
     * @return a list of appointments between [startDate, endDate].
     */
    @GET
    @UnitOfWork(readOnly = true)
    public List<Appointment> getAppointments(@QueryParam("startDate") final long startDate,
                                             @QueryParam("endDate") final long endDate) {
        return appointmentDao.getAppointmentsBetweenDatesByPrice(startDate, endDate);
    }

    /**
     * fetches an appointment by ID.
     *
     * @param appointmentId the appointment's id.
     * @return the appointment with the corresponding ID.
     * @throws NotFoundException if there is no corresponding appointment.
     */
    @GET
    @Path(("/{id}"))
    @UnitOfWork(readOnly = true)
    public Appointment getAppointment(@PathParam("id") final long appointmentId) {
        return getAppointmentWithId(appointmentId);
    }

    private Appointment getAppointmentWithId(long appointmentId) {
        return appointmentDao.getAppointmentById(appointmentId)
                .orElseThrow(() -> new NotFoundException("No appointment exists with id=" + appointmentId));
    }

    /**
     * updates the status of an appointment.
     *
     * @param appointmentId the id of the appointment to update.
     * @param request       the value to update the status to.
     * @throws NotFoundException if there is no corresponding appointment.
     */
    @PATCH
    @Path(("/{id}"))
    @UnitOfWork
    public void updateAppointmentStatus(@PathParam("id") final long appointmentId, @NotNull final UpdateStatusRequest request) {
        Appointment appointment = getAppointmentWithId(appointmentId);
        appointmentDao.updateStatus(appointment, request.getStatus());

    }

    /**
     * delete an appointment.
     *
     * @param appointmentId the id of the appointment to delete.
     * @throws NotFoundException if there is no corresponding appointment.
     */
    @DELETE
    @Path(("/{id}"))
    @UnitOfWork
    public void deleteAppointment(@PathParam("id") final long appointmentId) {
        Appointment appointment = getAppointmentWithId(appointmentId);
        appointmentDao.deleteAppointment(appointment);
    }

}
