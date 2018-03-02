package tool;

import bean.Group;
import struct.StructClass;
import struct.StructField;

@StructClass
public class codec09 {
	
	@StructField(order = 0)
	private short groupid;
	
	@StructField(order = 1)
	private boolean broadcast;
	
	@StructField(order = 2)
	private boolean interrupt;
	
	@StructField(order = 3)
	private byte usernum;
	
	@StructField(order = 4)
	private byte[] description = new byte[32];
	
	@StructField(order = 5)
	private byte[] userlist;
	
	public codec09(Group group,boolean broadcast,boolean interrupt){
		this.groupid = (short) group.getId();
		this.broadcast = broadcast;
		this.interrupt = interrupt;
		this.usernum = (byte) group.getUsernum();
		this.description = tools.str2Bytes(group.getDescription(), 32);
		this.userlist = tools.getUserlist(group.getUserlist());
	}

	public byte[] getUserlist() {
		return userlist;
	}

	public void setUserlist(byte[] userlist) {
		this.userlist = userlist;
	}

	public short getGroupid() {
		return groupid;
	}

	public void setGroupid(short groupid) {
		this.groupid = groupid;
	}

	public boolean getBroadcast() {
		return broadcast;
	}

	public void setBroadcast(boolean broadcast) {
		this.broadcast = broadcast;
	}

	public boolean getInterrupt() {
		return interrupt;
	}

	public void setInterrupt(boolean interrupt) {
		this.interrupt = interrupt;
	}

	public byte getUsernum() {
		return usernum;
	}

	public void setUsernum(byte usernum) {
		this.usernum = usernum;
	}

	public byte[] getDescription() {
		return description;
	}

	public void setDescription(byte[] description) {
		this.description = description;
	}
}
