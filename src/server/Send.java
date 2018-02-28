package server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
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
				try {
					byte[] idata = buffer.getList();
					int id = idata[0]&0x0ff;
					SocketChannel s = buffer.getSocketChannel(id);
					ByteBuffer byteBuffer = ByteBuffer.allocate(idata.length-1);
			        byteBuffer.put(Arrays.copyOfRange(idata, 1, idata.length));
			        byteBuffer.flip();
			        s.write(byteBuffer);
					System.out.print("已发送:");tools.printArray(idata);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
