package com.cts.osp.ems.test;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.cts.osp.ems.controller.EmployeeController;
import com.cts.osp.ems.model.Employee;
import com.cts.osp.ems.service.EmployeeService;


@ContextConfiguration(locations={"/servlet-context.xml"})
public class EmployeeTest {
	
	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private EmployeeController employeeController;

	@Test
	public void validateXml() {
		
		String xmlPayload ="<employee> <id>234</id> <name>alan</name> <joiningDate>2002-09-24</joiningDate> "
				+ "<department>hr</department> </employee>";
		boolean flag =employeeController.validateXML(xmlPayload);
		assertTrue(flag);
	}
	
	public void createOrUpdateEmployee() throws ParseException{
		String xmlPayload ="<employee> <id>234</id> <name>alan</name> <joiningDate>2002-09-24</joiningDate> "
				+ "<department>hr</department> </employee>";
		
		Employee empObj = new Employee();
		empObj.setId(234);
		empObj.setDepartment("hr");
		String date ="2009-02-03";
		DateFormat df = new SimpleDateFormat("yyyy-MM-DD");
		Date joiningDate =  df.parse(date);
		empObj.setJoiningDate(joiningDate);
		empObj.setName("Alan");
		String response =employeeService.createOrUpdateEmployee(empObj, xmlPayload);
		assertEquals(response,"Success");
		
	}
	
	public void checkForAddUpdate(){
		List<Employee> empList =employeeService.checkForAddUpdate(234);
		int empId =0;
		for(Employee emp:empList){
			empId =emp.getId();
		}
		assertEquals(234,empId);
	}

	public void invalidXmlProcessing(){
		
		String xmlPayload ="<employee> <id>234</id> <name>alan</name> <joiningDate>2002-09-24</joiningDate> "
				+ "<department>hr</department> </employee>";
		int affectedRows =employeeService.invalidXmlProcessing(xmlPayload,"INVALID-XML");
		assertEquals(1,affectedRows);
		
	}
}
