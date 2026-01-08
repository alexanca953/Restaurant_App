package commands.tablecommands;

import commands.ICommandHandler;
import restaurantclient.model.ITableRepository;
import restaurantclient.model.Message;
import restaurantclient.model.Table;

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