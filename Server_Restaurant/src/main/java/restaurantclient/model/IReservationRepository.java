package restaurantclient.model;

import java.util.List;
import java.util.Map;

public interface IReservationRepository {
    boolean addReservation(Reservation reservation);
    boolean deleteReservation(int reservationId);
    boolean updateReservation( Reservation reservation);
    List<Reservation> getAllReservations();
    List<Reservation> getReservationsByClient(int clientId);
    List<Map<String, Object>> getAllReservationsAsMap();
}
