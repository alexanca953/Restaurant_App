package presenter;
import model.*;
import ocsf.*;

import java.io.IOException;

public class ConcreteClient extends AbstractClient {

    /**
     * Constructs the client.
     *
     * @param host the server's host name.
     * @param port the port number.
     */
    public ConcreteClient(String host, int port) {
        super(host, port);
    }

    @Override
    protected void handleMessageFromServer(Object msg) {
        try{
            Message message = (Message) msg;
            System.out.println(message.toString());
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public void sendMessage(Object message) {
        try {
            Message messageToSend =(Message)message;
            sendToServer(messageToSend);
            System.out.println("CLIENT: Am trimis catre server: " + messageToSend.toString());
        } catch (IOException e) {
            System.out.println("CLIENT: Nu am putut trimite mesajul la server.");
            e.printStackTrace();
        }
    }


    @Override
    protected void connectionEstablished() {
        System.out.println("CLIENT: M-am conectat cu succes la server!");
    }

    @Override
    protected void connectionClosed() {
        System.out.println("CLIENT: Conexiunea cu serverul s-a inchis.");
    }

    @Override
    protected void connectionException(Exception exception) {
        System.out.println("CLIENT: Eroare de conexiune server: " + exception);
    }
}
