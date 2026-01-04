package commands.employeecommands;

import commands.ICommandHandler;
import model.Employee;
import model.Message;
import model.repository.EmployeeRepository;

public class UpdateEmployeeHandler implements ICommandHandler {
    private EmployeeRepository employeeRepo;

    public UpdateEmployeeHandler(EmployeeRepository employeeRepo) {
        this.employeeRepo = employeeRepo;
    }

    @Override
    public Message execute(Object data) {
        Employee employee = (Employee) data;
        boolean result = employeeRepo.updateEmployee(employee.getEmployeeId(), employee);
        return new Message("UPDATE_EMPLOYEE_RESPONSE", result);
    }
}