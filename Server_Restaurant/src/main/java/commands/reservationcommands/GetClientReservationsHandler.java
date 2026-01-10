package commands.reservationcommands;

import commands.ICommandHandler;
import restaurantproject.model.IReservationRepository;
import restaurantproject.model.Message;
import restaurantproject.model.Reservation;

import java.util.List;

public class GetClientReservationsHandler implements ICommandHandler {
    private IReservationRepository reservationRepo;

    public GetClientReservationsHandler(IReservationRepository reservationRepo) {
        this.reservationRepo = reservationRepo;
    }

    @Override
    public Message execute(Object data) {
        int clientId = (int) data;
        List<Reservation> list = reservationRepo.getReservationsByClient(clientId);
        return new Message("GET_CLIENT_RESERVATIONS_RESPONSE", list);
    }
}