package server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

import tool.tools;

public class Send implements Runnable {

	private Buffer buffer;
	
	public Send(Buffer buffer) {
		this.buffer = buffer;
	}

	@Override
	public void run() {
		while(true){
			if(!buffer.isEmpty()){
				byte[] idata = buffer.getList();
				Socket s = buffer.getSocket(idata[0]&0x0ff);
				try {
					OutputStream out = s.getOutputStream();
					out.write(Arrays.copyOfRange(idata, 1, idata.length));
					out.flush();
					System.out.print("已发送:");tools.printArray(idata);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
