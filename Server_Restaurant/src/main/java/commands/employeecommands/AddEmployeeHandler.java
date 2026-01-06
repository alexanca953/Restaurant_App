package commands.employeecommands;

import commands.ICommandHandler;
import model.Employee;
import model.IEmployeeRepository;
import model.Message;

public class AddEmployeeHandler implements ICommandHandler {
    private IEmployeeRepository employeeRepo;

    public AddEmployeeHandler(IEmployeeRepository employeeRepo) {
        this.employeeRepo = employeeRepo;
    }

    @Override
    public Message execute(Object data) {
        Employee employee = (Employee) data;
        boolean result = employeeRepo.addEmployee(employee);
        return new Message("ADD_EMPLOYEE_RESPONSE", result);
    }
}