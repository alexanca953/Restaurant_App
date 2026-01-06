package model;

import java.util.List;

public interface IClientRepository {
    boolean addClient(Client client);
    boolean deleteClient(int clientId);
    boolean updateClient(int clientId, Client client);
    List<Client> getAllClients();
}