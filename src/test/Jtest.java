package test;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import bean.Group;
import bean.User;
import dao.GroupDAO;
import dao.UserDAO;
import struct.JavaStruct;
import struct.StructException;
import tool.codec;
import tool.codec04;
import tool.codect;
import tool.tools;

public class Jtest {
	private byte[] b;

//	@Before
	public void setUp() throws Exception {
		codec04 c04 = new codec04();
		c04.setName(tools.str2Bytes("lisi",32));
		c04.setPwd(tools.str2Bytes("12345",32));
		c04.setCodenum((byte) 2);
		c04.setCodelist(new byte[2]);
		c04.setPrefix((byte) 0);
		byte[] data = JavaStruct.pack(c04);
		codec c = new codec(data);
		c.setCtw((byte) 0x04);
		this.b = JavaStruct.pack(c);
	}

//	@Test
	public void adduser() {
			User user = new User();
//			user.setId(6);
			user.setName("ede");
			user.setPwd("54321");
			user.setCurrgroup(3);
//			user.setGrouplist("3|5");
			UserDAO userdao = new UserDAO();
			userdao.add(user);
	}
	//@Test
	public void updategroup() {
		UserDAO userdao = new UserDAO();
		GroupDAO groupdao = new GroupDAO();
		Group gnull = new Group();
		for(int i=1;i<7;i++){
			gnull.setId(i);
			groupdao.update(gnull);
		}
		List<User> users = userdao.list("select * from user");
//		for(User user:users){
////			String[] groups = user.getGrouplist().split("\\|");
//			for(String groupid:groups){
//				int gid = Integer.parseInt(groupid);
//				Group group = groupdao.get(gid);
//				group.setUsernum(group.getUsernum()+1);
//            	if(group.getUserlist()!=null)
//            		group.setUserlist(group.getUserlist()+"|"+user.getId()+"."+user.getName());
//            	else
//            		group.setUserlist(user.getId()+"."+user.getName());
//            	groupdao.update(group);
//			}
//		}
	}
	@Test
	public void testnull() throws UnknownHostException, SocketException, StructException{
		codect ct = new codect();
		ct.list = new ArrayList<Integer>();
		ct.list.add(1);
		JavaStruct.pack(ct);
	}

}
