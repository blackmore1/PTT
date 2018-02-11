package tool;

import struct.StructClass;
import struct.StructField;

@StructClass
public class codec07 {
	@StructField(order = 0)
	private byte gpsperiod;
	@StructField(order = 1)
	private byte groupnum;
	@StructField(order = 2)
	private byte codenum;
	@StructField(order = 3)
	private byte[] codelist;
	public byte getGpsperiod() {
		return gpsperiod;
	}
	public void setGpsperiod(byte gpsperiod) {
		this.gpsperiod = gpsperiod;
	}
	public byte getGroupnum() {
		return groupnum;
	}
	public void setGroupnum(byte groupnum) {
		this.groupnum = groupnum;
	}
	public byte getCodenum() {
		return codenum;
	}
	public void setCodenum(byte codenum) {
		this.codenum = codenum;
	}
	public byte[] getCodelist() {
		return codelist;
	}
	public void setCodelist(byte[] codelist) {
		this.codelist = codelist;
	}
	
	
}
