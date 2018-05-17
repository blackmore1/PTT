package test;

import java.nio.ByteBuffer;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import tool.Student;
import tool.tools;

public class test {
	static Logger log = Logger.getLogger(test.class);
	public static void main(String args[]) { 
		PropertyConfigurator.configure("./log4j.properties");
		log.info("1");
		log.error("1");
		Student student = new Student();
//	     student.setD(1.1);
//	     student.setYear(2003);
//	     student.getYear().set(2003);
	     student.getGrade().set(12.5f);
	     System.out.println(student.toString());
	     ByteBuffer buf = student.getByteBuffer();
	     byte[] data = new byte[buf.remaining()];
	     buf.get(data);
	     System.out.println(data.length);
	     tools.printArray(data);
//	     System.out.println(student.getYear());
	     
    } 
}