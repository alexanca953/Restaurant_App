package commands.tablereservationcommands;

import commands.ICommandHandler;
import model.Message;
import model.TableReservation;
import model.repository.TableReservationRepository;

public class UpdateTableReservationHandler implements ICommandHandler {
    private TableReservationRepository repo;

    public UpdateTableReservationHandler(TableReservationRepository repo) {
        this.repo = repo;
    }

    @Override
    public Message execute(Object data) {
        TableReservation tr = (TableReservation) data;
        // Presupunem ca obiectul primit are ID-ul corect setat in el
        boolean result = repo.updateReservationTable(tr.getTableId(), tr);
        return new Message("UPDATE_TABLE_RESERVATION_RESPONSE", result);
    }
}