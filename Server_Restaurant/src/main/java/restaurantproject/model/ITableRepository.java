package restaurantproject.model;

import java.util.List;

public interface ITableRepository {
    boolean addTable(Table table);
    boolean deleteTable(int tableId);
     boolean updateTable(Table table);
    List<Table> getAllTables();
}