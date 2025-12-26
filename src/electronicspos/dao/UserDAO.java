package electronicspos.dao;

import electronicspos.db.DBConnection;
import electronicspos.model.User;

import java.sql.*;

public class UserDAO {

    public User validateLogin(String username, String password) {

        User user = null;

        try {
            Connection con = DBConnection.getConnection();

            String sql =
                "SELECT USER_ID, USERNAME, PASSWORD, ROLE " +
                "FROM USERS WHERE USERNAME = ? AND PASSWORD = ?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                user = new User(
                        rs.getInt("USER_ID"),
                        rs.getString("USERNAME"),
                        rs.getString("PASSWORD"),
                        rs.getString("ROLE").trim()   // 🔥 IMPORTANT
                );
            }

            rs.close();
            ps.close();

        } catch (Exception e) {
            e.printStackTrace(); // NEVER remove this
        }

        return user;
    }
}