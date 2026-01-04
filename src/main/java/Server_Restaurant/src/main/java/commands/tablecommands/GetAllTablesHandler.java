package commands.tablecommands;

import commands.ICommandHandler;
import model.Message;
import model.Table;
import model.repository.TableRepository;
import java.util.List;

public class GetAllTablesHandler implements ICommandHandler {
    private TableRepository tableRepo;

    public GetAllTablesHandler(TableRepository tableRepo) {
        this.tableRepo = tableRepo;
    }

    @Override
    public Message execute(Object data) {
        List<Table> list = tableRepo.getAllTables();
        return new Message("GET_ALL_TABLES_RESPONSE", list);
    }
}