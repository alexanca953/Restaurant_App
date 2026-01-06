package commands.productcategorycommands;

import commands.ICommandHandler;
import model.IProductCategoryRepository;
import model.Message;
import model.ProductCategory;
import model.repository.ProductCategoryRepository;

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