package commands.employeecommands;

import commands.ICommandHandler;
import model.Employee;
import model.IEmployeeRepository;
import model.Message;

import java.util.List;

public class GetAllEmployeesHandler implements ICommandHandler {
    private IEmployeeRepository employeeRepo;

    public GetAllEmployeesHandler(IEmployeeRepository employeeRepo) {
        this.employeeRepo = employeeRepo;
    }

    @Override
    public Message execute(Object data) {
        List<Employee> list = employeeRepo.getAllEmployees();
        return new Message("GET_ALL_EMPLOYEES_RESPONSE", list);
    }
}