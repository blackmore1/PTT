package server;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

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
	private Buffer buffer;
	public Admin(Buffer buffer){
		this.buffer = buffer;
	}
	@Override
	public void run() {
		Scanner sc = new Scanner(System.in); 
		while(true){
			System.out.println("输入指令：(1:用户信息;2:添加用户;3:删除用户4:权限管理;5:更新群组)");
			try{
				int order = sc.nextInt();
				switch(order){
				case 1:
					userInfo();
					break;
				case 2:
					addUser();
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
				default:
					System.out.println("输入错误");
				}
			}catch(InputMismatchException e){
				System.out.println("输入错误");
			}
		}
	}
	
	public void userInfo(){
		UserDAO userdao = new UserDAO();
		GroupDAO groupdao = new GroupDAO();
		Scanner sc = new Scanner(System.in); 
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
				String[] users = group.getUserlist().split("\\|");
				for(String user:users){
					String[] s = user.split("\\.");
					int id = Integer.parseInt(s[0]);
					System.out.println(userdao.getbyid(id));
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
			Scanner sc = new Scanner(System.in); 
			User user = new User();
			System.out.print("输入用户名：");
			String name = sc.nextLine();
			User user1 = userdao.get("select * from user where name = '"+name.trim()+"'");
			if(user1!=null){
				System.out.println("该用户已存在");
				return;
			}
			System.out.print("输入密码：");
			String pwd = sc.nextLine();
			System.out.printf("输入用户所在群组(最大群组为%d)：",total);
			String grouplist = sc.nextLine();
			String[] groups = grouplist.split("\\|");
			int[] gid = new int[groups.length];
			for(int i=0;i<groups.length;i++){
				gid[i] = Integer.parseInt(groups[i]);
				if(gid[i]>total){
					System.out.println("格式错误");
					return;
				}
			}
			if(tools.isRepeat(gid)){
				System.out.println("重复");
				return;
			}
			user.setName(name.trim());
			user.setPwd(pwd.trim());
			user.setCurrgroup(gid[0]);
			user.setGrouplist(grouplist);
			userdao.add(user);
			user = userdao.get("select * from user where name = '"+name.trim()+"'");
			//向所有用户发送通知
			for(int groupid:gid){
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
					buffer.addList((byte) u.getId(), data);
				}
			}
		}catch(NumberFormatException | StructException e){
			System.out.println("格式错误");
		}
	}
	
	public void delUser(){
		UserDAO userdao = new UserDAO();
		Scanner sc = new Scanner(System.in); 
		System.out.println("输入需要删除的用户id：");
		try{
			int id = sc.nextInt();
			User user = userdao.getbyid(id);
			SocketChannel s = buffer.getSocketChannel(id);
			if(s!=null){
				s.close();
				new Rec(buffer).close(id);
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
				if(user.getStatus()==false){
					System.out.println("用户不在线");
					return;
				}
				SocketChannel s = buffer.getSocketChannel(id);
				new Rec(buffer).close(id);
				if(s!=null){
					s.close();
				}
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
				group.setUserlist("");
				codec09 c09 = new codec09(group, user.getBroadcast(),user.getInterrupt());
				codec c = new codec(JavaStruct.pack(c09));
				c.setCtw((byte) 0x09);
				byte[] data = JavaStruct.pack(c);
				buffer.addList((byte) id, data);
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
			String[] groups = user.getGrouplist().split("\\|");
			for(String groupid:groups){
				int gid = Integer.parseInt(groupid);
				Group group = groupdao.get(gid);
				group.setUsernum(group.getUsernum()+1);
            	if(group.getUserlist()!=null)
            		group.setUserlist(group.getUserlist()+"|"+user.getId()+"."+user.getName());
            	else
            		group.setUserlist(user.getId()+"."+user.getName());
            	groupdao.update(group);
			}
		}
		System.out.println("更新完成");
	}

}
