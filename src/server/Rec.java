package server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
import tool.codec0d1;
import tool.codec11;
import tool.codec21;
import tool.codec22;
import tool.codec23;
import tool.tools;

public class Rec implements Runnable {

	private Buffer buffer;
	private Selector selector;
	private SocketChannel s;
	private int id;
	public Rec(Buffer buffer) throws IOException{
		this.buffer = buffer;
		selector = Selector.open();
	}
	public void add(SocketChannel s) throws IOException{
		String ip=s.getRemoteAddress().toString().split(":")[0];
		Date nowTime = new Date();
		id = new UserDAO().getidbymark(ip);
		if(id!=0){
			buffer.addSocketChannel(id, s);
			System.out.println(ip+"恢复连接"+nowTime);
		}
		else
			System.out.println(ip+"已连接"+nowTime);
		try {
			s.configureBlocking(false);
			s.register(selector, SelectionKey.OP_READ);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void add(ServerSocketChannel ss) throws IOException{
		try {
			ss.configureBlocking(false);
			ss.register(selector,SelectionKey.OP_ACCEPT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void run() {
		while(true){
			int readChannels;
			try {
				readChannels = selector.selectNow();
				if(readChannels == 0) continue;
			} catch (IOException e2) {
				e2.printStackTrace();
			}
			Set<SelectionKey> selectedKeys = selector.selectedKeys();
			Iterator<SelectionKey> it = selectedKeys.iterator();
			SelectionKey key = null;
			try {
				while(it.hasNext()){
					key = (SelectionKey) it.next();
					if(key.isValid()&&key.isAcceptable()){
						System.out.println("Acceptable");
						ServerSocketChannel ss = (ServerSocketChannel) key.channel();
						add(ss.accept());
					}
					else if(key.isValid()&&key.isReadable()){
//						System.out.println("Readable");
						s = (SocketChannel) key.channel();
						byte[] data = receive(1024);
						if(data == null){
							throw new IOException();
//							continue;
						}
						System.out.println(data.length);
//						if(data[5]!=0x0c&&data[5]!=0x22)
//							tools.printArray(data);
						nianbao(data);
					}
					else
						System.out.println("key无效");
				}
			} catch (IOException | StructException e) {
				//如果捕获到该SelectionKey对应的Channel时出现了异常,即表明该Channel对于的Client出现了问题
	            //所以从Selector中取消该SelectionKey的注册
				key.cancel();
				UserDAO userdao= new UserDAO();
	            System.out.println("关闭");
                try {
                	id = userdao.getidbymark(s.getRemoteAddress().toString().split(":")[0]);
                	close(id);
//                	s.close();
				} catch (IOException | StructException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}finally{
				selectedKeys.clear();
			}
		}
	}
	
	/**
	 * 接收大于等于真实长度的数组
	 * @throws IOException 
	 * */
	public byte[] receive(int n) throws IOException{
		ByteBuffer buf = ByteBuffer.allocate(n);
		int len = s.read(buf);
		buf.flip();
		int rlen = n;
		if(len==-1){
			System.out.println("收到空数组");
			return null;
		}
		byte[] data = new byte[buf.remaining()];
        buf.get(data);
		if((data.length>4)&&(data[2]==(byte)0xaa)&&(data[3]==0x55)){
			rlen = (data[0]&0x0ff)<<8|data[1]&0x0ff;
		}
		System.out.println("len:"+len+"  "+"rlen:"+rlen);
		if(len<rlen)
			data = tools.concat(data,receive(rlen-len));
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
	
	public void send(byte[] data) throws IOException{
		ByteBuffer byteBuffer = ByteBuffer.allocate(data.length);
        byteBuffer.put(data);
        System.out.print("已发送登录反馈:");
        tools.printArray(data);
        byteBuffer.flip();
        s.write(byteBuffer);
	}
	
	/**
	 * 导向到下一步
	 * @throws StructException 
	 * @throws IOException 
	 * */
	public void redirect(byte[] data) throws StructException, IOException{
		int rlen = (data[0]&0x0ff)<<8|data[1]&0x0ff;
		codec c = new codec(new byte[rlen-6]);
		JavaStruct.unpack(c, data);
		if((data[5]&0x0ff)!=0x04){
			UserDAO userdao = new UserDAO();
			id = userdao.getidbymark(s.getRemoteAddress().toString().split(":")[0]);//不是登录的话都要重置id为当前id
			if(id==0)
				return;
		}
		switch(data[5]&0x0ff){
		case 0x04:
			ctw05(c.getContent());
			break;
		case 0x0c:
			ctw0d(c.getContent(),0);
			break;
		case 0x10:
			ctw11(c.getContent());
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
	 * @throws IOException 
	 */
	public void ctw05(byte[] content) throws StructException, IOException{
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
		byte reason = 0x01;
		id = 0;
		if(user!=null&&((pwd.trim()).equals(user.getPwd()))){
			System.out.println("密码正确");
			id = user.getId();
			int codenum = c04.getCodenum();
			List<Integer> codelist = new ArrayList<Integer>();
			for(byte b:c04.getCodelist()){
				codelist.add(b&0x0ff);
			}
			String gps = c04.getLatitude()+" "+c04.getLongitude();
			String ipv4 = tools.ipv42Str(c04.getIpv4(),c04.getPrefix());
			user.setStatus(true);
			user.setCodenum(codenum);
			user.setCodelist(codelist);
			user.setGps(gps);
			user.setIpv4(ipv4);
			user.setMark(s.getRemoteAddress().toString().split(":")[0]);
			if(buffer.getSocketChannel(id)!=null){
				System.out.println("用户已登录");
				result = 0x01;
				reason = 0x03;
//				return;
			}
			else{
				result = 0x00;
				reason = 0x00;
				userdao.update(user);
				buffer.addSocketChannel(id, s);
			}
		}
		codec05 c05 = new codec05();
		c05.setResult(result);
		c05.setReason(reason);
		c05.setId((short) id);
		codec c = new codec(JavaStruct.pack(c05));
		c.setCtw((byte) 0x05);
		byte[] data = JavaStruct.pack(c);
		send(data);
		if(result == 0x00)
			ctw07();
	}
	
	/**
	 * 指导用户端配置参数
	 * @param
	 * @throws StructException 
	 * */
	public void ctw07() throws StructException{
		System.out.println("ctw07指导用户配置参数");
		User user = new UserDAO().getbyid(id);
		codec07 c07 = new codec07();
		c07.setGpsperiod((byte) 30);
		c07.setGroupnum((byte) user.getGroupnum());
		c07.setCodenum((byte) user.getCodenum());
		byte[] codelist = new byte[user.getGrouplist().size()];
		for(int i = 0;i<user.getGrouplist().size();i++){
			codelist[i] = user.getGrouplist().get(i).byteValue();
		}
		c07.setCodelist(codelist);
		codec c = new codec(JavaStruct.pack(c07));
		c.setCtw((byte) 0x07);
		byte[] data = JavaStruct.pack(c);
		buffer.addList((byte) id, data);
		ctw09();
	}
	
	/**
	 * 向终端发送群组配置和用户消息
	 * @throws StructException 
	 * */
	public void ctw09() throws StructException{
		System.out.println("ctw09群组配置");
		UserDAO userdao = new UserDAO();
		User user = userdao.getbyid(id);
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
		codec09 c09 = new codec09(curr,broadcast,user.getInterrupt());
		codec c = new codec(JavaStruct.pack(c09));
		c.setCtw((byte) 0x09);
		byte[] data = JavaStruct.pack(c);
		buffer.addList((byte)id, data);
		for(int gid:user.getGrouplist()){
			if(gid!=user.getCurrgroup()){
				Group group = groupdao.get(gid);
				c09 = new codec09(group, false,user.getInterrupt());
				c = new codec(JavaStruct.pack(c09));
				c.setCtw((byte) 0x09);
				data = JavaStruct.pack(c);
				buffer.addList((byte)id, data);
			}
		}
		ctw0b(user.getCurrgroup(),3);
	}
	
	/**
	 * 群组更新消息
	 * 3:新上线用户，需要向该用户发送所有群组激活用户，向当前群组其它用户发送激活通知
	 * 4:下线通知  ，都不用转发GPS
	 * 5：激活通知 
	 * */
	public void ctw0b(int groupid,int n) throws StructException{
		System.out.println("ctw0b群组更新");
		UserDAO userdao = new UserDAO();
		User user = userdao.getbyid(id);
		if(user==null)
			return;
		if(n==3){
			for(int gid:user.getGrouplist()){
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
				buffer.addList((byte)id, data);
			}
			n=5;
		}
		codec0b c0b = new codec0b();
		c0b.setMark((byte) n);
		c0b.setUsernum((byte) 1);
		c0b.setGroupid((short) groupid);
		c0b.setUserlist(tools.int2Bytes(user.getId(), 2));
		codec c = new codec(JavaStruct.pack(c0b));
		c.setCtw((byte) 0x0b);
		byte[] data = JavaStruct.pack(c);
		List<User> users = userdao.list("select * from user where status = 1 and id != "+id+" and currgroup = "+groupid);
		for(User u:users){
			buffer.addList((byte) u.getId(), data);
		}
		if(n==4)
			return;
		String[] gpss = user.getGps().split(" ");
		codec0c c0c = new codec0c();
		c0c.setAltitude(Double.parseDouble(gpss[0]));
		c0c.setLongitude(Double.parseDouble(gpss[1]));
		ctw0d(JavaStruct.pack(c0c),1);
	}
	
	/**
	 * 用户位置更新  n为1：用户新上线，给他发送当前群组所有用户地址,再给别人发送自己地址，为0发送只给别人发当前用户
	 * */
	public void ctw0d(byte[] gps,int n) throws StructException{
//		System.out.println("ctw0dGPS更新");
		//更新数据库地址
		UserDAO userdao = new UserDAO();
		User user = userdao.getbyid(id);
		codec0c c0c = new codec0c();
		JavaStruct.unpack(c0c, gps);
		user.setGps(c0c.getAltitude()+" "+c0c.getLongitude());
//		System.out.println(user.getGps());
		userdao.update(user);
		
		//心跳包处理
		if(n==0){
			if(buffer.hearts.containsKey(id))
				buffer.hearts.put(id, buffer.hearts.get(id));
			else
				buffer.hearts.put(id, 1);
		}
		//发送反馈
		codec0d c0d = new codec0d();
		List<User> users = userdao.list(String.format("select * from user where status = 1 and currgroup = %d and id!=%d", user.getCurrgroup(),id));
		if(n==1&&users.size()>0){
//			List<User> gpsusers = userdao.list("select * from user where status = 1 and currgroup = "+user.getCurrgroup());
			c0d.setUsernum((byte) users.size());
			byte[] gpslist = new byte[0];
			for(User gu:users){
				String[] gpss = user.getGps().split(" ");
				codec0c c0c1 = new codec0c(); 
				c0c1.setAltitude(Double.parseDouble(gpss[0])); 
				c0c1.setLongitude(Double.parseDouble(gpss[1])); 
				codec0d1 c0d1 = new codec0d1();
				c0d1.setId((short) gu.getId());
				c0d1.setGps(JavaStruct.pack(c0c1));
				gpslist = tools.concat(gpslist, JavaStruct.pack(c0d1));
			}
			c0d.setGpslist(gpslist);
			codec c = new codec(JavaStruct.pack(c0d));
			c.setCtw((byte) 0x0d);
			byte[] data = JavaStruct.pack(c);
			buffer.addList((byte) id, data);
		}
		c0d.setUsernum((byte) 1);
		codec0d1 c0d1 = new codec0d1();
		c0d1.setId((short)id);
		c0d1.setGps(gps);
		c0d.setGpslist(JavaStruct.pack(c0d1));
		codec c = new codec(JavaStruct.pack(c0d));
		c.setCtw((byte) 0x0d);
		byte[] data = JavaStruct.pack(c);
		for(User u:users){
			buffer.addList((byte) u.getId(), data);
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
				group.userlist.clear();
				codec09 c09 = new codec09(group, broadcast,u.getInterrupt());
				codec c = new codec(JavaStruct.pack(c09));
				c.setCtw((byte) 0x09);
				byte[] data = JavaStruct.pack(c);
				buffer.addList((byte) u.getId(), data);
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
		buffer.addList((byte)id, data);
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
		codec09 c09 = new codec09(group, broadcast,user.getInterrupt());
		c09.setUsernum((byte) 0);
		c09.setUserlist(new byte[0]);
		c = new codec(JavaStruct.pack(c09));
		c.setCtw((byte) 0x09);
		data = JavaStruct.pack(c);
		buffer.addList((byte)id, data);
		//向新群组用户发送0x0B,更新群组
		ctw0b(user.getCurrgroup(),5);
		user.setCurrgroup((content[2]&0x0ff)<<8|content[3]&0x0ff);
		//向旧群组用户发送0x0B
		ctw0b(user.getCurrgroup(),4);
	}
	
	/**
	 * 申请或取消语音
	 * @throws StructException 
	 * */
	public void ctw21(byte[] content) throws StructException{
		System.out.println("ctw21申请或取消语音");
		UserDAO userdao = new UserDAO();
		User user = userdao.getbyid(id);
		int gid = user.getCurrgroup();
		codec21 c21 = new codec21();
		if(content[0]==0x01){
			if(buffer.getStatus(gid-1)>0){
				c21.setResult((byte) 0x01);
				c21.setId((short) buffer.getStatus(gid-1));
			}
			else{
				buffer.setStatus(id,gid-1);
				c21.setResult((byte) 0x00);
				c21.setId((short) id);
			}
		}
		else{
			if(buffer.getStatus(gid-1)==id){
				buffer.setStatus(0,gid-1);
				c21.setResult((byte) 0x00);
				c21.setId((short) 0);//
			}
			else{
				c21.setResult((byte) 0x01);
				c21.setId((short) buffer.getStatus(gid-1));
			}
		}
		if(user.getInterrupt()&&content[0]==0x01){
			int lastid = buffer.getStatus(gid-1);
			if(lastid>0){
				c21.setResult((byte) 0x03);
				c21.setId((short) id);
				codec c = new codec(JavaStruct.pack(c21));
				c.setCtw((byte) 0x21);
				byte[] data = JavaStruct.pack(c);
				buffer.addList((byte)lastid, data);
			}
			buffer.setStatus(id,gid-1);
			c21.setResult((byte) 0x00);
			c21.setId((short) id);
		}
		codec c = new codec(JavaStruct.pack(c21));
		c.setCtw((byte) 0x21);
		byte[] data = JavaStruct.pack(c);
		buffer.addList((byte)id, data);
	}
	
	/**
	 * 转发语音
	 * @throws StructException 
	 * */
	public void ctw23(byte[] content) throws StructException{
//		System.out.println("ctw23发送语音");
//		int groupid = (content[2]&0x0ff)<<8|content[3]&0x0ff;
//		if(buffer.getStatus(groupid-1)!=id)
//			return;
		codec22 c22 = new codec22();
		c22.setMessage(new byte[content.length-4]);
		JavaStruct.unpack(c22, content);
		codec23 c23 = new codec23();
		c23.setCode(c22.getCode());
		c23.setGroupid(c22.getGroupid());
		c23.setUserid((short) id);
		c23.setMessage(c22.getMessage());
		codec c = new codec(JavaStruct.pack(c23));
		c.setCtw((byte) 0x23);
		byte[] data = JavaStruct.pack(c);
		UserDAO userdao = new UserDAO();
		List<User> users = userdao.listbybroad(c22.getGroupid());
		for(User u:users){
			buffer.addList((byte) u.getId(), data);
		}
	}
	
	/**
	 * 用户下线
	 * @throws StructException 
	 * @throws IOException 
	 * */
	public void close(int id) throws StructException, IOException{
		this.id = id;
		System.out.println("准备断开");
//		System.out.println("线程数："+Thread.activeCount());
		UserDAO userdao = new UserDAO();
		GroupDAO groupdao = new GroupDAO();
		User user = userdao.getbyid(id);
		if(user==null) return;
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
				group.userlist.clear();
				codec09 c09 = new codec09(group, true,u.getInterrupt());
				codec c = new codec(JavaStruct.pack(c09));
				c.setCtw((byte) 0x09);
				byte[] data = JavaStruct.pack(c);
				buffer.addList((byte) u.getId(), data);
			}
		}
		//下线通知
		if(user.getStatus())
			ctw0b(user.getCurrgroup(),4);
		//更新数据库
		user.setBroadcast(false);
		user.setStatus(false);
		user.setMark(null);
		userdao.update(user);
		if(buffer.getSocketChannel(id)!=null){
			buffer.getSocketChannel(id).close();
			buffer.delSocketChannel(id);
		}
		if(buffer.getStatus(user.getCurrgroup()-1)==id)
			buffer.setStatus(0, user.getCurrgroup()-1);
	}

}
