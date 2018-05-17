package server;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Scanner;

import com.google.gson.Gson;

import bean.Group;
import bean.User;
import dao.GroupDAO;
import dao.UserDAO;
import struct.JavaStruct;
import struct.StructException;
import tool.codec;
import tool.codec09;
import tool.codec0b;
import tool.tools;

public class Admin implements Runnable {
	Scanner sc = new Scanner(System.in); 
	@Override
	public void run() {
		while(true){
			System.out.println("输入指令：(1:用户信息;2:添加用户或群组;3:删除用户4:权限管理;5:更新群组;6:缓冲区)");
			try{
				int order = sc.nextInt();
				switch(order){
				case 1:
					userInfo();
					break;
				case 2:
					addUserorGroup();
					break;
				case 3:
					delUser();
					break;
				case 4:
					permission();
					break;
				case 5:
					updategroup();;
					break;
				case 6:
					bufferinfo();;
					break;
				default:
					System.out.println("输入错误:"+order);
				}
			}catch(NoSuchElementException e){
				String token = sc.next();
				System.out.println("输入错误:"+token);
			}
		}
	}
	
	private void bufferinfo() {
		System.out.println("输入指令：(1:socket缓冲区大小;2:socket缓冲区列表;3:发言权)");
		try{
			int order = sc.nextInt();
			if(order==1){
				System.out.println(Buffer.sockets.size());
			}
			else if(order==2){
				Iterator it = Buffer.sockets.entrySet().iterator();
				while(it.hasNext()){
					Map.Entry<Integer,SocketChannel> entry = (Entry) it.next();
					System.out.println(entry.getKey()+" "+entry.getValue().getRemoteAddress().toString());
				}
			}
			else if(order==3){
				for(int s:Buffer.status)
					System.out.print(s+" ");
				System.out.println();
			}
		}catch(InputMismatchException | IOException e){
			System.out.println("输入错误");
		}
	}
	private void addUserorGroup() {
		System.out.println("输入指令：(1:添加用户;2:添加群组)");
		try{
			int order = sc.nextInt();
			if(order==1)
				addUser();
			else if(order==2)
				addGroup();
		}catch(InputMismatchException e){
			System.out.println("输入错误");
		}
	}
	private void addGroup() {
		try{
			Group group = new Group();
			System.out.println("输入群组描述：");
			sc.nextLine();//防止在sc.nextInt()后出异常
			group.description = sc.nextLine();
			new GroupDAO().add(group);
			Buffer.status = tools.concat(Buffer.status, new int[1]);
		}catch(InputMismatchException e){
			System.out.println("输入错误");
		}
	}
	public void userInfo(){
		UserDAO userdao = new UserDAO();
		GroupDAO groupdao = new GroupDAO();
		System.out.println("输入指令：(1:所有用户;2:在线用户;3:某一群组用户;4:某一用户)");
		try{
			int order = sc.nextInt();
			if(order==1){
				List<User> users = userdao.list("select * from user");
				for(User user:users)
					System.out.println(user);
			}
			else if(order==2){
				List<User> users = userdao.list("select * from user where status=1");
				for(User user:users)
					System.out.println(user);
			}
			else if(order==3){
				System.out.print("输入群组id:");
				int gid = sc.nextInt();
				Group group = groupdao.get(gid);
				Iterator<Map.Entry<Integer, String>> it = group.userlist.entrySet().iterator();
				while(it.hasNext()){
					Map.Entry<Integer, String> entry = it.next();
					User u = userdao.getbyid(entry.getKey());
					System.out.println(u);
				}
			}
			else if(order==4){
				System.out.print("输入用户id:");
				int id = sc.nextInt();
				User user = userdao.getbyid(id);
				System.out.println(user);
			}
		}catch(InputMismatchException e){
			System.out.println("输入错误");
		}
	}
	
