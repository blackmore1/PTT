package tool;

import struct.StructClass;
import struct.StructField;

@StructClass
public class codec0d1 {
	@StructField(order = 0)
	private short id;
	
	@StructField(order = 1)
	private byte[] gps = new byte[16];

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
