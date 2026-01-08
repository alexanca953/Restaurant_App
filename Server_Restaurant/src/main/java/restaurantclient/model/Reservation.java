package restaurantclient.model;
import java.io.Serializable;
import java.time.LocalDateTime;

public class Reservation implements Serializable {
    private int reservationId;
    private int clientId;
    private int employeeId;
    private LocalDateTime dateTime;
    private int numberOfPeople;
    private String status;
    private String tempClientName;
    private String tempClientPhone;
    private int tableId;

    public int getTableId() { return tableId; }
    public void setTableId(int tableId) { this.tableId = tableId; }

    public Reservation() {}

    public Reservation(int reservationId, int clientId, int employeeId, LocalDateTime dateTime, int numberOfPeople, String status) {
        this.reservationId = reservationId;
        this.clientId = clientId;
        this.employeeId = employeeId;
        this.dateTime = dateTime;
        this.numberOfPeople = numberOfPeople;
        this.status = status;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "reservationId=" + reservationId +
                ", clientId=" + clientId +
                ", employeeId=" + employeeId +
                ", dateTime=" + dateTime +
                ", numberOfPeople=" + numberOfPeople +
                ", status='" + status + '\'' +
                '}';
    }
    // Getters si Setters
    public String getTempClientName() { return tempClientName; }
    public void setTempClientName(String tempClientName) { this.tempClientName = tempClientName; }

    public String getTempClientPhone() { return tempClientPhone; }
    public void setTempClientPhone(String tempClientPhone) { this.tempClientPhone = tempClientPhone; }

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