package restaurantclient.controller;

import restaurantclient.model.Message;
import restaurantclient.service.AbstractClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component
public class ConcreteClient extends AbstractClient {

    private CompletableFuture<Object> pendingResponse;

    public ConcreteClient() {
        super("localhost", 8080);
    }

    public Object sendAndReceive(Object message) {
        pendingResponse = new CompletableFuture<>();
        try {
            if (!isConnected()) {
                System.out.println("WEB CLIENT: Attempting to connect to backend server...");
                openConnection();
            }
            Message messageToSend = (Message) message;
            sendToServer(messageToSend);
            System.out.println("WEB CLIENT: Sent to server and waiting for response: " + messageToSend.toString());
            return pendingResponse.get(5, TimeUnit.SECONDS);

        } catch (TimeoutException e) {
            System.out.println("WEB CLIENT: Server did not respond within 5 seconds.");
            return null;
        } catch (Exception e) {
            System.out.println("WEB CLIENT: Communication error: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void handleMessageFromServer(Object msg) {
        try {
            Message message = (Message) msg;
            System.out.println("WEB CLIENT: Received response: " + message.toString());
            if (pendingResponse != null && !pendingResponse.isDone()) {
                pendingResponse.complete(message.getData());
            }
        } catch (Exception e) {
            System.out.println("WEB CLIENT: Error processing response: " + e.getMessage());
            if (pendingResponse != null) {
                pendingResponse.complete(null);
            }
        }
    }

    @Override
    protected void connectionEstablished() {
        System.out.println("WEB CLIENT: Successfully connected to backend server!");
    }

    @Override
    protected void connectionClosed() {
        System.out.println("WEB CLIENT: Connection to server closed.");
    }

    @Override
    protected void connectionException(Exception exception) {
        System.out.println("WEB CLIENT: Connection error: " + exception.getMessage());
    }
}