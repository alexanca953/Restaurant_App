package commands.tablereservationcommands;

import commands.ICommandHandler;
import restaurantclient.model.ITableReservationRepository;
import restaurantclient.model.Message;
import restaurantclient.model.TableReservation;

public class AddTableReservationHandler implements ICommandHandler {
    private ITableReservationRepository repo;

    public AddTableReservationHandler(ITableReservationRepository repo) {
        this.repo = repo;
    }

    @Override
    public Message execute(Object data) {
        TableReservation tr = (TableReservation) data;
        boolean result = repo.addReservationTable(tr);
        return new Message("ADD_TABLE_RESERVATION_RESPONSE", result);
    }
}