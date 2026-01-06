package commands.reservationcommands;

import commands.ICommandHandler;
import model.IReservationRepository;
import model.Message;
import model.Reservation;
import model.repository.ReservationRepository;

public class UpdateReservationHandler implements ICommandHandler {
    private IReservationRepository reservationRepo;

    public UpdateReservationHandler(IReservationRepository reservationRepo) {
        this.reservationRepo = reservationRepo;
    }

    @Override
    public Message execute(Object data) {
        Reservation reservation = (Reservation) data;
        boolean result = reservationRepo.updateReservation(reservation.getReservationId(), reservation);
        return new Message("UPDATE_RESERVATION_RESPONSE", result);
    }
}