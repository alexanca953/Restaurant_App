package commands.reservationcommands;

import commands.ICommandHandler;
import model.IReservationRepository;
import model.Message;
import model.Reservation;

public class UpdateReservationHandler implements ICommandHandler {
    private IReservationRepository reservationRepo;

    public UpdateReservationHandler(IReservationRepository reservationRepo) {
        this.reservationRepo = reservationRepo;
    }

    @Override
    public Message execute(Object data) {
        Reservation r = (Reservation) data;
        System.out.println("SERVER: Updating reservation ID " + r.getReservationId());
        boolean success = reservationRepo.updateReservation(r);
        return new Message("UPDATE_RESERVATION_RESPONSE", success);
    }
}