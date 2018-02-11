package tool;

import struct.StructClass;
import struct.StructField;

@StructClass
public class codec05 {

	@StructField(order = 0)
	private byte result;
	
	@StructField(order = 1)
	private byte reason;
	
	@StructField(order = 2)
	private short id;

	public byte getResult() {
		return result;
	}

	public void setResult(byte result) {
		this.result = result;
	}

	public byte getReason() {
		return reason;
	}

	public void setReason(byte reason) {
		this.reason = reason;
	}

	public short getId() {
		return id;
	}

	public void setId(short id) {
		this.id = id;
	}
	
}

