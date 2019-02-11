package com.bklugman.appointment.manager.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.concurrent.TimeUnit;

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

    @Valid
    @NotNull
    @JsonProperty("appointments")
    private BackgroundAppointmentCreation appointmentCreation = new BackgroundAppointmentCreation();

    public BackgroundAppointmentCreation getAppointmentCreation() {
        return appointmentCreation;
    }

    public void setAppointmentCreation(BackgroundAppointmentCreation appointmentCreation) {
        this.appointmentCreation = appointmentCreation;
    }

    public class BackgroundAppointmentCreation {

        @JsonProperty("delay")
        private long delay = 30;

        public long getDelay() {
            return delay;
        }

        public void setDelay(long delay) {
            this.delay = delay;
        }

        @JsonProperty("price")
        private double price = 50.0;

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        @JsonProperty("duration")
        private long duration = TimeUnit.HOURS.toMillis(1);

        public long getDuration() {
            return duration;
        }

        public void setDuration(long duration) {
            this.duration = duration;
        }

        @Valid
        @NotEmpty
        @JsonProperty("doctor")
        private String doctorsName = "Dr. Hello World";

        public String getDoctorsName() {
            return doctorsName;
        }

        public void setDoctorsName(String doctorsName) {
            this.doctorsName = doctorsName;
        }
    }
}
