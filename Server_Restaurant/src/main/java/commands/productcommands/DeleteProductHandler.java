package commands.productcommands;

import commands.ICommandHandler;
import model.IProductRepository;
import model.Message;

public class DeleteProductHandler implements ICommandHandler {
    private IProductRepository productRepo;

    public DeleteProductHandler(IProductRepository productRepo) {
        this.productRepo = productRepo;
    }

    @Override
    public Message execute(Object data) {
        int productId = (int) data;
        boolean result = productRepo.deleteProduct(productId);
        return new Message("DELETE_PRODUCT_RESPONSE", result);
    }
}