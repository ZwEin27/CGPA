package org.cgpa.common.util;




import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;



import org.cgpa.bean.gpa.GPA.COURSE;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ReadXMLUtil {

	//���ڷ��ص���Կ�ַ���
	//private static String keyMessage;
	

	/***
	 * ����xml�ļ�·�����õ���Կ
	 * @throws Exception 
	 */
	/*public static String getKey(String src){
		try {
			keyMessage = getMessage(src,"name");
		} catch (Exception e) {
			System.out.println("�����쳣��"+e.getMessage());
		}
		return keyMessage;
	}*/
	
	
	/**
	 * ��������
	 * @param xmlsrc
	 * @param name �ڵ�����
	 * @return  �ýڵ��ֵ
	 * @throws Exception
	 */
	public static List<COURSE> getMessage(String xmlsrc) throws Exception{
		String returnValue = null;
		//�õ�DOM�������Ĺ���ʵ��
		DocumentBuilderFactory domfac = DocumentBuilderFactory.newInstance();
		//��DOM�������DOM������
		DocumentBuilder dombuilder = domfac.newDocumentBuilder();
		//��Ҫ������XML�ĵ�ת��Ϊ���������Ա�DOM������������,InputStream��һ���ӿ�
		InputStream is = new FileInputStream(xmlsrc);
		//����XML�ĵ������������õ�һ��Document,��XML�ĵ����������õ�һ��org.w3c.dom.Document�����Ժ�Ĵ����Ƕ�Document������е�
		Document doc = dombuilder.parse(is);
		//�õ�XML�ĵ��ĸ��ڵ�,��DOM��ֻ�и��ڵ���һ��org.w3c.dom.Element����
		Element root = doc.getDocumentElement();
		//�õ��ڵ���ӽڵ�,������һ��org.w3c.dom.NodeList�ӿ�������������ӽڵ��
		NodeList nodeList = root.getChildNodes();
		
		COURSE course = null;
		
		List<COURSE> list = new ArrayList<COURSE>();
		
		if (nodeList != null){
			int i = 0;
			for (i = 0;i < nodeList.getLength();i++){
				Node courseNode = nodeList.item(i);
				if (courseNode.getNodeType() == Node.ELEMENT_NODE){
					Node node = null;
					course = new COURSE();
					for (node = courseNode.getFirstChild();node != null;node = node.getNextSibling()){	//����course�ڵ��µ�ÿһ����Ԫ��
						
						if (node.getNodeType() == Node.ELEMENT_NODE){
							/*if (node.getNodeName().equals(name)){
								returnValue = node.getFirstChild().getNodeValue();//��������ֵ��
								//������ΪDOM��<driver>com.mysql.jdbc.Driver</driver>Ҳ����������ṹ�Ľڵ�
							}*/
							
							returnValue = node.getFirstChild().getNodeValue();//��������ֵ��
							if(node.getNodeName().equals("NAME")){
								course.setNAME(returnValue);
							}else if(node.getNodeName().equals("CREDIT")){
								course.setCREDIT(Double.parseDouble(returnValue));
							}else if(node.getNodeName().equals("SCORE")){
								course.setSCORE(Double.parseDouble(returnValue));
							}
							
						}
						
					}
					list.add(course);
					node = null;
				}
				courseNode = null;
			}
			i = 0;
		}else{
			throw new Exception("��"+xmlsrc+"��Ӧ��XML�ĵ����ڵ������ӽڵ�!");
		}
		return list;
	}
}
