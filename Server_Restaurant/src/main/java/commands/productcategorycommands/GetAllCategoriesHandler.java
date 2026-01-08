package commands.productcategorycommands;

import commands.ICommandHandler;
import restaurantclient.model.IProductCategoryRepository;
import restaurantclient.model.Message;
import restaurantclient.model.ProductCategory;

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