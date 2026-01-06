package model;

public class Employee {
    private int employeeId;
    private int userId;

    public Employee() {}

    public Employee(int employeeId, int userId) {
        this.employeeId = employeeId;
        this.userId = userId;
    }

    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
}