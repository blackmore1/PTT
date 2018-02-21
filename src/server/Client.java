package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

import struct.JavaStruct;
import struct.StructException;
import tool.codec;
import tool.codec04;
import tool.tools;

public class Client {

	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException, StructException {
		codec04 c04 = new codec04();
		byte[] ip = {(byte) 192,(byte) 168,1,106};
		c04.setName(tools.str2Bytes("haha",32));
		c04.setPwd(tools.str2Bytes("12345",32));
		c04.setCodenum((byte) 2);
		c04.setCodelist(new byte[2]);
		c04.setPrefix((byte) 24);
		c04.setIpv4(ip);
		byte[] data = JavaStruct.pack(c04);
		codec c = new codec(data);
		c.setCtw((byte) 0x04);
		byte[] b = JavaStruct.pack(c);
		System.out.println(b.length);
		Socket socket=new Socket("192.168.1.104",3328);//198.35.45.72
		OutputStream out=socket.getOutputStream();
		InputStream in = socket.getInputStream();
			out.write(b);
			//out.write(buf1);
			while(true){
			byte[] buf=new byte[1024];
			int len=in.read(buf);
			byte[] b1 = Arrays.copyOfRange(buf, 0, len);
			tools.printArray(b1);
//			socket.close();
		}
	}

}

