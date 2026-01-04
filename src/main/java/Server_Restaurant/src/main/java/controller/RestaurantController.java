package controller;

import commands.ICommandHandler;
import model.*;
import model.repository.*;
import ocsf.ConnectionToClient;

import java.io.IOException;

public class RestaurantController {
    private IClientRepository clientRepository;
    private IEmployeeRepository employeeRepository;
    private IFeedbackRepository feedbackRepository;
    private IProductRepository productRepository;
    private IProductCategoryRepository productCategoryRepository;
    private IReservationRepository reservationRepository;
    private ITableRepository tableRepository;
    private ITableReservationRepository tableReservationRepository;
    private IUserRepository userRepository;

    private ConcreteServer server;

    public RestaurantController() {
        this.server =new ConcreteServer(8080);
        this.server.setController(this);

        this.clientRepository =  new ClientRepository();
        this.userRepository = new UserRepository();
        this.tableReservationRepository = new TableReservationRepository();
        this.tableRepository = new TableRepository();
        this.reservationRepository =new ReservationRepository();
        this.productCategoryRepository =new ProductCategoryRepository();
        this.productRepository = new ProductRepository();
        this.feedbackRepository =new FeedbackRepository();
        this.employeeRepository =new EmployeeRepository();

        try {
            server.listen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void processRequest(Object msg, ConnectionToClient client) {
        try {
            if (msg instanceof ICommandHandler) {
                ICommandHandler command = (ICommandHandler) msg;
                Object result = command.execute(this);
                client.sendToClient(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public IClientRepository getClientRepository() {
        return clientRepository;
    }

    public IEmployeeRepository getEmployeeRepository() {
        return employeeRepository;
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

    public void setClientRepository(IClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public void setEmployeeRepository(IEmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
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
