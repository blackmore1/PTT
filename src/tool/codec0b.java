package tool;

import struct.StructClass;
import struct.StructField;

@StructClass
public class codec0b {
	
	@StructField(order = 0)
	private byte mark;
	
	@StructField(order = 1)
	private short groupid;
	
	@StructField(order = 2)
	private byte usernum;
	
	@StructField(order = 3)
	private byte[] userlist;

	public byte[] getUserlist() {
		return userlist;
	}

	public void setUserlist(byte[] userlist) {
		this.userlist = userlist;
	}

	public byte getMark() {
		return mark;
	}

	public void setMark(byte mark) {
		this.mark = mark;
	}

	public short getGroupid() {
		return groupid;
	}

	public void setGroupid(short groupid) {
		this.groupid = groupid;
	}

	public byte getUsernum() {
		return usernum;
	}

	public void setUsernum(byte usernum) {
		this.usernum = usernum;
	}
}
