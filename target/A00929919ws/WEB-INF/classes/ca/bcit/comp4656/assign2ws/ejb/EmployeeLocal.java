package ca.bcit.comp4656.assign2ws.ejb;

import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.Local;

import ca.bcit.comp4656.assign2ws.jpa.entity.Employee;
import ca.bcit.comp4656.assign2ws.jpa.entity.EmployeeException;
import ca.bcit.comp4656.assign2ws.jpa.entity.EmployeeResponse;
import ca.bcit.comp4656.assign2ws.jpa.entity.ResponseCode;

@Local
public interface EmployeeLocal {

	static final String EMPLOYEE_BUNDLE_NAME = "employees-presentation";
	static final ResourceBundle EMPLOYEE_RESOURCE_BUNDLE = ResourceBundle.getBundle(EMPLOYEE_BUNDLE_NAME);
	static final String EMPLOYEE_ID_REGEX = "[A][0][0-9]{7}";
	
	public List<Employee> getEmployees() throws EmployeeException;
	public EmployeeResponse findEmployeeById(String id);
	public ResponseCode removeEmployee(String id);
	public ResponseCode addEmployee(Employee e);
}
