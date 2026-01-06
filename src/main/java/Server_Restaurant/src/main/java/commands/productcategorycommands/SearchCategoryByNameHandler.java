package commands.productcategorycommands;

import commands.ICommandHandler;
import model.IProductCategoryRepository;
import model.Message;
import model.ProductCategory;
import model.repository.ProductCategoryRepository;
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