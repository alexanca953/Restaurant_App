package commands.tablereservationcommands;

import commands.ICommandHandler;
import model.ITableReservationRepository;
import model.Message;
import model.TableReservation;
import model.repository.TableReservationRepository;

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