package tasknest.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;

import java.time.LocalDate;

public class Signup {
    @FXML
    private DatePicker myDatePicker;

    public void getDate(ActionEvent actionEvent) {
        LocalDate myDate = myDatePicker.getValue();
    }

}
