package tool;

import java.util.List;

import struct.StructClass;
import struct.StructField;

@StructClass
public class codect {
	@StructField(order = 0)
	private List<Integer> result;

	public List<Integer> getResult() {
		return result;
	}

	public void setResult(List<Integer> result) {
		this.result = result;
	}
	
}
