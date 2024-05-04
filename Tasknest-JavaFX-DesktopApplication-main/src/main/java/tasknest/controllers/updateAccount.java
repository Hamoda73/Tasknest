package tasknest.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import tasknest.models.userInfo;
import tasknest.models.users;
import tasknest.services.AdminService;
import tasknest.tests.MainFx;
import tasknest.utils.DataSource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.regex.Pattern;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class updateAccount {

    @FXML
    private Button DeleteAccountBtn;

    @FXML
    private TextField EmailField;

    @FXML
    private TextField FirstNameField;

    @FXML
    private TextField LastNameField;

    @FXML
    private PasswordField PasswordField;

    @FXML
    private TextField PhoneNumberField;

    @FXML
    private Button ProfileBtn;

    @FXML
    private Button saveBtn;
    private AdminService adminService;
    private Connection connection = DataSource.getInstance().getConnection();
    @FXML
    public void initialize() {
        // Populate fields with current user data
        users currentUser = MainFx.getSession();
        FirstNameField.setText(currentUser.getFname());
        LastNameField.setText(currentUser.getLname());
        EmailField.setText(currentUser.getEmail());
        PhoneNumberField.setText(String.valueOf(currentUser.getPhonenumber()));

    }
    /*public boolean isEmailUnique(String email) {
        String query = "SELECT COUNT(*) FROM user WHERE email = ?";
        try {
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setString(1, email);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count == 1; // Return true if count is 1 (email is unique)
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Return false by default or in case of an exception
    }*/
    public boolean validateInput() {
        String Fname = FirstNameField.getText();
        String Lname = LastNameField.getText();
        String Email = EmailField.getText();
        String Password = PhoneNumberField.getText();
        if (Fname.length() < 3) {
            showAlert(Alert.AlertType.ERROR,"Error", "Firstname too short");
            return false;
        }
        if (Lname.length() < 5) {
            showAlert(Alert.AlertType.ERROR,"Error", "Lastname too short");
            return false;
        }
        if (!Pattern.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$", Email)) {
            showAlert(Alert.AlertType.ERROR,"Error", "Your Email should have a valid format");
            return false;
        }
        if (Password.length() < 6) {
            showAlert(Alert.AlertType.ERROR,"Error", "Password too short");
            return false;
        }
        // If all validations pass, return true
        return true;
    }
    @FXML
    public void handleSave(MouseEvent event) {
        // Get updated information from fields
        String firstName = FirstNameField.getText();
        String lastName = LastNameField.getText();
        String email = EmailField.getText();
        String phoneNumber = PhoneNumberField.getText();
        String password = PasswordField.getText();

        // Update user information in the database
        try (Connection connection = DataSource.getInstance().getConnection()) {
            if(validateInput()) {
                String query = "UPDATE user SET fname = ?, lname = ?, email = ?, phonenumber = ?, password = ? WHERE id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, firstName);
                preparedStatement.setString(2, lastName);
                preparedStatement.setString(3, email);
                preparedStatement.setString(4, phoneNumber);
                preparedStatement.setString(5, password); // Note: Storing plaintext password is insecure. Consider hashing.
                preparedStatement.setInt(6, MainFx.getSession().getId());

                int affectedRows = preparedStatement.executeUpdate();
                users user = new users(
                        email,
                        firstName,
                        lastName,
                        Integer.parseInt(phoneNumber),
                        MainFx.getSession().getBirthdate(),
                        MainFx.getSession().getRoles(),
                        password,
                        MainFx.getSession().getImage(),
                        false
                );

                if (affectedRows > 0) {
                    MainFx.setSession(user);
                    // Update successful
                    showAlert(Alert.AlertType.INFORMATION, "Success", "User information updated successfully.");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to update user information.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while updating user information.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void navigateToProfile(MouseEvent event) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            Connection connection = DataSource.getInstance().getConnection();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/profile.fxml"));
            Parent root = loader.load();
            Scene scene = ProfileBtn.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
