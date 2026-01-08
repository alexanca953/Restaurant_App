package commands.tablecommands;

import commands.ICommandHandler;
import restaurantclient.model.ITableRepository;
import restaurantclient.model.Message;
import restaurantclient.model.Table;

public class UpdateTableHandler implements ICommandHandler {
    private ITableRepository tableRepo;

    public UpdateTableHandler(ITableRepository tableRepo) {
        this.tableRepo = tableRepo;
    }

    @Override
    public Message execute(Object data) {
        Table table = (Table) data;
        boolean result = tableRepo.updateTable(table.getTableId(), table);
        return new Message("UPDATE_TABLE_RESPONSE", result);
    }
}