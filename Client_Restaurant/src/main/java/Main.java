import model.*;
import presenter.ConcreteClient;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Setarile de conectare (trebuie sa fie la fel ca in Server)
        String host = "localhost";
        int port = 8080;

        System.out.println("CLIENT: Incerc sa ma conectez la " + host + ":" + port + "...");

        // 1. Initializam clientul
        ConcreteClient client = new ConcreteClient(host, port);

        try {
            // 2. Deschidem conexiunea (CRITIC: Fara asta nu merge nimic)
            client.openConnection();
            System.out.println("CLIENT: Conexiune reusita! Poti scrie mesaje in consola.");

            // 3. Citim de la tastatura ca sa tinem aplicatia pornita si sa testam
            Scanner scanner = new Scanner(System.in);

            while (true) {
                // Citim ce scrii tu in consola
                String comanda = scanner.nextLine();

                if (comanda.equals("exit")) {
                    client.closeConnection();
                    break;
                }

                // 4. Trimitem mesajul catre Server folosind metoda facuta de noi in ConcreteClient
                // Nota: In viitor aici vei trimite obiecte de tip Message, nu String-uri simple!
                Message msgToSend=new Message(comanda,null);
                client.sendMessage(msgToSend);
            }

        } catch (IOException e) {
            System.out.println("CLIENT: Eroare! Nu m-am putut conecta la server.");
            System.out.println("Verifica daca Serverul este pornit!");
            e.printStackTrace();
        }
    }
}