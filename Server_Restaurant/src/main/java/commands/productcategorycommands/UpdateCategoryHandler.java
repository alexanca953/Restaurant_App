package commands.productcategorycommands;

import commands.ICommandHandler;
import restaurantclient.model.IProductCategoryRepository;
import restaurantclient.model.Message;
import restaurantclient.model.ProductCategory;

public class UpdateCategoryHandler implements ICommandHandler {
    private IProductCategoryRepository repo;

    public UpdateCategoryHandler(IProductCategoryRepository repo) {
        this.repo = repo;
    }

    @Override
    public Message execute(Object data) {
        ProductCategory category = (ProductCategory) data;
        // Obiectul category trebuie sa aiba ID-ul setat
        boolean result = repo.updateCategory(category.getCategoryId(), category);
        return new Message("UPDATE_CATEGORY_RESPONSE", result);
    }
}