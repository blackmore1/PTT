package tool;

import java.util.List;

import javolution.io.Struct;
import javolution.io.Struct.BitField;
import javolution.io.Struct.Float32;
import javolution.io.Struct.Unsigned16;
import javolution.lang.Immutable;
import javolution.util.FastTable;

public class Student extends Struct {
	private Immutable<List<String>> winners 
    = new FastTable<String>().addAll("John Deuff", "Otto Graf", "Sim Kamil").toImmutable();
    private UTF8String name  = new UTF8String(8);
	private byte[] d = new byte[10];
    private Float32    grade = new Float32();
    private Unsigned16 year  = new Unsigned16();
    
	public Float32 getGrade() {
		return grade;
	}
	public void setGrade(Float32 grade) {
		this.grade = grade;
	}
	public Unsigned16 getYear() {
		return year;
	}
	public void setYear(Unsigned16 year) {
		this.year = year;
	}
//	public UTF8String getName() {
//		return name;
//	}
//	public void setName(UTF8String name) {
//		this.name = name;
//	}
	public byte[] getD() {
		return d;
	}
	public void setD(byte[] d) {
		this.d = d;
	}
	public UTF8String getName() {
		return name;
	}
	public void setName(UTF8String name) {
		this.name = name;
	}
	public Immutable<List<String>> getWinners() {
		return winners;
	}
	public void setWinners(Immutable<List<String>> winners) {
		this.winners = winners;
	}
}