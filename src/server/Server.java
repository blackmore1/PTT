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
		Receive r = new Receive();
		r.add(ss);
		new Thread(r).start();
		new Thread(new Send()).start();
		new Thread(new Admin()).start();
		new Thread(new Heart()).start();
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