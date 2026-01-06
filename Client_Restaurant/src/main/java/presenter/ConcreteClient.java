package presenter;

import model.Message;
import ocsf.AbstractClient;
import org.springframework.stereotype.Component;

import java.io.IOException;
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
        // 1. Reset the promise (create a new empty container for the response)
        pendingResponse = new CompletableFuture<>();
        try {
            // 2. Ensure connection is open
            if (!isConnected()) {
                System.out.println("WEB CLIENT: Attempting to connect to backend server...");
                openConnection();
            }
            // 3. Send the message
            Message messageToSend = (Message) message;
            sendToServer(messageToSend);
            System.out.println("WEB CLIENT: Sent to server and waiting for response: " + messageToSend.toString());

            // 4. WAIT FOR RESPONSE (Block here for max 5 seconds)
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

            // If there is a pending request from the Web Controller, complete it now
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