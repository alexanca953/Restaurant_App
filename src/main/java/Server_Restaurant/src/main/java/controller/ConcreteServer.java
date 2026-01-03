package controller;

import ocsf.AbstractServer;
import ocsf.ConnectionToClient;


public class ConcreteServer extends AbstractServer {
    /**
     * Constructs a new server.
     *
     * @param port the port number on which to listen.
     */
    final public static int DEFAULT_PORT=8080;

    public ConcreteServer(int port) {
        super(port);
    }
    @Override
    protected void serverStarted() {
        super.serverStarted();
        System.out.println("SERVER: Server started, listening on port: " + getPort());
    }
    @Override
    protected void serverStopped() {
        super.serverStopped();
        System.out.println("SERVER: Server stopped");
    }
    @Override
    protected synchronized void clientConnected(ConnectionToClient client) {
        super.clientConnected(client);
        System.out.println("SERVER: A client has connected: " + client.toString());
    }
    @Override
    protected synchronized void clientDisconnected(ConnectionToClient client) {
        super.clientDisconnected(client);
        System.out.println("SERVER: A client has disconnected: " + client.toString());
    }
    @Override
    protected void handleMessageFromClient(Object msg, ConnectionToClient client) {

    }
    public static void main(String[] args)
    {
        int port=DEFAULT_PORT;
        ConcreteServer sv = new ConcreteServer(port);
        try {
            sv.listen();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
}
