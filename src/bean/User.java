package bean;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class User {
	private int id;
	private String name;
	private String pwd;
	private boolean status;
	private boolean broadcast;
	private String ipv4;
	private int codenum;
	private List<Integer> codelist = new ArrayList<Integer>();
	private String gps;
	private int currgroup;
	private int groupnum;
	private List<Integer> grouplist = new ArrayList<Integer>();
	private String mark;
	private boolean interrupt;
	public boolean getInterrupt() {
		return interrupt;
	}
	public void setInterrupt(boolean interrupt) {
		this.interrupt = interrupt;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	public boolean getBroadcast() {
		return broadcast;
	}
	public void setBroadcast(boolean broadcast) {
		this.broadcast = broadcast;
	}
	public int getCurrgroup() {
		return currgroup;
	}
	public void setCurrgroup(int currgroup) {
		this.currgroup = currgroup;
	}
	public int getGroupnum() {
		return groupnum;
	}
	public void setGroupnum(int groupnum) {
		this.groupnum = groupnum;
	}
	public List<Integer> getGrouplist() {
		return grouplist;
	}
	public void setGrouplist(List<Integer> grouplist) {
		this.grouplist = grouplist;
	}
	public String getGps() {
		return gps;
	}
	public void setGps(String gps) {
		this.gps = gps;
	}
	public String getIpv4() {
		return ipv4;
	}
	public void setIpv4(String ipv4) {
		this.ipv4 = ipv4;
	}
	public int getCodenum() {
		return codenum;
	}
	public void setCodenum(int codenum) {
		this.codenum = codenum;
	}
	public List<Integer> getCodelist() {
		return codelist;
	}
	public void setCodelist(List<Integer> codelist) {
		this.codelist = codelist;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public boolean getStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return new Gson().toJson(this);
	}

}
