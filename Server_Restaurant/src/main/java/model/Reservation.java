package model;
import java.time.LocalDateTime;

public class Reservation {
    private int reservationId;
    private int clientId;
    private int employeeId;
    private LocalDateTime dateTime;
    private int numberOfPeople;
    private String status;

    public Reservation() {}

    public Reservation(int reservationId, int clientId, int employeeId, LocalDateTime dateTime, int numberOfPeople, String status) {
        this.reservationId = reservationId;
        this.clientId = clientId;
        this.employeeId = employeeId;
        this.dateTime = dateTime;
        this.numberOfPeople = numberOfPeople;
        this.status = status;
    }

    public int getReservationId() { return reservationId; }
    public void setReservationId(int reservationId) { this.reservationId = reservationId; }

    public int getClientId() { return clientId; }
    public void setClientId(int clientId) { this.clientId = clientId; }

    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }

    public LocalDateTime getDateTime() { return dateTime; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }

    public int getNumberOfPeople() { return numberOfPeople; }
    public void setNumberOfPeople(int numberOfPeople) { this.numberOfPeople = numberOfPeople; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}