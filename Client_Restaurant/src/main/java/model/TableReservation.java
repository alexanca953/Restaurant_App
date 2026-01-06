package model;

import java.io.Serializable;

public class TableReservation implements Serializable {
    private int reservationTableId;
    private int reservationId;
    private int tableId;

    public TableReservation() {}

    public TableReservation(int reservationTableId, int reservationId, int tableId) {
        this.reservationTableId = reservationTableId;
        this.reservationId = reservationId;
        this.tableId = tableId;
    }

    @Override
    public String toString() {
        return "TableReservation{" +
                "reservationTableId=" + reservationTableId +
                ", reservationId=" + reservationId +
                ", tableId=" + tableId +
                '}';
    }

    public int getReservationTableId() { return reservationTableId; }
    public void setReservationTableId(int reservationTableId) { this.reservationTableId = reservationTableId; }

    public int getReservationId() { return reservationId; }
    public void setReservationId(int reservationId) { this.reservationId = reservationId; }

    public int getTableId() { return tableId; }
    public void setTableId(int tableId) { this.tableId = tableId; }
}