package commands.productcommands;

import commands.ICommandHandler;
import model.IProductRepository;
import model.Message;
import model.Product;
import model.repository.ProductRepository;

public class UpdateProductHandler implements ICommandHandler {
    private IProductRepository productRepo;

    public UpdateProductHandler(IProductRepository productRepo) {
        this.productRepo = productRepo;
    }

    @Override
    public Message execute(Object data) {
        Product product = (Product) data;
        // Presupunem ca obiectul product are ID-ul setat corect
        boolean result = productRepo.updateProduct(product.getProductId(), product);
        return new Message("UPDATE_PRODUCT_RESPONSE", result);
    }
}