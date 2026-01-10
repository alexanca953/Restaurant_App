package commands.tablereservationcommands;

import commands.ICommandHandler;
import restaurantproject.model.ITableReservationRepository;
import restaurantproject.model.Message;
import restaurantproject.model.TableReservation;

public class UpdateTableReservationHandler implements ICommandHandler {
    private ITableReservationRepository repo;

    public UpdateTableReservationHandler(ITableReservationRepository repo) {
        this.repo = repo;
    }

    @Override
    public Message execute(Object data) {
        TableReservation tr = (TableReservation) data;
        boolean result = repo.updateReservationTable(tr.getTableId(), tr);
        return new Message("UPDATE_TABLE_RESERVATION_RESPONSE", result);
    }
}