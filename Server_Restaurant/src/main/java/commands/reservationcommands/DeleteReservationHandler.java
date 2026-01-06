package commands.reservationcommands;

import commands.ICommandHandler;
import model.IReservationRepository;
import model.Message;

public class DeleteReservationHandler implements ICommandHandler {
    private IReservationRepository reservationRepo;

    public DeleteReservationHandler(IReservationRepository reservationRepo) {
        this.reservationRepo = reservationRepo;
    }

    @Override
    public Message execute(Object data) {
        int reservationId = (int) data;
        boolean result = reservationRepo.deleteReservation(reservationId);
        return new Message("DELETE_RESERVATION_RESPONSE", result);
    }
}