package commands.tablereservationcommands;

import commands.ICommandHandler;
import restaurantproject.model.ITableReservationRepository;
import restaurantproject.model.Message;

public class DeleteTableReservationHandler implements ICommandHandler {
    private ITableReservationRepository repo;

    public DeleteTableReservationHandler(ITableReservationRepository repo) {
        this.repo = repo;
    }

    @Override
    public Message execute(Object data) {
        int id = (int) data;
        boolean result = repo.deleteReservationTable(id);
        return new Message("DELETE_TABLE_RESERVATION_RESPONSE", result);
    }
}