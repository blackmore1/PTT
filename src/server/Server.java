package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Server {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException {
		SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss" );
        Date d= new Date();
        String str = sdf.format(d);
		ServerSocket ss=new ServerSocket(3328);
		System.out.println(InetAddress.getLocalHost().getHostAddress()+str);
		Buffer buffer = new Buffer();
		new Thread(new Send(buffer)).start();
		while(true){
			//System.out.println(1);
			Socket s=ss.accept();
			new Thread(new Rec(s,buffer)).start();
		}
		//ss.close();
	}

}