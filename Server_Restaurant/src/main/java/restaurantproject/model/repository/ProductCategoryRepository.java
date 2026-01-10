package restaurantproject.model.repository;

import restaurantproject.model.IProductCategoryRepository;
import restaurantproject.model.ProductCategory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductCategoryRepository implements IProductCategoryRepository {
    private Repository repository;

    public ProductCategoryRepository() {
        this.repository = new Repository();
    }

    @Override
    public boolean addCategory(ProductCategory category) {
        String sql = "INSERT INTO product_category (name) VALUES (?)";
        try (Connection conn = repository.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, category.getName());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteCategory(int categoryId) {
        String deleteProductsSql = "DELETE FROM product WHERE category_id = ?";
        String deleteCategorySql = "DELETE FROM product_category WHERE category_id = ?";
        Connection conn = null;
        PreparedStatement deleteProdsStmt = null;
        PreparedStatement deleteCatStmt = null;

        try {
            conn = repository.getConnection();
            conn.setAutoCommit(false);
            deleteProdsStmt = conn.prepareStatement(deleteProductsSql);
            deleteProdsStmt.setInt(1, categoryId);
            deleteProdsStmt.executeUpdate();
            deleteCatStmt = conn.prepareStatement(deleteCategorySql);
            deleteCatStmt.setInt(1, categoryId);
            int affectedRows = deleteCatStmt.executeUpdate();
            conn.commit();
            return affectedRows > 0;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (deleteProdsStmt != null) deleteProdsStmt.close();
                if (deleteCatStmt != null) deleteCatStmt.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean updateCategory(int categoryId, ProductCategory category) {
        String sql = "UPDATE product_category SET name = ? WHERE category_id = ?";
        try (Connection conn = repository.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, category.getName());
            stmt.setInt(2, categoryId);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<ProductCategory> getAllCategories() {
        List<ProductCategory> list = new ArrayList<>();
        String sql = "SELECT * FROM product_category";

        try (Connection conn = repository.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new ProductCategory(
                        rs.getInt("category_id"),
                        rs.getString("name")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<ProductCategory> searchCategoryByName(String name) {
        List<ProductCategory> list = new ArrayList<>();
        String sql = "SELECT * FROM product_category WHERE name LIKE ?";

        try (Connection conn = repository.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + name + "%";
            stmt.setString(1, searchPattern);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(new ProductCategory(
                        rs.getInt("category_id"),
                        rs.getString("name")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}