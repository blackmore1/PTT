package tool;

import struct.StructClass;
import struct.StructField;

@StructClass
public class codec0d {
	@StructField(order = 0)
	private byte usernum;
	
	@StructField(order = 1)
	private byte[] gpslist;
	
	public byte getUsernum() {
		return usernum;
	}

	public void setUsernum(byte usernum) {
		this.usernum = usernum;
	}

	public byte[] getGpslist() {
		return gpslist;
	}

	public void setGpslist(byte[] gpslist) {
		this.gpslist = gpslist;
	}
	
	
}
