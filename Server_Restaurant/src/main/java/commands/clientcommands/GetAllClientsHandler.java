package commands.clientcommands;

import commands.ICommandHandler;
import model.Client;
import model.IClientRepository;
import model.Message;

import java.util.List;

public class GetAllClientsHandler implements ICommandHandler {
    private IClientRepository clientRepo;

    public GetAllClientsHandler(IClientRepository clientRepo) {
        this.clientRepo = clientRepo;
    }

    @Override
    public Message execute(Object data) {
        List<Client> list = clientRepo.getAllClients();
        return new Message("GET_ALL_CLIENTS_RESPONSE", list);
    }
}