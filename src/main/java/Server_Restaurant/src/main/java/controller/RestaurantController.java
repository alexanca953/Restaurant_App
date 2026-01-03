package controller;

import model.*;
import model.repository.*;

public class RestaurantController {
    IClientRepository clientRepository;
    IEmployeeRepository employeeRepository;
    IFeedbackRepository feedbackRepository;
    IProductRepository productRepository;
    IProductCategoryRepository productCategoryRepository;
    IReservationRepository reservationRepository;
    ITableRepository tableRepository;
    ITableReservationRepository tableReservationRepository;
    IUserRepository userRepository;
    ConcreteServer server;

    public RestaurantController() {
        this.server =new ConcreteServer(8080);
        this.clientRepository =  new ClientRepository();
        this.userRepository = new UserRepository();
        this.tableReservationRepository = new TableReservationRepository();
        this.tableRepository = new TableRepository();
        this.reservationRepository =new ReservationRepository();
        this.productCategoryRepository =new ProductCategoryRepository();
        this.productRepository = new ProductRepository();
        this.feedbackRepository =new FeedbackRepository();
        this.employeeRepository =new EmployeeRepository();
    }
}
