package commands.reservationcommands;

import commands.ICommandHandler;
import restaurantproject.model.IReservationRepository;
import restaurantproject.model.Message;
import restaurantproject.model.Reservation;

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