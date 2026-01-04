package commands.tablereservationcommands;

import commands.ICommandHandler;
import model.Message;
import model.TableReservation;
import model.repository.TableReservationRepository;
import java.util.List;

public class GetAllTableReservationsHandler implements ICommandHandler {
    private TableReservationRepository repo;

    public GetAllTableReservationsHandler(TableReservationRepository repo) {
        this.repo = repo;
    }

    @Override
    public Message execute(Object data) {
        List<TableReservation> list = repo.getAllReservationTables();
        return new Message("GET_ALL_TABLE_RESERVATIONS_RESPONSE", list);
    }
}