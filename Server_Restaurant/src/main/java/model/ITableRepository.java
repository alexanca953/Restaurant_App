package model;

import java.util.List;

public interface ITableRepository {
    boolean addTable(Table table);
    boolean deleteTable(int tableId);
    boolean updateTable(int tableId, Table table);
    List<Table> getAllTables();
}