package ca.bcit.comp4656.assign2ws.ejb;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.apache.log4j.Logger;

import ca.bcit.comp4656.assign2ws.jpa.entity.Employee;
import ca.bcit.comp4656.assign2ws.jpa.entity.EmployeeException;
import ca.bcit.comp4656.assign2ws.jpa.entity.EmployeeResponse;
import ca.bcit.comp4656.assign2ws.jpa.entity.ResponseCode;
import ca.bcit.comp4656.assign2ws.util.PropertiesUtil;

@Stateless
public class EmployeeBean implements EmployeeLocal {

	@PersistenceContext(unitName="EmployeePU")
	private EntityManager entityManager;
	
	private static Logger logger = Logger.getLogger(EmployeeBean.class);

	@Override
	public List<Employee> getEmployees() throws EmployeeException {
		List<Employee> employees = null;
		try {
			logger.info("Employee Bean: Attempting to retrieve all employees!");
			TypedQuery<Employee> query = entityManager.createNamedQuery("getAllEmployees", Employee.class);
			employees = query.getResultList();
		} catch (EJBTransactionRolledbackException ejbExp) {
			System.err.println("Rollback Exception");
			ejbExp.printStackTrace();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return employees;
	}

	@Override
	public EmployeeResponse findEmployeeById(String id) {
		logger.info("Employee Bean: Attempting to retrieve employee with id: " + id);
		EmployeeResponse employeeResponse = new EmployeeResponse();
		ResponseCode responseCode = new ResponseCode();
		Employee employee = null;
		if(validateID(id)) {
			logger.info("Valid ID!");
			try {
				employee = entityManager.createNamedQuery("Employee.findById", Employee.class).setParameter("id", id).getSingleResult();
				logger.info("Found an Employee!");
				responseCode.setCode(PropertiesUtil.getString(EMPLOYEE_RESOURCE_BUNDLE, "find.employee.success.code"));
				responseCode.setDesc(PropertiesUtil.getString(EMPLOYEE_RESOURCE_BUNDLE, "find.employee.success.desc"));
			} catch (NoResultException nre) {
				logger.info("Unable to find an Employee!");
				responseCode.setCode(PropertiesUtil.getString(EMPLOYEE_RESOURCE_BUNDLE, "error.find.employee.code"));
				responseCode.setDesc(PropertiesUtil.getString(EMPLOYEE_RESOURCE_BUNDLE, "error.find.employee.desc"));
			}
		} else {
			logger.info("Invalid ID passed!");
			responseCode.setCode(PropertiesUtil.getString(EMPLOYEE_RESOURCE_BUNDLE, "error.add.employee.invalid.code"));
			responseCode.setDesc(PropertiesUtil.getString(EMPLOYEE_RESOURCE_BUNDLE, "error.add.employee.invalid.desc"));
		}
		employeeResponse.setEmployee(employee);
		employeeResponse.setResponseCode(responseCode);
		
		return employeeResponse;
	}

	@Override
	public ResponseCode removeEmployee(String id) {
		logger.info("Employee Bean: Attempting to delete employee with id: " + id);
		ResponseCode responseCode = new ResponseCode();
		if(validateID(id)) {
			logger.info("Valid ID!");
			try {
				Employee employee = entityManager.find(Employee.class, id);
				if (employee == null) {
					logger.info("Unable to delete Employee with given id: " + id);
					responseCode.setCode(PropertiesUtil.getString(EMPLOYEE_RESOURCE_BUNDLE, "error.find.employee.code"));
					responseCode.setDesc(PropertiesUtil.getString(EMPLOYEE_RESOURCE_BUNDLE, "error.find.employee.desc"));
				} else {
					entityManager.createNamedQuery("Employee.deleteById").setParameter("id", id).executeUpdate();
					logger.info("Deleted Employee with given id: " + id);
					responseCode.setCode(PropertiesUtil.getString(EMPLOYEE_RESOURCE_BUNDLE, "remove.employee.success.code"));
					responseCode.setDesc(PropertiesUtil.getString(EMPLOYEE_RESOURCE_BUNDLE, "remove.employee.success.desc", id));
				}
				
			} catch (NoResultException | SecurityException | IllegalStateException nre) {
				logger.info("Unable to delete Employee!");
				logger.info(nre.getMessage());
				responseCode.setCode(PropertiesUtil.getString(EMPLOYEE_RESOURCE_BUNDLE, "error.remove.employee.code"));
				responseCode.setDesc(PropertiesUtil.getString(EMPLOYEE_RESOURCE_BUNDLE, "error.remove.employee.desc"));
			}
		} else {
			logger.info("Invalid ID passed!");
			responseCode.setCode(PropertiesUtil.getString(EMPLOYEE_RESOURCE_BUNDLE, "error.add.employee.invalid.code"));
			responseCode.setDesc(PropertiesUtil.getString(EMPLOYEE_RESOURCE_BUNDLE, "error.add.employee.invalid.desc"));
		}
		return responseCode;
	}

	@Override
	public ResponseCode addEmployee(Employee e) {
		logger.info("Employee Bean: Attempting to add an Employee!");
		ResponseCode responseCode = new ResponseCode();
		Set<String> errorMessages = null;
		if(validateID(e.getId())) {
			logger.info("Valid ID!");
			try {
				Employee employee = entityManager.find(Employee.class, e.getId());
				if(employee != null) {
					logger.info("Employee already Exists!");
					responseCode.setCode(PropertiesUtil.getString(EMPLOYEE_RESOURCE_BUNDLE, "error.add.employee.duplicate.code"));
					responseCode.setDesc(PropertiesUtil.getString(EMPLOYEE_RESOURCE_BUNDLE, "error.add.employee.duplicate.desc"));
				} else {
					errorMessages = validate(e);
					
					if(errorMessages != null && errorMessages.size() > 0) {
						logger.info("Some Errors Happened!");
						responseCode.setCode(PropertiesUtil.getString(EMPLOYEE_RESOURCE_BUNDLE, "error.generic.code"));
						responseCode.setDesc(PropertiesUtil.getString(EMPLOYEE_RESOURCE_BUNDLE, "error.generic.desc"));
					} else {
						logger.info("Adding Employee!");
						entityManager.persist(e);
						logger.info("Added Employee! Yay!");
						Object[] args = { e.getFirstName(), e.getLastName() };
						responseCode.setCode(PropertiesUtil.getString(EMPLOYEE_RESOURCE_BUNDLE, "add.employee.success.code"));
						responseCode.setDesc(PropertiesUtil.getString(EMPLOYEE_RESOURCE_BUNDLE, "add.employee.success.desc", args));
					}
				}
			} catch(NoResultException | SecurityException | IllegalStateException nre) {
				logger.info("Unable to add Employee!");
				logger.info(nre.getMessage());
				responseCode.setCode(PropertiesUtil.getString(EMPLOYEE_RESOURCE_BUNDLE, "error.add.employee.code"));
				responseCode.setDesc(PropertiesUtil.getString(EMPLOYEE_RESOURCE_BUNDLE, "error.add.employee.desc"));
			}
			
		} else {
			logger.info("Invalid ID passed!");
			responseCode.setCode(PropertiesUtil.getString(EMPLOYEE_RESOURCE_BUNDLE, "error.add.employee.invalid.code"));
			responseCode.setDesc(PropertiesUtil.getString(EMPLOYEE_RESOURCE_BUNDLE, "error.add.employee.invalid.desc"));
		}
		return responseCode;
	}
	
	private boolean validateID(String id) {
		if (id.matches(EMPLOYEE_ID_REGEX)) {
			return true;
		} else {
			return false;
		}
	}
	
	private Set<String> validate(Employee e){
		
		Set<String> messages = new HashSet<String>();
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		Set<ConstraintViolation<Employee>> constraintViolations = validator.validate(e);
		if(constraintViolations.size() > 0) {
			for(ConstraintViolation<Employee> constraintViolation: constraintViolations) {
				messages.add(constraintViolation.getPropertyPath() + " : " + constraintViolation.getMessage());
			}
		}
		return messages;
	}
}
