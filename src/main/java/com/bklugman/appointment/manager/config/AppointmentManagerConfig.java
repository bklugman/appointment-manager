package com.bklugman.appointment.manager.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * A java representation of the applications configuration.
 */
public class AppointmentManagerConfig extends Configuration {

    /**
     * The connection pool to the database.
     */
    @Valid
    @NotNull
    private DataSourceFactory database = new DataSourceFactory();

    @JsonProperty("database")
    public DataSourceFactory getDataSourceFactory() {
        return database;
    }

    public void setDataSourceFactory(final DataSourceFactory database) {
        this.database = database;
    }
}
