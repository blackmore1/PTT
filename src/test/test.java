package test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Deque;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import bean.Group;
import bean.User;
import dao.GroupDAO;
import dao.UserDAO;
import server.Buffer;
import struct.JavaStruct;
import struct.StructClass;
import struct.StructException;
import struct.StructField;
import tool.codec;
import tool.codec04;
import tool.codec05;
import tool.codec07;
import tool.codec09;
import tool.codect;
import tool.tools;

public class test {
	public static void main(String args[]) throws StructException, IOException {  
    	test1.t1.print();
    } 
}
class test1 {  
    
    public static test2 t1= new test2();
      
     
} 
class test2{
	public void print(){
		System.out.println(1);
	}
}