package commands.productcategorycommands;

import commands.ICommandHandler;
import restaurantclient.model.IProductCategoryRepository;
import restaurantclient.model.Message;
import restaurantclient.model.ProductCategory;

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