package commands.reservationcommands;

import commands.ICommandHandler;
import restaurantclient.model.IReservationRepository;
import restaurantclient.model.Message;
import restaurantclient.model.Reservation;

public class DeleteReservationHandler implements ICommandHandler {
    private IReservationRepository reservationRepo;

    public DeleteReservationHandler(IReservationRepository reservationRepo) {
        this.reservationRepo = reservationRepo;
    }

    @Override
    public Message execute(Object data) {
        Reservation r = (Reservation) data;
        boolean success = reservationRepo.deleteReservation(r.getReservationId());

        return new Message("DELETE_RESERVATION_RESPONSE", success);
    }
}