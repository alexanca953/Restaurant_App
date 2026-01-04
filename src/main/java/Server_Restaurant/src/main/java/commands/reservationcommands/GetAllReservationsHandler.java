package commands.reservationcommands;

import commands.ICommandHandler;
import model.Message;
import model.Reservation;
import model.repository.ReservationRepository;
import java.util.List;

public class GetAllReservationsHandler implements ICommandHandler {
    private ReservationRepository reservationRepo;

    public GetAllReservationsHandler(ReservationRepository reservationRepo) {
        this.reservationRepo = reservationRepo;
    }

    @Override
    public Message execute(Object data) {
        List<Reservation> list = reservationRepo.getAllReservations();
        return new Message("GET_ALL_RESERVATIONS_RESPONSE", list);
    }
}