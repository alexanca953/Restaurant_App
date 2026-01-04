package commands.clientcommands;

import commands.ICommandHandler;
import model.Client;
import model.Message;
import model.repository.ClientRepository;

public class AddClientHandler implements ICommandHandler {
    private ClientRepository clientRepo;

    public AddClientHandler(ClientRepository clientRepo) {
        this.clientRepo = clientRepo;
    }

    @Override
    public Message execute(Object data) {
        Client client = (Client) data;
        boolean result = clientRepo.addClient(client);
        return new Message("ADD_CLIENT_RESPONSE", result);
    }
}