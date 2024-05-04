package tasknest.controllers.offer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import tasknest.models.offers;
import tasknest.services.OfferService;

import java.io.IOException;

public class editOffer {

    @FXML
    private TextField entrepriseNameField;

    @FXML
    private TextField domainField;

    @FXML
    private TextField descriptionField;

    @FXML
    private TextField localisationField;

    @FXML
    private TextField postField;

    @FXML
    private TextField periodField;

    @FXML
    private TextField salaryField;

    @FXML
    private Button submitButton;

    private OfferService offerService;

    private offers editedOffer;

    public void setOfferService(OfferService offerService) {
        this.offerService = offerService;
    }



        private offers editOffer;
        private OfferService OfferService;





    public void setEditedOffer(offers editedOffer) {
        this.editedOffer = editedOffer;
        // data
        if (editedOffer != null) {
            entrepriseNameField.setText(editedOffer.getEntreprise_name());
            domainField.setText(editedOffer.getDomain());
            descriptionField.setText(editedOffer.getDescription());
            localisationField.setText(editedOffer.getLocalisation());
            postField.setText(editedOffer.getPost());
            periodField.setText(editedOffer.getPeriod());
            salaryField.setText(String.valueOf(editedOffer.getSalary())); // Convert salary to string
        }
    }

    @FXML
    private void handleSaveChanges(ActionEvent event) {
        if (offerService == null || editedOffer == null) {
            System.err.println("OfferService or editedOffer is not initialized.");
            return;
        }


        editedOffer.setEntreprise_name(entrepriseNameField.getText());
        editedOffer.setDomain(domainField.getText());
        editedOffer.setDescription(descriptionField.getText());
        editedOffer.setLocalisation(localisationField.getText());
        editedOffer.setPost(postField.getText());
        editedOffer.setPeriod(periodField.getText());
        editedOffer.setSalary(Float.parseFloat(salaryField.getText())); // Convert salary to float


        offerService.modifier(editedOffer);


        showAlert(Alert.AlertType.INFORMATION, "Success", "Offer Modified", "The offer has been modified successfully.");


        System.out.println("Offer modified successfully.");
    }

    private void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    @FXML
    private void navigateToDisplayAllOffers() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/offer/DisplayAllOffersEntreprise.fxml"));
            Parent root = loader.load();
            domainField.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void navigateToDisplayuseroff() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/offer/Displayoffuser.fxml"));
            Parent root = loader.load();
            domainField.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void navigateToAddOffer() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/offer/addOffer.fxml"));
            Parent root = loader.load();
            domainField.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    private void redirectFreelancers() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CV/showAllCVSEntreprise.fxml"));
            Parent root = loader.load();
            domainField.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void goprofile(MouseEvent event) {
        try {
            // Load the FXML file of the DisplayAllOffers view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/profile.fxml"));
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
