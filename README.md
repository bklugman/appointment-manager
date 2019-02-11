# appointment-manager
An application to book and manage appointments

## Running Locally
### Pre-requisites
Before running locally the following dependencies must be installed

- [Docker](https://www.docker.com/products/docker-desktop)

### Running
Make sure that nothing is running on port 8080, or update 
the local port that is exposed in the [docker-compose.yaml](https://github.com/bklugman/appointment-manager/blob/master/docker-compose.yaml#L20)
file.

To run the application:

run `$: docker-compose up` in the appointment-manager directory
  - this will start the application and all of its dependencies
  - Note: there may be some error logs on the first run, but docker-compose will restart the app and resolve the issue
    - this happens because the application can start before the database is accepting connections
  
When you see the following logs, the application has finished launching:
```
appointment-manager-app | INFO  [2019-02-10 04:27:07,823] io.dropwizard.jersey.DropwizardResourceConfig: The following paths were found for the configured resources:
appointment-manager-app |
appointment-manager-app |     GET     /v1/appointments (com.bklugman.appointment.manager.resource.AppointmentResource)
appointment-manager-app |     POST    /v1/appointments (com.bklugman.appointment.manager.resource.AppointmentResource)
appointment-manager-app |     DELETE  /v1/appointments/{id} (com.bklugman.appointment.manager.resource.AppointmentResource)
appointment-manager-app |     GET     /v1/appointments/{id} (com.bklugman.appointment.manager.resource.AppointmentResource)
appointment-manager-app |     PATCH   /v1/appointments/{id} (com.bklugman.appointment.manager.resource.AppointmentResource)
```

### Sample Curls
These sample curls all assume that the application is running on port 8080

Create an appointment:
```
curl http://localhost:8080/v1/appointments -d '{"appointmentDate":1549744018033,"appointmentDurationMillis":7200000,"doctorName":"dr. foo bar","appointmentStatus":"AVAILABLE","price":124.6}' -H "Content-type: application/json"
```
Get an appointment: 
```
curl 'http://localhost:8080/v1/appointments/12'
```
Delete an appointment:
```
curl -X DELETE 'http://localhost:8080/v1/appointments/12'
```
Update the status of an appointment:
```
curl -X PATCH 'http://localhost:8080/v1/appointments/12' -d '{"status": "BOOKED"}' -H "Content-type: application/json" -v 
```
Retrieve appointments between a date range:
```
curl 'http://localhost:8080/v1/appointments?startDate=1549644018032&endDate=1549844018034'
```

### Configuring Background Appointment Creation 
This application has a background service that will create appointments
at a random interval. Below are the different configuration options that can be changed

- [delay](https://github.com/bklugman/appointment-manager/blob/master/config.yml#L24)  - controls the frequency at which random appointments are created (larger delays means fewer appointments will be 
  created and vice-versa)
- [price](https://github.com/bklugman/appointment-manager/blob/master/config.yml#L25) - the price of the random appointments created
- [duration](https://github.com/bklugman/appointment-manager/blob/master/config.yml#L26) - the duration of the random appointments in millis
- [doctor](https://github.com/bklugman/appointment-manager/blob/master/config.yml#L27) - the name of the doctor for the random appointments  
  
### Running Unit Tests
run `$: ./gradlew test` in the appointment-manager directory

## Technologies
The following technologies were used for this application:

- [Java 8](https://openjdk.java.net/projects/jdk8/)
- [MySql](https://dev.mysql.com/doc/)
- [Dropwizard](https://www.dropwizard.io/1.3.8/docs/index.html)
- [Liquidbase](http://www.liquibase.org/) to manage database schema
- [Hibernate](http://hibernate.org/orm/) as an ORM
