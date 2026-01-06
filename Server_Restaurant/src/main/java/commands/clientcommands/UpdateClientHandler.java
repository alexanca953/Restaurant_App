package commands.clientcommands;

import commands.ICommandHandler;
import model.Client;
import model.IClientRepository;
import model.Message;

public class UpdateClientHandler implements ICommandHandler {
    private IClientRepository clientRepo;

    public UpdateClientHandler(IClientRepository clientRepo) {
        this.clientRepo = clientRepo;
    }

    @Override
    public Message execute(Object data) {
        Client client = (Client) data;
        boolean result = clientRepo.updateClient(client.getClientId(), client);
        return new Message("UPDATE_CLIENT_RESPONSE", result);
    }
}