package commands.clientcommands;

import commands.ICommandHandler;
import model.Client;
import model.Message;
import model.repository.ClientRepository;

public class UpdateClientHandler implements ICommandHandler {
    private ClientRepository clientRepo;

    public UpdateClientHandler(ClientRepository clientRepo) {
        this.clientRepo = clientRepo;
    }

    @Override
    public Message execute(Object data) {
        Client client = (Client) data;
        boolean result = clientRepo.updateClient(client.getClientId(), client);
        return new Message("UPDATE_CLIENT_RESPONSE", result);
    }
}