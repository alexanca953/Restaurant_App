package controller;

import model.Message;
import ocsf.AbstractServer;
import ocsf.ConnectionToClient;


public class ConcreteServer extends AbstractServer {
    /**
     * Constructs a new server.
     *
     * @param port the port number on which to listen.
     */
    final public static int DEFAULT_PORT=8080;
    private RestaurantController controller;

    public ConcreteServer(int port) {
        super(port);
    }

    public void setController(RestaurantController controller) {
        this.controller = controller;
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
        try {
           Message messageRecived = (Message)msg;
            if (controller != null) {
                System.out.println("SERVER: Received a message "+ messageRecived.toString());
                controller.processRequest(messageRecived, client);
            } else
            {
                System.out.println("Error: Controller is null!");
            }
        }
        catch (Exception e) {
            System.out.println("SERVER: Error processing a message: " + e.toString());
        }

    }

}
