package tool;

import struct.StructClass;
import struct.StructField;

@StructClass
public class codec11 {
	@StructField(order = 0)
	private byte result;
	
	@StructField(order = 1)
	private short groupid;

	public byte getResult() {
		return result;
	}

	public void setResult(byte result) {
		this.result = result;
	}

	public short getGroupid() {
		return groupid;
	}

	public void setGroupid(short groupid) {
		this.groupid = groupid;
	}
	
}
