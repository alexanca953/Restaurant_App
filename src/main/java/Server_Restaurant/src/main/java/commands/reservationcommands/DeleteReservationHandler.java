package commands.reservationcommands;

import commands.ICommandHandler;
import model.Message;
import model.repository.ReservationRepository;

public class DeleteReservationHandler implements ICommandHandler {
    private ReservationRepository reservationRepo;

    public DeleteReservationHandler(ReservationRepository reservationRepo) {
        this.reservationRepo = reservationRepo;
    }

    @Override
    public Message execute(Object data) {
        int reservationId = (int) data;
        boolean result = reservationRepo.deleteReservation(reservationId);
        return new Message("DELETE_RESERVATION_RESPONSE", result);
    }
}