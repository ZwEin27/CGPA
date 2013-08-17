package org.cgpa.httpclient;

import java.awt.FlowLayout;
import java.beans.ConstructorProperties;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.fluent.Response;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.cgpa.common.Constants;
import org.cgpa.common.parser.HtmlParser;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sun.tools.jxc.ap.Const;

public class HttpClientUtil {

	
	
	@Test
	public void test() throws Exception {
		DefaultHttpClient httpclient = new DefaultHttpClient(
				new ThreadSafeClientConnManager());
		// test check code
		// getCheckCodeImage(httpclient,
		// "http://jwgl.fjnu.edu.cn/CheckCode.aspx");

		// test view state
		// String str = getViewState(httpclient,"http://jwgl.fjnu.edu.cn");
		// System.out.println(str);
		
		System.out.println(getHtml());
	}
	
	public static String getHtml(){
		String result = "";
		DefaultHttpClient httpclient = new DefaultHttpClient(
				new ThreadSafeClientConnManager());
		System.out.println("To login...");
		login(httpclient);
		System.out.println("Login success , redirect to " + Constants.REDIRECT_URL);
		//doGet(httpclient, Constants.REDIRECT_URL);
		result = getTargetPageHtml(httpclient);
		return result;
	}
	
	
	public static String getTargetPageHtml(HttpClient httpclient){
		String html = "";
		String viewstate = "";
		Map<String,String> map = new HashMap<String,String>();
//		map.put("", "");
//		map.put("Referer", "http://jwgl.fjnu.edu.cn/xs_main.aspx?xh=123012010196");
//		map.put("Host", "jwgl.fjnu.edu.cn");
//		map.put("Connection", "Keep-Alive");
//		map.put("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)");
//		map.put("Accept-Language", "zh-CN");
//		map.put("Accept-Encoding", "gzip");
//		map.put("Accept", "text/html, application/xhtml+xml, */*");
//		try {
//			html = doGet(httpclient, "http://jwgl.fjnu.edu.cn/content.aspx", map);
//		} catch (Exception e) {
//		}
//		map = new HashMap<String,String>();
		map.put("Referer", "http://jwgl.fjnu.edu.cn/content.aspx");
		map.put("Connection", "Keep-Alive");
		map.put("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)");
		map.put("Accept-Language", "zh-CN");
		map.put("Accept-Encoding", "gzip");
		map.put("Accept", "text/html, application/xhtml+xml, */*");
		try {
			html = doGet(httpclient, Constants.CJ_URL, map);
			viewstate = HtmlParser.parserViewState(html);
			System.out.println(viewstate);
		} catch (Exception e) {
		}
		//System.out.println(html);
		
		
		map = new HashMap<String,String>();
		map.put("Accept","text/html, application/xhtml+xml, */*");
		map.put("Referer",Constants.CJ_URL);	//"http://jwgl.fjnu.edu.cn/xscj_gc.aspx?xh=123012010196&xm=Î¯¡Ë’‹&gnmkdm=N121617"
		map.put("Accept-Language","zh-CN");
		map.put("User-Agent","Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)");
		map.put("Content-Type","application/x-www-form-urlencoded");
		map.put("Accept-Encoding","gzip, deflate");
		map.put("Host","jwgl.fjnu.edu.cn");
		//map.put("Content-Length","2131");
		map.put("Connection","Keep-Alive");
		map.put("Cache-Control","no-cache");
		
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("__VIEWSTATE", viewstate));
		nvps.add(new BasicNameValuePair("ddlXN", ""));
		nvps.add(new BasicNameValuePair("ddlXQ", ""));
		nvps.add(new BasicNameValuePair("Button2", "%D4%DA%D0%A3%D1%A7%CF%B0%B3%C9%BC%A8%B2%E9%D1%AF"));
		
