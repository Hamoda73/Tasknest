package tasknest.services;

import tasknest.models.users;
import tasknest.utils.DataSource;

import java.sql.*;

public class userrService {

    private Connection connection = DataSource.getInstance().getConnection();

    public users getUserById(int userId)  {
        users user = null;
        String query = "SELECT * FROM user WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, userId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                user = extractUserFromResultSet(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    private users extractUserFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String email = rs.getString("email");
        String fname = rs.getString("fname");
        String lname = rs.getString("lname");
        int phoneNumber = rs.getInt("phonenumber");
        Date birthdate = rs.getDate("Birthdate");
        String roles = rs.getString("roles");
        String password = rs.getString("password");
        String image= rs.getString("image");
        boolean blocked= rs.getBoolean("blocked");


        // Create and return a new users object
        return new users(id, email, fname, lname, phoneNumber,birthdate,roles,password,image,blocked);
    }
}
