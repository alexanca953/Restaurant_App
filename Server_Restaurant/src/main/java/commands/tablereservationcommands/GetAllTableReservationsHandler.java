package commands.tablereservationcommands;

import commands.ICommandHandler;
import restaurantproject.model.ITableReservationRepository;
import restaurantproject.model.Message;
import restaurantproject.model.TableReservation;

import java.util.List;

public class GetAllTableReservationsHandler implements ICommandHandler {
    private ITableReservationRepository repo;

    public GetAllTableReservationsHandler(ITableReservationRepository repo) {
        this.repo = repo;
    }

    @Override
    public Message execute(Object data) {
        List<TableReservation> list = repo.getAllReservationTables();
        return new Message("GET_ALL_TABLE_RESERVATIONS_RESPONSE", list);
    }
}