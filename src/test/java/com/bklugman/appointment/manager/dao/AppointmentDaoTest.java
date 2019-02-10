package com.bklugman.appointment.manager.dao;

import com.bklugman.appointment.manager.model.Appointment;
import com.bklugman.appointment.manager.model.AppointmentStatus;
import com.bklugman.appointment.manager.util.RuleToJUnit5Bridge;
import io.dropwizard.testing.junit.DAOTestRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * a test class for {@link AppointmentDao}
 */
class AppointmentDaoTest {

    private final DAOTestRule database = DAOTestRule.newBuilder()
            .addEntityClass(Appointment.class)
            .build();

    private AppointmentDao appointmentDao;

    @BeforeEach
    void setUpDao() {
        appointmentDao = new AppointmentDao(database.getSessionFactory());
    }

    @Test
    void shouldBeAbleToFetchTokenByIdWhenSavedWrapper() throws Throwable {
        RuleToJUnit5Bridge.runTestWithRule(this::shouldBeAbleToFetchTokenByIdWhenSaved, database);
    }

    private void shouldBeAbleToFetchTokenByIdWhenSaved() {
        Appointment newAppointment = getAppointment(50.5);
        Date dateInThePast = new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1));
        newAppointment.setCreated(dateInThePast);
        Appointment createdAppointment = database.inTransaction(() ->
                appointmentDao.createAppointment(newAppointment)
        );
        assertTrue(newAppointment.getCreated().after(dateInThePast));
        Appointment fetchedAppointment = appointmentDao.getAppointmentById(createdAppointment.getId())
                .orElseThrow(() -> new NullPointerException("not expecting null"));

        assertEquals(createdAppointment, fetchedAppointment);
    }

    private Appointment getAppointment(double price) {
        return getAppointment(price, new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(15)));
    }

    private Appointment getAppointment(double price, Date appointmentDate) {
        return new Appointment(
                new Date(),
                appointmentDate,
                TimeUnit.HOURS.toMillis(2),
                "dr. foo bar",
                AppointmentStatus.AVAILABLE,
                price
        );
    }

    @Test
    void shouldReturnEmptyWhenInvalidIdIsPassedWrapper() throws Throwable {
        RuleToJUnit5Bridge.runTestWithRule(this::shouldReturnEmptyWhenInvalidIdIsPassed, database);
    }

    private void shouldReturnEmptyWhenInvalidIdIsPassed() {
        assertEquals(Optional.empty(), appointmentDao.getAppointmentById(-1));
    }

    @Test
    void shouldBeAbleToDeleteAppointmentsWrapper() throws Throwable {
        RuleToJUnit5Bridge.runTestWithRule(this::shouldBeAbleToDeleteAppointments, database);
    }

    private void shouldBeAbleToDeleteAppointments() {
        Appointment newAppointment = getAppointment(125.98);
        Appointment storedAppointment = database.inTransaction(() -> appointmentDao.createAppointment(newAppointment));
        database.inTransaction(() -> appointmentDao.deleteAppointment(storedAppointment));
        assertEquals(Optional.empty(), appointmentDao.getAppointmentById(storedAppointment.getId()));
    }

    @Test
    void shouldBeAbleToUpdateStatusOnAppointmentsWrapper() throws Throwable {
        RuleToJUnit5Bridge.runTestWithRule(this::shouldBeAbleToUpdateStatusOnAppointments, database);
    }

    private void shouldBeAbleToUpdateStatusOnAppointments() {
        Appointment newAppointment = getAppointment(65.0);
        Appointment storedAppointment = database.inTransaction(() -> appointmentDao.createAppointment(newAppointment));
        database.inTransaction(() -> appointmentDao.updateStatus(storedAppointment, AppointmentStatus.BOOKED));
        AppointmentStatus newStatus = appointmentDao.getAppointmentById(storedAppointment.getId())
                .map(Appointment::getAppointmentStatus)
                .orElseThrow(() -> new NullPointerException("not expecting null"));
        assertEquals(AppointmentStatus.BOOKED, newStatus);
    }

    @Test
    void shouldBeAbleToFetchAppointmentsWithinDateWrapper() throws Throwable {
        RuleToJUnit5Bridge.runTestWithRule(this::shouldBeAbleToFetchAppointmentsWithinDate, database);
    }

    private void shouldBeAbleToFetchAppointmentsWithinDate() {
        final long currentTime = System.currentTimeMillis();
        List<Appointment> appointments = Stream.of(20, 10, 30, 40)
                .map(increment ->
                        database.inTransaction(() ->
                                appointmentDao.createAppointment(getAppointment(increment, new Date(currentTime + TimeUnit.SECONDS.toMillis(increment))))
                        )
                ).collect(Collectors.toList());

        assertEquals(Arrays.asList(appointments.get(0), appointments.get(2)), appointmentDao.getAppointmentsBetweenDatesByPrice(currentTime + TimeUnit.SECONDS.toMillis(20), currentTime + TimeUnit.SECONDS.toMillis(30)));
        assertEquals(Arrays.asList(appointments.get(1), appointments.get(0)), appointmentDao.getAppointmentsBetweenDatesByPrice(currentTime + TimeUnit.SECONDS.toMillis(5), currentTime + TimeUnit.SECONDS.toMillis(29)));
        assertEquals(Collections.emptyList(), appointmentDao.getAppointmentsBetweenDatesByPrice(currentTime, currentTime + TimeUnit.SECONDS.toMillis(9)));
    }

}