		try {
			html = doPost(httpclient, Constants.CJ_URL, nvps, map);
		} catch (Exception e) {
		}
		//System.out.println(html);
		return html;
	}

	public static void login(HttpClient httpclient) {
		String checkcode = "";
		String viewstate = "";
		try {
			checkcode = getCheckCodeImage(httpclient, Constants.CHECKCODE_URL);
			viewstate = getViewState(httpclient, Constants.ROOT_URL);
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("TextBox1", Constants.STU_NO));
			nvps.add(new BasicNameValuePair("TextBox2", Constants.PASSWORD));
			nvps.add(new BasicNameValuePair("TextBox3", checkcode));
			nvps.add(new BasicNameValuePair("RadioButtonList1", "%D1%A7%C9%FA"));
			nvps.add(new BasicNameValuePair("Button1", ""));
			nvps.add(new BasicNameValuePair("lbLanguage", ""));
			nvps.add(new BasicNameValuePair("__VIEWSTATE", viewstate));
			String redirectUrl = doPost(httpclient, Constants.FORM_ACTION_URL, nvps,Constants.REDIRECT_MARK);
			Constants.REDIRECT_URL = Constants.ROOT_URL + redirectUrl;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getCheckCodeImage(HttpClient httpclient,
			String checkCodeUrl) {
		String checkcode = "";
		HttpResponse response;
		try {
			HttpGet httpget = new HttpGet(checkCodeUrl);
			response = httpclient.execute(httpget);

			File file = new File(Constants.CHECKCODE_PATH);
			FileOutputStream ops = new FileOutputStream(file);
			response.getEntity().writeTo(ops);
			ops.close();

			JFrame frame = new JFrame();
			frame.setVisible(false);
			frame.setBounds(100, 100, 100, 100);
			frame.setLayout(new FlowLayout());
			ImageIcon icon = new ImageIcon(Constants.CHECKCODE_PATH);
			frame.add(new JLabel(icon));
			frame.setVisible(true);

			System.out.print("Please input the check code :");
			Scanner scr = new Scanner(System.in);
			checkcode = scr.nextLine();

			EntityUtils.consume(response.getEntity());

		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		} finally {

		}
		return checkcode;
	}

	public static String getViewState(HttpClient httpclient, String url)
			throws Exception {
		String result = "";
		String responseMsg = doGet(httpclient, url);
		result = HtmlParser.parserViewState(responseMsg);
		return result;
	}

	public static String doPost(HttpClient httpclient, String url,
			List<NameValuePair> params) throws Exception {
		String responseBody = "";
		HttpPost post = new HttpPost(url);
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		try {
			post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			responseBody = httpclient.execute(post, responseHandler);
			return responseBody;
		} catch (UnsupportedEncodingException e) {
			throw new UnsupportedEncodingException();
		} catch (ClientProtocolException e) {
			throw new ClientProtocolException();
		} catch (IOException e) {
			throw new IOException();
		}
	}

	public static String doPost(HttpClient httpclient, String url,
			List<NameValuePair> params,String headerName) throws Exception {
		String result = "";
		HttpResponse response = null;
		HttpPost post = new HttpPost(url);
		try {
			post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			response = httpclient.execute(post);
			Header header = response.getFirstHeader(headerName);
			result = header.getValue();
			EntityUtils.consume(response.getEntity());
			return result;
		} catch (UnsupportedEncodingException e) {
			throw new UnsupportedEncodingException();
		} catch (ClientProtocolException e) {
			throw new ClientProtocolException();
		} catch (IOException e) {
			throw new IOException();
		}
	}
	
	public static String doPost(HttpClient httpclient, String url,
			List<NameValuePair> params,Map<String,String> map) throws Exception {
		String responseBody = "";
		HttpPost httppost = new HttpPost(url);
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		httppost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		for(String key : map.keySet()){
			httppost.addHeader(key, map.get(key));
		}
		try {
			responseBody = httpclient.execute(httppost, responseHandler);
			return responseBody;
		} catch (UnsupportedEncodingException e) {
			throw new UnsupportedEncodingException();
		} catch (ClientProtocolException e) {
			throw new ClientProtocolException();
		} catch (IOException e) {
			throw new IOException();
		}
	}

	public static String doGet(HttpClient httpclient, String url)
			throws Exception {
		String responseBody = "";
		HttpGet httpget = new HttpGet(url);
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		try {
			responseBody = httpclient.execute(httpget, responseHandler);
			return responseBody;
		} catch (ClientProtocolException e) {
			throw new ClientProtocolException();
		} catch (IOException e) {
			throw new Exception();
		}
	}
	
	public static String doGet(HttpClient httpclient, String url,Map<String,String> map)
			throws Exception {
		String responseBody = "";
		HttpGet httpget = new HttpGet(url);
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		for(String key : map.keySet()){
			httpget.addHeader(key, map.get(key));
		}
		try {
			responseBody = httpclient.execute(httpget, responseHandler);
			return responseBody;
		} catch (ClientProtocolException e) {
			throw new ClientProtocolException();
		} catch (IOException e) {
			throw new Exception();
		}
	}
	

	public static void printText(HttpResponse response) throws Exception,
			IOException {
		System.out.println(EntityUtils.toString(response.getEntity()));
	}

}
