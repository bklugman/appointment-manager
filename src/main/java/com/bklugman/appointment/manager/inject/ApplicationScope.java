package com.bklugman.appointment.manager.inject;

import org.hibernate.SessionFactory;

public class ApplicationScope {
    private final SessionFactory sessionFactory;

    ApplicationScope(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
