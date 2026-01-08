package controller;

import jakarta.servlet.http.HttpSession;
import model.*;
import model.*;
import model.Message;
import model.Product;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.FileStore;
import java.util.*;

import java.beans.PropertyEditorSupport;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {
    ConcreteClient client = new ConcreteClient();
    @GetMapping("/")
    public String home(Model model,HttpSession session){

        User userLogat = (User) session.getAttribute("userLogat");

        if (userLogat != null) {
            System.out.println("User: " + userLogat.getFirstName());
        } else {
            System.out.println("No user logged in");
        }
        model.addAttribute("userLogat", userLogat);

        return "home";
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
        ArrayList<ProductCategory> categories = new ArrayList<>();

        try{
            products=(ArrayList<Product>) client.sendAndReceive(new Message("GET_ALL_PRODUCTS",null));
            categories=(ArrayList<ProductCategory>) client.sendAndReceive(new Message("GET_ALL_CATEGORIES",null));
        }
        catch(Exception e){
            System.out.println("Error at loading products or categories"+ e.getMessage());
        }

        Map<Integer, String> categoryNamesMap = new HashMap<>();
        if (categories != null) {
            for (ProductCategory cat : categories) {
                categoryNamesMap.put(cat.getCategoryId(), cat.getName());
            }
        }

        List<Product> filteredList;
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
        Map<String, List<Product>> menuGrouped = new LinkedHashMap<>();

        if (filteredList != null) {
            menuGrouped = filteredList.stream()
                    .collect(Collectors.groupingBy(product -> {
                        int idCat = product.getCategoryId();
                        return categoryNamesMap.getOrDefault(idCat, "Diverse");
                    }));
        }

        model.addAttribute("menuMap", menuGrouped);
        model.addAttribute("keyword", keyword);

        return "menu";
    }
    ///RESERVATIONS
    @GetMapping("/reservations")
    public String showReservations(Model model) {

        List<Map<String, Object>> reservations = new ArrayList<>();
        List<Table> tables = new ArrayList<>();

        try {
            if (client != null && client.isConnected()) {
                Object response = client.sendAndReceive(new Message("GET_RESERVATIONS_MAP", null));
                if (response instanceof List) {
                    reservations = (List<Map<String, Object>>) response;
                }
                Object responseTables = client.sendAndReceive(new Message("GET_ALL_TABLES", null));
                if (responseTables instanceof List) {
                    tables = (List<Table>) responseTables;
                }
            }
        } catch (Exception e) {
            System.out.println("Eroare la preluarea datelor: " + e.getMessage());
            e.printStackTrace();
        }

        model.addAttribute("reservations", reservations);
        model.addAttribute("tables", tables);

        return "reservations";
    }
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(LocalDateTime.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                if (text != null && !text.isEmpty()) {
                    try {
                        setValue(LocalDateTime.parse(text));
                    } catch (Exception e) {
                        setValue(null);
                    }
                } else {
                    setValue(null);
                }
            }
        });
    }
    @PostMapping("/reservations/save")
    public String saveReservation(@ModelAttribute Reservation reservation) {
        try {
            String command = (reservation.getReservationId() == 0) ? "ADD_RESERVATION" : "UPDATE_RESERVATION";
            System.out.println("Sending command: " + command);
            Object response = client.sendAndReceive(new Message(command, reservation));
            if (Boolean.FALSE.equals(response)) {
                return "redirect:/reservations?error=true";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/reservations";
    }
    @PostMapping("/reservations/delete")
    public String deleteReservation(@RequestParam("reservationId") int id) {
        try {
            Reservation r = new Reservation();
            r.setReservationId(id);
            System.out.println("Deleting reservation ID: " + id);
            client.sendAndReceive(new Message("DELETE_RESERVATION", r));

        } catch (Exception e) {
            System.out.println("Eroare la ștergere: " + e.getMessage());
            e.printStackTrace();
        }
        return "redirect:/reservations";
    }
    ///tables
    @GetMapping("/tables")
    public String showTablesDashboard(Model model) {
        List<Table> tables = new ArrayList<>();
        Map<Integer, List<Map<String, Object>>> reservationsPerTable = new HashMap<>();
        try {
            if (client != null && client.isConnected()) {
                Object tablesResponse = client.sendAndReceive(new Message("GET_ALL_TABLES", null));
                if (tablesResponse instanceof List) {
                    tables = (List<Table>) tablesResponse;
                }
                Object resResponse = client.sendAndReceive(new Message("GET_RESERVATIONS_MAP", null));
                List<Map<String, Object>> allReservations = new ArrayList<>();
                if (resResponse instanceof List) {
                    allReservations = (List<Map<String, Object>>) resResponse;
                }
                LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0);
                LocalDateTime endOfDay = LocalDateTime.now().withHour(23).withMinute(59);
                for (Map<String, Object> res : allReservations) {
                    LocalDateTime resDate = (LocalDateTime) res.get("reservationDate");
                    int tableId = (int) res.get("tableId");
                    if (resDate != null && resDate.isAfter(startOfDay) && resDate.isBefore(endOfDay)) {
                        reservationsPerTable.putIfAbsent(tableId, new ArrayList<>());
                        reservationsPerTable.get(tableId).add(res);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        tables.sort((t1, t2) -> Integer.compare(t1.getTableNumber(), t2.getTableNumber()));
        model.addAttribute("tables", tables);
        model.addAttribute("reservationsMap", reservationsPerTable);

        return "tables";
    }
    ///admin
    // 1. AFIȘARE PAGINĂ ADMIN
    @GetMapping("/users")
    public String showAdminUsers(Model model) {
        try {
            Object response = client.sendAndReceive(new Message("GET_ALL_USERS", null));
            if (response instanceof List) {
                model.addAttribute("users", (List<User>) response);
            } else {
                model.addAttribute("users", new ArrayList<>());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "admin";
    }

    @PostMapping("/users/save")
    public String saveUser(@ModelAttribute User user) {
        try {
            String command = (user.getUserId() == 0) ? "ADD_USER" : "UPDATE_USER";
            client.sendAndReceive(new Message(command, user));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/users";
    }
    @PostMapping("/users/delete")
    public String deleteUser(@RequestParam("userId") int userId) {
        try {
            client.sendAndReceive(new Message("DELETE_USER", userId));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/users";
    }

    ///login

    // 1. Afișează pagina de Login
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User()); // Obiect gol pentru formular
        return "login";
    }

    // 2. Procesează datele (Când apeși butonul Submit)
    @PostMapping("/login")
    public String processLogin(@ModelAttribute User user, HttpSession session, Model model) {
        try {
            System.out.println("Trying to login with email: " + user.getEmail());

            // TRIMITEM COMANDA LA SERVER
            // Serverul va verifica în baza de date username-ul și parola
            User loggedUser = (User) client.sendAndReceive(new Message("LOGIN", user));

            if (loggedUser != null) {
                // SUCCES!
                // Salvăm userul în sesiune ca să știm cine e pe tot site-ul
                session.setAttribute("userLogat", loggedUser);

                System.out.println("Login success: " + loggedUser.getFirstName() + " " + loggedUser.getLastName());
                return "redirect:/"; // Îl trimitem pe Home Page
            } else {
                // EȘEC (Parolă greșită)
                model.addAttribute("error", "Invalid email or password!");
                return "login"; // Rămâne pe pagina de login cu eroare
            }

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Server connection error!");
            return "login";
        }
    }

    // 3. Logout (Deconectare)
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    ///register

    // 1. Afișăm formularul de înregistrare
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User()); // Trimitem un user gol
        return "register";
    }

    // 2. Procesăm datele când userul dă click pe "Create Account"
    @PostMapping("/register")
    public String processRegister(@ModelAttribute User user,
                                  @RequestParam("confirmPassword") String confirmPassword,
                                  Model model) {
        try {
            // VALIDARE 1: Parolele să fie identice
            if (!user.getPassword().equals(confirmPassword)) {
                model.addAttribute("error", "Passwords do not match!");
                return "register"; // Rămânem pe pagină cu eroare
            }

            // VALIDARE 2: Câmpuri goale (opțional, dar recomandat)
            if (user.getEmail().isEmpty() || user.getPassword().isEmpty()) {
                model.addAttribute("error", "All fields are required!");
                return "register";
            }

            user.setRole("CLIENT");
            Object response = client.sendAndReceive(new Message("ADD_USER", user));

            if (Boolean.TRUE.equals(response)) {
                return "redirect:/login?registered"; // Succes -> Trimitem la Login
            } else {
                model.addAttribute("error", "Username or Email already exists!");
                return "register";
            }

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Server connection error: " + e.getMessage());
            return "register";
        }
    }
}
