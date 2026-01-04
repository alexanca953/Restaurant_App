package commands.tablereservationcommands;

import commands.ICommandHandler;
import model.Message;
import model.repository.TableReservationRepository;

public class DeleteTableReservationHandler implements ICommandHandler {
    private TableReservationRepository repo;

    public DeleteTableReservationHandler(TableReservationRepository repo) {
        this.repo = repo;
    }

    @Override
    public Message execute(Object data) {
        int id = (int) data;
        boolean result = repo.deleteReservationTable(id);
        return new Message("DELETE_TABLE_RESERVATION_RESPONSE", result);
    }
}