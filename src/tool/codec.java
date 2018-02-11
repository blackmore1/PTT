package tool;

import struct.JavaStruct;
import struct.StructClass;
import struct.StructField;

@StructClass
public class codec {
	 @StructField(order = 0)  
//	 @ArrayLengthMarker (fieldName = "content")
     private short length;  
       
     @StructField(order = 1)  
     private short b = (short) 0xaa55;  
     
     @StructField(order = 2)  
     private byte version = 0x10;  
     
     @StructField(order = 3)  
     private byte ctw;  
     
     @StructField(order = 4)  
     private byte[] content;  
     
     public codec(byte[] content){
    	 this.length = (short) (content.length+6);
    	 this.content = content;
     }

	public short getLength() {
		return length;
	}

	public void setLength(short length) {
		this.length = length;
	}

	public short getB() {
		return b;
	}

	public void setB(short b) {
		this.b = b;
	}

	public byte getVersion() {
		return version;
	}

	public void setVersion(byte version) {
		this.version = version;
	}

	public byte getCtw() {
		return ctw;
	}

	public void setCtw(byte ctw) {
		this.ctw = ctw;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}
	
     
}
/*@StructClass
class codec05 extends codec{

	@StructField(order = 0)
	public byte status;
	
	@StructField(order = 1)
	public byte reason;
	
	public codec05(byte[] content) {
		super(content);
		// TODO Auto-generated constructor stub
	}
	
}*/
