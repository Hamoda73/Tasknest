package tasknest.controllers.Skill;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.Window;
import tasknest.controllers.CV.ShowCV;
import tasknest.controllers.CV.showAllCVS;
import tasknest.controllers.applications.seeMyApps;
import tasknest.models.CV;
import tasknest.models.Skill;
import tasknest.services.CvService;
import tasknest.services.SkillService;
import tasknest.tests.MainFx;

import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class EditSkill {

    @FXML
    private TextField SkillNameId;
    @FXML
    private TextField ValueId;
    private int skillid;
    private int idcv;

    private Skill editedSkill;
    SkillService skillService = new SkillService();

    public void setSkillId(int skillId, int idcvtoedit) {
        skillid = skillId;
        idcv = idcvtoedit;
        System.out.println("skillid11= " + skillid);
        System.out.println("CVid= " + idcv);
        try {
            // Retrieve the Skill details from the database based on skillId and idcv
            editedSkill = skillService.getSkillByIdAndCVId(skillid, idcv);
            if (editedSkill != null) {
                SkillNameId.setText(editedSkill.getName());
                ValueId.setText(String.valueOf(editedSkill.getSkill_value()));
            } else {
                System.err.println("Skill not found in the database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void editskill(ActionEvent actionEvent) {
        if (editedSkill == null) {
            System.err.println("editedSkill is not initialized.");
            return;
        }
        editedSkill.setName(SkillNameId.getText());
        editedSkill.setSkill_value(Integer.parseInt(ValueId.getText()));
        skillService.modifier(editedSkill);

        // Display a message dialog indicating successful modification
        showAlert(Alert.AlertType.INFORMATION, "Success", "Skill Modified", "The Skill has been modified successfully.");
        redirectToShowOWNSkilledit(actionEvent, editedSkill.getCv_id());
    }







    public void redirectFreelancers(MouseEvent event) {
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



    @FXML
    void AjouterSkill(ActionEvent event) {
        // Validate input fields
        if (!validateInput()) {
            return; // Return if validation fails
        }

        // Create a CV object with attributes
        Skill skill = new Skill(SkillNameId.getText(), Integer.parseInt(ValueId.getText()), idcv); // Assuming user ID is stored as an integer
        // Add the CV using the service
        int ret = skillService.ajouter(skill);

        // Show success message
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("CV Added");
        alert.setContentText("Skill added successfully");
        alert.showAndWait();

        // Clear input fields
        // clearFields();

        // Redirect to addSkillController
        redirectToaddSkillController(event, idcv);
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
            if (skillService.skillNameExistsForCV(idcv, name)) {
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
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }



    private void redirectToShowOWNSkilledit(ActionEvent event, int id) {
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

    private void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
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
