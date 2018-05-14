package server;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import dao.UserDAO;
import struct.StructException;

public class Heart implements Runnable {
	public Buffer buffer;
	public Heart(Buffer buffer){
		this.buffer = buffer;
	}
	@Override
	public void run() {
		while(true){
			try {
				List<Integer> list = new UserDAO().onlinelist();
				for(int id:list){
					if(buffer.hearts.get(id)==null||buffer.hearts.get(id)==0){
						System.out.println("未收到心跳包，强制下线");
						new Rec(buffer).close(id);
					}
				}
				buffer.hearts.clear();
				Thread.sleep(40000);
			} catch (InterruptedException | StructException | IOException e) {
				e.printStackTrace();
			}
		}
	}

}
