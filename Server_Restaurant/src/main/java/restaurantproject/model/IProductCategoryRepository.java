package restaurantproject.model;

import java.util.List;

public interface IProductCategoryRepository {
    boolean addCategory(ProductCategory category);
    boolean deleteCategory(int categoryId);
    boolean updateCategory(int categoryId, ProductCategory category);

    List<ProductCategory> getAllCategories();
    List<ProductCategory> searchCategoryByName(String name);
}