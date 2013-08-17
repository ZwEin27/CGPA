package org.cgpa.bean;

import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.SchemaFactory;

import org.cgpa.bean.gpa.GPA;
import org.cgpa.bean.gpa.ObjectFactory;
import org.cgpa.bean.gpa.GPA.COURSE;
import org.xml.sax.SAXException;


/**
 * test class
 * @author Administrator
 *
 */
public class GPAParser {

	public static void main(String[] args) {

		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(GPA.class);
			ObjectFactory objFactory = new ObjectFactory(); 
			GPA gpa = objFactory.createGPA();
			
			COURSE course1 = new COURSE();
			course1.setNAME("haha");
			course1.setCREDIT(4);
			course1.setSCORE(98);
			
			
			COURSE course2 = new COURSE();
			course2.setNAME("lala");
			course2.setCREDIT(5);
			course2.setSCORE(94);
			
			
			gpa.getCOURSE().add(course1);
			gpa.getCOURSE().add(course2);
			
			

            Marshaller marshaller = jaxbContext.createMarshaller(); 

            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            marshaller.marshal( gpa,new FileOutputStream("output_gpa.xml"));
            
            
            
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            SchemaFactory sf = SchemaFactory.newInstance(W3C_XML_SCHEMA_NS_URI);
            
            try {
				unmarshaller.setSchema(sf.newSchema(GPAParser.class.getResource("gpa.xsd")));
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            Object bce = unmarshaller.unmarshal(new File("output_gpa.xml"));
            marshaller.marshal(bce, System.out);
            
            GPA hellogpa = (GPA)bce;
            
            for(COURSE course : hellogpa.getCOURSE()){
            	System.out.println("name:" + course.getNAME());
            	System.out.println("credit:" + course.getCREDIT());
            	System.out.println("score:" + course.getSCORE());
            }
            
			
			
		} catch (JAXBException e) {
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
