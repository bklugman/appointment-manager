package com.bklugman.appointment.manager.dao;

import com.bklugman.appointment.manager.model.Appointment;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.util.Optional;

public class AppointmentDao extends AbstractDAO<Appointment> {
    /**
     * Creates a new DAO with a given session provider.
     *
     * @param sessionFactory a session provider
     */
    public AppointmentDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Optional<Appointment> getApointmentById(final long id) {
        return Optional.empty();
    }
}
