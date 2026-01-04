package commands.tablecommands;

import commands.ICommandHandler;
import model.Message;
import model.Table;
import model.repository.TableRepository;

public class UpdateTableHandler implements ICommandHandler {
    private TableRepository tableRepo;

    public UpdateTableHandler(TableRepository tableRepo) {
        this.tableRepo = tableRepo;
    }

    @Override
    public Message execute(Object data) {
        Table table = (Table) data;
        boolean result = tableRepo.updateTable(table.getTableId(), table);
        return new Message("UPDATE_TABLE_RESPONSE", result);
    }
}