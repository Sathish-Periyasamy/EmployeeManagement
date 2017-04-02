package com.cts.osp.ems.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import com.cts.osp.ems.model.Employee;

@Component
public class EmployeeDao {
	
    @Autowired
	private JdbcTemplate jdbcTemplate;  
	  
	public int saveEmployee(Employee e){  
	    String query="insert into "+'"'+"Employee"+'"'+" values('"+e.getId()+"','"+e.getName()+"',"
	    		+ "'"+e.getJoiningDate()+"','"+e.getDepartment()+"',current_timestamp,current_timestamp)"; 
	    System.out.println(query);
	    //jdbcTemplate.getClass();
	    return jdbcTemplate.update(query);  
	}  
	
	
	public int updateEmployee(Employee e){  
	    String query="update Employee set name='"+e.getName()+"',department='"+e.getDepartment()+"',joining_Date='"+e.getJoiningDate()+"' ,last_Modified_On=current_timestamp'"+
	" where id='"+e.getId()+"' ";  
	    return jdbcTemplate.update(query);  
	}  

	
	public List<Employee> checkQuery(int employeeId){
		String query ="Select * from"+ '"'+"Employee"+'"'+" where employee_id ='"+employeeId+"'";
		return jdbcTemplate.query(query,new ResultSetExtractor<List<Employee>>(){  
		    public List<Employee> extractData(ResultSet rs) throws SQLException,  
		            DataAccessException {  
		      
		        List<Employee> list=new ArrayList<Employee>();  
		        while(rs.next()){  
		        Employee e=new Employee();  
		        e.setId(rs.getInt(1));  
		        e.setName(rs.getString(2));
		        e.setJoiningDate(rs.getDate(3));
		        e.setDepartment(rs.getString(4));
		        e.setLastModifiedOn(rs.getTimestamp(6));
		        list.add(e);  
		        }  
		        return list;  
		        }});
		}
	
	public int updateErrorTable(String body,String errorReason){
		StringBuilder updateQuery = new StringBuilder();
		updateQuery.append("insert into "+'"'+"Error_Table"+'"'+"(Xml_File,Error_Reason,Created_On) values ('"+body+"','"+errorReason+"',current_timestamp)" );
		return jdbcTemplate.update(updateQuery.toString());
	}
}