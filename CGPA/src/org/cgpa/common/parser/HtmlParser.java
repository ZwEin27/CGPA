package org.cgpa.common.parser;

import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

import org.cgpa.bean.gpa.GPA;
import org.cgpa.bean.gpa.ObjectFactory;
import org.cgpa.bean.gpa.GPA.COURSE;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.InputTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

public class HtmlParser {
	
	public static GPA parseHtml2GPA(String html){
		GPA gpa = null;
		Source source=new Source(html);
		Element table = source.getFirstElement(HTMLElementName.TABLE);
		List<Element> allTr = table.getAllElements(HTMLElementName.TR);
		int rowNum = allTr.size();
		JAXBContext jaxbContext = null;
		try {
			jaxbContext = JAXBContext.newInstance(GPA.class);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		ObjectFactory objFactory = new ObjectFactory(); 
		gpa = objFactory.createGPA();
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
		return gpa;
	}
	
	
	/**
	 * 根据所给定的标签搜索添加，返回指定标签中属性名对应的值
	 * @param html
	 * @param attrName	标签搜索条件:属性名称
	 * @param attr_value	标签搜索条件：属性值
	 * @param target_attr	目标标签中的目标属性的值
	 * @return
	 * @throws ParserException
	 */
	public static String parserAttribute(String html,String attrName,String attr_value,String target_attr) throws ParserException {
		String result = "";
		try {
			Parser parser = new Parser(html);
			NodeClassFilter ncf = new NodeClassFilter(InputTag.class);
			HasAttributeFilter haf = new HasAttributeFilter(attrName,
					attr_value);
			AndFilter af = new AndFilter(ncf, haf);
			NodeList list;
			list = parser.parse(af);
			Node node = list.elementAt(0);
			InputTag it = (InputTag) node;
			result = it.getAttribute(target_attr).trim();
			parser.reset();
			return result;
		} catch (ParserException e) {
			throw new ParserException();
		}
	}

	
	/**
	 * 
	 * @param html
	 * @return 返回html中的ViewState值
	 * @throws ParserException
	 */
	public static String parserViewState(String html) throws ParserException {
		return parserAttribute(html,"name","__VIEWSTATE","value");
	}

}
