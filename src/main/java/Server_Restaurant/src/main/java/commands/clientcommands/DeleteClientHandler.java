package commands.clientcommands;

import commands.ICommandHandler;
import model.Message;
import model.repository.ClientRepository;

public class DeleteClientHandler implements ICommandHandler {
    private ClientRepository clientRepo;

    public DeleteClientHandler(ClientRepository clientRepo) {
        this.clientRepo = clientRepo;
    }

    @Override
    public Message execute(Object data) {
        int clientId = (int) data;
        boolean result = clientRepo.deleteClient(clientId);
        return new Message("DELETE_CLIENT_RESPONSE", result);
    }
}