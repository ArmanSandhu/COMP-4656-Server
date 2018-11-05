package ca.bcit.comp4656.assign2ws.services;

import java.util.List;

import ca.bcit.comp4656.assign2ws.jpa.entity.Employee;
import ca.bcit.comp4656.assign2ws.jpa.entity.EmployeeResponse;
import ca.bcit.comp4656.assign2ws.jpa.entity.ResponseCode;

public interface EmployeeService {

	public List<Employee> getEmployees();
	public EmployeeResponse findEmployeeById(String id);
	public ResponseCode addEmployee(Employee e);
	public ResponseCode deleteEmployee(String id);
}
