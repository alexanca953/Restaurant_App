package commands.tablecommands;

import commands.ICommandHandler;
import restaurantproject.model.ITableRepository;
import restaurantproject.model.Message;
import restaurantproject.model.Table;

import java.util.List;

public class GetAllTablesHandler implements ICommandHandler {
    private ITableRepository tableRepo;

    public GetAllTablesHandler(ITableRepository tableRepo) {
        this.tableRepo = tableRepo;
    }

    @Override
    public Message execute(Object data) {
        List<Table> list = tableRepo.getAllTables();
        return new Message("GET_ALL_TABLES_RESPONSE", list);
    }
}