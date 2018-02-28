package server;

import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.LinkedList;

public class Buffer {
	private HashMap<Integer,SocketChannel> sockets = new HashMap<>();
	private LinkedList<Byte> list = new LinkedList<Byte>();
	private int[] status = new int[7];//默认为false
	
	public synchronized void addList(byte id, byte[] buf){
		list.add(id);
		for(byte b:buf)
			list.add(b);
		this.notify();
//		System.out.println("添加list"+list.size());
	}
	
	public synchronized byte[] getList(){
		int len = (list.get(1)&0x0ff)<<8|list.get(2)&0x0ff;
		byte[] b = new byte[len+1];
		for(int i = 0;i<len+1;i++)
			b[i] = list.removeFirst();
		return b;
	}
	
	public synchronized void addSocketChannel(Integer id,SocketChannel s){
		sockets.put(id, s);
	}
	
	public synchronized SocketChannel getSocketChannel(Integer id){
		return sockets.get(id);
	}
	
	public synchronized void delSocketChannel(long id){
		sockets.remove(id);
	}
	
	public synchronized int getStatus(int gid) {
		return status[gid];
	}

	public synchronized void setStatus(int id,int gid) {
		this.status[gid] = id;
	}

	public synchronized boolean isEmpty(){
		boolean b = false;
		if(list.isEmpty()){
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			b = true;
		}
		return b;
	}
	
	public LinkedList<Byte> getlist(){
		return list;
	}
}
