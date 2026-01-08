package restaurantclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// @SpringBootApplication singur e suficient dacă Main e în pachetul părinte 'restaurantclient'.
// El va scana automat TOT ce e sub el (config, controller, model).
@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
        System.out.println(">>> SERVER WEB PORNIT! Intră pe http://localhost:8081");
    }
}