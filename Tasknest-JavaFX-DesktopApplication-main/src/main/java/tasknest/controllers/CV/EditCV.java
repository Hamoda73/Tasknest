package tasknest.controllers.CV;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.Window;
import tasknest.controllers.applications.seeMyApps;
import tasknest.models.CV;
import tasknest.services.CvService;
import tasknest.tests.MainFx;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.StringTokenizer;
public class EditCV {
    @FXML
    private TextField bioId;
    @FXML
    private TextArea descriptionId;
    @FXML
    private CheckBox arabicCheckbox;
    @FXML
    private CheckBox frenchCheckbox;
    @FXML
    private CheckBox englishCheckbox;
    @FXML
    private CheckBox germanCheckbox;
    @FXML
    private TextField locationId;
    @FXML
    private TextField certificationId;
    @FXML
    private TextField contactId;
    @FXML
    private TextField user_idId;
    @FXML
    private TextField userNameId;
    private CV editedcv;
    CvService cvService = new CvService();
    public void setCVService(CvService cvService) {
        this.cvService = cvService;
    }







    public void setEditedCV(CV editedcv) {
        // Set editedcv with the provided CV object
        this.editedcv = editedcv;

        // Populate fields with existing CV data when editedcv is set
        if (editedcv != null) {
            bioId.setText(editedcv.getBio());
            descriptionId.setText(editedcv.getDescription());
            locationId.setText(editedcv.getLocation());
            certificationId.setText(editedcv.getCertification());
            contactId.setText(editedcv.getContact());
            try {
                String userName = cvService.fetchUserNameById(editedcv.getUser_id());
                userNameId.setText(userName);
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle the error appropriately
            }

            String[] languages = editedcv.getLanguage();

// Iterate over each language and set the corresponding checkbox
            for (String language : languages) {
                // Remove leading and trailing whitespace and special characters
                language = language.trim().replaceAll("[\\[\\]\"]", "");
                System.out.println("Language: " + language);
                switch (language) {
                    case "Arabic":
                        arabicCheckbox.setSelected(true);
                        break;
                    case "French":
                        frenchCheckbox.setSelected(true);
                        break;
                    case "English":
                        englishCheckbox.setSelected(true);
                        break;
                    case "German":
                        germanCheckbox.setSelected(true);
                        break;
                    // Add cases for other languages if needed
                }
            }



        }
    }






    @FXML
    private void handleSaveChanges(ActionEvent event) {
        if (editedcv == null) {
            System.err.println("editedCV is not initialized.");
            return;
        }

        // Update the CV object with the new values from the UI fields
        editedcv.setBio(bioId.getText());
        editedcv.setDescription(descriptionId.getText());
        editedcv.setLocation(locationId.getText());
        editedcv.setCertification(certificationId.getText());
        editedcv.setContact(contactId.getText());

        // Clear existing languages before adding new ones
        editedcv.clearLanguages();

        // Add new languages based on checkbox state
        if (arabicCheckbox.isSelected()) {
            editedcv.addLanguage("Arabic");
        }
        if (frenchCheckbox.isSelected()) {
            editedcv.addLanguage("French");
        }
        if (englishCheckbox.isSelected()) {
            editedcv.addLanguage("English");
        }
        if (germanCheckbox.isSelected()) {
            editedcv.addLanguage("German");
        }
        // Add similar blocks for other languages if needed

        // Call the EditCV method in the CvService to update the CV in the database
        cvService.modifier(editedcv);

        // Display a message dialog indicating successful modification
        showAlert(Alert.AlertType.INFORMATION, "Success", "CV Modified", "The CV has been modified successfully.");
        redirectToShowOWNCVedit(event,editedcv.getId());
    }


    private void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

   // @FXML
  /*  private void navigateToDisplayAllOffers() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CV/showAllCVS.fxml"));
            Parent root = loader.load();
            bioId.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }*/


    @FXML
    private void redirectFreelancers(MouseEvent event) {
        try {
            Window window = ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CV/showAllCVs.fxml"));
            Parent root = loader.load();

            // Pass the authenticated user to the controller
            showAllCVS ShowAllCVsController = loader.getController();

            // Set the loggedInPatient in the UpdateUser controller

            // Show the UpdateUser view
            Stage stage = (Stage) window;

            // Set the new scene
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void redirectToShowOWNCVedit(ActionEvent event, int id) {
        try {
            Window window = ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CV/showCV.fxml"));
            Parent root = loader.load();

            // Pass the authenticated user to the controller
            ShowCV ShowCVController = loader.getController();

            // Set the loggedInPatient in the UpdateUser controller
            ShowCVController.setCvIdd(id);

            // Show the UpdateUser view
            Stage stage = (Stage) window;

            // Set the new scene
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void navigateUserApps(MouseEvent mouseEvent){

        navigateUserAppss(mouseEvent, MainFx.getSession().getId())  ;

    }

    public void navigateUserAppss(MouseEvent mouseEvent,int user_Id) {
        try {
            System.out.println("user_Idaz= "+user_Id);
            // Load the FXML file of the DisplayAllOffers view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Applications/seeMyApps.fxml"));
            Parent root = loader.load();
            System.out.println("user_Idaz= "+user_Id);
            // Create a new scene
            Scene scene = new Scene(root);
            seeMyApps seeMyAppsController = loader.getController();
            seeMyAppsController.setUserallApps(user_Id);
            // Get the current stage
            Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();

            // Set the scene on the stage
            stage.setScene(scene);

            // Show the stage
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void navigateAlloffers(MouseEvent event) {
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

    public void goprofile(MouseEvent event) {
        try {
            // Load the FXML file of the DisplayAllOffers view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/profile_front.fxml"));
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

    public void reclamation(MouseEvent event) {
        try {
            // Load the FXML file of the DisplayAllOffers view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/complaint.fxml"));
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
