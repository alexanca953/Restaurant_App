package model;

import java.util.List;

public interface IReservationRepository {
    boolean addReservation(Reservation reservation);
    boolean deleteReservation(int reservationId);
    boolean updateReservation(int reservationId, Reservation reservation);
    List<Reservation> getAllReservations();
    List<Reservation> getReservationsByClient(int clientId);
}
