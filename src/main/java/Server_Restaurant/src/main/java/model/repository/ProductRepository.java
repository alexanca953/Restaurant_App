package model.repository;

import model.IProductRepository;
import model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository implements IProductRepository {
    private Repository repository;

    public ProductRepository() {
        this.repository = new Repository();
    }

    @Override
    public boolean addProduct(Product product) {
        String sql = "INSERT INTO product (category_id, name, description, price, ingredients) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = repository.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, product.getCategoryId());
            stmt.setString(2, product.getProductName());
            stmt.setString(3, product.getDescription());
            stmt.setDouble(4, product.getPrice());
            stmt.setString(5, product.getIngredients());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteProduct(int productId) {
        String sql = "DELETE FROM product WHERE product_id = ?";
        try (Connection conn = repository.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, productId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateProduct(int productId, Product product) {
        String sql = "UPDATE product SET category_id = ?, name = ?, description = ?, price = ?, ingredients = ? WHERE product_id = ?";
        try (Connection conn = repository.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, product.getCategoryId());
            stmt.setString(2, product.getProductName());
            stmt.setString(3, product.getDescription());
            stmt.setDouble(4, product.getPrice());
            stmt.setString(5, product.getIngredients());
            stmt.setInt(6, productId);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        String sql = "SELECT * FROM product";

        try (Connection conn = repository.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                productList.add(new Product(
                        rs.getInt("product_id"),
                        rs.getInt("category_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getString("ingredients")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productList;
    }

    @Override
    public List<Product> searchProductByName(String name) {
        List<Product> productList = new ArrayList<>();
        String sql = "SELECT * FROM product WHERE name LIKE ?";

        try (Connection conn = repository.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + name + "%";
            stmt.setString(1, searchPattern);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                productList.add(new Product(
                        rs.getInt("product_id"),
                        rs.getInt("category_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getString("ingredients")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productList;
    }
}