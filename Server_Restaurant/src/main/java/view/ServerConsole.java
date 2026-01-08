package view;

import restaurantclient.controller.RestaurantController;

public class ServerConsole {
    RestaurantController controller;
    public ServerConsole(RestaurantController controller) {
        this.controller = controller;
    }
    public ServerConsole() {
        this.controller = new RestaurantController();
    }
}
