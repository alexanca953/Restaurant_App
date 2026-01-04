package commands.employeecommands;

import commands.ICommandHandler;
import model.Employee;
import model.Message;
import model.repository.EmployeeRepository;
import java.util.List;

public class GetAllEmployeesHandler implements ICommandHandler {
    private EmployeeRepository employeeRepo;

    public GetAllEmployeesHandler(EmployeeRepository employeeRepo) {
        this.employeeRepo = employeeRepo;
    }

    @Override
    public Message execute(Object data) {
        List<Employee> list = employeeRepo.getAllEmployees();
        return new Message("GET_ALL_EMPLOYEES_RESPONSE", list);
    }
}