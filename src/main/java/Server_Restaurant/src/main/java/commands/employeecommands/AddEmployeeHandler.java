package commands.employeecommands;

import commands.ICommandHandler;
import model.Employee;
import model.Message;
import model.repository.EmployeeRepository;

public class AddEmployeeHandler implements ICommandHandler {
    private EmployeeRepository employeeRepo;

    public AddEmployeeHandler(EmployeeRepository employeeRepo) {
        this.employeeRepo = employeeRepo;
    }

    @Override
    public Message execute(Object data) {
        Employee employee = (Employee) data;
        boolean result = employeeRepo.addEmployee(employee);
        return new Message("ADD_EMPLOYEE_RESPONSE", result);
    }
}