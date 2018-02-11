package tool;

import struct.StructClass;
import struct.StructField;

@StructClass
public class codec04 {

	@StructField(order = 0)
	private byte[] name = new byte[32];
	
	@StructField(order = 1)
	private byte[] pwd = new byte[32];
	
	@StructField(order = 2)
	private byte codenum;
	
	@StructField(order = 3)
	private byte[] codelist;
	
	@StructField(order = 4)
	private double latitude;
	
	@StructField(order = 5)
	private double longitude;
	
	@StructField(order = 6)
	private byte[] ipv4 = new byte[4];
	
	@StructField(order = 7)
	private byte prefix;
	
	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
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

	public byte[] getIpv4() {
		return ipv4;
	}

	public void setIpv4(byte[] ipv4) {
		this.ipv4 = ipv4;
	}

	public byte getPrefix() {
		return prefix;
	}

	public void setPrefix(byte prefix) {
		this.prefix = prefix;
	}

	public byte[] getName() {
		return name;
	}

	public void setName(byte[] name) {
		this.name = name;
	}

	public byte[] getPwd() {
		return pwd;
	}

	public void setPwd(byte[] pwd) {
		this.pwd = pwd;
	}
}
