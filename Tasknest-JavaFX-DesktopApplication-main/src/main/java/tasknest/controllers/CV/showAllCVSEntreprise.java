package tasknest.controllers.CV;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.Window;
import tasknest.controllers.applications.seeMyApps;
import tasknest.models.CV;
import tasknest.models.Skill;
import tasknest.services.CvService;
import tasknest.services.SkillService;
import tasknest.tests.MainFx;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class showAllCVSEntreprise {

    @FXML
    ScrollPane allCVSscroll;
    @FXML
    TextField search;
    @FXML
    private VBox skillbx;
    private tasknest.services.CvService CvService;
    private SkillService skService;
    private ObservableList<CV> allCVsList = FXCollections.observableArrayList();
    private List<Skill> selectedSkills = new ArrayList<>();
    private int user_id= MainFx.getSession().getId();

    public void initialize() {
        CvService = new CvService();
        skService = new SkillService();
        Allcvs();
        allCVsList.addAll(CvService.afficher());

        search.textProperty().addListener((observable, oldValue, newValue) -> {
            filterCVs(newValue);});
        populateSkillsCheckboxes();
    }

    // Update the event handler for checkbox actions
    private void populateSkillsCheckboxes() {
        // Retrieve skills from the database
        List<Skill> skills = skService.afficherskills();

        // Create and add checkboxes for each skill
        for (Skill skill : skills) {
            CheckBox checkBox = new CheckBox(skill.getName());
            // Set margin between checkboxes
            VBox.setMargin(checkBox, new Insets(0, 0, 5, 20)); // Adjust values as needed
            checkBox.setOnAction(event -> {
                if (checkBox.isSelected()) {
                    selectedSkills.add(skill);
                } else {
                    selectedSkills.remove(skill);
                }
                // Call filterCVs whenever a checkbox state changes
                filterCVs(search.getText());
            });

            // Set radius for the checkbox
            checkBox.setStyle("-fx-border-radius: 20px; -fx-background-radius: 20px;");

            // Set color and size for the checkbox font
            checkBox.setStyle("-fx-font-size: 18px; -fx-text-fill: #31768f;"); // Adjust values as needed

            skillbx.getChildren().add(checkBox);
        }
    }

    // Update the filterCVs method to consider both search query and selected skills
    private void filterCVs(String query) {
        Predicate<CV> containsQuery = cv ->
                (query == null || cv.getBio().toLowerCase().contains(query.toLowerCase()) ||
                        cv.getLocation().toLowerCase().contains(query.toLowerCase()));

        Predicate<CV> containsSelectedSkills = cv ->
                selectedSkills.isEmpty() || selectedSkills.stream().allMatch(skill ->
                        cv.getSkills(cv.getId()).stream().anyMatch(cvSkill ->
                                cvSkill.getName().equalsIgnoreCase(skill.getName())));

        Predicate<CV> combinedPredicate = containsQuery.and(containsSelectedSkills);

        FilteredList<CV> filteredList = allCVsList.filtered(combinedPredicate);

        VBox offersContainer = new VBox(10);

        for (CV cv : filteredList) {
            AnchorPane card = createCVCard(cv);
            offersContainer.getChildren().add(card);
        }

        allCVSscroll.setContent(offersContainer);
    }







    private void Allcvs() {
        List<CV> allcvs = CvService.afficher();

        VBox CVContainer = new VBox(10);


        if (allcvs.isEmpty()) {
            // Display a message when there are no applications available
            AnchorPane noAppsPane = new AnchorPane();
            Label noAppsLabel = new Label("No CVs Available");
            noAppsLabel.getStyleClass().add("no-apps-label");
            AnchorPane.setTopAnchor(noAppsLabel, 200.0);
            AnchorPane.setBottomAnchor(noAppsLabel, 200.0);
            AnchorPane.setLeftAnchor(noAppsLabel, 130.0);
            AnchorPane.setRightAnchor(noAppsLabel, 100.0);
            noAppsPane.getChildren().add(noAppsLabel);
            CVContainer.getChildren().add(noAppsPane);
        } else {
            // Populate applications if available

            for (CV cv : allcvs) {
                AnchorPane card = createCVCard(cv);
                CVContainer.getChildren().add(card);
            }
        }


        allCVSscroll.setContent(CVContainer);
    }

    private AnchorPane createCVCard(CV cv) {

        AnchorPane card = new AnchorPane();
        card.getStyleClass().add("cv-card");

        card.setOnMouseClicked(event -> {
            // Redirect to the "showcv" page with the CV's ID
            redirectToShowCV(event,cv.getId());
        });

        // Get user image path using CvService
        String imagePath = CvService.getUserImageForCV(cv.getId());
        System.out.println("image="+imagePath);
        // Load the image dynamically into the ImageView
        try {
            if (imagePath != null) {
                // Use getClass().getResource() to obtain the URL
                URL imageUrl = getClass().getResource("/images/" + imagePath);
                if (imageUrl != null) {
                    // Create the Image object using the URL
                    Image image = new Image(imageUrl.toExternalForm());

                    // Create and configure the ImageView
                    ImageView imageView = new ImageView(image);

                    // Create a Circle object as a mask for the ImageView
                    Circle clip = new Circle(75); // Set the radius according to your image size
                    clip.setCenterX(75); // Set the center X coordinate of the circle
                    clip.setCenterY(75); // Set the center Y coordinate of the circle

                    // Apply the circular clip to the ImageView
                    imageView.setClip(clip);

                    // Set the fit width and height of the ImageView to match the circle diameter
                    imageView.setFitWidth(150);
                    imageView.setFitHeight(150);

                    // Optionally, set the preserve ratio property to true to maintain the aspect ratio of the image
                    imageView.setPreserveRatio(true);

                    // Optionally, set the layout coordinates for the ImageView
                    imageView.setLayoutX(20); // Adjust the X coordinate as needed
                    imageView.setLayoutY(10); // Adjust the Y coordinate as needed

                    // Add the ImageView to the card
                    card.getChildren().add(imageView);
                }  else {
                    // Image resource not found
                    System.err.println("Image resource not found: " + imagePath);
                }
            }
        } catch (IllegalArgumentException e) {
            // Handle image loading errors (e.g., image file not found)
            System.err.println("Error loading image: " + e.getMessage());
            // Optionally, provide a fallback image
            Image fallbackImage = new Image("../images/planet.png");
            ImageView fallbackImageView = new ImageView(fallbackImage);
            fallbackImageView.setFitWidth(50);
            fallbackImageView.setFitHeight(50);
            card.getChildren().add(fallbackImageView);
        }

        // Create and configure the labels for CV information
        Label BIOPrefixLabel = new Label("BIO: ");
        BIOPrefixLabel.setLayoutX(250);
        BIOPrefixLabel.setLayoutY(10);
        BIOPrefixLabel.setStyle(" -fx-text-fill: #892193FF;");

        Label BIOLabel = new Label(cv.getBio());
        BIOLabel.setLayoutX(300);
        BIOLabel.setLayoutY(10);
        double yOffset = 70;//160


// Add the labels to the card (outside the loop)
        card.getChildren().addAll(BIOPrefixLabel, BIOLabel);

        Label SkillsPrefixLabel = new Label("Skills: ");
        SkillsPrefixLabel.setLayoutX(250);
        SkillsPrefixLabel.setLayoutY(yOffset);
        SkillsPrefixLabel.setStyle("-fx-text-fill: #892193FF;");
        yOffset += 40;
// Create a FlowPane to hold the skill labels
        FlowPane skillPane = new FlowPane();
        skillPane.setLayoutX(250);
        skillPane.setLayoutY(yOffset);
        skillPane.setHgap(20); // Set horizontal gap between skill labels
        skillPane.setVgap(20); // Set vertical gap between skill labels
        skillPane.setPrefWrapLength(400); // Set preferred wrap length
        yOffset += 40;
// Add the skill pane to the card
        card.getChildren().addAll(SkillsPrefixLabel, skillPane);

// Iterate through skills and add labels to the skill pane
        List<Skill> skills = CvService.getSkillsForCV(cv.getId());

        if (skills.isEmpty()) {
            // Add a message indicating no skills are available
            Label noSkillsLabel = new Label("No skills available");
            skillPane.getChildren().add(noSkillsLabel);
        } else {
            int skillCount = 0;
            for (Skill skill : skills) {
                Label skillLabel = new Label(skill.getName());
                skillLabel.setStyle("-fx-background-color: #6cb9b9; -fx-padding: 10px; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 20px; -fx-background-radius: 20px;");
                skillPane.getChildren().add(skillLabel);
                skillCount++;

                // Check if the maximum number of skills in a line is reached
                if (skillCount % 6 == 0) {
                    // Add a new line separator
                    skillPane.getChildren().add(new Label("\n"));
                }
            }
        }

        yOffset += 40;

        Label nameuserLabel = new Label(CvService.getUserInfoForCV(cv.getId()));
        nameuserLabel.setLayoutX(45);
        nameuserLabel.setLayoutY(190);
        nameuserLabel.setStyle(" -fx-text-fill: #ea8d22;");


        // Create an ImageView for the image
        ImageView locationImage = new ImageView(new Image(getClass().getResourceAsStream("/images/loc.png")));
        locationImage.setFitWidth(20); // Adjust the width as needed
        locationImage.setFitHeight(30); // Adjust the height as needed

// Create an HBox to hold the image and the label
        HBox locationBox = new HBox(10); // Set spacing between image and label
        locationImage.setLayoutX(5);
        locationImage.setLayoutY(250);

        Label LocationPrefixLabel = new Label("Location: ");
        LocationPrefixLabel.setLayoutX(30);
        LocationPrefixLabel.setLayoutY(250);
        LocationPrefixLabel.setStyle(" -fx-text-fill: #892193FF;");

        Label LocationLabel = new Label(cv.getLocation());
        LocationLabel.setLayoutX(130);
        LocationLabel.setLayoutY(250);



// Add the labels to the card

        card.getChildren().addAll(nameuserLabel,locationImage, LocationPrefixLabel,LocationLabel);

       /* Image gifImage = new Image(getClass().getResourceAsStream("/images/CVANIMATION.gif"));
        ImageView gifImageView = new ImageView(gifImage);

// Schedule the positioning of the ImageView to be executed after the layout pass
        Platform.runLater(() -> {
            gifImageView.setLayoutX(card.getWidth()-56);
            gifImageView.setLayoutY(250);
            gifImageView.setFitWidth(80);
            gifImageView.setFitHeight(80);
        });

// Add the GIF ImageView to the card
        card.getChildren().add(gifImageView);*/
        return card;
    }

    private void redirectToShowCV(MouseEvent event, int id) {
        try {
            Window window = ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CV/showcvEntreprise.fxml"));
            Parent root = loader.load();

            // Pass the authenticated user to the controller
            ShowCV ShowCVController = loader.getController();

            // Set the loggedInPatient in the UpdateUser controller
            ShowCVController.setCvIdtoShowCV(id);

            // Show the UpdateUser view
            Stage stage = (Stage) window;

            // Set the new scene
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void redirectToShowOWNCV(MouseEvent event, int id) {
        try {
            Window window = ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CV/showCV.fxml"));
            Parent root = loader.load();

            // Pass the authenticated user to the controller
            ShowCV ShowCVController = loader.getController();

            ShowCVController.setCvIdd(id);

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CV/showAllCVSEntreprise.fxml"));
            Parent root = loader.load();
            allCVSscroll.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    @FXML
    private void navigateToAddOffer() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/offer/addOffer.fxml"));
            Parent root = loader.load();
            allCVSscroll.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    private void navigateToDisplayuseroff() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/offer/Displayoffuser.fxml"));
            Parent root = loader.load();
            allCVSscroll.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    private void navigateAlloffers(MouseEvent event) {
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

    public void goprofile(MouseEvent event) {
        try {
            // Load the FXML file of the DisplayAllOffers view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/profile_entreprise.fxml"));
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
