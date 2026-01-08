package commands.productcommands;

import commands.ICommandHandler;
import restaurantclient.model.IProductRepository;
import restaurantclient.model.Message;
import restaurantclient.model.Product;

public class DeleteProductHandler implements ICommandHandler {
    private IProductRepository productRepo;

    public DeleteProductHandler(IProductRepository productRepo) {
        this.productRepo = productRepo;
    }

    @Override
    public Message execute(Object data) {
        ///int productId = (int) data;
        Product product = (Product) data;
        boolean result = productRepo.deleteProduct(product.getProductId());
        return new Message("DELETE_PRODUCT_RESPONSE", result);
    }
}