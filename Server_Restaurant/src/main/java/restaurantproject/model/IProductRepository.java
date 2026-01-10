package restaurantproject.model;

import java.util.List;

public interface IProductRepository {
    boolean addProduct(Product product);
    boolean deleteProduct(int productId);
    boolean updateProduct(int productId, Product product);

    List<Product> getAllProducts();
    List<Product> searchProductByName(String name);
}