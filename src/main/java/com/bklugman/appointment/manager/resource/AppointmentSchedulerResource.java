package com.bklugman.appointment.manager.resource;

import com.bklugman.appointment.manager.dao.AppointmentDao;
import com.bklugman.appointment.manager.model.Appointment;
import io.dropwizard.hibernate.UnitOfWork;
import org.eclipse.jetty.http.HttpStatus;

import javax.validation.constraints.NotNull;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Supplier;

/**
 * a resource for creating new appointments
 */
@Path("/v1/appointment-scheduler")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AppointmentSchedulerResource {
    private final AppointmentDao appointmentDao;
    private final Supplier<Integer> randomNumberGenerator;

    public AppointmentSchedulerResource(AppointmentDao appointmentDao, Supplier<Integer> randomNumberGenerator) {
        this.appointmentDao = appointmentDao;
        this.randomNumberGenerator = randomNumberGenerator;
    }

    /**
     * schedules N appointments at a random interval from the provided appointment time.
     *
     * @param numberToCreate the number of appointments to create.
     * @param appointment    the base appointment information (e.g. doctor name, price, etc.)
     * @return the created appointments.
     * @throws javax.ws.rs.BadRequestException if the provided number to create is less than or equal to 0.
     */
    @POST
    @UnitOfWork
    public Response scheduleAppointments(@QueryParam("numberToCreate") @DefaultValue("1") final int numberToCreate,
                                         @NotNull final Appointment appointment) {
        if (numberToCreate <= 0) {
            throw new BadRequestException("Cannot create <= 0 appointments");
        }
        List<Appointment> createdAppointments = createAppointments(numberToCreate, appointment);
        return Response.status(HttpStatus.CREATED_201)
                .entity(createdAppointments)
                .build();
    }

    private List<Appointment> createAppointments(final int numberToCreate, final Appointment appointment) {
        List<Appointment> createdAppointments = new ArrayList<>();
        createdAppointments.add(appointmentDao.createAppointment(appointment));
        for (int i = 1; i < numberToCreate; ++i) {
            Appointment appointmentToCreate = adjustAppointmentDate(appointment);
            createdAppointments.add(appointmentDao.createAppointment(appointmentToCreate));
        }
        return createdAppointments;
    }

    private Appointment adjustAppointmentDate(final Appointment baseAppointment) {
        return new Appointment(
                baseAppointment.getCreated(),
                new Date(baseAppointment.getAppointmentDate().getTime() + randomNumberGenerator.get()),
                baseAppointment.getAppointmentDurationMillis(),
                baseAppointment.getDoctorName(),
                baseAppointment.getAppointmentStatus(),
                baseAppointment.getPrice()
        );
    }
}
