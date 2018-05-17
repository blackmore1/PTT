package test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import struct.JavaStruct;
import struct.StructException;
import tool.codect;

public class Jtest {
	@Test
	public void t1() throws StructException{
		codect ct = new codect();
		List<Integer> list = new ArrayList<>();
		list.add(1);
		ct.setResult(list);
		JavaStruct.pack(ct);
	}
}
