package commands.tablecommands;

import commands.ICommandHandler;
import model.Message;
import model.repository.TableRepository;

public class DeleteTableHandler implements ICommandHandler {
    private TableRepository tableRepo;

    public DeleteTableHandler(TableRepository tableRepo) {
        this.tableRepo = tableRepo;
    }

    @Override
    public Message execute(Object data) {
        int tableId = (int) data;
        boolean result = tableRepo.deleteTable(tableId);
        return new Message("DELETE_TABLE_RESPONSE", result);
    }
}