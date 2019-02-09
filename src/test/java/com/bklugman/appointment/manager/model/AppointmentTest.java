package com.bklugman.appointment.manager.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * a test class for {@link Appointment}
 */
class AppointmentTest {
    private static final String EXPECTED_RESPONSE = "{\"id\":987236,\"created\":1549744018033,\"appointmentDate\":1549744018033,\"appointmentDurationMillis\":7200000,\"doctorName\":\"dr. foo bar\",\"appointmentStatus\":\"AVAILABLE\",\"price\":124.6}";
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void shouldSerializeDatesAsLongs() throws IOException {
        Appointment appointment = new Appointment(
                new Date(1549744018033L),
                new Date(1549744018033L),
                TimeUnit.HOURS.toMillis(2),
                "dr. foo bar",
                AppointmentStatus.AVAILABLE,
                124.60
        );
        appointment.setId(987236L);
        assertEquals(EXPECTED_RESPONSE, mapper.writeValueAsString(appointment));
        assertEquals(appointment, mapper.readValue(EXPECTED_RESPONSE, Appointment.class));
    }

}