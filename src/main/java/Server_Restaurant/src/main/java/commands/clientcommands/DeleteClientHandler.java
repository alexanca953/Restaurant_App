package commands.clientcommands;

import commands.ICommandHandler;
import model.IClientRepository;
import model.Message;
import model.repository.ClientRepository;

public class DeleteClientHandler implements ICommandHandler {
    private IClientRepository clientRepo;

    public DeleteClientHandler(IClientRepository clientRepo) {
        this.clientRepo = clientRepo;
    }

    @Override
    public Message execute(Object data) {
        int clientId = (int) data;
        boolean result = clientRepo.deleteClient(clientId);
        return new Message("DELETE_CLIENT_RESPONSE", result);
    }
}