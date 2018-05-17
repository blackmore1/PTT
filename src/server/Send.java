package server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

import struct.StructException;
import tool.tools;

public class Send implements Runnable {

	@Override
	public void run() {
		while(true){
			if(!Buffer.isEmpty()){
				byte[] idata = Buffer.getList();
				int id = idata[0]&0x0ff;
				SocketChannel s = Buffer.getSocketChannel(id);
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
						new Receive().close(id);
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
