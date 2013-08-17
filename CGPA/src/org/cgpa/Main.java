package org.cgpa;


import java.io.ObjectInputStream.GetField;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cgpa.bean.gpa.GPA;
import org.cgpa.bean.gpa.GPA.COURSE;
import org.cgpa.common.Constants;
import org.cgpa.common.parser.FjnuXscjTableParser;
import org.cgpa.common.parser.HtmlParser;
import org.cgpa.common.util.JAXBUtil;
import org.cgpa.common.util.ReadXMLUtil;
import org.cgpa.core.GPAArith;
import org.cgpa.httpclient.HttpClientUtil;



public class Main {
	
	public Map<String,String> gpaFileMap = new HashMap<String, String>();
	
	public Main() {
	}
	
	
	public static void main(String[] args) throws Exception {
		//String s = ReadXMLUtil.getKey("key.xml");
		
		
		/*for(Course course : ReadXMLUtil.getMessage("key.xml")){
			System.out.println("name:" + course.getName() 
								+ " | credit:" + course.getCredit()
								+ " | score:" + course.getScore());
		}*/
		
		Main main = new Main();
		String html = HttpClientUtil.getHtml();
		//new FjnuXscjTableParser().parserAllGrade(html);
		GPA gpa = HtmlParser.parseHtml2GPA(html);
		JAXBUtil.obj2xmlfile(gpa, GPA.class, "output_gpa.xml");
		
		
		printGPA(Constants.TEST);
		/*for(String key : main.gpaFileMap.keySet()){
			//System.out.println(gpa);
			String value = main.gpaFileMap.get(key);
			System.out.println(value+":");
			printGPA(key);
			System.out.println("-------------------------------------------------");
		}*/
		
	}
	
	public static void printGPA(String pga) throws Exception{
		DecimalFormat df=new DecimalFormat(".##");
		List<COURSE> list = ReadXMLUtil.getMessage(pga);
		double d1 = GPAArith.calInFPS(list);
		double d2 = GPAArith.callInSAS(list);
		double d3 = GPAArith.callInBOLE(list);
		System.out.println("四分制成绩  	" + df.format(d1));
		System.out.println("标准制成绩 	" + df.format(d2));
		System.out.println("伯乐算法 	" + df.format(d3));
	}
	
	
}
