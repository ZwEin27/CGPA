package org.cgpa.common.util;

import java.io.FileOutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class JAXBUtil {
	 // 同步锁
    private static Lock lock = new ReentrantLock();
    // 线程安全Map
    private static ConcurrentHashMap<String, JAXBContext> contextCache = new ConcurrentHashMap<String, JAXBContext>();
      
	public static JAXBContext getContext(Class<?> objClass)
			throws JAXBException {
		String className = objClass.getName();
		boolean hasKey = false;
		JAXBContext context = null;

		try {
			if (contextCache.containsKey(className)) {
				hasKey = true;
			}

			if (!hasKey) {
				lock.lock();
			}

			context = contextCache.get(className);
			if (context == null) {
				context = JAXBContext.newInstance(objClass);
				contextCache.putIfAbsent(className, context);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (!hasKey) {
				lock.unlock();
			}
		}
		return context;
	}
	
	public static void obj2xmlfile(Object obj,Class<?> clazz,String filename) throws Exception{
		try {
			JAXBContext jaxbContext=getContext(clazz);
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			Writer writer = new StringWriter();
			marshaller.marshal(obj,new FileOutputStream(filename));
		} catch (Exception e) {
			throw new Exception(); 
		}
	}
	
	
	
	
	
	
	
}
