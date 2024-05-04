package tasknest.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import tasknest.models.complaint;
import tasknest.services.ComplaintService;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import tasknest.tests.MainFx;

import java.io.IOException;

public class complaintController {

    @FXML
    private ComboBox<String> complainttype;

    @FXML
    private TextField complainttext;

    @FXML
    private Button sendcomplaint;

    @FXML
    private Hyperlink consultnav;

    @FXML
    private Label adminbutton;

    ComplaintService complaintService = new ComplaintService();

    @FXML
    void navigadmin(MouseEvent event) {
        try {
            // Load the FXML file
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/admin.fxml"));
            Parent root = fxmlLoader.load();

            // Get the reference to the current scene
            Scene scene = adminbutton.getScene();

            // Set the root of the scene to the content loaded from complaint.fxml
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    @FXML
    void consultnavfunc(MouseEvent event) {
        try {
            // Load the FXML file
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/consult.fxml"));
            Parent root = fxmlLoader.load();

            // Get the reference to the current scene
            Scene scene = consultnav.getScene();

            // Set the root of the scene to the content loaded from complaint.fxml
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @FXML
    void sendcomplaintdata(MouseEvent event) {
        // Validate the selected item in the complaint type ComboBox

        String selectedItem = complainttype.getValue();
        int userId = MainFx.getSession().getId();
        if (selectedItem == null || selectedItem.isEmpty()) {
            showAlert(AlertType.ERROR, "Error", "Complaint Type", "Please select a complaint type.");
            return;
        }

        // Validate the complaint text TextField
        String text = complainttext.getText().trim(); // Trim whitespace
        if (text.isEmpty()) {
            showAlert(AlertType.ERROR, "Error", "Complaint Message", "Please enter a complaint message.");
            return;
        }

        // If all input is valid, create a new complaint object and add it using the service

        complaint com = new complaint(selectedItem, text);
        com.setUser_id(userId);
        System.out.println(com);
        complaintService.ajouter(com);

        // Show success message
        showAlert(AlertType.INFORMATION, "Success", "Complaint Sent", "Your complaint has been successfully submitted.");
    }

    private void showAlert(AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }


    public void showalloffers(MouseEvent event) {
        try {
            // Load the FXML file of the DisplayAllOffers view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/offer/DisplayAllOffers.fxml"));
            Parent root = loader.load();

            // Create a new scene
            Scene scene = new Scene(root);

            // Get the current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the scene on the stage
            stage.setScene(scene);

            // Show the stage
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showallcvs(MouseEvent event) {
        try {
            // Load the FXML file of the DisplayAllOffers view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CV/showAllCVs.fxml"));
            Parent root = loader.load();

            // Create a new scene
            Scene scene = new Scene(root);

            // Get the current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the scene on the stage
            stage.setScene(scene);

            // Show the stage
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showalloffersentreprise(MouseEvent event) {
        try {
            // Load the FXML file of the DisplayAllOffers view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/offer/DisplayAllOffersEntreprise.fxml"));
            Parent root = loader.load();

            // Create a new scene
            Scene scene = new Scene(root);

            // Get the current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the scene on the stage
            stage.setScene(scene);

            // Show the stage
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showallcvsentreprise(MouseEvent event) {
        try {
            // Load the FXML file of the DisplayAllOffers view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CV/showAllCVSEntreprise.fxml"));
            Parent root = loader.load();

            // Create a new scene
            Scene scene = new Scene(root);

            // Get the current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the scene on the stage
            stage.setScene(scene);

            // Show the stage
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
