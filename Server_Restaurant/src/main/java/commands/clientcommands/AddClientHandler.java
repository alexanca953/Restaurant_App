package commands.clientcommands;

import commands.ICommandHandler;
import model.Client;
import model.IClientRepository;
import model.Message;

public class AddClientHandler implements ICommandHandler {
    private IClientRepository clientRepo;

    public AddClientHandler(IClientRepository clientRepo) {
        this.clientRepo = clientRepo;
    }

    @Override
    public Message execute(Object data) {
        Client client = (Client) data;
        boolean result = clientRepo.addClient(client);
        return new Message("ADD_CLIENT_RESPONSE", result);
    }
}