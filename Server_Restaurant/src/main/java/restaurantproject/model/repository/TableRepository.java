package restaurantproject.model.repository;
import restaurantproject.model.ITableRepository;
import restaurantproject.model.Table;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TableRepository implements ITableRepository {
    private Repository repository;
    public TableRepository() {
        this.repository = new Repository();
    }
    public boolean addTable(Table table) {
        String sql = "INSERT INTO restaurant_table (table_number, capacity, is_occupied) VALUES (?, ?, ?)";
        try (Connection conn = repository.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, table.getTableNumber());
            stmt.setInt(2, table.getCapacity());
            stmt.setBoolean(3, false);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean updateTable(Table table) {
        String sql = "UPDATE restaurant_table SET table_number=?, capacity=?, status=? WHERE table_id=?";
        try (Connection conn = repository.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, table.getTableNumber());
            stmt.setInt(2, table.getCapacity());
            stmt.setString(3, table.getStatus());
            stmt.setInt(4, table.getTableId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean deleteTable(int tableId) {
        String sql = "DELETE FROM restaurant_table WHERE table_id=?";
        try (Connection conn = repository.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, tableId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
    @Override
    public List<Table> getAllTables() {
        List<Table> tableList = new ArrayList<>();
        String sql = "SELECT * FROM restaurant_table";
        try (Connection conn = repository.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                tableList.add(new Table(
                        rs.getInt("table_id"),
                        rs.getInt("table_number"),
                        rs.getInt("capacity"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tableList;
    }
}