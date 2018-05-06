package bean;

import java.util.HashMap;

public class Group {
	public int id;
	public String description;
	public int usernum;
	public HashMap<Integer,String> userlist = new HashMap<>();
//	@Override
//	public String toString() {
//		return "Group [id=" + id + ", description=" + description + ", usernum=" + usernum + ", userlist=" + userlist
//				+ "]";
//	}
	public HashMap<Integer,String> getUserlist() {
		return userlist;
	}
	public void setUserlist(HashMap<Integer,String> userlist) {
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
