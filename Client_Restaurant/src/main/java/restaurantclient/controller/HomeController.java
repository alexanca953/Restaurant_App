package restaurantclient.controller;

import jakarta.servlet.http.HttpSession;
import restaurantclient.model.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import java.beans.PropertyEditorSupport;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class HomeController {
    ConcreteClient client = new ConcreteClient();

    @ModelAttribute
    public void addGlobalAttributes(HttpSession session, Model model) {
        User user = (User) session.getAttribute("userLogat");
        if (user != null) {
            model.addAttribute("userLogat", user);
            model.addAttribute("role", user.getRole());
        }
    }
    @GetMapping("/")
    public String home(Model model) {
        return "home";
    }
    @GetMapping("/dashboard")
    public String showDashboard() {
        return "dashboard";
    }
    @GetMapping("/menu-management")
    public String showManager(Model model) {
        ArrayList<Product> products = null;
        try {
            products = (ArrayList<Product>) client.sendAndReceive(new Message("GET_ALL_PRODUCTS",null));
            ArrayList<ProductCategory> categories= (ArrayList<ProductCategory>) client.sendAndReceive(new Message("GET_ALL_CATEGORIES",null));
            model.addAttribute("categories", categories);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if(products!=null)
        {
            model.addAttribute("products", products);
        }
        return "menu-management";
    }
    @PostMapping("/menu-management/save")
    public String saveProduct(Product product) {
        try {
            if (product.getProductId() == 0) {
                client.sendAndReceive(new Message("ADD_PRODUCT",product));
            }
            else {
                client.sendAndReceive(new Message("UPDATE_PRODUCT",product));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
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
        catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/menu-management";
    }

    @PostMapping("/menu-management/save-category")
    public String saveCategory(ProductCategory category) {
        try{
            client.sendAndReceive(new Message("ADD_CATEGORY",category));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/menu-management";
    }

    @PostMapping("/menu-management/delete-category")
    public String deleteCategory(@RequestParam("categoryId") int categoryId) {
        try {
            client.sendAndReceive(new Message("DELETE_CATEGORY",categoryId));
        }
        catch (Exception e) {
            e.printStackTrace();
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
            e.printStackTrace();
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
            client.sendAndReceive(new Message("DELETE_RESERVATION", r));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/reservations";
    }

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
        tables.sort(Comparator.comparingInt(Table::getTableNumber));
        model.addAttribute("tables", tables);
        model.addAttribute("reservationsMap", reservationsPerTable);

        return "tables";
    }

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

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@ModelAttribute User user, HttpSession session, Model model) {
        try {
            User loggedUser = (User) client.sendAndReceive(new Message("LOGIN", user));

            if (loggedUser != null) {
                session.setAttribute("userLogat", loggedUser);
                if(loggedUser.getRole().equals("CLIENT"))
                    return "redirect:/";
                else if(loggedUser.getRole().equals("MANAGER")||loggedUser.getRole().equals("WAITER")||loggedUser.getRole().equals("ADMIN"))
                    return "redirect:/dashboard";
                else
                    return "redirect:/";
            } else {
                model.addAttribute("error", "Invalid email or password!");
                return "login";
            }

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Server connection error!");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String processRegister(@ModelAttribute User user,
                                  @RequestParam("confirmPassword") String confirmPassword,
                                  Model model) {
        try {
            if (!user.getPassword().equals(confirmPassword)) {
                model.addAttribute("error", "Passwords do not match!");
                return "register";
            }
            if (user.getEmail().isEmpty() || user.getPassword().isEmpty()) {
                model.addAttribute("error", "All fields are required!");
                return "register";
            }

            user.setRole("CLIENT");
            Object response = client.sendAndReceive(new Message("ADD_USER", user));

            if (Boolean.TRUE.equals(response)) {
                return "redirect:/login?registered";
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

    @PostMapping("/submit-feedback")
    public String submitFeedback(@ModelAttribute Feedback feedback, @SessionAttribute("userLogat") User userLogat) {
        feedback.setClientId(userLogat.getUserId());
        feedback.setDateTime(LocalDateTime.now());
        try {
            client.sendAndReceive(new Message("ADD_FEEDBACK", feedback));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/?feedbackSuccess";
    }
    ///table
    // === TABLE MANAGEMENT (MANAGER ONLY) ===

    @GetMapping("/table-management")
    public String showTableManagement(Model model) {
        try {

            Object response = client.sendAndReceive(new Message("GET_ALL_TABLES", null));
            if (response instanceof List) {
                List<Table> tables = (List<Table>) response;
                // Le sortăm frumos după numărul mesei
                tables.sort(Comparator.comparingInt(Table::getTableNumber));
                model.addAttribute("tables", tables);
            } else {
                model.addAttribute("tables", new ArrayList<>());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "table-management";
    }

    @PostMapping("/table-management/save")
    public String saveTable(Table table) {
        try {
            // Logica specială cerută:
            // Dacă e UPDATE și capacitatea scade, ar trebui să verificăm rezervările.
            // Pentru moment, facem update direct. Dacă vrei să ștergi rezervările,
            // poți trimite un flag către server sau să faci logica aia în Handler.

            String command = (table.getTableId() == 0) ? "ADD_TABLE" : "UPDATE_TABLE";
            client.sendAndReceive(new Message(command, table));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/table-management";
    }

    @PostMapping("/table-management/delete")
    public String deleteTable(@RequestParam("tableId") int tableId) {
        try {
            // Asta va șterge masa și (datorită CASCADE din SQL) legăturile cu rezervările
            client.sendAndReceive(new Message("DELETE_TABLE", tableId));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/table-management";
    }
}