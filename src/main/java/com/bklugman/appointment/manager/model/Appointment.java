package com.bklugman.appointment.manager.model;

import com.google.common.annotations.VisibleForTesting;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

/**
 * the java representation of the APPOINTMENT table.
 */
@Entity
@Table(name = "appointment")
public class Appointment {
    private Long id;
    private Date created;
    private Date appointmentDate;
    private long appointmentDurationMillis;
    private String doctorName;
    private AppointmentStatus appointmentStatus;
    private double price;

    /**
     * convenience constructor for testing
     */
    @VisibleForTesting
    public Appointment(Date created, Date appointmentDate, long appointmentDurationMillis, String doctorName, AppointmentStatus appointmentStatus, double price) {
        this.created = created;
        this.appointmentDate = appointmentDate;
        this.appointmentDurationMillis = appointmentDurationMillis;
        this.doctorName = doctorName;
        this.appointmentStatus = appointmentStatus;
        this.price = price;
    }

    private Appointment() {
        // needed for jackson serialization
    }

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "appointment_date")
    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    @Column(name = "appointment_duration")
    public long getAppointmentDurationMillis() {
        return appointmentDurationMillis;
    }

    public void setAppointmentDurationMillis(long appointmentDurationMillis) {
        this.appointmentDurationMillis = appointmentDurationMillis;
    }

    @Column(name = "name_of_doctor")
    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    @Column(name = "status")
    public AppointmentStatus getAppointmentStatus() {
        return appointmentStatus;
    }

    public void setAppointmentStatus(AppointmentStatus appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }

    @Column(name = "price")
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Appointment that = (Appointment) o;
        return appointmentDurationMillis == that.appointmentDurationMillis &&
                Double.compare(that.price, price) == 0 &&
                Objects.equals(id, that.id) &&
                Objects.equals(created, that.created) &&
                Objects.equals(appointmentDate, that.appointmentDate) &&
                Objects.equals(doctorName, that.doctorName) &&
                appointmentStatus == that.appointmentStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, created, appointmentDate, appointmentDurationMillis, doctorName, appointmentStatus, price);
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", created=" + created +
                ", appointmentDate=" + appointmentDate +
                ", appointmentDurationMillis=" + appointmentDurationMillis +
                ", doctorName='" + doctorName + '\'' +
                ", appointmentStatus=" + appointmentStatus +
                ", price=" + price +
                '}';
    }
}
