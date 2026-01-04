package commands.productcategorycommands;

import commands.ICommandHandler;
import model.Message;
import model.repository.ProductCategoryRepository;

public class DeleteCategoryHandler implements ICommandHandler {
    private ProductCategoryRepository repo;

    public DeleteCategoryHandler(ProductCategoryRepository repo) {
        this.repo = repo;
    }

    @Override
    public Message execute(Object data) {
        int categoryId = (int) data;
        boolean result = repo.deleteCategory(categoryId);
        return new Message("DELETE_CATEGORY_RESPONSE", result);
    }
}