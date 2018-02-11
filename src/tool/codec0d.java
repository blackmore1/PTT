package tool;

import struct.StructClass;
import struct.StructField;

@StructClass
public class codec0d {
	@StructField(order = 0)
	private byte usernum;
	
	@StructField(order = 1)
	private short id;
	
	@StructField(order = 2)
	private byte[] gps = new byte[16];

	public byte getUsernum() {
		return usernum;
	}

	public void setUsernum(byte usernum) {
		this.usernum = usernum;
	}

	public short getId() {
		return id;
	}

	public void setId(short id) {
		this.id = id;
	}

	public byte[] getGps() {
		return gps;
	}

	public void setGps(byte[] gps) {
		this.gps = gps;
	}
}
