package commands.reservationcommands;

import commands.ICommandHandler;
import model.IReservationRepository;
import model.Message;
import model.Reservation;

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