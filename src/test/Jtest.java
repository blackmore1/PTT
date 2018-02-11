package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import struct.JavaStruct;
import tool.codec;
import tool.codec04;
import tool.tools;

public class Jtest {
	private byte[] b;

	@Before
	public void setUp() throws Exception {
		codec04 c04 = new codec04();
		c04.setName(tools.str2Bytes("lisi",32));
		c04.setPwd(tools.str2Bytes("12345",32));
		c04.setCodenum((byte) 2);
		c04.setCodelist(new byte[2]);
		c04.setPrefix((byte) 0);
		byte[] data = JavaStruct.pack(c04);
		codec c = new codec(data);
		c.setCtw((byte) 0x04);
		this.b = JavaStruct.pack(c);
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
