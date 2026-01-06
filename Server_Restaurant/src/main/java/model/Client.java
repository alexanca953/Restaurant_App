package model;

import java.io.Serializable;

public class Client implements Serializable {
    private int clientId;
    private int userId;

    public Client() {}

    public Client(int clientId, int userId) {
        this.clientId = clientId;
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Client{" +
                "clientId=" + clientId +
                ", userId=" + userId +
                '}';
    }

    public int getClientId() { return clientId; }
    public void setClientId(int clientId) { this.clientId = clientId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
}