package commands.reservationcommands;

import commands.ICommandHandler;
import model.IReservationRepository;
import model.Message;
import model.Reservation;
import model.repository.ReservationRepository;

public class AddReservationHandler implements ICommandHandler {
    private IReservationRepository reservationRepo;

    public AddReservationHandler(IReservationRepository reservationRepo) {
        this.reservationRepo = reservationRepo;
    }

    @Override
    public Message execute(Object data) {
        Reservation reservation = (Reservation) data;
        boolean result = reservationRepo.addReservation(reservation);
        return new Message("ADD_RESERVATION_RESPONSE", result);
    }
}