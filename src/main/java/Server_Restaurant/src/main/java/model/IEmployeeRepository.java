package model;

import java.util.List;

public interface IEmployeeRepository {
    boolean addEmployee(Employee employee);
    boolean deleteEmployee(int employeeId);
    boolean updateEmployee(int employeeId, Employee employee);
    List<Employee> getAllEmployees();
}