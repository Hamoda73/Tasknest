package tasknest.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tasknest.models.users;
import tasknest.services.AdminService;
import tasknest.tests.MainFx;
import tasknest.utils.DataSource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import tasknest.models.userInfo;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class Login {

    @FXML
    private Hyperlink DontHaveAccountLink;
    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginBtn;
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private static int userId; // Static variable to store the logged-in user ID

    @FXML
    public void initialize() {
        // Initialization code if needed
    }

    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();


        users loggedInUser = AdminService.getInstance().isValidUser(email);
        if (loggedInUser != null) {
            if(!loggedInUser.isBlocked()) {
                MainFx.getInstance().login(loggedInUser);
                System.out.println("Login successful");
                showAlert(Alert.AlertType.INFORMATION, "Success", "Login successful!");
            }
            else {
                showAlert(Alert.AlertType.ERROR, "Error", "Your account is blocked!");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid email or password.");
        }
    }

    private users isValidUser(String email) {
        // Initialize database resources
        Connection connection;
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
                user.setPhonenumber(resultSet.getInt("phonenumber"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user; // Return the populated user object
    }


    private void navigateToProfilePage() {
        try {
            // Load the profile page FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/profile.fxml"));
            Parent profilePageParent = loader.load();

            // Get the current stage
            Stage stage = (Stage) loginBtn.getScene().getWindow();

            // Set the profile page scene
            Scene scene = new Scene(profilePageParent);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load profile page.");
        }
    }
    @FXML
    void navigateToSignIn(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/signup.fxml"));
            Parent root = loader.load();

            Scene scene = DontHaveAccountLink.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void navigateToForgetPasswordFirst(MouseEvent event) {
        MainFx.getInstance().loadForgotPasswordFirst();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static int getUserId() {
        return userId;
    }
}
