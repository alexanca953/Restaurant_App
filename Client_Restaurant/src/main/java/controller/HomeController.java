package controller;

import model.Message;
import model.Product;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {
    ConcreteClient client=new ConcreteClient();
    @GetMapping("/")
    public String home() {
        return "home"; // Va căuta home.html
    }

    @GetMapping("/menu")
    public String showMenu(@RequestParam(name = "keyword", required = false) String keyword, Model model) {
        ArrayList<Product> products=new ArrayList<>();
        try{
            Message response= (Message) client.sendAndReceive(new Message("GET_ALL_PRODUCTS",null));
            if(response!=null && response.getData()!=null){
                products=(ArrayList<Product>) response.getData();
            }
        }
        catch(Exception e){
            System.out.println("Error at loading products "+ e.getMessage());
        }

        List<Product> filteredList=new ArrayList<>();

        if (keyword != null && !keyword.isEmpty()) {
            filteredList = new ArrayList<>();
            for (Product p : products) {
                if (p.getProductName().toLowerCase().contains(keyword.toLowerCase())) {
                    filteredList.add(p);
                }
            }
        } else {
            filteredList = products;
        }
        model.addAttribute("productList", filteredList);
        model.addAttribute("keyword", keyword);
        return "menu";
    }
}
