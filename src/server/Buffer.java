package server;

import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;

public class Buffer {
	private HashMap<Long,Socket> sockets = new HashMap<>();
	private LinkedList<Byte> list = new LinkedList<Byte>();
	private boolean status = false;
	private int id;
	
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
	
	public synchronized void addSocket(long l,Socket s){
		sockets.put(l, s);
	}
	
	public synchronized Socket getSocket(long id){
		return sockets.get(id);
	}
	
	public synchronized void delSocket(long id){
		sockets.remove(id);
	}
	
	public synchronized boolean isStatus() {
		return status;
	}

	public synchronized void setStatus(boolean status) {
		this.status = status;
	}

	public synchronized int getId() {
		return id;
	}

	public synchronized void setId(int id) {
		this.id = id;
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
