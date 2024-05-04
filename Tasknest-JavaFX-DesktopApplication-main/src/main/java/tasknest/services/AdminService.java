package tasknest.services;
import javafx.scene.control.Alert;
import tasknest.models.complaint;
import tasknest.models.users;

import tasknest.utils.DataSource;

import java.sql.*;
import java.util.Date;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
public final class AdminService extends UserService{

    private Connection connection = DataSource.getInstance().getConnection();
    private static AdminService instance;
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public int ajouter(users users) {
        String req = "INSERT INTO `user`( email,  fname,  lname,  phonenumber,  birthdate,  roles,  password,  image,  blocked) VALUES (?, ?, ?, ?, ?, ? , ?, ?, false);";

        Gson gson = new Gson();
        String rolesJson = gson.toJson(users.getRoles());

        String encodedPassword = passwordEncoder.encode(users.getPassword());
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, users.getEmail());
            pst.setString(2, users.getFname());
            pst.setString(3, users.getLname());
            pst.setInt(4, users.getPhonenumber());
            pst.setDate(5,new java.sql.Date(users.getBirthdate().getTime()));
            pst.setString(6, rolesJson);
            pst.setString(7, encodedPassword);
            pst.setString(8, users.getImage());
            pst.executeUpdate();
            System.out.println("added successfully !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    @Override
    public void supprimer(users user) {
        PreparedStatement preparedStatement = null;
        try {
            // Establish connection
            Connection connection = DataSource.getInstance().getConnection();
            if (connection == null || connection.isClosed()) {
                // Reconnect to the database
                DataSource.getInstance().getConnection();
            }

            String query = "DELETE FROM user WHERE id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, user.getId());

            int rowsDeleted = preparedStatement.executeUpdate();

            // Check if the deletion was successful
            if (rowsDeleted > 0) {
                showAlert("Account Deleted", "Your account has been successfully deleted.");
            } else {
                showAlert("Error", "Failed to delete account. Please try again.");
            }

        } catch (SQLException e) {
            showAlert("Error", "An error occurred while deleting the account. Please try again.");
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void modifier(users users) {
        try {
            // SQL update statement
            String sql = "UPDATE users SET firstName=?, lastName=?, email=?, getPhonenumber=?, password=? WHERE id=?";

            // Create prepared statement with SQL query
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            // Set parameters for the prepared statement
            preparedStatement.setString(1, users.getFname());
            preparedStatement.setString(2, users.getLname());
            preparedStatement.setString(3, users.getEmail());
            preparedStatement.setInt(4, users.getPhonenumber());
            preparedStatement.setString(5, users.getPassword());
            preparedStatement.setInt(6, users.getId());

            // Execute the update statement
            preparedStatement.executeUpdate();

            // Close the prepared statement


        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception
        }
    }

    @Override
    public List<users> afficher() {
        return null;
    }

    @Override
    public void handleBlockUser(users user) {

    }

   /* @Override
    public List<complaint> afficher1() {
        return null;
    }*/

    public void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }





    public boolean isEmailUnique(String email) {
        String query = "SELECT COUNT(*) FROM user WHERE email = ?";
        try {
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setString(1, email);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count == 0; // Return true if count is 0 (email is unique)
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Return false by default or in case of an exception
    }

    public static AdminService getInstance() {
        if (instance == null) {
            instance = new AdminService();
        }
        return instance;
    }
    public users isValidUser(String email) {
        // Initialize database resources
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        users user = null;

        try {
            // Get database connection from DataSource
            connection = DataSource.getInstance().getConnection();

            // SQL query to check user credentials
            String query = "SELECT * FROM user WHERE email = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);

            resultSet = preparedStatement.executeQuery();

            // Check if the user exists
            if (resultSet.next()) {
                // Populate the user object with the fetched data
                user = new users();
                user.setId(resultSet.getInt("id"));
                user.setFname(resultSet.getString("fname"));
                user.setLname(resultSet.getString("lname"));
                user.setEmail(resultSet.getString("email"));
                user.setBirthdate(resultSet.getDate("birthdate"));
                user.setRoles(resultSet.getString("roles"));
                user.setImage(resultSet.getString("image"));
                user.setBlocked(resultSet.getBoolean("blocked"));
                user.setPassword(resultSet.getString("password"));
                user.setPhonenumber(resultSet.getInt("phonenumber"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user; // Return the populated user object
    }


}
