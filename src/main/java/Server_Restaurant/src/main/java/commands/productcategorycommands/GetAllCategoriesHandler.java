package commands.productcategorycommands;

import commands.ICommandHandler;
import model.Message;
import model.ProductCategory;
import model.repository.ProductCategoryRepository;
import java.util.List;

public class GetAllCategoriesHandler implements ICommandHandler {
    private ProductCategoryRepository repo;

    public GetAllCategoriesHandler(ProductCategoryRepository repo) {
        this.repo = repo;
    }

    @Override
    public Message execute(Object data) {
        List<ProductCategory> list = repo.getAllCategories();
        return new Message("GET_ALL_CATEGORIES_RESPONSE", list);
    }
}