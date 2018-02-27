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
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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
    
    @StructClass   
    public class Foo {  
          
        @StructField(order = 0)  
        public byte b;  
          
        @StructField(order = 1)  
        public byte[] i = new byte[3];  
        
        @StructField(order = 2)  
        public int j;  
          
    }  
      
    public void TestFoo() {  
        try {   
            // Pack the class as a byte buffer   
            Foo f = new Foo();  
            f.b = (byte)258;  
//            Arrays.fill(f.i, (byte) 2);
            f.j = 128;
            byte[] b = JavaStruct.pack(f);  
            for (int i = 0; i < b.length; i++) {  
                System.out.printf("b[%d]: %d\n", i, b[i]);  
            }  
              
            // Unpack it into an object  
            Foo f2 = new Foo();  
            JavaStruct.unpack(f2, b);  
            System.out.println("f2.b: " + f2.b);  
            System.out.println("f2.i: " + f2.i);  
              
        } catch(StructException e) {   
            e.printStackTrace();  
        }   
    }  
      
    public static void main(String args[]) throws StructException, IOException {  
    	byte[] data = {0x00,0x0a,(byte) 0xaa,0x55,0x10,0x05,0x00,0x00,0x00,0x01};
    	boolean[] status = new boolean[7];
    	System.out.println(status[0]);
    }  
} 