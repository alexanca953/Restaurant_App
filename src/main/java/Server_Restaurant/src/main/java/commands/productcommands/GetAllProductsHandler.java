package commands.productcommands;

import commands.ICommandHandler;
import model.Message;
import model.Product;
import model.repository.ProductRepository;
import java.util.List;

public class GetAllProductsHandler implements ICommandHandler {
    private ProductRepository productRepo;

    public GetAllProductsHandler(ProductRepository productRepo) {
        this.productRepo = productRepo;
    }

    @Override
    public Message execute(Object data) {
        List<Product> list = productRepo.getAllProducts();
        return new Message("GET_ALL_PRODUCTS_RESPONSE", list);
    }
}