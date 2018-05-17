package server;

import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.LinkedList;

import dao.GroupDAO;

public class Buffer {
	static Object obj = new Object();
	public static HashMap<Integer,SocketChannel> sockets = new HashMap<>();
	public static LinkedList<Byte> list = new LinkedList<Byte>();
	public static int[] status = new int[new GroupDAO().getTotal()];//默认为false
	
	public static HashMap<Integer,Integer> hearts = new HashMap<>();
	
	public static void addList(byte id, byte[] buf){
		synchronized(obj){
		list.add(id);
		for(byte b:buf)
			list.add(b);
		obj.notify();
		}
//		System.out.println("添加list"+list.size());
	}
	
	public synchronized static byte[] getList(){
		int len = (list.get(1)&0x0ff)<<8|list.get(2)&0x0ff;
		byte[] b = new byte[len+1];
		for(int i = 0;i<len+1;i++)
			b[i] = list.removeFirst();
		return b;
	}
	
	public synchronized static void addSocketChannel(Integer id,SocketChannel s){
		sockets.put(id, s);
	}
	
	public synchronized static SocketChannel getSocketChannel(Integer id){
		return sockets.get(id);
	}
	
	public synchronized static void delSocketChannel(int id){
		sockets.remove(id);
	}
	
	public synchronized static int getStatus(int gid) {
		return status[gid];
	}

	public synchronized static void setStatus(int id,int gid) {
		status[gid] = id;
	}

	public static boolean isEmpty(){
		synchronized(obj){
		boolean b = false;
		if(list.isEmpty()){
			try {
				obj.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			b = true;
		}
		return b;
		}
	}
	
	public static LinkedList<Byte> getlist(){
		return list;
	}
}
