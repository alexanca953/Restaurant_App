package restaurantproject.controller;

import commands.ICommandHandler;
import commands.usercommands.*;
import commands.tablecommands.*;
import commands.tablereservationcommands.*;
import commands.reservationcommands.*;
import commands.productcommands.*;
import commands.productcategorycommands.*;
import commands.feedbackcommands.*;
import restaurantproject.model.*;
import restaurantproject.model.repository.*;
import ocsf.ConnectionToClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RestaurantController {
    private IFeedbackRepository feedbackRepository;
    private IProductRepository productRepository;
    private IProductCategoryRepository productCategoryRepository;
    private IReservationRepository reservationRepository;
    private ITableRepository tableRepository;
    private ITableReservationRepository tableReservationRepository;
    private IUserRepository userRepository;

    private ConcreteServer server;
    private Map<String, ICommandHandler> handlers;

    public RestaurantController() {
        this.server =new ConcreteServer(8080);
        this.server.setController(this);
        this.userRepository = new UserRepository();
        this.tableReservationRepository = new TableReservationRepository();
        this.tableRepository = new TableRepository();
        this.reservationRepository =new ReservationRepository();
        this.productCategoryRepository =new ProductCategoryRepository();
        this.productRepository = new ProductRepository();
        this.feedbackRepository =new FeedbackRepository();
        initHandlers();

        try {
            server.listen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void initHandlers() {
        this.handlers = new HashMap<>();

        // --- User Commands ---
        handlers.put("ADD_USER", new AddUserHandler(userRepository));
        handlers.put("DELETE_USER", new DeleteUserHandler(userRepository));
        handlers.put("GET_ALL_USERS", new GetAllUsersHandler(userRepository));
        handlers.put("GET_USER_BY_EMAIL", new GetUserByEmailHandler(userRepository));
        handlers.put("GET_USER_BY_ROLE", new GetUsersByRoleHandler(userRepository));
        handlers.put("LOGIN", new LoginHandler(userRepository));
        handlers.put("SEARCH_USER_BY_NAME", new SearchUserByNameHandler(userRepository));
        handlers.put("UPDATE_USER", new UpdateUserHandler(userRepository));
        // --- Feedback Commands ---
        handlers.put("ADD_FEEDBACK", new AddFeedbackHandler(feedbackRepository));
        handlers.put("DELETE_FEEDBACK", new DeleteFeedbackHandler(feedbackRepository));
        handlers.put("GET_ALL_FEEDBACKS", new GetAllFeedbacksHandler(feedbackRepository));

        // --- Product Category Commands ---
        handlers.put("ADD_CATEGORY", new AddCategoryHandler(productCategoryRepository));
        handlers.put("DELETE_CATEGORY", new DeleteCategoryHandler(productCategoryRepository));
        handlers.put("GET_ALL_CATEGORIES", new GetAllCategoriesHandler(productCategoryRepository));
        handlers.put("SEARCH_CATEGORY_BY_NAME", new SearchCategoryByNameHandler(productCategoryRepository));
        handlers.put("UPDATE_CATEGORY", new UpdateCategoryHandler(productCategoryRepository));

        // --- Product Commands ---
        handlers.put("ADD_PRODUCT", new AddProductHandler(productRepository));
        handlers.put("DELETE_PRODUCT", new DeleteProductHandler(productRepository));
        handlers.put("GET_ALL_PRODUCTS", new GetAllProductsHandler(productRepository));
        handlers.put("SEARCH_PRODUCT_BY_NAME", new SearchProductByNameHandler(productRepository));
        handlers.put("UPDATE_PRODUCT", new UpdateProductHandler(productRepository));

        // --- Reservation Commands ---
        handlers.put("ADD_RESERVATION", new AddReservationHandler(reservationRepository));
        handlers.put("DELETE_RESERVATION", new DeleteReservationHandler(reservationRepository));
        handlers.put("GET_ALL_RESERVATIONS", new GetAllReservationsHandler(reservationRepository));
        handlers.put("GET_RESERVATIONS_MAP", new GetAllReservationsHandlerMap(reservationRepository));
        handlers.put("GET_CLIENT_RESERVATIONS", new GetClientReservationsHandler(reservationRepository));
        handlers.put("UPDATE_RESERVATION", new UpdateReservationHandler(reservationRepository));

        // --- Table Commands ---
        handlers.put("ADD_TABLE", new AddTableHandler(tableRepository));
        handlers.put("DELETE_TABLE", new DeleteTableHandler(tableRepository));
        handlers.put("GET_ALL_TABLES", new GetAllTablesHandler(tableRepository));
        handlers.put("UPDATE_TABLE", new UpdateTableHandler(tableRepository));

        // --- Table Reservation Commands ---
        handlers.put("ADD_TABLE_RESERVATION", new AddTableReservationHandler(tableReservationRepository));
        handlers.put("DELETE_TABLE_RESERVATION", new DeleteTableReservationHandler(tableReservationRepository));
        handlers.put("GET_ALL_TABLE_RESERVATIONS", new GetAllTableReservationsHandler(tableReservationRepository));
        handlers.put("UPDATE_TABLE_RESERVATION", new UpdateTableReservationHandler(tableReservationRepository));
    }
    public void processRequest(Message msg, ConnectionToClient client) {
        try {

            if (handlers.get(msg.getCommand()) != null) {
                ICommandHandler command = (ICommandHandler) handlers.get(msg.getCommand()) ;
                Message result = command.execute(msg.getData());
                System.out.println("res= "+result.toString());
                client.sendToClient(result);
            }
            else
            {
                System.out.println("is not an Icommand instance!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public IFeedbackRepository getFeedbackRepository() {
        return feedbackRepository;
    }

    public IProductRepository getProductRepository() {
        return productRepository;
    }

    public IProductCategoryRepository getProductCategoryRepository() {
        return productCategoryRepository;
    }

    public IReservationRepository getReservationRepository() {
        return reservationRepository;
    }

    public ITableRepository getTableRepository() {
        return tableRepository;
    }

    public ITableReservationRepository getTableReservationRepository() {
        return tableReservationRepository;
    }

    public IUserRepository getUserRepository() {
        return userRepository;
    }

    public ConcreteServer getServer() {
        return server;
    }



    public void setFeedbackRepository(IFeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    public void setProductRepository(IProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void setProductCategoryRepository(IProductCategoryRepository productCategoryRepository) {
        this.productCategoryRepository = productCategoryRepository;
    }

    public void setReservationRepository(IReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public void setTableRepository(ITableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }

    public void setTableReservationRepository(ITableReservationRepository tableReservationRepository) {
        this.tableReservationRepository = tableReservationRepository;
    }

    public void setUserRepository(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void setServer(ConcreteServer server) {
        this.server = server;
    }
}
