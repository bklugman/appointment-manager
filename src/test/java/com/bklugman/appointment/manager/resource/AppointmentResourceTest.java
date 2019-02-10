package com.bklugman.appointment.manager.resource;

import com.bklugman.appointment.manager.dao.AppointmentDao;
import com.bklugman.appointment.manager.model.Appointment;
import com.bklugman.appointment.manager.model.AppointmentStatus;
import com.bklugman.appointment.manager.request.UpdateStatusRequest;
import com.bklugman.appointment.manager.util.RuleToJUnit5Bridge;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * a test class for {@link AppointmentResource}
 */
class AppointmentResourceTest {

    private static final AppointmentDao MOCK_DAO = mock(AppointmentDao.class);

    private final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new AppointmentResource(MOCK_DAO))
            .build();

    @BeforeEach
    void resetDao() {
        reset(MOCK_DAO);
    }

    @Test
    void createAppointment() throws Throwable {
        RuleToJUnit5Bridge.runTestWithRule(this::shouldReturn201AndNewAppointmentWhenPostIsCalled, resources);
    }

    private void shouldReturn201AndNewAppointmentWhenPostIsCalled() {
        Appointment newAppointment = getAppointmentWithId(null);
        newAppointment.setCreated(null);
        Appointment appointmentToReturn = getAppointmentWithId(9823764L);
        when(MOCK_DAO.createAppointment(newAppointment)).thenReturn(appointmentToReturn);
        Response response = resources.target("v1/appointments")
                .request()
                .post(Entity.entity(newAppointment, MediaType.APPLICATION_JSON));
        assertEquals(HttpStatus.CREATED_201, response.getStatus());
        appointmentToReturn.setCreated(null);
        assertEquals(appointmentToReturn, response.readEntity(Appointment.class));
    }

    private Appointment getAppointmentWithId(Long id) {
        Appointment newAppointment = new Appointment(
                new Date(),
                new Date(),
                TimeUnit.HOURS.toMillis(2),
                "dr. foo bar",
                AppointmentStatus.AVAILABLE,
                124.60
        );
        newAppointment.setId(id);
        return newAppointment;
    }

    @Test
    void getAppointment() throws Throwable {
        RuleToJUnit5Bridge.runTestWithRule(this::shouldReturnAppointmentForId, resources);
        RuleToJUnit5Bridge.runTestWithRule(this::shouldReturn404WhenAppointmentCannotBeFound, resources);
    }

    private void shouldReturnAppointmentForId() {
        long id = 8937264;
        Appointment appointment = mockReturnAppointmentWithId(id);
        Response response = resources.target("v1/appointments/" + id)
                .request()
                .get();
        assertEquals(HttpStatus.OK_200, response.getStatus());
        appointment.setCreated(null);
        assertEquals(appointment, response.readEntity(Appointment.class));
    }

    private void shouldReturn404WhenAppointmentCannotBeFound() {
        Response response = resources.target("v1/appointments/" + 239846)
                .request()
                .get();
        assertEquals(HttpStatus.NOT_FOUND_404, response.getStatus());
    }

    @Test
    void getAppointments() throws Throwable {
        RuleToJUnit5Bridge.runTestWithRule(this::shouldReturnAllAppointmentsWithinRange, resources);
    }

    private void shouldReturnAllAppointmentsWithinRange() {
        long start = System.currentTimeMillis();
        long end = System.currentTimeMillis() + 9827364;
        List<Appointment> appointments = Arrays.asList(getAppointmentWithId(19078L), getAppointmentWithId(9286374L));
        when(MOCK_DAO.getAppointmentsBetweenDatesByPrice(start, end)).thenReturn(appointments);
        Response response = resources.target("v1/appointments")
                .queryParam("startDate", start)
                .queryParam("endDate", end)
                .request()
                .get();
        assertEquals(HttpStatus.OK_200, response.getStatus());
        String expected = getJsonText(appointments);
        assertEquals(expected, response.readEntity(String.class));
    }

    private String getJsonText(List<Appointment> appointments) {
        try {
            return new ObjectMapper().writeValueAsString(appointments);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }

    @Test
    void updateAppointmentStatus() throws Throwable {
        RuleToJUnit5Bridge.runTestWithRule(this::shouldReturn204WhenUpdateIsAccepted, resources);
        RuleToJUnit5Bridge.runTestWithRule(this::shouldReturn404WhenUpdateCalledWithBadId, resources);

    }

    private void shouldReturn204WhenUpdateIsAccepted() {
        long id = 968724;
        Appointment appointment = mockReturnAppointmentWithId(id);
        UpdateStatusRequest request = new UpdateStatusRequest(AppointmentStatus.AVAILABLE);
        Response response = resources.target("v1/appointments/" + id)
                .request()
                .method("PATCH", Entity.entity(request, MediaType.APPLICATION_JSON));
        assertEquals(HttpStatus.NO_CONTENT_204, response.getStatus());
        verify(MOCK_DAO).updateStatus(appointment, request.getStatus());

    }

    private Appointment mockReturnAppointmentWithId(long id) {
        Appointment appointment = getAppointmentWithId(id);
        when(MOCK_DAO.getAppointmentById(id)).thenReturn(Optional.of(appointment));
        return appointment;
    }

    private void shouldReturn404WhenUpdateCalledWithBadId() {
        long id = 36245234;
        UpdateStatusRequest request = new UpdateStatusRequest(AppointmentStatus.AVAILABLE);
        Response response = resources.target("v1/appointments/" + id)
                .request()
                .method("PATCH", Entity.entity(request, MediaType.APPLICATION_JSON));
        assertEquals(HttpStatus.NOT_FOUND_404, response.getStatus());
    }

    @Test
    void deleteAppointment() throws Throwable {
        RuleToJUnit5Bridge.runTestWithRule(this::shouldDeleteAppointmentWhenDeleteIsCalled, resources);
        RuleToJUnit5Bridge.runTestWithRule(this::shouldReturn404WhenUpdateCalledWithBadId, resources);
    }

    private void shouldDeleteAppointmentWhenDeleteIsCalled() {
        long id = 298376;
        Appointment appointment = mockReturnAppointmentWithId(id);
        Response response = resources.target("v1/appointments/" + id)
                .request()
                .delete();
        assertEquals(HttpStatus.NO_CONTENT_204, response.getStatus());
        verify(MOCK_DAO).deleteAppointment(appointment);
    }

    private void shouldReturn404WhenDeleteCalledWithBadId() {
        Response response = resources.target("v1/appointments/" + 97861423)
                .request()
                .delete();
        assertEquals(HttpStatus.NOT_FOUND_404, response.getStatus());
    }
}
