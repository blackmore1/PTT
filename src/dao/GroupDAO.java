package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import bean.Group;

public class GroupDAO {
	  
    public GroupDAO() {
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
  
    public int getTotal() {
        int total = 0;
        try (Connection c = getConnection(); Statement s = c.createStatement();) {
  
            String sql = "select count(*) from groups";
  
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                total = rs.getInt(1);
            }
  
        } catch (SQLException e) {
  
            e.printStackTrace();
        }
        return total;
    }
  
    public void add(Group group) {
  
        String sql = "insert into groups values(?,?,?,?)";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
  
        	ps.setInt(1, group.getId());
            ps.setString(2, group.getDescription());
            ps.setInt(3, group.getUsernum());
            ps.setString(4, group.getUserlist().toString());
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                group.setId(id);
            }
        } catch (SQLException e) {
  
            e.printStackTrace();
        }
    }
  
    public void update(Group groups) {
  
        String sql = "update groups set usernum = ?,userlist = ? where id = ?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
  
        	ps.setInt(1, groups.getUsernum());
            ps.setString(2, groups.getUserlist().toString());
            ps.setInt(3, groups.getId());
  
            ps.execute();
  
        } catch (SQLException e) {
  
            e.printStackTrace();
        }
  
    }
  
    public void deleteall() {
    	
    	try (Connection c = getConnection(); Statement s = c.createStatement();) {
    		
    		String sql = "truncate table user";
    		
    		s.execute(sql);
    		
    	} catch (SQLException e) {
    		
    		e.printStackTrace();
    	}
    }
    
    public void delete(String name) {
  
        try (Connection c = getConnection(); Statement s = c.createStatement();) {
  
            String sql = "delete from group where name = \"" + name+"\"";
  
            s.execute(sql);
  
        } catch (SQLException e) {
  
            e.printStackTrace();
        }
    }
  
    public Group get(int id) {
    	Group group= null;
        try (Connection c = getConnection(); Statement s = c.createStatement();) {
  
            String sql = "select * from groups where id ="+id;
  
            ResultSet rs = s.executeQuery(sql);
  
            if (rs.next()) {
            	group = new Group();
                String description = rs.getString(2);
                int usernum = rs.getInt(3);
                Gson gson = new Gson();
                HashMap<Integer,String> userlist = gson.fromJson(rs.getString(4), new TypeToken<HashMap<Integer,String>>() {
                	
                }.getType());
                group.setId(id);
                group.setDescription(description);
                group.setUsernum(usernum);
                group.setUserlist(userlist);
            }
  
        } catch (SQLException e) {
  
            e.printStackTrace();
        }
        return group;
    }
  
    public List<Group> list() {
        return list(0, Short.MAX_VALUE);
    }
  
    public List<Group> list(int start, int count) {
        List<Group> groups = new ArrayList<Group>();
        int i=start;
        String sql = "select * from groups order by id asc limit ?,? ";
  
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql);Statement s=c.createStatement();) {
  
            ps.setInt(1, start);
            ps.setInt(2, count);
  
            ResultSet rs = ps.executeQuery();
            //sql="alter table user AUTO_INCREMENT = "+(new UserDAO().getTotal()+1);
            //s.execute(sql);
            while (rs.next()) {
            	Group group = new Group();
                int id = rs.getInt(1);
                String description = rs.getString(2);
                int usernum = rs.getInt(3);
                Gson gson = new Gson();
                HashMap<Integer,String> userlist = gson.fromJson(rs.getString(4), new TypeToken<HashMap<Integer,String>>() {
                	
                }.getType());
                group.setId(id);//若是上面有效则id应改为i
                group.setDescription(description);
                group.setUsernum(usernum);
                group.setUserlist(userlist);
                groups.add(group);
            }
            
        } catch (SQLException e) {
  
            e.printStackTrace();
        }
        return groups;
    }
  
}


