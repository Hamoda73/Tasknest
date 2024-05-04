package tasknest.controllers;

import javafx.fxml.Initializable;

import javafx.scene.control.Alert;
import tasknest.tests.MainFx;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ResourceBundle;

public class ForgotPasswordSecond implements Initializable {
    static int resetCode;

    @FXML
    public TextField inputResetCode;

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
    @FXML
    public void next(ActionEvent actionEvent) {
        if (inputResetCode.getText().isEmpty()) {
            showAlert("ERROR","Empty Code Field!");
        } else {
            if (inputResetCode.getText().equalsIgnoreCase(String.valueOf(resetCode))) {
                MainFx.getInstance().loadForgotPasswordThird();
            } else {
                showAlert("ERROR","Wrong Code!");
            }
        }
    }
}
