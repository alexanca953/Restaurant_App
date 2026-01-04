package commands.productcommands;

import commands.ICommandHandler;
import model.Message;
import model.repository.ProductRepository;

public class DeleteProductHandler implements ICommandHandler {
    private ProductRepository productRepo;

    public DeleteProductHandler(ProductRepository productRepo) {
        this.productRepo = productRepo;
    }

    @Override
    public Message execute(Object data) {
        int productId = (int) data;
        boolean result = productRepo.deleteProduct(productId);
        return new Message("DELETE_PRODUCT_RESPONSE", result);
    }
}