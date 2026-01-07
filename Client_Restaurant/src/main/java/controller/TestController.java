package controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

    // Această metodă prinde link-ul http://localhost:8081/dashboard
    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        // Returnează numele fișierului HTML din folderul 'templates' (fără .html)
        // Spring va căuta: src/main/resources/templates/dashboard.html
        model.addAttribute("role","MANAGER");
        return "dashboard";
    }
}