package model;

import java.util.List;

public interface ITableReservationRepository {
    boolean addReservationTable(TableReservation rt);
    boolean deleteReservationTable(int id);
    boolean updateReservationTable(int id, TableReservation rt);
    List<TableReservation> getAllReservationTables();
}