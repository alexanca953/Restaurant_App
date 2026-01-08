package commands.productcommands;

import commands.ICommandHandler;
import restaurantclient.model.IProductRepository;
import restaurantclient.model.Message;
import restaurantclient.model.Product;

import java.util.List;

public class GetAllProductsHandler implements ICommandHandler {
    private IProductRepository productRepo;

    public GetAllProductsHandler(IProductRepository productRepo) {
        this.productRepo = productRepo;
    }

    @Override
    public Message execute(Object data) {
        List<Product> list = productRepo.getAllProducts();
        return new Message("GET_ALL_PRODUCTS_RESPONSE", list);
    }
}