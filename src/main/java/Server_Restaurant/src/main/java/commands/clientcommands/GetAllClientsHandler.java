package commands.clientcommands;

import commands.ICommandHandler;
import model.Client;
import model.Message;
import model.repository.ClientRepository;
import java.util.List;

public class GetAllClientsHandler implements ICommandHandler {
    private ClientRepository clientRepo;

    public GetAllClientsHandler(ClientRepository clientRepo) {
        this.clientRepo = clientRepo;
    }

    @Override
    public Message execute(Object data) {
        List<Client> list = clientRepo.getAllClients();
        return new Message("GET_ALL_CLIENTS_RESPONSE", list);
    }
}