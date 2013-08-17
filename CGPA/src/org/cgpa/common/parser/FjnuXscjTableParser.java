package org.cgpa.common.parser;

import net.htmlparser.jericho.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;


import net.htmlparser.jericho.Source;

import org.apache.http.util.EntityUtils;
import org.cgpa.bean.gpa.GPA;
import org.cgpa.bean.gpa.ObjectFactory;
import org.cgpa.bean.gpa.GPA.COURSE;

public class FjnuXscjTableParser {
	
	
	public void parserAllGrade(String src){
		Source source=new Source(src);
		Element table = source.getFirstElement(HTMLElementName.TABLE);
		List<Element> allTr = table.getAllElements(HTMLElementName.TR);
		int rowNum = allTr.size();
		Element header = allTr.get(0);
		//int columnNum = header.getAllElements(HTMLElementName.TD).size();
		System.out.println(header.getContent().toString());
		JAXBContext jaxbContext = null;
		try {
			jaxbContext = JAXBContext.newInstance(GPA.class);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		ObjectFactory objFactory = new ObjectFactory(); 
		GPA gpa = objFactory.createGPA();
		COURSE course = null;
		for(int i=1;i<rowNum;i++){
			Element row = allTr.get(i);
			List<Element> columns = row.getAllElements();
			course = new COURSE();
			course.setNAME(columns.get(4).getContent().toString());
			course.setCREDIT(Double.parseDouble(columns.get(7).getContent().toString()));
			course.setSCORE(Double.parseDouble(columns.get(9).getContent().toString()));
			gpa.getCOURSE().add(course);
		}
		
		Marshaller marshaller = null;
		try {
			marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
	        marshaller.marshal( gpa,new FileOutputStream("output_gpa.xml"));
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
        
	}
	
	
	
}
