package controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "home"; // Va căuta home.html
    }

    @GetMapping("/manager")
    public String showManager(Model model) {
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        model.addAttribute("newProduct", new Product());
        return "manager"; // trebuie să ai manager.html în templates
    }
}