package tool;

import struct.StructClass;
import struct.StructField;

@StructClass
public class codec22 {
	
	@StructField(order = 0)
	private byte code;
	
	@StructField(order = 1)
	private byte b = 0x00;
	
	@StructField(order = 2)
	private short groupid;
	
	@StructField(order = 3)
	private byte[] message;

	public byte getCode() {
		return code;
	}

	public void setCode(byte code) {
		this.code = code;
	}

	public byte getB() {
		return b;
	}

	public void setB(byte b) {
		this.b = b;
	}

	public short getGroupid() {
		return groupid;
	}

	public void setGroupid(short groupid) {
		this.groupid = groupid;
	}

	public byte[] getMessage() {
		return message;
	}

	public void setMessage(byte[] message) {
		this.message = message;
	}
}
