package tool;

import struct.StructClass;
import struct.StructField;

@StructClass
public class codec21 {
	@StructField(order = 0)
	private byte result;
	@StructField(order = 1)
	private short id;
	public byte getResult() {
		return result;
	}
	public void setResult(byte result) {
		this.result = result;
	}
	public short getId() {
		return id;
	}
	public void setId(short id) {
		this.id = id;
	}
}
