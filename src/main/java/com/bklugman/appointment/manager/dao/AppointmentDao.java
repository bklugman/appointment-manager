package com.bklugman.appointment.manager.dao;

import com.bklugman.appointment.manager.model.Appointment;
import com.bklugman.appointment.manager.model.AppointmentStatus;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public class AppointmentDao extends AbstractDAO<Appointment> {
    private static final String START_DATE_KET = "startDate";
    private static final String END_DATE_KEY = "endDate";
    private static final String DATE_QUERY = String.format("FROM %s A WHERE A.appointmentDate >= :%s and A.appointmentDate <= :%s",
            Appointment.class.getName(), START_DATE_KET, END_DATE_KEY);

    /**
     * Creates a new DAO with a given session provider.
     *
     * @param sessionFactory a session provider
     */
    public AppointmentDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Optional<Appointment> getAppointmentById(final long id) {
        return Optional.ofNullable(get(id));
    }

    public Appointment createAppointment(final Appointment appointment) {
        return persist(appointment);
    }

    public void deleteAppointment(final Appointment appointment) {
        currentSession().delete(appointment);
    }

    public List<Appointment> getAppointmentsBetweenDates(final long startDateMillis, final long endDateMillis) {
        Date startDate = new Date(startDateMillis);
        Date endDate = new Date(endDateMillis);

        return currentSession().createQuery(DATE_QUERY, Appointment.class)
                .setParameter(START_DATE_KET, startDate)
                .setParameter(END_DATE_KEY, endDate)
                .getResultList();
    }

    public Appointment updateStatus(final Appointment appointment, final AppointmentStatus newStatus) {
        appointment.setAppointmentStatus(newStatus);
        return persist(appointment);
    }
}
