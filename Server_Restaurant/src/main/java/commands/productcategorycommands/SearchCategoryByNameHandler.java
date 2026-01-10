package commands.productcategorycommands;

import commands.ICommandHandler;
import restaurantproject.model.IProductCategoryRepository;
import restaurantproject.model.Message;
import restaurantproject.model.ProductCategory;

import java.util.List;

public class SearchCategoryByNameHandler implements ICommandHandler {
    private IProductCategoryRepository repo;

    public SearchCategoryByNameHandler(IProductCategoryRepository repo) {
        this.repo = repo;
    }

    @Override
    public Message execute(Object data) {
        String name = (String) data;
        List<ProductCategory> list = repo.searchCategoryByName(name);
        return new Message("SEARCH_CATEGORY_RESPONSE", list);
    }
}