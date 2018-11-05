package ca.bcit.comp4656.assign2ws.jpa.entity;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class EmployeeResponse {
	
	private Employee employee;
	private ResponseCode responseCode;
	
	public Employee getEmployee() {
		return employee;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	public ResponseCode getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(ResponseCode responseCode) {
		this.responseCode = responseCode;
	}
	
	@Override
	public String toString() {
		return "EmployeeResponse [employee=" + employee + ", responseCode=" + responseCode + "]";
	}
}
