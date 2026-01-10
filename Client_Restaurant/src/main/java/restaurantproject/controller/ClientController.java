package restaurantproject.controller;

import jakarta.servlet.http.HttpSession;
import restaurantproject.model.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.beans.PropertyEditorSupport;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Controller
public class ClientController {
    ConcreteClient client = new ConcreteClient();

    // --- PAZNICUL (HELPER PENTRU SECURITATE) ---
    // Această metodă verifică dacă userul e logat și dacă are rolul potrivit.
    // Returnează un link de redirect (spre login sau access-denied) sau NULL dacă e totul ok.
    private String checkAccess(HttpSession session, String... allowedRoles) {
        User user = (User) session.getAttribute("userLogat");
        if (user == null) {
            return "redirect:/login";
        }
        for (String role : allowedRoles) {
            if (role.equals(user.getRole())) {
                return null; // Acces permis!
            }
        }
        return "redirect:/access-denied"; // Acces interzis!
    }

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
        return "home"; // Pagina publică, accesibilă oricui
    }

    @GetMapping("/access-denied")
    public String showAccessDenied() {
        return "access-denied";
    }

    // ==========================================
    // 1. DASHBOARD (Admin, Manager, Waiter)
    // ==========================================
    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session) {
        // Paznic: Doar angajații au voie
        String redirect = checkAccess(session, "ADMIN", "MANAGER", "WAITER");
        if (redirect != null) return redirect;

        return "dashboard";
    }

    // ==========================================
    // 2. MENU MANAGEMENT (Admin, Manager)
    // ==========================================
    @GetMapping("/menu-management")
    public String showManager(Model model, HttpSession session) {
        // Paznic: Doar Admin si Manager pot modifica meniul
        String redirect = checkAccess(session, "ADMIN", "MANAGER");
        if (redirect != null) return redirect;

        ArrayList<Product> products = null;
        try {
            products = (ArrayList<Product>) client.sendAndReceive(new Message("GET_ALL_PRODUCTS",null));
            ArrayList<ProductCategory> categories= (ArrayList<ProductCategory>) client.sendAndReceive(new Message("GET_ALL_CATEGORIES",null));
            model.addAttribute("categories", categories);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(products!=null) {
            model.addAttribute("products", products);
        }
        return "menu-management";
    }

    @PostMapping("/menu-management/save")
    public String saveProduct(Product product, HttpSession session) {
        // Paznic si pe POST (ca sa nu dea request din Postman)
        String redirect = checkAccess(session, "ADMIN", "MANAGER");
        if (redirect != null) return redirect;

        try {
            if (product.getProductId() == 0) {
                client.sendAndReceive(new Message("ADD_PRODUCT",product));
            } else {
                client.sendAndReceive(new Message("UPDATE_PRODUCT",product));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/menu-management";
    }

    @PostMapping("/menu-management/delete")
    public String deleteProduct(@RequestParam("productId") int id, HttpSession session) {
        String redirect = checkAccess(session, "ADMIN", "MANAGER");
        if (redirect != null) return redirect;

        try{
            Product product = new Product();
            product.setProductId(id);
            client.sendAndReceive(new Message("DELETE_PRODUCT",product));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/menu-management";
    }

    @PostMapping("/menu-management/save-category")
    public String saveCategory(ProductCategory category, HttpSession session) {
        String redirect = checkAccess(session, "ADMIN", "MANAGER");
        if (redirect != null) return redirect;

        try{
            client.sendAndReceive(new Message("ADD_CATEGORY",category));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/menu-management";
    }

    @PostMapping("/menu-management/delete-category")
    public String deleteCategory(@RequestParam("categoryId") int categoryId, HttpSession session) {
        String redirect = checkAccess(session, "ADMIN", "MANAGER");
        if (redirect != null) return redirect;

        try {
            client.sendAndReceive(new Message("DELETE_CATEGORY",categoryId));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/menu-management";
    }

    // ==========================================
    // 3. PUBLIC MENU (Oricine)
    // ==========================================
    @GetMapping("/menu")
    public String showMenu(@RequestParam(name = "keyword", required = false) String keyword, Model model) {
        // Aici NU punem paznic, meniul e public pentru clienti
        ArrayList<Product> products=new ArrayList<>();
        ArrayList<ProductCategory> categories = new ArrayList<>();

        try{
            products=(ArrayList<Product>) client.sendAndReceive(new Message("GET_ALL_PRODUCTS",null));
            categories=(ArrayList<ProductCategory>) client.sendAndReceive(new Message("GET_ALL_CATEGORIES",null));
        } catch(Exception e){
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

    // ==========================================
    // 4. RESERVATIONS (INTERNAL - Waiter, Manager, Admin)
    // ==========================================
    @GetMapping("/reservations")
    public String showReservations(Model model, HttpSession session) {
        // Paznic: Doar angajatii vad rezervarile interne
        String redirect = checkAccess(session, "ADMIN", "MANAGER", "WAITER");
        if (redirect != null) return redirect;

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
    public String saveReservation(@ModelAttribute Reservation reservation, HttpSession session) {
        String redirect = checkAccess(session, "ADMIN", "MANAGER", "WAITER");
        if (redirect != null) return redirect;

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
    public String deleteReservation(@RequestParam("reservationId") int id, HttpSession session) {
        String redirect = checkAccess(session, "ADMIN", "MANAGER", "WAITER");
        if (redirect != null) return redirect;

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

    // ==========================================
    // 5. TABLES (Waiter - View, Manager/Admin - Edit)
    // ==========================================
    @GetMapping("/tables")
    public String showTablesDashboard(Model model, HttpSession session) {
        // Toti angajatii pot vedea mesele
        String redirect = checkAccess(session, "ADMIN", "MANAGER", "WAITER");
        if (redirect != null) return redirect;

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

    // ==========================================
    // 6. USERS (ADMIN ONLY)
    // ==========================================
    @GetMapping("/users")
    public String showAdminUsers(Model model, HttpSession session) {
        // STRICT ADMIN
        String redirect = checkAccess(session, "ADMIN");
        if (redirect != null) return redirect;

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
    public String saveUser(@ModelAttribute User user, HttpSession session) {
        String redirect = checkAccess(session, "ADMIN");
        if (redirect != null) return redirect;

        try {
            String command = (user.getUserId() == 0) ? "ADD_USER" : "UPDATE_USER";
            client.sendAndReceive(new Message(command, user));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/users";
    }

    @PostMapping("/users/delete")
    public String deleteUser(@RequestParam("userId") int userId, HttpSession session) {
        String redirect = checkAccess(session, "ADMIN");
        if (redirect != null) return redirect;

        try {
            client.sendAndReceive(new Message("DELETE_USER", userId));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/users";
    }

    // ==========================================
    // LOGIN & REGISTER (PUBLIC)
    // ==========================================
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

    // ==========================================
    // FEEDBACK
    // ==========================================
    @PostMapping("/submit-feedback")
    public String submitFeedback(@ModelAttribute Feedback feedback, HttpSession session) {
        User userLogat = (User) session.getAttribute("userLogat");
        if (userLogat == null) {
            return "redirect:/login";
        }
        feedback.setClientId(userLogat.getUserId());
        feedback.setDateTime(LocalDateTime.now());

        try {
            System.out.println("Se trimite feedback de la user ID: " + userLogat.getUserId());
            client.sendAndReceive(new Message("ADD_FEEDBACK", feedback));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/?feedbackSuccess";
    }

    // ==========================================
    // TABLE CONFIGURATION (MANAGER/ADMIN Only)
    // ==========================================
    @GetMapping("/table-management")
    public String showTableManagement(Model model, HttpSession session) {
        // Paznic: Waiterul nu are voie sa stearga mese din sistem, doar Manager/Admin
        String redirect = checkAccess(session, "ADMIN", "MANAGER");
        if (redirect != null) return redirect;

        try {
            Object response = client.sendAndReceive(new Message("GET_ALL_TABLES", null));
            if (response instanceof List) {
                List<Table> tables = (List<Table>) response;
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
    public String saveTable(Table table, HttpSession session) {
        String redirect = checkAccess(session, "ADMIN", "MANAGER");
        if (redirect != null) return redirect;

        try {
            String command = (table.getTableId() == 0) ? "ADD_TABLE" : "UPDATE_TABLE";
            client.sendAndReceive(new Message(command, table));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/table-management";
    }

    @PostMapping("/table-management/delete")
    public String deleteTable(@RequestParam("tableId") int tableId, HttpSession session) {
        String redirect = checkAccess(session, "ADMIN", "MANAGER");
        if (redirect != null) return redirect;

        try {
            client.sendAndReceive(new Message("DELETE_TABLE", tableId));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/table-management";
    }

    // ==========================================
    // STATISTICS (ADMIN/MANAGER Only)
    // ==========================================
    @GetMapping("/statistics")
    public String showStatistics(Model model, HttpSession session) {
        // Paznic: Waiterul nu vede banii
        String redirect = checkAccess(session, "ADMIN", "MANAGER");
        if (redirect != null) return redirect;

        List<Feedback> allFeedbacks = new ArrayList<>();
        try {
            Object response = client.sendAndReceive(new Message("GET_ALL_FEEDBACKS", null));
            if (response instanceof List) {
                allFeedbacks = (List<Feedback>) response;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        double sumFood=0, sumServ=0, sumAmb=0, sumGen=0, sumTotal=0;
        int cntFood=0, cntServ=0, cntAmb=0, cntGen=0;

        for (Feedback f : allFeedbacks) {
            sumTotal += f.getScore();

            if ("Food".equalsIgnoreCase(f.getType())) { sumFood += f.getScore(); cntFood++; }
            else if ("Service".equalsIgnoreCase(f.getType())) { sumServ += f.getScore(); cntServ++; }
            else if ("Ambiance".equalsIgnoreCase(f.getType())) { sumAmb += f.getScore(); cntAmb++; }
            else if ("General".equalsIgnoreCase(f.getType())) { sumGen += f.getScore(); cntGen++; }
        }
        double avgFood = cntFood > 0 ? sumFood / cntFood : 0;
        double avgServ = cntServ > 0 ? sumServ / cntServ : 0;
        double avgAmb  = cntAmb  > 0 ? sumAmb  / cntAmb  : 0;
        double avgGen  = cntGen  > 0 ? sumGen  / cntGen  : 0;

        double avgTotal = allFeedbacks.isEmpty() ? 0 : sumTotal / allFeedbacks.size();
        List<Double> stats = Arrays.asList(avgFood, avgServ, avgAmb, avgGen, avgTotal);
        model.addAttribute("chartStats", stats);
        model.addAttribute("averageScore", String.format("%.1f", avgTotal));
        model.addAttribute("totalReviews", allFeedbacks.size());
        allFeedbacks.sort((f1, f2) -> f2.getDateTime().compareTo(f1.getDateTime()));
        model.addAttribute("feedbacks", allFeedbacks);

        return "statistics";
    }

    // ==========================================
    // ZONA REZERVĂRI PENTRU CLIENT (Numai pentru USERI logați)
    // ==========================================
    @GetMapping("/clientReservation")
    public String showReservationForm(HttpSession session, Model model) {
        User userLogat = (User) session.getAttribute("userLogat");
        if (userLogat == null) {
            return "redirect:/login";
        }
        model.addAttribute("clientReservation", new Reservation());
        return "clientReservation";
    }

    @PostMapping("/clientReservation/submit")
    public String submitReservation(@ModelAttribute Reservation reservation, HttpSession session) {
        User userLogat = (User) session.getAttribute("userLogat");
        if (userLogat == null) {
            return "redirect:/login";
        }

        try {
            reservation.setClientId(userLogat.getUserId());
            String fullName = userLogat.getFirstName() + " " + userLogat.getLastName();
            reservation.setTempClientName(fullName);
            reservation.setTempClientPhone(userLogat.getPhoneNumber());
            reservation.setStatus("PENDING");
            reservation.setTableId(0);

            System.out.println("Sending reservation for: " + fullName);
            client.sendAndReceive(new Message("ADD_RESERVATION", reservation));

            return "redirect:/?reservationSuccess";

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/reservation?error=server";
        }
    }

    @GetMapping("/clientReservationList")
    public String showClientReservations(Model model, @SessionAttribute(value = "userLogat", required = false) User userLogat) {
        if (userLogat == null) {
            return "redirect:/login";
        }

        List<Reservation> myReservations = new ArrayList<>();
        try {
            System.out.println("Cer rezervări pentru client ID: " + userLogat.getUserId());
            Object response = client.sendAndReceive(new Message("GET_CLIENT_RESERVATIONS", userLogat.getUserId()));

            if (response instanceof List) {
                List<Reservation> serverList = (List<Reservation>) response;
                myReservations = new ArrayList<>(serverList);
                myReservations.sort((r1, r2) -> {
                    if (r1.getDateTime() == null || r2.getDateTime() == null) return 0;
                    return r2.getDateTime().compareTo(r1.getDateTime());
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("myReservations", myReservations);
        return "clientReservationList";
    }
}