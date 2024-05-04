package tasknest.controllers.CV;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.Window;
import tasknest.controllers.Skill.AddSkill;
import tasknest.controllers.applications.seeMyApps;
import tasknest.services.CvService;
import tasknest.models.CV;
import tasknest.tests.MainFx;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class AddCv {

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
    private int user_id= MainFx.getSession().getId();
    CvService ps= new CvService();
    // Method to initialize the interface
    public void initialize() {
        try {
            // Fetch the username using the provided user ID
            String userName = ps.fetchUserNameById((user_id));

            // Set the fetched username in the username field
            userNameId.setText(userName);
        } catch (SQLException e) {
            // Show error message for SQL exception
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setContentText("Error occurred while fetching user name.");
            alert.showAndWait();
        }
    }



    public String[] getSelectedLanguages() {
        List<String> selectedLanguagesList = new ArrayList<>();

        if (arabicCheckbox.isSelected()) {
            selectedLanguagesList.add("Arabic");
        }
        if (frenchCheckbox.isSelected()) {
            selectedLanguagesList.add("French");
        }
        if (englishCheckbox.isSelected()) {
            selectedLanguagesList.add("English");
        }
        if (germanCheckbox.isSelected()) {
            selectedLanguagesList.add("German");
        }

        // Convert the list to an array
        return selectedLanguagesList.toArray(new String[0]);
    }



    @FXML
    void AjouterCV(ActionEvent event) {
        // Validate input fields
        if (validateInput()) {


        // Create a CV object with attributes
        CV cv = new CV(bioId.getText(), descriptionId.getText(), getSelectedLanguages(), locationId.getText(), certificationId.getText(), contactId.getText(), user_id); // Assuming user ID is stored as an integer

        // Add the CV using the service
        int idcv = ps.ajouter(cv);

        // Show success message
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("CV Added");
        alert.setContentText("CV added successfully");
        alert.showAndWait();

        // Clear input fields
        clearFields();

        // Redirect to addSkillController
        redirectToAddSkillController(event, idcv);
        }
    }



    private boolean validateInput() {
        // Check if any of the required fields are empty
        if (bioId.getText().isEmpty() || descriptionId.getText().isEmpty() || locationId.getText().isEmpty() ||
                certificationId.getText().isEmpty() || contactId.getText().isEmpty() || userNameId.getText().isEmpty()) {
            showAlert("Error", "All fields are required.");
            return false;
        }
        String bio = bioId.getText();
        if (bio.length() > 40) {
            showAlert("Error", "Your BIO cannot contain more than 40 characters.");
            return false;
        }
        if (!Pattern.matches("^[a-zA-Z\\s\\-\\.\\,]*$", bio)) {
            showAlert("Error", "Your bio must only contain letters, hyphens (-), periods (.), comma (,) and spaces.");
            return false;
        }
        // Validate location field
        String location = locationId.getText();
        if (location.length() > 20) {
            showAlert("Error", "Your Location cannot contain more than 20 characters.");
            return false;
        }
        if (!Pattern.matches("^[a-zA-Z\\s]*$", location)) {
            showAlert("Error", "Your Location must only contain letters and spaces.");
            return false;
        }
        // Validate description field
        String description = descriptionId.getText();
        if (description.length() > 1000) {
            showAlert("Error", "Your Description cannot contain more than 1000 characters.");
            return false;
        }
        if (getSelectedLanguages() == null || getSelectedLanguages().length == 0) {
            showAlert("Error", "Select at least one language.");
            return false;
        }
        // Validate certification field
        String certification = certificationId.getText();
        if (certification.length() > 40) {
            showAlert("Error", "Your Certification cannot contain more than 40 characters.");
            return false;
        }
        if (!Pattern.matches("^[a-zA-Z\\s\\-\\.\\,]*$", certification)) {
            showAlert("Error", "Your Certification must only contain letters, hyphens (-), periods (.), comma (,) and spaces.");
            return false;
        }
        // Validate contact field
        String contact = contactId.getText();
        if (!Pattern.matches("^https?://(www\\.)?facebook\\.com/[a-zA-Z0-9_.-]+/?(\\?locale=[a-zA-Z_]+)?$", contact)) {
            showAlert("Error", "Your Contact information must be in the format of a Facebook profile URL.");
            return false;
        }
        // Check if user ID exists in CV table
        try {
            //int userId = Integer.parseInt(user_idId.getText());
            if (ps.userExistsInCVTable(user_id)) {
                showAlert("Error", "User already has a CV.");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid user ID format.");
            return false;
        } catch (SQLException e) {
            showAlert("Error", "Error occurred while checking user ID.");
            return false;
        }

        // If all validations pass, return true
        return true;
    }
    private void clearFields() {
        bioId.clear();
        descriptionId.clear();
        locationId.clear();
        certificationId.clear();
        contactId.clear();
        userNameId.clear();
        arabicCheckbox.setSelected(false);
        frenchCheckbox.setSelected(false);
        englishCheckbox.setSelected(false);
        germanCheckbox.setSelected(false);

    }
    // Helper method to show alert dialog
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private void redirectToAddSkillController(ActionEvent event, int cvId) {
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


    @FXML
    private void redirectFreelancers() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CV/showAllCVs.fxml"));
            Parent root = loader.load();
            bioId.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
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

    public void userapps(MouseEvent event) {
        try {
            System.out.println("user_Idaz= "+user_id);
            // Load the FXML file of the DisplayAllOffers view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Applications/seeMyApps.fxml"));
            Parent root = loader.load();
            System.out.println("user_Idaz= "+user_id);
            // Create a new scene
            Scene scene = new Scene(root);
            seeMyApps seeMyAppsController = loader.getController();
            seeMyAppsController.setUserallApps(user_id);
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
