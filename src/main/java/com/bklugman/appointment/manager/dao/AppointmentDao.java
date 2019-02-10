package com.bklugman.appointment.manager.dao;

import com.bklugman.appointment.manager.model.Appointment;
import com.bklugman.appointment.manager.model.AppointmentStatus;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * A data access object for {@link Appointment}
 */
public class AppointmentDao extends AbstractDAO<Appointment> {
    private static final String START_DATE_KET = "startDate";
    private static final String END_DATE_KEY = "endDate";
    private static final String DATE_QUERY = String.format("FROM %s A WHERE A.appointmentDate >= :%s and A.appointmentDate <= :%s order by A.price asc",
            Appointment.class.getName(), START_DATE_KET, END_DATE_KEY);

    /**
     * Instantiates a DAO with a given session provider.
     *
     * @param sessionFactory a session provider
     */
    public AppointmentDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    /**
     * fetches appointments by their id
     *
     * @param id the id to lookup
     * @return an {@link Appointment} with the provided id if it exists;
     * else, {@link Optional#empty()}
     */
    public Optional<Appointment> getAppointmentById(final long id) {
        return Optional.ofNullable(get(id));
    }

    /**
     * will create and save a new appointment to the database.
     * Note: this method will override the created field to the
     * current time.
     *
     * @param appointment the appointment to create
     * @return the newly created appointment
     */
    public Appointment createAppointment(final Appointment appointment) {
        // this is the date we're inserting the appointment
        appointment.setCreated(new Date());
        return persist(appointment);
    }

    /**
     * deletes the provided appointment.
     *
     * @param appointment the appointment to delete.
     */
    public void deleteAppointment(final Appointment appointment) {
        currentSession().delete(appointment);
    }

    /**
     * fetches all of the appointments between the provided times,
     * and sorts them by price.
     *
     * @param startDateMillis the start date in milliseconds
     * @param endDateMillis   the end date in milliseconds
     * @return the list of all appointments whose appointment date is
     * between {@code startDateMillis} and {@code endDateMillis} inclusive.
     * The appointments will be sorted by price ascending.
     */
    public List<Appointment> getAppointmentsBetweenDatesByPrice(final long startDateMillis, final long endDateMillis) {
        Date startDate = new Date(startDateMillis);
        Date endDate = new Date(endDateMillis);

        return currentSession().createQuery(DATE_QUERY, Appointment.class)
                .setParameter(START_DATE_KET, startDate)
                .setParameter(END_DATE_KEY, endDate)
                .getResultList();
    }

    /**
     * update the status of the provided appointment.
     *
     * @param appointment the appointment to update.
     * @param newStatus   the status to set.
     * @return the newly updated appointment.
     */
    public Appointment updateStatus(final Appointment appointment, final AppointmentStatus newStatus) {
        appointment.setAppointmentStatus(newStatus);
        return persist(appointment);
    }
}
