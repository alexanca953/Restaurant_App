package commands.tablecommands;

import commands.ICommandHandler;
import restaurantproject.model.ITableRepository;
import restaurantproject.model.Message;
import restaurantproject.model.Table;

public class AddTableHandler implements ICommandHandler {
    private ITableRepository tableRepo;

    public AddTableHandler(ITableRepository tableRepo) {
        this.tableRepo = tableRepo;
    }

    @Override
    public Message execute(Object data) {
        Table table = (Table) data;
        boolean result = tableRepo.addTable(table);
        return new Message("ADD_TABLE_RESPONSE", result);
    }
}