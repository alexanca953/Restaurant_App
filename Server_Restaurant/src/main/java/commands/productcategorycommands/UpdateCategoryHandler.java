package commands.productcategorycommands;

import commands.ICommandHandler;
import restaurantproject.model.IProductCategoryRepository;
import restaurantproject.model.Message;
import restaurantproject.model.ProductCategory;

public class UpdateCategoryHandler implements ICommandHandler {
    private IProductCategoryRepository repo;

    public UpdateCategoryHandler(IProductCategoryRepository repo) {
        this.repo = repo;
    }

    @Override
    public Message execute(Object data) {
        ProductCategory category = (ProductCategory) data;
        boolean result = repo.updateCategory(category.getCategoryId(), category);
        return new Message("UPDATE_CATEGORY_RESPONSE", result);
    }
}