package commands.productcategorycommands;

import commands.ICommandHandler;
import model.Message;
import model.ProductCategory;
import model.repository.ProductCategoryRepository;

public class AddCategoryHandler implements ICommandHandler {
    private ProductCategoryRepository repo;

    public AddCategoryHandler(ProductCategoryRepository repo) {
        this.repo = repo;
    }

    @Override
    public Message execute(Object data) {
        ProductCategory category = (ProductCategory) data;
        boolean result = repo.addCategory(category);
        return new Message("ADD_CATEGORY_RESPONSE", result);
    }
}