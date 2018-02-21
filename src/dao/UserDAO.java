package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import bean.Group;
import bean.User;

public class UserDAO {
	  
    public UserDAO() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
  
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/ptt?characterEncoding=UTF-8", "root",
                "3598085656");
    }
  
    public void add(User user) {
  
        String sql = "insert into user values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
  
        	ps.setInt(1, user.getId());
        	ps.setString(2, user.getName());
        	ps.setString(3, user.getPwd());
        	ps.setBoolean(4, user.getStatus());
        	ps.setBoolean(5, user.getBroadcast());
            ps.setString(6, user.getIpv4());
            ps.setInt(7, user.getCodenum());
            ps.setString(8, user.getCodelist());
            ps.setString(9, user.getGps());
            ps.setInt(10, user.getCurrgroup());
            ps.setInt(11, user.getGroupnum());
            ps.setString(12, user.getGrouplist());
            ps.setLong(13, user.getThreadid());
            ps.execute();
            GroupDAO gdao = new GroupDAO();
            String[] groups = user.getGrouplist().split("\\|");
            for(String g:groups){
            	Group group = gdao.get(Integer.parseInt(g));
            	group.setUsernum(group.getUsernum()+1);
            	if(group.getUserlist()!=null)
            		group.setUserlist(group.getUserlist()+"|"+user.getId()+"."+user.getName());
            	else
            		group.setUserlist(user.getId()+"."+user.getName());
            	gdao.update(group);
            }
            
        } catch (SQLException e) {
  
            e.printStackTrace();
        }
    }
  
    public void update(User user) {
  
        String sql = "update user set status = ?,broadcast = ?,ipv4 = ?,codenum = ?,codelist = ?,gps = ?,currgroup = ?,groupnum = ?,"
        		+ "grouplist = ?,threadid = ? where id = ?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
  
        	ps.setBoolean(1, user.getStatus());
        	ps.setBoolean(2, user.getBroadcast());
            ps.setString(3, user.getIpv4());
            ps.setInt(4, user.getCodenum());
            ps.setString(5, user.getCodelist());
            ps.setString(6, user.getGps());
            ps.setInt(7, user.getCurrgroup());
            ps.setInt(8, user.getGroupnum());
            ps.setString(9, user.getGrouplist());
            ps.setLong(10, user.getThreadid());
            ps.setInt(11, user.getId());
  
            ps.execute();
  
        } catch (SQLException e) {
  
            e.printStackTrace();
        }
  
    }
    
    public User get(String sql) {
    	User user= null;
  
        try (Connection c = getConnection(); Statement s = c.createStatement();) {
  
            ResultSet rs = s.executeQuery(sql);
  
            if (rs.next()) {
            	user = new User();
            	int id = rs.getInt(1);
                String name = rs.getString(2);
                String password = rs.getString(3);
                boolean status = rs.getBoolean(4);
                boolean broadcast = rs.getBoolean(5);
                String ipv4 = rs.getString(6);
                int codenum = rs.getInt(7);
                String codelist = rs.getString(8);
                String gps = rs.getString(9);
                int currgroup = rs.getInt(10);
                int groupnum = rs.getInt(11);
                String grouplist = rs.getString(12);
                long threadid = rs.getLong(13);
                user.setId(id);
                user.setName(name);
                user.setPwd(password);
                user.setStatus(status);
                user.setBroadcast(broadcast);
                user.setIpv4(ipv4);
                user.setCodenum(codenum);
                user.setCodelist(codelist);
                user.setGps(gps);
                user.setCurrgroup(currgroup);
                user.setGroupnum(groupnum);
                user.setThreadid(threadid);
                user.setGrouplist(grouplist);
            }
  
        } catch (SQLException e) {
  
            e.printStackTrace();
        }
        return user;
    }
  
    public List<User> list(String sql) {
        List<User> users = new ArrayList<User>();
//        String sql = "select * from user where broadcast = 1";
  
        try (Connection c = getConnection();Statement s=c.createStatement();) {
  
            ResultSet rs = s.executeQuery(sql);
            //sql="alter table user AUTO_INCREMENT = "+(new UserDAO().getTotal()+1);
            //s.execute(sql);
            while (rs.next()) {
            	User user = new User();
                int id = rs.getInt(1);
                String name = rs.getString(2);
                String password = rs.getString(3);
                boolean status = rs.getBoolean(4);
                boolean broadcast = rs.getBoolean(5);
                String ipv4 = rs.getString(6);
                int codenum = rs.getInt(7);
                String codelist = rs.getString(8);
                String gps = rs.getString(9);
                int currgroup = rs.getInt(10);
                int groupnum = rs.getInt(11);
                String grouplist = rs.getString(12);
                long threadid = rs.getLong(13);
                user.setId(id);
                user.setName(name);
                user.setPwd(password);
                user.setStatus(status);
                user.setBroadcast(broadcast);
                user.setIpv4(ipv4);
                user.setCodenum(codenum);
                user.setCodelist(codelist);
                user.setGps(gps);
                user.setCurrgroup(currgroup);
                user.setGroupnum(groupnum);
                user.setGrouplist(grouplist);
                user.setThreadid(threadid);
                users.add(user);
            }
            
        } catch (SQLException e) {
  
            e.printStackTrace();
        }
        return users;
    }
  
}

