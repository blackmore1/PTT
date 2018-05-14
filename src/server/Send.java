package server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

import struct.StructException;
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
				int id = idata[0]&0x0ff;
				SocketChannel s = buffer.getSocketChannel(id);
				if(s==null)	continue;
				try {
					ByteBuffer byteBuffer = ByteBuffer.allocate(idata.length-1);
			        byteBuffer.put(Arrays.copyOfRange(idata, 1, idata.length));
			        byteBuffer.flip();
			        s.write(byteBuffer);
					System.out.print("已发送:");tools.printArray(idata);
				} catch (IOException e) {
					e.printStackTrace();
					try {
//						s.close();
						new Rec(buffer).close(id);
					} catch (StructException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
	}

}
