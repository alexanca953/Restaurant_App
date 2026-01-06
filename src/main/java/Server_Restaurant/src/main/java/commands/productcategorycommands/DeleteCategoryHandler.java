package commands.productcategorycommands;

import commands.ICommandHandler;
import model.IProductCategoryRepository;
import model.Message;
import model.repository.ProductCategoryRepository;

public class DeleteCategoryHandler implements ICommandHandler {
    private IProductCategoryRepository repo;

    public DeleteCategoryHandler(IProductCategoryRepository repo) {
        this.repo = repo;
    }

    @Override
    public Message execute(Object data) {
        int categoryId = (int) data;
        boolean result = repo.deleteCategory(categoryId);
        return new Message("DELETE_CATEGORY_RESPONSE", result);
    }
}