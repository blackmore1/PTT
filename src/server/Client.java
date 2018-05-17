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
import tool.codec0c;
import tool.codec22;
import tool.tools;

public class Client {

	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException, StructException {
		codec04 c04 = new codec04();
		byte[] ip = {(byte) 192,(byte) 168,1,106};
		c04.setName(tools.str2Bytes("zhangsan",32));
		c04.setPwd(tools.str2Bytes("1111",32));
		c04.setCodenum((byte) 2);
		c04.setCodelist(new byte[2]);
		c04.setPrefix((byte) 24);
		c04.setIpv4(ip);
		byte[] data = JavaStruct.pack(c04);
		codec c = new codec(data);
		c.setCtw((byte) 0x04);
		byte[] b = JavaStruct.pack(c);
		System.out.println(b.length);
		Socket socket=new Socket("198.35.45.72",3328);//198.35.45.72
		OutputStream out=socket.getOutputStream();
		gps g = new gps(out);
		InputStream in = socket.getInputStream();
		out.write(b);
		new Thread(g).start();
		new Thread(new heart1(out)).start();
		while(true){
			byte[] buf=receive(in,1024);
			nianbao(in, buf);
		}
	}
	/**
	 * 接收大于等于真实长度的数组
	 * @throws IOException 
	 * */
	public static byte[] receive(InputStream in,int n) throws IOException{
		byte[] data = null;
		byte[] buf=new byte[n];
		int len=in.read(buf);
		int rlen = n;
		//tools.printArray(buf);
		if(len==-1)
			return data;
		if((buf.length>4)&&(buf[2]==(byte)0xaa)&&(buf[3]==0x55)){
			rlen = (buf[0]&0x0ff)<<8|buf[1]&0x0ff;
		}
		System.out.println("len:"+len+"  "+"rlen:"+rlen);
		if(len >= rlen)
			data = Arrays.copyOfRange(buf, 0, len);
		else if(len<rlen)
			data = tools.concat(Arrays.copyOfRange(buf, 0, len),receive(in,rlen-len));
		return data;
	}
	
	/**
	 * 处理粘包
	 * @throws IOException 
	 * @throws StructException 
	 * */
	public static void nianbao(InputStream in,byte[] data) throws IOException, StructException{
		int rlen = (data[0]&0x0ff)<<8|data[1]&0x0ff;
		if(rlen<data.length){
			byte[] split1 = Arrays.copyOfRange(data, 0, rlen);
			byte[] split2 = Arrays.copyOfRange(data, rlen, data.length);
			tools.printArray(split1);
			int rlen2 = (split2[0]&0x0ff)<<8|split2[1]&0x0ff;
			if(rlen2 == split2.length)
				tools.printArray(split2);
			else if(rlen2>split2.length){
				byte[] b = tools.concat(split2, receive(in,rlen2-split2.length));
				nianbao(in,b);
			}
			else
				nianbao(in,split2);
		}
		else
			tools.printArray(data);
	}
}
class gps implements Runnable{
	OutputStream out;
	public gps(OutputStream out){
		this.out = out;
	}

	@Override
	public void run() {
		while(true){
			try {
				Thread.sleep(5000);
				codec22 c22 = new codec22();
				c22.setCode((byte) 0);
				c22.setGroupid((short) 3);
				byte[] b= new byte[6100];
				for(int i=0;i<b.length;i++)
					b[i]=1;
				c22.setMessage(b);
				codec c = new codec(JavaStruct.pack(c22));
				c.setCtw((byte) 0x22);
				byte[] data = JavaStruct.pack(c);
				System.out.println(data.length);
				tools.printArray(data);
				out.write(data);
//				System.out.println(1);
			} catch (InterruptedException | StructException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
class heart1 implements Runnable{
	OutputStream out;
	public heart1(OutputStream out){
		this.out = out;
	}
	
	@Override
	public void run() {
		while(true){
			try {
				Thread.sleep(10000);
				codec0c c0c = new codec0c();
				c0c.setAltitude(1.1);
				c0c.setLongitude(2.2);
				codec c = new codec(JavaStruct.pack(c0c));
				c.setCtw((byte) 0x0c);
				byte[] data = JavaStruct.pack(c);
				System.out.println(data.length);
				tools.printArray(data);
				out.write(data);
//				System.out.println(1);
			} catch (InterruptedException | StructException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