	public void addUser(){
		try{
			GroupDAO gdao = new GroupDAO();
			UserDAO userdao = new UserDAO();
			int total = gdao.getTotal();
			User user = new User();
			System.out.print("输入用户名：");
			sc.nextLine();
			String name = sc.nextLine();
			User user1 = userdao.get("select * from user where name = '"+name.trim()+"'");
			if(user1!=null){
				System.out.println("该用户已存在");
				return;
			}
			System.out.print("输入密码：");
			String pwd = sc.nextLine();
			System.out.printf("输入用户所在群组(最大群组为%d,多个群用空格隔开)：",total);
			String groupliststr = sc.nextLine();
			String[] groups = groupliststr.split(" ");
			List<Integer> grouplist = new ArrayList<Integer>();
			for(int i=0;i<groups.length;i++){
				int gid = Integer.parseInt(groups[i]);
				if(gid>total){
					System.out.println("格式错误");
					return;
				}
				grouplist.add(gid);
			}
			if(tools.isRepeat(grouplist)){
				System.out.println("重复");
				return;
			}
			user.setName(name.trim());
			user.setPwd(pwd.trim());
			user.setCurrgroup(grouplist.get(0));
			user.setGrouplist(grouplist);
			userdao.add(user);
			user = userdao.get("select * from user where name = '"+name.trim()+"'");
			//向所有用户发送通知
			for(int groupid:grouplist){
				codec0b c0b = new codec0b();
				c0b.setMark((byte) 3);
				c0b.setUsernum((byte) 1);
				c0b.setGroupid((short) groupid);
				byte[] userlist = tools.concat(tools.int2Bytes(user.getId(), 2), tools.str2Bytes(user.getName(), 16));
				c0b.setUserlist(userlist);
				codec c = new codec(JavaStruct.pack(c0b));
				c.setCtw((byte) 0x0b);
				byte[] data = JavaStruct.pack(c);
				List<User> users = userdao.list("select * from user where broadcast = 1 and currgroup = "+groupid);
				for(User u:users){
					Buffer.addList((byte) u.getId(), data);
				}
			}
		}catch(NumberFormatException | StructException e){
			System.out.println("格式错误");
		}
	}
	
	public void delUser(){
		UserDAO userdao = new UserDAO();
		System.out.println("输入需要删除的用户id：");
		try{
			int id = sc.nextInt();
			SocketChannel s = Buffer.getSocketChannel(id);
			if(s!=null){
				s.close();
				new Receive().close(id);
			}
			userdao.delete(id);
		}catch(InputMismatchException | IOException | StructException e){
			System.out.println("输入错误");
		}
	}
	
	public void permission(){
		UserDAO userdao = new UserDAO();
		GroupDAO groupdao = new GroupDAO();
		Scanner sc = new Scanner(System.in); 
		System.out.println("输入指令：(1:强迫下线;2:变更插话权限)");
		try{
			int order = sc.nextInt();
			if(order==1){
				System.out.println("输入用户id:");
				int id = sc.nextInt();
				User user = userdao.getbyid(id);
				if(user==null){
					System.out.println("用户不存在");
					return;
				}
//				if(user.getStatus()==false){
//					System.out.println("用户不在线");
//					return;
//				}
//				SocketChannel s = Buffer.getSocketChannel(id);
				new Receive().close(id);
//				if(s!=null){
//					s.close();
//				}
			}
			else if(order == 2){
				System.out.println("输入用户id:");
				int id = sc.nextInt();
				User user = userdao.getbyid(id);
				if(user==null){
					System.out.println("用户不存在");
					return;
				}
				user.setInterrupt(!user.getInterrupt());
				userdao.update(user);
				Group group = groupdao.get(user.getCurrgroup());
				group.setUsernum(0);
				group.userlist.clear();
				codec09 c09 = new codec09(group, user.getBroadcast(),user.getInterrupt());
				codec c = new codec(JavaStruct.pack(c09));
				c.setCtw((byte) 0x09);
				byte[] data = JavaStruct.pack(c);
				Buffer.addList((byte) id, data);
			}
		}catch(InputMismatchException | IOException | StructException e){
			System.out.println("输入错误");
		}
	}
	
	public void updategroup() {
		UserDAO userdao = new UserDAO();
		GroupDAO groupdao = new GroupDAO();
		Group gnull = new Group();
		for(int i=1;i<7;i++){
			gnull.setId(i);
			groupdao.update(gnull);
		}
		List<User> users = userdao.list("select * from user");
		for(User user:users){
			for(int gid:user.getGrouplist()){
				Group group = groupdao.get(gid);
				group.setUsernum(group.getUsernum()+1);
				group.userlist.put(user.getId(), user.getName());
            	groupdao.update(group);
			}
		}
		System.out.println("更新完成");
	}

}
