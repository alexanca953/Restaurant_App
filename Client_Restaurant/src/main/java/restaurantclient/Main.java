package restaurantclient;

import restaurantclient.model.Message;
import restaurantclient.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import restaurantclient.controller.ConcreteClient;

import java.util.Scanner;

@SpringBootApplication(scanBasePackages = {
        "restaurantclient",
        "restaurantclient.controller",
        "restaurantclient.config",
        "restaurantclient.model",
        "restaurantclient.service",
})
@ComponentScan(basePackages = {"restaurantclient.controller", "restaurantclient.model"})
public class Main implements CommandLineRunner {

    @Autowired
    private ConcreteClient client;

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
            User a=new User(10,"a","a","a","a","a","a");
            Message msg = new Message(comanda, a);
            Object raspuns = client.sendAndReceive(msg);
            if (raspuns != null) {
                System.out.println("SERVER A RASPUNS: " + raspuns.toString());
            } else {
                System.out.println("SERVER: Niciun raspuns (sau timeout).");
            }
        }
        System.exit(0);
    }
}