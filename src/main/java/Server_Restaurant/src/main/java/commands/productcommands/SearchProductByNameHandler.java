package commands.productcommands;

import commands.ICommandHandler;
import model.IProductRepository;
import model.Message;
import model.Product;
import model.repository.ProductRepository;
import java.util.List;

public class SearchProductByNameHandler implements ICommandHandler {
    private IProductRepository productRepo;

    public SearchProductByNameHandler(IProductRepository productRepo) {
        this.productRepo = productRepo;
    }

    @Override
    public Message execute(Object data) {
        String name = (String) data;
        List<Product> list = productRepo.searchProductByName(name);
        return new Message("SEARCH_PRODUCT_RESPONSE", list);
    }
}