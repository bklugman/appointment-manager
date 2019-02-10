package com.bklugman.appointment.manager.resource;

import com.bklugman.appointment.manager.dao.AppointmentDao;
import com.bklugman.appointment.manager.model.Appointment;
import com.bklugman.appointment.manager.model.AppointmentStatus;
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
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * a test class for {@link AppointmentSchedulerResource}
 */
class AppointmentSchedulerResourceTest {
    private static final AppointmentDao MOCK_DAO = mock(AppointmentDao.class);
    private static final Supplier<Long> MOCK_RANDOM_NUMBER_GENERATOR = mock(Supplier.class);
    private static final long RANDOM_NUMBER = 100;
    private int callCount;

    private final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new AppointmentSchedulerResource(MOCK_DAO, MOCK_RANDOM_NUMBER_GENERATOR))
            .build();

    @BeforeEach
    void resetDao() {
        when(MOCK_DAO.createAppointment(any())).thenAnswer(invoke -> invoke.getArgument(0));
        callCount = 1;
        when(MOCK_RANDOM_NUMBER_GENERATOR.get()).thenAnswer(invoke -> RANDOM_NUMBER * callCount++);
    }

    @Test
    void createAppointments() throws Throwable {
        RuleToJUnit5Bridge.runTestWithRule(this::shouldCreateNAppointments, resources);
    }

    private void shouldCreateNAppointments() {
        Appointment baseAppointment = getAppointment();
        List<Appointment> appointments = getAppointments(baseAppointment);
        Response response = resources.target("v1/appointment-scheduler")
                .queryParam("numberToCreate", 25)
                .request()
                .post(Entity.entity(baseAppointment, MediaType.APPLICATION_JSON_TYPE));
        assertEquals(HttpStatus.CREATED_201, response.getStatus());
        String expected = getJsonText(appointments);
        assertEquals(expected, response.readEntity(String.class));
    }

    private Appointment getAppointment() {
        return new Appointment(
                new Date(),
                new Date(),
                TimeUnit.HOURS.toMillis(2),
                "dr. foo bar",
                AppointmentStatus.AVAILABLE,
                124.60
        );
    }

    private List<Appointment> getAppointments(final Appointment baseAppointment) {
        return IntStream.range(0, 25)
                .mapToObj(offset ->
                        new Appointment(
                                new Date(),
                                new Date(baseAppointment.getAppointmentDate().getTime() + offset * RANDOM_NUMBER),
                                TimeUnit.HOURS.toMillis(2),
                                "dr. foo bar",
                                AppointmentStatus.AVAILABLE,
                                124.60
                        )).collect(Collectors.toList());
    }

    private String getJsonText(final List<Appointment> appointments) {
        try {
            return new ObjectMapper().writeValueAsString(appointments);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }

    @Test
    void clientErrorHandling() throws Throwable {
        RuleToJUnit5Bridge.runTestWithRule(this::shouldThrowWhenNegativeNumberThrown, resources);
    }

    private void shouldThrowWhenNegativeNumberThrown() {
        Appointment appointment = getAppointment();
        Response response = resources.target("v1/appointment-scheduler")
                .queryParam("numberToCreate", 0)
                .request()
                .post(Entity.entity(appointment, MediaType.APPLICATION_JSON_TYPE));
        assertEquals(HttpStatus.BAD_REQUEST_400, response.getStatus());
    }
}
