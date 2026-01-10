package commands.reservationcommands;

import commands.ICommandHandler;
import restaurantproject.model.IReservationRepository;
import restaurantproject.model.Message;

import java.util.List;
import java.util.Map;

public class GetAllReservationsHandlerMap implements ICommandHandler {
    private IReservationRepository reservationRepo;

    public GetAllReservationsHandlerMap(IReservationRepository reservationRepo) {
        this.reservationRepo = reservationRepo;
    }

    @Override
    public Message execute(Object data) {
        List<Map<String, Object>> list = reservationRepo.getAllReservationsAsMap();
        return new Message("GET_RESERVATIONS_MAP_RESPONSE", list);
    }
}