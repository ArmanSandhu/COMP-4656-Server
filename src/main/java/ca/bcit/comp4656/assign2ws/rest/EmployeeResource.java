package ca.bcit.comp4656.assign2ws.rest;

import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;

import ca.bcit.comp4656.assign2ws.jpa.entity.Employee;
import ca.bcit.comp4656.assign2ws.jpa.entity.EmployeeResponse;
import ca.bcit.comp4656.assign2ws.jpa.entity.ResponseCode;
import ca.bcit.comp4656.assign2ws.services.EmployeeServiceImpl;

@Path("/employees")
public class EmployeeResource {
	
	private static Logger logger = Logger.getLogger(EmployeeResource.class);
	
	@EJB(lookup="java:app/A00929919ws/EmployeeServiceImpl")
	private EmployeeServiceImpl service;

	@Context
	UriInfo uriInfo;
	
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public List<Employee> getEmployees(){
		logger.info("Resource Layer: attempting to grab Employees");
		return service.getEmployees();
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_XML)
	public EmployeeResponse findEmployeeById(@PathParam("id") String id) {
		logger.info("Resource Layer: Attempting to grab Employee with id: " + id);
		return service.findEmployeeById(id);
	}
	
	@PUT
	@Path("/add")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	public ResponseCode addEmployee(Employee employee) {
		logger.info("Resource Layer: Attempting to add an Employee!");
		return service.addEmployee(employee);	
	}
	
	@DELETE
	@Path("/delete/{id}")
	@Produces(MediaType.APPLICATION_XML)
	public ResponseCode deleteEmployee(@PathParam("id") String id) {
		logger.info("Resource Layer: Attempting to delete Employee with id: " + id);
		return service.deleteEmployee(id);
		
	}
}
