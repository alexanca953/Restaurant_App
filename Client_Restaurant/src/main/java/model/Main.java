package com.example.client; // Pune pachetul tau corect

import model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import presenter.ConcreteClient;

import java.util.Scanner;

@SpringBootApplication
@ComponentScan(basePackages = {"presenter", "model"}) // Ca sa gaseasca ConcreteClient-ul tau
public class Main implements CommandLineRunner {

    @Autowired
    private ConcreteClient client; // Spring ni-l da pe ala gata facut

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) {
        System.out.println("CLIENT: Gata de testare! Scrie comanda (ex: GET_ALL_USERS):");

        Scanner scanner = new Scanner(System.in);

        while (true) {
            String comanda = scanner.nextLine();

            if (comanda.equals("exit")) {
                try { client.closeConnection(); } catch(Exception e) {}
                break;
            }

            // 1. Facem mesajul simplu (fara date suplimentare momentan)
            Message msg = new Message(comanda, null);

            // 2. Il trimitem si ASTEPTAM raspunsul pe loc (metoda aia cu sendAndReceive)
            Object raspuns = client.sendAndReceive(msg);

            // 3. Afisam ce am primit
            if (raspuns != null) {
                System.out.println("SERVER A RASPUNS: " + raspuns.toString());
            } else {
                System.out.println("SERVER: Niciun raspuns (sau timeout).");
            }
        }
        System.exit(0);
    }
}