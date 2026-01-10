package commands.tablecommands;

import commands.ICommandHandler;
import restaurantproject.model.ITableRepository;
import restaurantproject.model.Message;

public class DeleteTableHandler implements ICommandHandler {
    private ITableRepository tableRepo;

    public DeleteTableHandler(ITableRepository tableRepo) {
        this.tableRepo = tableRepo;
    }

    @Override
    public Message execute(Object data) {
        int tableId = (int) data;
        boolean result = tableRepo.deleteTable(tableId);
        return new Message("DELETE_TABLE_RESPONSE", result);
    }
}