package commands.productcommands;

import commands.ICommandHandler;
import model.Message;
import model.Product;
import model.repository.ProductRepository;

public class AddProductHandler implements ICommandHandler {
    private ProductRepository productRepo;

    public AddProductHandler(ProductRepository productRepo) {
        this.productRepo = productRepo;
    }

    @Override
    public Message execute(Object data) {
        Product product = (Product) data;
        boolean result = productRepo.addProduct(product);
        return new Message("ADD_PRODUCT_RESPONSE", result);
    }
}