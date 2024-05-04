package tasknest.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tasknest.models.complaint;
import tasknest.models.respond;
import tasknest.services.ComplaintService;
import tasknest.services.RespondService;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.control.Alert.AlertType;
import tasknest.tests.MainFx;
import tasknest.models.users;

import javax.mail.MessagingException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class adminComplaintController {

    @FXML
    private TableView<complaint> admindatatable;

    @FXML
    private TextField adminresponse;

    @FXML
    private Button respondbutton;

    @FXML
    private Button updateadminbutton;

    @FXML
    private Button deleteadminbutton;

    @FXML
    private TextFlow adminmessage;

    @FXML
    private TextFlow admintype;

    @FXML
    private TableColumn<complaint, String> typecolumn;

    @FXML
    private TableColumn<complaint, String> messagecolumn;

    @FXML
    private TableColumn<complaint, String> responsecolumn;

    @FXML
    private Button statbutton;

    @FXML
    private Hyperlink geniq;

    @FXML
    private Hyperlink billiss;

    @FXML
    private Hyperlink cs;

    private complaint selectedComplaint;

    private ComplaintService database;

    RespondService respondService = new RespondService();
    private respond selectedRespond;

    @FXML
    public void initialize() {
        database = new ComplaintService();

        messagecolumn.setCellValueFactory(new PropertyValueFactory<>("message"));
        typecolumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        responsecolumn.setCellValueFactory(new PropertyValueFactory<>("responseMessage"));
        // Load data into table
        loadData();

        // Set TableView selection listener
        admindatatable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<complaint>() {
            @Override
            public void changed(ObservableValue<? extends complaint> observable, complaint oldValue, complaint newValue) {
                if (newValue != null) {
                    selectedComplaint = newValue;
                    admintype.getChildren().clear(); // Clear existing content
                    admintype.getChildren().add(new Text(newValue.getType())); // Add new Text object with the updated type
                    adminmessage.getChildren().clear(); // Clear existing content
                    adminmessage.getChildren().add(new Text(newValue.getMessage())); // Add new Text object with the updated message
                    adminresponse.setText(newValue.getResponseMessage());
                    selectedRespond = newValue.getResponse();
                }
            }
        });


    }

    private void loadData() {
        List<complaint> complaints = database.afficher();
        admindatatable.getItems().addAll(complaints);
    }


    @FXML
    void billissfilter(MouseEvent event) {
        filterComplaints("Billing issue");
    }

    @FXML
    void csfilter(MouseEvent event) {
        filterComplaints("Customer support");
    }

    @FXML
    void geniqfilter(MouseEvent event) {
        filterComplaints("General inquiry");
    }

    private void filterComplaints(String type) {
        admindatatable.getItems().clear(); // Clear existing data in the table
        List<complaint> filteredComplaints = database.getComplaintsByType(type); // Implement this method in ComplaintService
        admindatatable.getItems().addAll(filteredComplaints); // Add filtered complaints to the table
    }




    @FXML
    void statnavfunc(MouseEvent event) {
        try {
            // Load the FXML file
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/stats.fxml"));
            Parent root = fxmlLoader.load();

            // Get the reference to the current scene
            Scene scene = statbutton.getScene();

            // Set the root of the scene to the content loaded from complaint.fxml
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void deleterespondfunc(MouseEvent event) {

        if (selectedComplaint != null) {
            database.supprimer(selectedComplaint); // Implement delete method in ComplaintService
            // Refresh TableView
            refreshTable();
        }

    }



    @FXML
    void respondfunc(MouseEvent event) throws MessagingException {
        String text = adminresponse.getText();
        int userId = MainFx.getSession().getId();

        // Check if response is empty or too short (less than 5 characters, for example)
        if (text.isEmpty() || text.length() < 5) {
            // Create and configure an alert
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Invalid Response");
            alert.setHeaderText(null);
            alert.setContentText("Response is empty or too short. Please provide a valid response.");

            // Show the alert dialog
            alert.showAndWait();

            return; // Exit the method if response is not valid
        }

        if (selectedComplaint != null) {
            int complaintId = selectedComplaint.getId(); // Assuming getId() returns the complaint ID
            respond com = new respond(complaintId, text);
            respondService.ajouter(com);
            String receiverEmail = respondService.getEmailOfComplaintSender(complaintId);
            respondService.sendEmail(receiverEmail,"About your complaint",text);
            System.out.println("Response added successfully!");
            // You may want to update the UI or provide feedback to the user here
        } else {
            System.out.println("No complaint selected.");
            // You may want to inform the user that they need to select a complaint before responding
        }
        refreshTable();
    }


    private void refreshTable() {
        // Clear TableView and reload data
        admindatatable.getItems().clear();
        loadData();
    }


    @FXML
    void updaterespondfunc(MouseEvent event) {
        // Get the updated message from the adminresponse TextField
        String updatedMessage = adminresponse.getText();

        // Check if the selected complaint is not null
        if (selectedComplaint != null) {
            // Update the response message of the selected complaint
            selectedComplaint.setResponseMessage(updatedMessage);

            // Update the response object associated with the complaint
            if (selectedComplaint.getResponse() != null) {
                selectedComplaint.getResponse().setMessage(updatedMessage);
            } else {
                // If there is no existing response object, create a new one
                selectedComplaint.setResponse(new respond(selectedComplaint.getId(), updatedMessage));
            }

            // Call the modifier method in the RespondService to update the response in the database
            respondService.modifier(selectedComplaint.getResponse());

            // Optionally, update the UI to reflect the changes
            adminmessage.getChildren().clear(); // Clear previous message
            Text updatedText = new Text(selectedComplaint.getResponseMessage());
            adminmessage.getChildren().add(updatedText); // Add updated message to adminmessage TextFlow

            // Clear the adminresponse TextField or perform any other necessary UI updates
            adminresponse.clear();
            admindatatable.refresh();

            System.out.println("Response updated successfully!");
        } else {
            // Handle the case where no complaint is selected
            System.out.println("No complaint selected.");
        }
    }


    public void listusers(MouseEvent event) {
        try {
            // Load the FXML file of the DisplayAllOffers view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/diaplayUsers.fxml"));
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

    public void alloffers(MouseEvent event) {
        try {
            // Load the FXML file of the DisplayAllOffers view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/offer/displayofferback.fxml"));
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

    public void allcvs(MouseEvent event) {
        try {
            // Load the FXML file of the DisplayAllOffers view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CV/showAllCVsBACK.fxml"));
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
