package bean;

public class User {
	private int id;
	private String name;
	private String pwd;
	private boolean status;
	private boolean broadcast;
	private String ipv4;
	private int codenum;
	private String codelist;
	private String gps;
	private int currgroup;
	private int groupnum;
	private String grouplist;
	private long threadid;
	public long getThreadid() {
		return threadid;
	}
	public void setThreadid(long threadid) {
		this.threadid = threadid;
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
	public String getGrouplist() {
		return grouplist;
	}
	public void setGrouplist(String grouplist) {
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
	public String getCodelist() {
		return codelist;
	}
	public void setCodelist(String codelist) {
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
		return "User [id=" + id + ", name=" + name + ", pwd=" + pwd + ", status=" + status + ", broadcast=" + broadcast
				+ ", ipv4=" + ipv4 + ", codenum=" + codenum + ", codelist=" + codelist + ", gps=" + gps + ", currgroup="
				+ currgroup + ", groupnum=" + groupnum + ", grouplist=" + grouplist + "]";
	}

}
