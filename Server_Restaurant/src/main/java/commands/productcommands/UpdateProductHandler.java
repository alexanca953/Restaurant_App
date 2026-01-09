package commands.productcommands;

import commands.ICommandHandler;
import restaurantclient.model.IProductRepository;
import restaurantclient.model.Message;
import restaurantclient.model.Product;

public class UpdateProductHandler implements ICommandHandler {
    private IProductRepository productRepo;

    public UpdateProductHandler(IProductRepository productRepo) {
        this.productRepo = productRepo;
    }

    @Override
    public Message execute(Object data) {
        Product product = (Product) data;
        boolean result = productRepo.updateProduct(product.getProductId(), product);
        return new Message("UPDATE_PRODUCT_RESPONSE", result);
    }
}