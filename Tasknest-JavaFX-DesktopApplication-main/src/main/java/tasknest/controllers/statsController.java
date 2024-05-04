package tasknest.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;

import java.io.IOException;
import java.util.List;

import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import tasknest.models.complaint;
import tasknest.services.ComplaintService;

public class statsController {

    @FXML
    private PieChart complaintPieChart;

    @FXML
    private Button backbutton;
    private ComplaintService complaintService;

    @FXML
    public void initialize() {
        complaintService = new ComplaintService();
        loadPieChartData();
    }


    private void loadPieChartData() {
        // Get the count of complaints for each type
        List<complaint> complaints = complaintService.afficher();
        int GeneralinquiryCount = 0;
        int BillingissueCount = 0;
        int CustomersupportCount = 0;
        for (complaint complaint : complaints) {
            String type = complaint.getType();
            if (type.equals("General inquiry")) {
                GeneralinquiryCount++;
            } else if (type.equals("Billing issue")) {
                BillingissueCount++;
            } else if (type.equals("Customer support")) {
                CustomersupportCount++;
            }
        }

        // Prepare data for the pie chart
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("General inquiry", GeneralinquiryCount),
                new PieChart.Data("Billing issue", BillingissueCount),
                new PieChart.Data("Customer support", CustomersupportCount)
        );

        // Set the data to the pie chart
        complaintPieChart.setData(pieChartData);
    }

    @FXML
    void backnavfunc(MouseEvent event) {

        try {
            // Load the FXML file
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/admin.fxml"));
            Parent root = fxmlLoader.load();

            // Get the reference to the current scene
            Scene scene = backbutton.getScene();

            // Set the root of the scene to the content loaded from complaint.fxml
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
