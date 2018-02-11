package server;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import bean.Group;
import bean.User;
import dao.GroupDAO;
import dao.UserDAO;
import struct.JavaStruct;
import struct.StructException;
import tool.codec;
import tool.codec04;
import tool.codec05;
import tool.codec07;
import tool.codec09;
import tool.codec0b;
import tool.codec0c;
import tool.codec0d;
import tool.codec11;
import tool.codec21;
import tool.codec22;
import tool.codec23;
import tool.tools;

public class Rec implements Runnable {

	private Socket s;
	private InputStream in;
	private Buffer buffer;
	private int id = 0;
	public Rec(Socket s,Buffer buffer){
		String ip=s.getInetAddress().getHostAddress();
		SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss" );
        Date d= new Date();
        String str = sdf.format(d);
		System.out.println(ip+"已连接"+str);
		this.s = s;
		this.buffer = buffer;
		try {
			this.in = s.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void run() {
		buffer.addSocket(Thread.currentThread().getId(), s);
		while(true){
			try{
				byte[] data=receive(1024);
				if(data == null){
					System.out.println(s.getInetAddress().getHostAddress()+"已断开");
					s.close();
					break;
				}
				tools.printArray(data);
				nianbao(data);
			}catch(Exception e){
				try {
					System.out.println("id:"+id);
//					if(id!=0)
						close();
					in.close();
				} catch (IOException | StructException ioe) {
					ioe.printStackTrace();
				}
				e.printStackTrace();
				System.out.println(s.getInetAddress().getHostAddress()+"已断开");
				break;
			}
		}
	}
	
	/**
	 * 接收大于等于真实长度的数组
	 * @throws IOException 
	 * */
	public byte[] receive(int n) throws IOException{
		byte[] data = null;
		byte[] buf=new byte[n];
		int len=in.read(buf);
		int rlen = n;
		//tools.printArray(buf);
		if(len==-1)
			return data;
		if((buf.length>4)&&(buf[2]==(byte)0xaa)&&(buf[3]==0x55)){
			rlen = (buf[0]&0x0ff)<<8|buf[1]&0x0ff;
		}
		System.out.println("len:"+len+"  "+"rlen:"+rlen);
		if(len >= rlen)
			data = Arrays.copyOfRange(buf, 0, len);
		else if(len<rlen)
			data = tools.concat(Arrays.copyOfRange(buf, 0, len),receive(rlen-len));
		return data;
	}
	
	/**
	 * 处理粘包
	 * @throws IOException 
	 * @throws StructException 
	 * */
	public void nianbao(byte[] data) throws IOException, StructException{
		int rlen = (data[0]&0x0ff)<<8|data[1]&0x0ff;
		if(rlen<data.length){
			byte[] split1 = Arrays.copyOfRange(data, 0, rlen);
			byte[] split2 = Arrays.copyOfRange(data, rlen, data.length);
			redirect(split1);
			int rlen2 = (split2[0]&0x0ff)<<8|split2[1]&0x0ff;
			if(rlen2 == split2.length)
				redirect(split2);
			else if(rlen2>split2.length){
				byte[] b = tools.concat(split2, receive(rlen2-split2.length));
				nianbao(b);
			}
			else
				nianbao(split2);
		}
		else
			redirect(data);
	}
	
	/**
	 * 导向到下一步
	 * @throws StructException 
	 * */
	public void redirect(byte[] data) throws StructException{
		int rlen = (data[0]&0x0ff)<<8|data[1]&0x0ff;
		codec c = new codec(new byte[rlen-6]);
		JavaStruct.unpack(c, data);
		switch(data[5]&0x0ff){
		case 0x04:
			ctw05(c.getContent());
			break;
		case 0x0c:
			ctw0d(c.getContent());
			break;
		case 0x20:
			ctw21(c.getContent());
			break;
		case 0x22:
			ctw23(c.getContent());
			break;
		default:
			System.out.println("控制字出错");
		}
	}
	
	/**
	 * 验证登录
	 * @param
	 * @throws StructException 
	 */
	public void ctw05(byte[] content) throws StructException{
		System.out.println("ctw05用户登录");
		codec04 c04 = new codec04();
		c04.setCodelist(new byte[(content[64]&0x0ff)]);
		JavaStruct.unpack(c04, content);
		String name = new String(c04.getName());
		String pwd = new String(c04.getPwd());
		System.out.println("name:"+name+"---"+"pwd:"+pwd);
		UserDAO userdao = new UserDAO();
		User user = userdao.get("select * from user where name = '"+name.trim()+"'");
		byte result = 0x01;
		id = 0;
		if(user!=null&&((pwd.trim()).equals(user.getPwd()))){
			System.out.println("密码正确");
			result = 0x00;
			id = user.getId();
			int codenum = c04.getCodenum();
			String codelist = tools.bytes2Str(c04.getCodelist());
			String gps = ""+c04.getLatitude()+" "+c04.getLongitude();
			String ipv4 = tools.ipv42Str(c04.getIpv4(),c04.getPrefix());
			user.setStatus(true);
			user.setCodenum(codenum);
			user.setCodelist(codelist);
			user.setGps(gps);
			user.setIpv4(ipv4);
			user.setThreadid((byte)Thread.currentThread().getId());
			userdao.update(user);
		}
		codec05 c05 = new codec05();
		c05.setResult(result);
		c05.setReason((byte)0x00);
		c05.setId((short) id);
		codec c = new codec(JavaStruct.pack(c05));
		c.setCtw((byte) 0x05);
		byte[] data = JavaStruct.pack(c);
		buffer.addList((byte)Thread.currentThread().getId(), data);
		//send(data);
		if(result == 0x00)
			ctw07(user);
	}
	
	/**
	 * 指导用户端配置参数
	 * @param
	 * @throws StructException 
	 * */
	public void ctw07(User user) throws StructException{
		System.out.println("ctw07指导用户配置参数");
		codec07 c07 = new codec07();
		c07.setGpsperiod((byte) 10);
		c07.setGroupnum((byte) user.getGroupnum());
		c07.setCodenum((byte) user.getCodenum());
		if(user.getCodelist().equals("")){
			c07.setCodelist(new byte[0]);
		}
		else{
			String[] strs = user.getCodelist().split("\\|");
			byte[] codelist = new byte[strs.length];
			for(int i = 0;i<strs.length;i++){
				codelist[i] = (byte) Integer.parseInt(strs[i]);
			}
			c07.setCodelist(codelist);
		}
		codec c = new codec(JavaStruct.pack(c07));
		c.setCtw((byte) 0x07);
		byte[] data = JavaStruct.pack(c);
		buffer.addList((byte)Thread.currentThread().getId(), data);
		ctw09(user);
	}
	
	/**
	 * 向终端发送群组配置和用户消息
	 * @throws StructException 
	 * */
	public void ctw09(User user) throws StructException{
		System.out.println("ctw09群组配置");
		UserDAO userdao = new UserDAO();
		GroupDAO groupdao = new GroupDAO();
		Group curr = groupdao.get(user.getCurrgroup());
		String ipv4 = user.getIpv4();
		List<User> users = userdao.list("select * from user where broadcast = 1 and currg"
				+ "roup = "+user.getCurrgroup()+" and ipv4 = '"+ipv4+"'");
		boolean broadcast = false;
		if(users.isEmpty()){
			broadcast = true;
			user.setBroadcast(true);
			userdao.update(user);
		}
		codec09 c09 = new codec09(curr,broadcast);
		codec c = new codec(JavaStruct.pack(c09));
		c.setCtw((byte) 0x09);
		byte[] data = JavaStruct.pack(c);
		buffer.addList((byte)Thread.currentThread().getId(), data);
		String[] groups = user.getGrouplist().split("\\|");
		for(String str:groups){
			int gid = Integer.parseInt(str);
			if(gid!=user.getCurrgroup()){
				Group group = groupdao.get(gid);
				c09 = new codec09(group, false);
				c = new codec(JavaStruct.pack(c09));
				c.setCtw((byte) 0x09);
				data = JavaStruct.pack(c);
				buffer.addList((byte)Thread.currentThread().getId(), data);
			}
		}
		ctw0b(user,3);
	}
	
	/**
	 * 群组更新消息
	 * 3:新上线用户，需要向该用户发送所有群组激活用户，向当前群组其它用户发送上线通知
	 * 4:下线通知  6:取消激活通知，都不用转发GPS
	 * 5：激活通知 
	 * */
	public void ctw0b(User user,int n) throws StructException{
		System.out.println("ctw0b群组更新");
		UserDAO userdao = new UserDAO();
		if(n==3){
			String[] groups = user.getGrouplist().split("\\|");
			for(String str:groups){
				int gid = Integer.parseInt(str);
				List<User> users = userdao.list("select * from user where status = 1 and currgroup = "+gid);
				codec0b c0b = new codec0b();
				c0b.setMark((byte) 5);
				c0b.setGroupid((short) gid);
				c0b.setUsernum((byte) users.size());
				byte[] userlist = new byte[users.size()*2];
				int i = 0;
				for(User u:users){
					System.arraycopy(tools.int2Bytes(u.getId(), 2), 0, userlist, i, 2);
					i +=2;
				}
				c0b.setUserlist(userlist);
				codec c = new codec(JavaStruct.pack(c0b));
				c.setCtw((byte) 0x0b);
				byte[] data = JavaStruct.pack(c);
				buffer.addList((byte)Thread.currentThread().getId(), data);
			}
		}
		codec0b c0b = new codec0b();
		c0b.setMark((byte) n);
		c0b.setUsernum((byte) 1);
		c0b.setGroupid((short) user.getCurrgroup());
		c0b.setUserlist(tools.int2Bytes(user.getId(), 2));
		codec c = new codec(JavaStruct.pack(c0b));
		c.setCtw((byte) 0x0b);
		byte[] data = JavaStruct.pack(c);
		List<User> users = userdao.list("select * from user where broadcast = 1 and currgroup = "+user.getCurrgroup());
		for(User u:users){
			buffer.addList((byte) u.getThreadid(), data);
		}
		if(n==6||n==4)
			return;
		String[] gpss = user.getGps().split(" ");
		codec0c c0c = new codec0c();
		c0c.setAltitude(Double.parseDouble(gpss[0])); 
		c0c.setLongitude(Double.parseDouble(gpss[1])); 
		ctw0d(JavaStruct.pack(c0c));
	}
	
	/**
	 * 用户位置更新
	 * */
	public void ctw0d(byte[] gps) throws StructException{
		System.out.println("ctw0dGPS更新");
		UserDAO userdao = new UserDAO();
		User user = userdao.get("select * from user where id ="+id);
		codec0c c0c = new codec0c();
		JavaStruct.unpack(c0c, gps);
		user.setGps(""+c0c.getAltitude()+" "+c0c.getLongitude());
		userdao.update(user);
		codec0d c0d = new codec0d();
		c0d.setUsernum((byte) 1);
		c0d.setId((short) id);
		c0d.setGps(gps);
		codec c = new codec(JavaStruct.pack(c0d));
		c.setCtw((byte) 0x0d);
		byte[] data = JavaStruct.pack(c);
		List<User> users = userdao.list("select * from user where broadcast = 1 and currgroup = "+user.getCurrgroup());
		for(User u:users){
			buffer.addList((byte) u.getThreadid(), data);
		}
	}
	
	/**
	 * 服务器响应群组激活设定
	 * */
	public void ctw11(byte[] content) throws StructException{
		System.out.println("ctw11响应群组激活");
		UserDAO userdao = new UserDAO();
		GroupDAO groupdao = new GroupDAO();
		User user = userdao.get("select * from user where id ="+id);
		boolean broadcast = user.getBroadcast();
		//重新选择本地广播并向该用户发送0x09，通知使能广播
		String ipv4 = user.getIpv4();
		if(broadcast==true){	
			List<User> users = userdao.list("select * from user where status = 1 and currg"
					+ "roup = "+((content[2]&0x0ff)<<8|content[3]&0x0ff)+" and id!="+id+" and ipv4 = '"+ipv4+"'");
			if(!users.isEmpty()){
				User u = users.get(0);
				u.setBroadcast(true);
				userdao.update(u);
				Group group = groupdao.get(u.getCurrgroup());
				group.setUsernum(0);
				group.setUserlist("");
				codec09 c09 = new codec09(group, broadcast);
				codec c = new codec(JavaStruct.pack(c09));
				c.setCtw((byte) 0x09);
				byte[] data = JavaStruct.pack(c);
				buffer.addList((byte) u.getThreadid(), data);
			}
		}
		//向当前用户发送0x11,响应群组激活
		user.setCurrgroup((content[0]&0x0ff)<<8|content[1]&0x0ff);
		codec11 c11 = new codec11();	
		c11.setResult((byte) 0);
		c11.setGroupid((short) user.getCurrgroup());
		codec c = new codec(JavaStruct.pack(c11));
		c.setCtw((byte) 0x11);
		byte[] data = JavaStruct.pack(c);
		buffer.addList((byte)Thread.currentThread().getId(), data);
		//在新群组中是否使能广播,向当前用户发送0x09设置使能广播
		List<User> users = userdao.list("select * from user where broadcast = 1 and currg"
				+ "roup = "+user.getCurrgroup()+" and ipv4 = '"+ipv4+"'");
		if(users.isEmpty()){
			broadcast = true;
		}
		else
			broadcast = false;
		user.setBroadcast(broadcast);
		userdao.update(user);
		Group group = groupdao.get(user.getCurrgroup());
		codec09 c09 = new codec09(group, broadcast);
		c09.setUsernum((byte) 0);
		c09.setUserlist(new byte[0]);
		c = new codec(JavaStruct.pack(c09));
		c.setCtw((byte) 0x09);
		data = JavaStruct.pack(c);
		buffer.addList((byte)Thread.currentThread().getId(), data);
		//向新群组用户发送0x0B,更新群组
		ctw0b(user,5);
		user.setCurrgroup((content[2]&0x0ff)<<8|content[3]&0x0ff);
		//向旧群组用户发送0x0B
		ctw0b(user,6);
	}
	
	/**
	 * 申请或取消语音
	 * @throws StructException 
	 * */
	public void ctw21(byte[] content) throws StructException{
		System.out.println("ctw21申请或取消语音");
		codec21 c21 = new codec21();
		if(content[0]==0x01){
			if(buffer.isStatus()){
				c21.setResult((byte) 0x01);
				c21.setId((short) buffer.getId());;
			}
			else{
				buffer.setStatus(true);
				buffer.setId(id);
				c21.setResult((byte) 0x00);
				c21.setId((short) id);
			}
		}
		else{
			if(buffer.isStatus()&&buffer.getId()==id){
				buffer.setStatus(false);
			}
			c21.setResult((byte) 0x00);
			c21.setId((short) id);
		}
		codec c = new codec(JavaStruct.pack(c21));
		byte[] data = JavaStruct.pack(c);
		c.setCtw((byte) 0x21);
		buffer.addList((byte)Thread.currentThread().getId(), data);
	}
	
	/**
	 * 转发语音
	 * @throws StructException 
	 * */
	public void ctw23(byte[] content) throws StructException{
		System.out.println("ctw23发送语音");
		codec22 c22 = new codec22();
		c22.setMessage(new byte[content.length-4]);
		JavaStruct.unpack(c22, content);
		int groupid = (content[2]&0x0ff)<<8|content[3]&0x0ff;
		codec23 c23 = new codec23();
		c23.setCode(c22.getCode());
		c23.setGroupid(c22.getGroupid());
		c23.setUserid((short) id);
		c23.setMessage(c22.getMessage());
		codec c = new codec(JavaStruct.pack(c23));
		c.setCtw((byte) 0x23);
		byte[] data = JavaStruct.pack(c);
		UserDAO userdao = new UserDAO();
		List<User> users = userdao.list("select * from user where broadcast = 1 and currgroup = "+groupid);
		for(User u:users){
			buffer.addList((byte) u.getThreadid(), data);
		}
	}
	
	/**
	 * 用户下线
	 * @throws StructException 
	 * */
	public void close() throws StructException{
		System.out.println("准备断开");
		UserDAO userdao = new UserDAO();
		GroupDAO groupdao = new GroupDAO();
		User user = userdao.get("select * from user where id ="+id);
		if(user.getBroadcast()){
			//重新选择本地广播
			String ipv4 = user.getIpv4();
			List<User> users = userdao.list("select * from user where status = 1 and currg"
					+ "roup = "+user.getCurrgroup()+" and id!="+id+" and ipv4 = '"+ipv4+"'");
			if(!users.isEmpty()){
				User u = users.get(0);
				u.setBroadcast(true);
				userdao.update(u);
				Group group = groupdao.get(u.getCurrgroup());
				group.setUsernum(0);
				group.setUserlist("");
				codec09 c09 = new codec09(group, true);
				codec c = new codec(JavaStruct.pack(c09));
				c.setCtw((byte) 0x09);
				byte[] data = JavaStruct.pack(c);
				buffer.addList((byte) u.getThreadid(), data);
			}
		}
		//更新数据库
		user.setBroadcast(false);
		user.setStatus(false);
		user.setThreadid(-1);
		userdao.update(user);
		buffer.delSocket(Thread.currentThread().getId());
		//下线通知
		ctw0b(user,4);
	}

}
