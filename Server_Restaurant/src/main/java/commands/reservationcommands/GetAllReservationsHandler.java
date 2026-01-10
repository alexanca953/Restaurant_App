package commands.reservationcommands;

import commands.ICommandHandler;
import restaurantproject.model.IReservationRepository;
import restaurantproject.model.Message;
import restaurantproject.model.Reservation;

import java.util.List;

public class GetAllReservationsHandler implements ICommandHandler {
    private IReservationRepository reservationRepo;

    public GetAllReservationsHandler(IReservationRepository reservationRepo) {
        this.reservationRepo = reservationRepo;
    }

    @Override
    public Message execute(Object data) {
        List<Reservation> list = reservationRepo.getAllReservations();
        return new Message("GET_ALL_RESERVATIONS_RESPONSE", list);
    }
}