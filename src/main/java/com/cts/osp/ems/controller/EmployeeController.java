package com.cts.osp.ems.controller;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;

import com.cts.osp.ems.dao.EmployeeDao;
import com.cts.osp.ems.model.Employee;
import com.cts.osp.ems.service.EmployeeService;


/**
 *@author Sathish
 *@version 1.0
 */
@RestController
public class EmployeeController {
	
	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private Jaxb2Marshaller jaxb2Marshaller;
	
	private static final Logger LOGGER=Logger.getLogger(EmployeeController.class);
	
	/**
	 *  This Method is used to save/update the employee information in DB.
	 * @param xmlPayload
	 * @return response
	 */
	@RequestMapping(value=EmployeeConstants.GET_ALL_EMP,method = RequestMethod.POST,
			consumes="application/xml")
    public Response saveEmployee(@RequestBody String xmlPayload){
		Long currentTime = System.currentTimeMillis();
		LOGGER.info("XML Payload is : "+xmlPayload);
		//Validate the XML
		if(validateXML(xmlPayload)){
			Source source = new StreamSource(new StringReader(xmlPayload));
			Employee empObj = (Employee) jaxb2Marshaller.unmarshal(source);
			LOGGER.info("Validated xml successfully");
			employeeService.createOrUpdateEmployee(empObj,xmlPayload);
		}
		else{
			employeeService.invalidXmlProcessing(xmlPayload,EmployeeConstants.INVALID_XML);
		}
		Long diffTime =System.currentTimeMillis()-currentTime;
		Long totalResponseTime =diffTime/1000 % 60;
		return Response.status(200).header("Total Response Time", totalResponseTime+" sec").build();
    }
	
	/** This method is used to validate the XML with XSD and return the status.
	 * @param xmlPayload
	 * @return boolean
	 */
	public boolean validateXML(String xmlPayload){
		Long currentTime = System.currentTimeMillis();
		try {			
	        URL xsdResource = EmployeeController.class.getClassLoader().getResource(EmployeeConstants.XSD_PATH);
            SchemaFactory factory = 
                    SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(xsdResource);
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new StringReader(xmlPayload)));
        } catch (IOException e) {
            LOGGER.error("Exception: "+e.getMessage());
            return false;
        }
		catch(SAXException e){
			LOGGER.error("Exception: "+e.getMessage());
            return false;
		}
		LOGGER.info("Time taken in ValidateXml Method is "+(System.currentTimeMillis()-currentTime)+" ms");
        return true;
    
	}
}
