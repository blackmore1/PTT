package server;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import dao.UserDAO;
import struct.StructException;

public class Heart implements Runnable {
	@Override
	public void run() {
		while(true){
			try {
				List<Integer> list = new UserDAO().onlinelist();
				for(int id:list){
					if(Buffer.hearts.get(id)==null||Buffer.hearts.get(id)==0){
						System.out.println("未收到心跳包，强制下线");
						new Receive().close(id);
					}
				}
				Buffer.hearts.clear();
				Thread.sleep(40000);
			} catch (InterruptedException | StructException | IOException e) {
				e.printStackTrace();
			}
		}
	}

}
