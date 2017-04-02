package com.cts.osp.ems.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.sun.xml.txw2.annotation.XmlAttribute;

@XmlRootElement(name="employee")
@XmlAccessorType(XmlAccessType.NONE)
public class Employee implements Serializable{

	@XmlElement
	private int id;
	@XmlElement
	private String name;
	@XmlElement
	private Date joiningDate;
	@XmlElement
	private String department;
	@XmlElement
	private Timestamp lastModifiedOn;
	
	public Employee(int id,String name,Date joiningDate,String department){
		this.id =id;
		this.name=name;
		this.joiningDate=joiningDate;
		this.department=department;
	}
	
	public Employee(){
		
	}
	public int getId() {
		return id;
	}
	@XmlAttribute
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getJoiningDate() {
		return joiningDate;
	}
	public void setJoiningDate(Date joiningDate) {
		this.joiningDate = joiningDate;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	
	public Timestamp getLastModifiedOn() {
		return lastModifiedOn;
	}

	public void setLastModifiedOn(Timestamp lastModifiedOn) {
		this.lastModifiedOn = lastModifiedOn;
	}

	public String toString(){
		return "Employee Created Successfully";
		
	}
	
}
