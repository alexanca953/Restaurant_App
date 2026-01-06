package commands.employeecommands;

import commands.ICommandHandler;
import model.IEmployeeRepository;
import model.Message;
import model.repository.EmployeeRepository;

public class DeleteEmployeeHandler implements ICommandHandler {
    private IEmployeeRepository employeeRepo;

    public DeleteEmployeeHandler(IEmployeeRepository employeeRepo) {
        this.employeeRepo = employeeRepo;
    }

    @Override
    public Message execute(Object data) {
        int employeeId = (int) data;
        boolean result = employeeRepo.deleteEmployee(employeeId);
        return new Message("DELETE_EMPLOYEE_RESPONSE", result);
    }
}