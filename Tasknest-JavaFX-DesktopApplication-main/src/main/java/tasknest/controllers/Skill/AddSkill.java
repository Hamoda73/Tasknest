package tasknest.controllers.Skill;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.Window;
import tasknest.controllers.CV.ShowCV;
import tasknest.controllers.CV.showAllCVS;
import tasknest.controllers.applications.seeMyApps;
import tasknest.models.Skill;
import tasknest.services.SkillService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import tasknest.tests.MainFx;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.regex.Pattern;

public class AddSkill {
    private int cvIdADD;
    @FXML
    private TextField SkillNameId;
    @FXML
    private TextField ValueId;


    public void setCvIdforaddskill(int cvId,ActionEvent event) {
        cvIdADD = cvId;
        //AjouterSkill(event);

    }

   SkillService ss = new SkillService();

    @FXML
    void AjouterSkill(ActionEvent event) {
        // Validate input fields
        if (!validateInput()) {
            return; // Return if validation fails
        }

        // Create a CV object with attributes
        Skill skill = new Skill(SkillNameId.getText(), Integer.parseInt(ValueId.getText()), cvIdADD); // Assuming user ID is stored as an integer
        // Add the CV using the service
        int ret = ss.ajouter(skill);

        // Show success message
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("CV Added");
        alert.setContentText("Skill added successfully");
        alert.showAndWait();

        // Clear input fields
       // clearFields();

        // Redirect to addSkillController
        redirectToaddSkillController(event, cvIdADD);
    }


    private boolean validateInput() {
        // Check if any of the required fields are empty
        if (SkillNameId.getText().isEmpty() || ValueId.getText().isEmpty()) {
            showAlert("Error", "All fields are required.");
            return false;
        }
        // Validate name field
        String name = SkillNameId.getText();
        if (name.length() > 15) {
            showAlert("Error", "Skill name cannot be longer than 15 characters.");
            return false;
        }
        if (!Pattern.matches("^[a-zA-Z0-9\\s\\+]*$", name)) {
            showAlert("Error", "Your Skill name must only contain letters, numbers and spaces.");
            return false;
        }
        if (Integer.parseInt(ValueId.getText()) < 0 || Integer.parseInt(ValueId.getText()) > 100) {
            showAlert("Error", "Skill value must be between 0 and 100.");
            return false;
        }
        // Check if the skill name already exists for the same CV
        try {
            if (ss.skillNameExistsForCV(cvIdADD, name)) {
                showAlert("Error", "CV already contains a skill with the same name.");
                return false;
            }
        } catch (SQLException e) {
            showAlert("Error", "Error occurred while checking skill name.");
            return false;
        }

        // If all validations pass, return true
        return true;
    }
    private void clearFields() {
        SkillNameId.clear();
        ValueId.clear();

    }
    // Helper method to show alert dialog
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }




    private void redirectToaddSkillController(ActionEvent event, int cvId) {
        try {
            Window window = ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Skill/AddSkill.fxml"));
            Parent root = loader.load();

            // Pass the authenticated user to the controller
            AddSkill AddSkillController = loader.getController();

            // Set the loggedInPatient in the UpdateUser controller
            AddSkillController.setCvIdforaddskill(cvId,event);

            // Show the UpdateUser view
            Stage stage = (Stage) window;

            // Set the new scene
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //navigate to show user cv
    @FXML
    private void RedirectToshowcv(ActionEvent event) {
        redirectToshowcvController(event,cvIdADD);
    }


    private void redirectToshowcvController(ActionEvent event, int cvId) {
        try {
            Window window = ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CV/showCV.fxml"));
            Parent root = loader.load();

            // Pass the authenticated user to the controller
            ShowCV ShowCVController = loader.getController();

            // Set the loggedInPatient in the UpdateUser controller
            ShowCVController.setCvIdd(cvId);

            // Show the UpdateUser view
            Stage stage = (Stage) window;

            // Set the new scene
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
  /*  @FXML
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
    }*/
  @FXML
  private void redirectFreelancers() {
      try {
          FXMLLoader loader = new FXMLLoader(getClass().getResource("/CV/showAllCVs.fxml"));
          Parent root = loader.load();
          SkillNameId.getScene().setRoot(root);
      } catch (IOException e) {
          throw new RuntimeException(e);
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
