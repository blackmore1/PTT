package tool;

import struct.StructClass;
import struct.StructField;

@StructClass
public class codec23 {

	@StructField(order = 0)
	private byte code;
	
	@StructField(order = 1)
	private byte b = 0x00;
	
	@StructField(order = 2)
	private short groupid;
	
	@StructField(order = 3)
	private short userid;
	
	@StructField(order = 4)
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

	public short getUserid() {
		return userid;
	}

	public void setUserid(short userid) {
		this.userid = userid;
	}

	public byte[] getMessage() {
		return message;
	}

	public void setMessage(byte[] message) {
		this.message = message;
	}
}
