package tasknest.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import tasknest.models.users;
import tasknest.services.AdminService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.regex.Pattern;

public class AddAdmin {
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField emailField;
    @FXML
    private DatePicker myDatePicker;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private TextField profileImageField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField confirmPasswordField;
    @FXML
    private CheckBox roleAdminBox;
    @FXML
    private CheckBox roleCompanyBox;
    @FXML
    private CheckBox roleFreelancerBox;
    @FXML
    private Button submitButton;
    @FXML
    private Hyperlink alreadyHaveAnAccount;
    private AdminService adminService;

    public void getDate(ActionEvent actionEvent) {
        LocalDate myDate = myDatePicker.getValue();
    }

    @FXML
    private void initialize() {
        adminService = new AdminService();
        String constantImagePath = "file:C:/Users/21694/Desktop/planet.png";
        ImageView ImageView = new ImageView(new Image(constantImagePath));
        ImageView.setFitWidth(200); // Set width to 200 (adjust as needed)
        ImageView.setFitHeight(200); // Set height to 200 (adjust as needed)
    }

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @FXML
    private void addAdmin() {
        // Get the text from all text fields
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String phoneNumber = phoneNumberField.getText();
        String profileImage = profileImageField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        LocalDate birthdate = myDatePicker.getValue();



        // Check if any of the required fields are empty
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || phoneNumber.isEmpty() || profileImage.isEmpty() || password.isEmpty() || birthdate.toString().isEmpty() || confirmPassword.isEmpty()) {
            // If any field is empty, display an error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all the fields");
            alert.showAndWait();
            return; // Exit the method
        }
        if (!validateInput()) {
            return; // Return if validation fails
        }

        int PhoneNum = Integer.parseInt(phoneNumber);
        Date dateOfBirth = Date.from(birthdate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        // Create a JSON array for roles
        Gson gson = new Gson();
        JsonArray rolesArray = new JsonArray();
        if (roleAdminBox.isSelected()) {
            rolesArray.add("ROLE_ADMIN");
        }
        if (roleCompanyBox.isSelected()) {
            rolesArray.add("ROLE_COMPANY");
        }
        if (roleFreelancerBox.isSelected()) {
            rolesArray.add("ROLE_FREELANCER");
        }

        // Convert JSON array to string
        String rolesJson = gson.toJson(rolesArray);

        String encodedPassword = passwordEncoder.encode(password);

        // Create the admin object
        users newAdmin = new users( email,  firstName,  lastName,  PhoneNum,  dateOfBirth,  rolesJson,  encodedPassword,  profileImage,  false);

        // Add the offer
        adminService.ajouter(newAdmin);
        clearFields();
        //navigateToLogin();

        // Show a confirmation message
        Alert confirmation = new Alert(Alert.AlertType.INFORMATION);
        confirmation.setTitle("Success");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Admin added successfully!");
        confirmation.showAndWait();
    }

    public boolean validateInput() {
        String Fname = firstNameField.getText();
        String Lname = lastNameField.getText();
        String Email = emailField.getText();
        String Password = passwordField.getText();
        String ConfirmPassword = confirmPasswordField.getText();
        LocalDate selectedDate = myDatePicker.getValue();
        if (Fname.length() < 5) {
            showAlert("Error", "Firstname too short");
            return false;
        }
        if (Lname.length() < 5) {
            showAlert("Error", "Lastname too short");
            return false;
        }
        if (!isEmailUnique(Email)) {
            showAlert("Error", "Email already exists");
            return false;
        }
        if (!Pattern.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$", Email)) {
            showAlert("Error", "Your Email should have a valid format");
            return false;
        }
        if (Password.length() < 6) {
            showAlert("Error", "Password too short");
            return false;
        }
        if(!Password.equals(ConfirmPassword)) {
            showAlert("Error", "Passwords should match");
            return false;
        }
        if (selectedDate == null || selectedDate.isAfter(LocalDate.now())) {
            showAlert("Error", "Please select a valid date");
            return false;
        }
        // If all validations pass, return true
        return true;
    }

    private boolean isEmailUnique(String email) {
        return adminService.isEmailUnique(email);
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private void navigateToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Parent root = loader.load();
            Login controller = loader.getController();

            // Get the current stage
            Stage stage = (Stage) alreadyHaveAnAccount.getScene().getWindow();

            // Set the new scene
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    private void clearFields() {
        firstNameField.clear();
        lastNameField.clear();
        myDatePicker = null;
        phoneNumberField.clear();
        profileImageField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
        emailField.clear();
    }



}
