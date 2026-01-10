package commands.productcategorycommands;

import commands.ICommandHandler;
import restaurantproject.model.IProductCategoryRepository;
import restaurantproject.model.Message;

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