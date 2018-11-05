package ca.bcit.comp4656.assign2ws.services;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import org.apache.log4j.Logger;

import ca.bcit.comp4656.assign2ws.ejb.EmployeeLocal;
import ca.bcit.comp4656.assign2ws.jpa.entity.Employee;
import ca.bcit.comp4656.assign2ws.jpa.entity.EmployeeException;
import ca.bcit.comp4656.assign2ws.jpa.entity.EmployeeResponse;
import ca.bcit.comp4656.assign2ws.jpa.entity.ResponseCode;


@Stateless
@EJB(beanInterface= EmployeeServiceImpl.class, name="EmployeeServiceImpl")
@LocalBean
public class EmployeeServiceImpl implements EmployeeService {
	
	private static Logger logger = Logger.getLogger(EmployeeServiceImpl.class);
	
	@EJB(lookup="java:app/A00929919ws/EmployeeBean")
	EmployeeLocal employeeBean;

	@Override
	public List<Employee> getEmployees() {
		logger.info("Inside Service Layer: Getting Employees!");
		try {
			logger.info("Attempting to get Employees");
			return employeeBean.getEmployees();
		} catch (EmployeeException e) {
			logger.info("Unable to get all Employees");
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public EmployeeResponse findEmployeeById(String id) {
		logger.info("Inside Service Layer: Finding Employee with id: " + id);
		return employeeBean.findEmployeeById(id);
	}

	@Override
	public ResponseCode addEmployee(Employee e) {
		logger.info("Inside Service Layer: Attempting to add an Employee!");
		return employeeBean.addEmployee(e);
	}

	@Override
	public ResponseCode deleteEmployee(String id) {
		logger.info("Inside Service Layer: Attempting to delete Employee with id: " + id);
		return employeeBean.removeEmployee(id);
	}

}
