package controller;

import model.Message;
import model.Product;
import model.ProductCategory;
import model.Message;
import model.Product;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {
    ConcreteClient client = new ConcreteClient();
    @GetMapping("/")
    public String home() {
        return "home"; // Va căuta home.html
    }
///edit menu
    @GetMapping("/menu-management")
    public String showManager(Model model) {

        ArrayList<Product> products = null;
       try {
            products = (ArrayList<Product>) client.sendAndReceive(new Message("GET_ALL_PRODUCTS",null));
           ArrayList<ProductCategory> categories= (ArrayList<ProductCategory>) client.sendAndReceive(new Message("GET_ALL_CATEGORIES",null));
           model.addAttribute("categories", categories);
       }
       catch (Exception e) {
          System.out.println(e+"eror at getting the products from server");
       }
       if(products!=null)
       {
           model.addAttribute("products", products);
       }
        return "menu-management"; // trebuie să ai menu-management.html.html în templates
    }
    @PostMapping("/menu-management/save")
    public String saveProduct(Model model, Product product) {
        try
        {
            if (product.getProductId() == 0) {
            // CAZUL 1: ID este 0 -> ADĂUGARE PRODUS NOU
                client.sendAndReceive(new Message("ADD_PRODUCT",product));
            }
            else {
            // CAZUL 2: ID > 0 -> EDITARE PRODUS EXISTENT
            client.sendAndReceive(new Message("UPDATE_PRODUCT",product));
                 }
            ///client.sendAndReceive(new Message("UPDATE_PRODUCT",product));
            ///return "redirect:/menu-management";
        }
        catch (Exception e)
        {
            System.out.println(e+"eror at saving the product");
        }
        return "redirect:/menu-management";
    }
    @PostMapping("/menu-management/delete")
    public String deleteProduct(@RequestParam("productId") int id) {

        try{
            Product product = new Product();
            product.setProductId(id);
            client.sendAndReceive(new Message("DELETE_PRODUCT",product));
        }
        catch (Exception e)
        {
            System.out.println(e+"eror at deleting the product");
        }
        return "redirect:/menu-management";
    }
    @PostMapping("/menu-management/save-category")
    public String saveCategory(Model model, ProductCategory category) {

       try{
           client.sendAndReceive(new Message("ADD_CATEGORY",category));
       }
       catch (Exception e)
       {
           System.out.println(e+"eror at saving the category");
       }
        return "redirect:/menu-management";
    }
    @PostMapping("/menu-management/delete-category")
    public String deleteCategory(@RequestParam("categoryId") int categoryId) {
        try
        {
            client.sendAndReceive(new Message("DELETE_CATEGORY",categoryId));
        }
        catch (Exception e)
        {
            System.out.println(e+"eror at deleting the category");
        }
        return "redirect:/menu-management";
    }

    @GetMapping("/menu")
    public String showMenu(@RequestParam(name = "keyword", required = false) String keyword, Model model) {
        ArrayList<Product> products=new ArrayList<>();
        try{
                products=(ArrayList<Product>) client.sendAndReceive(new Message("GET_ALL_PRODUCTS",null));
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
        }
        catch(Exception e){
            System.out.println("Error at loading products "+ e.getMessage());
        }

        return "menu";
    }
}
