package commands.tablecommands;

import commands.ICommandHandler;
import model.ITableRepository;
import model.Message;
import model.Table;
import model.repository.TableRepository;

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