package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class Server {

	public static void main(String[] args) throws IOException {
        System.out.println(InetAddress.getLocalHost().getHostAddress());
        ServerSocketChannel ss = ServerSocketChannel.open();
		ss.socket().bind(new InetSocketAddress(3328));
		ss.configureBlocking(false);
//		SocketChannel s = null;
//		boolean flag = true;
		Buffer buffer = new Buffer();
		new Thread(new Send(buffer)).start();
		new Thread(new Admin(buffer)).start();
		new Thread(new Heart(buffer)).start();
		Rec r = new Rec(buffer);
		r.add(ss);
		new Thread(r).start();
//		while(true){
//			s = ss.accept();
//			if(s!=null)
//				r.add(s);
//			if(flag&&s!=null){
//				new Thread(r).start();
//				flag = false;
//			}
//		}	
	}

}