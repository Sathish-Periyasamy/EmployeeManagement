package com.cts.osp.ems.service;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.osp.ems.controller.EmployeeConstants;
import com.cts.osp.ems.dao.EmployeeDao;
import com.cts.osp.ems.model.Employee;

@Service
public class EmployeeService {

	private static final Logger LOGGER=Logger.getLogger(EmployeeService.class);
	
	@Autowired
	private EmployeeDao employeeDao;
	
	/**
	 * This method is used to check whether to insert or update the employee information in DB.
	 * @param e
	 * @param xmlPayload
	 * @return String
	 */
	public String createOrUpdateEmployee(Employee empEntity,String xmlPayload){
		LOGGER.info("createOrUpdateEmployee method >>IN");
		Long currentTime = System.currentTimeMillis();
		long createdTime =0L;
		String response =null;
		List<Employee> empList=checkForAddUpdate(empEntity.getId());
		if(empList.isEmpty()){
		 	employeeDao.saveEmployee(empEntity);
		 	response =EmployeeConstants.SAVED_SUCCESS;
		}
		else{
			Employee employee = (Employee)empList.get(0);// Only one element will be present here.
			LOGGER.info("The LastModified Time is "+employee.getLastModifiedOn());
			createdTime=employee.getLastModifiedOn().getTime();
			if(validateTimeDifference(createdTime)){
				employeeDao.updateEmployee(empEntity);
				response =EmployeeConstants.UPDATED_SUCCESS;
			}else{
			// Add restricted XML to error table
				LOGGER.info("XML is Resticted");
				invalidXmlProcessing(xmlPayload,EmployeeConstants.RESTRICTED_XML);
				response =EmployeeConstants.RESTRICTED_RESPONSE;
			}
		}
		LOGGER.info("Time taken in createOrUpdateEmployee method is "+(System.currentTimeMillis()-currentTime)+" ms");
		return response;
	}
	
	/**
	 * This method is used to check the existing employee is present in the DB.
	 * @param employeeId
	 * @return List<Employee>
	 */
	public List<Employee> checkForAddUpdate(int employeeId){
		Long currentTime = System.currentTimeMillis();
		List<Employee> employeeList =employeeDao.checkQuery(employeeId);
		LOGGER.info("Time taken in checkForAddUpdate method is "+(System.currentTimeMillis()-currentTime)+" ms");
		return employeeList;
	}
	
	/**
	 * This method is used to check whether the payload is processed within the given timeline( 24 hour timeline).
	 * If the payload is coming within 24 hrs then payload will be restricted.
	 * @param createdTime
	 * @return boolean
	 */
	public boolean validateTimeDifference(long createdTime){
		Long currentTime = System.currentTimeMillis();
		long timeNow = new Date().getTime();
		LOGGER.info("The long val of time is "+timeNow);
		long diff=timeNow - createdTime;
		long diffHours = diff / (60 * 60 * 1000) % 24;
		LOGGER.info("The difference in time is "+diffHours);
		LOGGER.info("Time taken in validateTimeDifference method is "+(System.currentTimeMillis()-currentTime)+" ms");
		if(diffHours>24){
			return true;
		}
		return false;
	}
	
	/**
	 * This method is used to save the error-invalid/restricted payload in Error_Table in database.
	 * @param xmlPayload
	 * @param errorReason
	 * @return int 
	 */
	public int invalidXmlProcessing(String xmlPayload,String errorReason){
		Long currentTime = System.currentTimeMillis();
		int rows = employeeDao.updateErrorTable(xmlPayload,errorReason);
		LOGGER.info("Updated rows in Error_Table is ;"+rows);
		LOGGER.info("Time taken in invalidXmlProcessing method is "+(System.currentTimeMillis()-currentTime)+" ms");
		return rows;
		
	}
}
