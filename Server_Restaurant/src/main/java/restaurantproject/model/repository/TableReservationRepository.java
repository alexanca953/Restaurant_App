package restaurantproject.model.repository;

import restaurantproject.model.ITableReservationRepository;
import restaurantproject.model.TableReservation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TableReservationRepository implements ITableReservationRepository {

    private Repository repository;

    public TableReservationRepository() {
        this.repository = new Repository();
    }

    @Override
    public boolean addReservationTable(TableReservation rt) {
        String sql = "INSERT INTO reservation_table (reservation_id, table_id) VALUES (?, ?)";
        try (Connection conn = repository.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, rt.getReservationId());
            stmt.setInt(2, rt.getTableId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteReservationTable(int id) {
        String sql = "DELETE FROM reservation_table WHERE id = ?";
        try (Connection conn = repository.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateReservationTable(int id, TableReservation rt) {
        String sql = "UPDATE reservation_table SET reservation_id = ?, table_id = ? WHERE id = ?";
        try (Connection conn = repository.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, rt.getReservationId());
            stmt.setInt(2, rt.getTableId());
            stmt.setInt(3, id);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<TableReservation> getAllReservationTables() {
        List<TableReservation> list = new ArrayList<>();
        String sql = "SELECT * FROM reservation_table";

        try (Connection conn = repository.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new TableReservation(
                        rs.getInt("id"),
                        rs.getInt("reservation_id"),
                        rs.getInt("table_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}