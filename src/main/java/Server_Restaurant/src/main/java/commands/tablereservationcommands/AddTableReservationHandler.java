package commands.tablereservationcommands;

import commands.ICommandHandler;
import model.Message;
import model.TableReservation;
import model.repository.TableReservationRepository;

public class AddTableReservationHandler implements ICommandHandler {
    private TableReservationRepository repo;

    public AddTableReservationHandler(TableReservationRepository repo) {
        this.repo = repo;
    }

    @Override
    public Message execute(Object data) {
        TableReservation tr = (TableReservation) data;
        boolean result = repo.addReservationTable(tr);
        return new Message("ADD_TABLE_RESERVATION_RESPONSE", result);
    }
}