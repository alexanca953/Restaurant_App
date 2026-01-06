package commands.productcategorycommands;

import commands.ICommandHandler;
import model.IProductCategoryRepository;
import model.Message;
import model.ProductCategory;
import model.repository.ProductCategoryRepository;
import java.util.List;

public class GetAllCategoriesHandler implements ICommandHandler {
    private IProductCategoryRepository repo;

    public GetAllCategoriesHandler(IProductCategoryRepository repo) {
        this.repo = repo;
    }

    @Override
    public Message execute(Object data) {
        List<ProductCategory> list = repo.getAllCategories();
        return new Message("GET_ALL_CATEGORIES_RESPONSE", list);
    }
}