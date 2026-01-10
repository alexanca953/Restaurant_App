package commands.productcategorycommands;

import commands.ICommandHandler;
import restaurantproject.model.IProductCategoryRepository;
import restaurantproject.model.Message;
import restaurantproject.model.ProductCategory;

public class AddCategoryHandler implements ICommandHandler {
    private IProductCategoryRepository repo;

    public AddCategoryHandler(IProductCategoryRepository repo) {
        this.repo = repo;
    }

    @Override
    public Message execute(Object data) {
        ProductCategory category = (ProductCategory) data;
        boolean result = repo.addCategory(category);
        return new Message("ADD_CATEGORY_RESPONSE", result);
    }
}