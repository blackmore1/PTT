package bean;

public class Group {
	private int id;
	private String description;
	private int usernum;
	private String userlist;
	@Override
	public String toString() {
		return "Group [id=" + id + ", description=" + description + ", usernum=" + usernum + ", userlist=" + userlist
				+ "]";
	}
	public String getUserlist() {
		return userlist;
	}
	public void setUserlist(String userlist) {
		this.userlist = userlist;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getUsernum() {
		return usernum;
	}
	public void setUsernum(int usernum) {
		this.usernum = usernum;
	}
}
