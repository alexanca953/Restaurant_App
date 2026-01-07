package controller; // pachetul tău

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

    @GetMapping("/dashboard") // Sau "/" pentru pagina principală
    public String showDashboard() {
        // Returnezi numele paginii specifice (dashboard.html), NU layout-ul!
        return "dashboard";
    }
}