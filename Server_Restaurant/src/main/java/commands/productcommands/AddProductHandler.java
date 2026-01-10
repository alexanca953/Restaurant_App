package commands.productcommands;

import commands.ICommandHandler;
import restaurantproject.model.IProductRepository;
import restaurantproject.model.Message;
import restaurantproject.model.Product;

public class AddProductHandler implements ICommandHandler {
    private IProductRepository productRepo;

    public AddProductHandler(IProductRepository productRepo) {
        this.productRepo = productRepo;
    }

    @Override
    public Message execute(Object data) {
        Product product = (Product) data;
        boolean result = productRepo.addProduct(product);
        return new Message("ADD_PRODUCT_RESPONSE", result);
    }
}