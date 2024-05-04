package tasknest.controllers;

import javafx.scene.control.Alert;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import tasknest.tests.MainFx;
import tasknest.services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import tasknest.utils.DataSource;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ForgotPasswordThird implements Initializable{
    @FXML
    public TextField inputPassword;
    PreparedStatement preparedStatement;
    Connection connection = DataSource.getInstance().getConnection();
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    public void backToLogin(ActionEvent actionEvent) {
        MainFx.getInstance().loadLogin();
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccessAlert(String text) {
        Alert confirmation = new Alert(Alert.AlertType.INFORMATION);
        confirmation.setTitle("Success");
        confirmation.setHeaderText(null);
        confirmation.setContentText(text);
        confirmation.showAndWait();
    }
    public boolean updatePassword(String email, String newPassword) {
        String encodedPassword = passwordEncoder.encode(newPassword);

        String request = "UPDATE `user` SET `password` = ? WHERE `email` LIKE '" + email + "'";
        try {
            preparedStatement = connection.prepareStatement(request);

            preparedStatement.setString(1, encodedPassword);

            preparedStatement.executeUpdate();
            System.out.println("User updated");
            return true;
        } catch (SQLException exception) {
            System.out.println("Error (update) user : " + exception.getMessage());
        }
        return false;
    }

    @FXML
    public void next(ActionEvent actionEvent) {
        if (inputPassword.getText().isEmpty()) {
            showAlert("ERROR","Empty Password Field!");
            return;
        }
        if (updatePassword(ForgotPasswordFirst.email, inputPassword.getText())) {
            showSuccessAlert("Password updated successfully!");
            MainFx.getInstance().loadLogin();
        } else {
            showAlert("ERROR","ERROR!");
        }
    }
}
