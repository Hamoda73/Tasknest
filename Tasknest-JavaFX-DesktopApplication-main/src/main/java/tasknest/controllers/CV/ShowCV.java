package tasknest.controllers.CV;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import tasknest.controllers.Skill.AddSkill;
import tasknest.controllers.Skill.EditSkill;
import tasknest.controllers.applications.seeMyApps;
import tasknest.models.CV;
import tasknest.models.Skill;
import tasknest.services.CvService;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javafx.scene.control.Label;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import tasknest.services.SkillService;
import javafx.stage.Stage;
import tasknest.tests.MainFx;

public class ShowCV {

    @FXML
    private ScrollPane cvScrollPane;

    private CvService cvService;
    private int cvIddd;
    private int cvidtoshow;
    public void setCvIdd(int cvIdd) {
        cvIddd = cvIdd;
      //  System.out.println("dsfsdfsd"+cvIddd);
        initialize();
    }
    public void setCvIdtoShowCV(int id) { //on click lil carte
         cvidtoshow = id;
         System.out.println("cvid to show= "+cvidtoshow);
        initialize2();
    }


    public void initialize() {
        cvService = new CvService();
        CV cv = cvService.getCVById(cvIddd);
        if (cv != null) {
            VBox CVContainer = new VBox(10);
            AnchorPane card = createCVCard(cv);
            CVContainer.getChildren().add(card); // Adjust spacing between cards

            cvScrollPane.setContent(CVContainer);
        }
    }

    private AnchorPane createCVCard(CV cv) {
        AnchorPane card = new AnchorPane();
        card.getStyleClass().add("cv-card");

        // Get user image path using CvService
        String imagePath = cvService.getUserImageForCV(cvIddd);
        //System.out.println("image="+imagePath);
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
            Image fallbackImage = new Image("/images/planet.png");
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
        Label DescriptionPrefixLabel = new Label("Experience: ");
        DescriptionPrefixLabel.setLayoutX(250);
        DescriptionPrefixLabel.setLayoutY(yOffset);
        DescriptionPrefixLabel.setStyle(" -fx-text-fill: #892193FF;");
        yOffset += 40;

        Label DescriptionLabel = new Label(cv.getDescription());
        DescriptionLabel.setLayoutX(250);
        DescriptionLabel.setLayoutY(yOffset);
        DescriptionLabel.setMaxWidth(400); // Set the maximum width for wrapping
        DescriptionLabel.setWrapText(true); // Enable wrapping

// Calculate the height based on the wrapped text
        double textHeight = DescriptionLabel.getLayoutBounds().getHeight();
        yOffset *= textHeight + 3; // Add 5 for spacing

// Add labels to the card




// Get the languages array from the CV object

        yOffset+=40;
// Create and configure the labels for languages
        Label LanguagesPrefixLabel = new Label("Languages: ");
        LanguagesPrefixLabel.setLayoutX(250);
        LanguagesPrefixLabel.setLayoutY(yOffset);
        LanguagesPrefixLabel.setStyle(" -fx-text-fill: #892193FF;");

        yOffset+=40;
// Add the labels to the card (outside the loop)
        card.getChildren().addAll(BIOPrefixLabel, BIOLabel, DescriptionPrefixLabel, DescriptionLabel, LanguagesPrefixLabel);
        String[] languages = cv.getLanguage();
         // Initial Y position for the first language label

        for (String language : languages) {
            // Remove the square brackets and double quotes from the language string
            String cleanLanguage = language.replace("[", "").replace("]", "").replace("\"", "");
            // Create a Label for each language with a point before it
            Label languageLabel = new Label("• " + cleanLanguage.trim());
            languageLabel.setLayoutX(270);
            languageLabel.setLayoutY(yOffset);
            yOffset += 20; // Increase Y position for the next language label
            card.getChildren().add(languageLabel);
        }

        yOffset += 40;
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
        int skillCount = 0;
        List<Skill> skills = cvService.getSkillsForCV(cvIddd);

// Check if there are any skills associated with the CV
        if (skills.isEmpty()) {
            // Create a button for adding skills
            Button addSkillButton = new Button("Add Skill");
            addSkillButton.setOnAction(event -> {
                // Redirect to the page for adding skills
                redirectToAddSkill(event, cv.getId());
            });

            // Set styles for the button
            addSkillButton.setStyle("-fx-background-color: #6cb9b9; -fx-padding: 10px; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 20px; -fx-background-radius: 20px;");

            // Add the button to the skill pane
            skillPane.getChildren().add(addSkillButton);
        } else {

            for (Skill skill : cvService.getSkillsForCV(cvIddd)) {
                Label skillLabel = new Label(skill.getName());

                ImageView crossIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/crosss.png")));
                crossIcon.setFitHeight(40); // Set the height of the cross icon
                crossIcon.setFitWidth(40); // Set the width of the cross icon

                // Create an HBox to hold the skill label and the cross icon
                HBox skillBox = new HBox(skillLabel, crossIcon);

                // Set the alignment of the skill label within the HBox
                skillBox.setAlignment(Pos.CENTER_LEFT);

                // Add padding between the skill label and the cross icon
                HBox.setMargin(crossIcon, new Insets(0, 5, 0, 0));

                // Set the click event handler only on the cross icon
                crossIcon.setOnMouseClicked(event -> {
                    // Remove the skill label from the skillPane
                    skillPane.getChildren().remove(skillBox);
                    // Delete the skill when the cross icon is clicked
                    deleteSkill(skill);

                    // Check if there are no more skills left
                    if (skillPane.getChildren().stream().allMatch(node -> !(node instanceof HBox))) {
                        // If no skills are left, add the "Add Skill" button again
                        skillPane.getChildren().clear(); // Clear existing children

                        // Create a button for adding skills
                        Button addSkillButton = new Button("Add Skill");
                        addSkillButton.setOnAction(addSkillEvent -> {
                            // Redirect to the page for adding skills
                            redirectToAddSkill(addSkillEvent, cv.getId());
                        });

                        // Set styles for the button
                        addSkillButton.setStyle("-fx-background-color: #6cb9b9; -fx-padding: 10px; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 20px; -fx-background-radius: 20px;");

                        // Add the button to the skill pane
                        skillPane.getChildren().add(addSkillButton);
                    }
                });


                // Set the click event handler for editing on the skill label
                skillLabel.setOnMouseClicked(event -> {
                    redirectToEditSkill(event, skill.getId(), cv.getId());
                });

                skillLabel.setStyle("-fx-background-color: #6cb9b9; -fx-padding: 10px; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 20px; -fx-background-radius: 20px;");


                // Add the skillBox to the skillPane
                skillPane.getChildren().add(skillBox);
                skillCount++;

                // Check if the maximum number of skills in a line is reached
                if (skillCount % 3 == 0) {
                    // Add a new line separator
                    skillPane.getChildren().add(new Label("\n"));
                }
            }
            // After displaying skills


        }

        yOffset += 40;

        Label nameuserLabel = new Label(cvService.getUserInfoForCV(cvIddd));
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

        yOffset += 40;
        Label CertificationPrefixLabel = new Label("Certification: ");
        CertificationPrefixLabel.setLayoutX(10);
        CertificationPrefixLabel.setLayoutY(yOffset);
        CertificationPrefixLabel.setStyle(" -fx-text-fill: #892193FF;");

        Label CertificationLabel = new Label(cv.getCertification());
        CertificationLabel.setLayoutX(150);
        CertificationLabel.setLayoutY(yOffset);

        yOffset += 40;
        Label ContactPrefixLabel = new Label("Contact: ");
        ContactPrefixLabel.setLayoutX(10);
        ContactPrefixLabel.setLayoutY(yOffset);
        ContactPrefixLabel.setStyle("-fx-text-fill: #892193FF;");

        Label ContactLabel = new Label(cv.getContact());
        ContactLabel.setLayoutX(110);
        ContactLabel.setLayoutY(yOffset);
        ContactLabel.setStyle("-fx-text-fill: blue; -fx-underline: true;");

// Set a mouse click event handler



        yOffset += 50;


// Add the labels to the card

        card.getChildren().addAll(nameuserLabel,locationImage, LocationPrefixLabel,LocationLabel,CertificationPrefixLabel,CertificationLabel,ContactPrefixLabel,ContactLabel);

        ContactLabel.setOnMouseClicked(event -> {
            if (Desktop.isDesktopSupported()) {
                try {
                    // Create a URI object from the contact text
                    URI uri = new URI(cv.getContact());
                    // Open the URI in the default browser
                    Desktop.getDesktop().browse(uri);
                } catch (IOException | URISyntaxException e) {
                    e.printStackTrace(); // Handle any potential exceptions
                }
            }
        });

        // Create Delete button
        Button resumeButton = new Button("Customize your CV");
        resumeButton.setLayoutX(30); // Adjust X position as needed
        resumeButton.setLayoutY( yOffset); // Adjust Y position as needed
        resumeButton.setOnAction(event -> handleCustomizedCV(cv,event)); // Attach event handler for delete action
        resumeButton.setStyle("-fx-background-color: #31b2e7; -fx-padding: 10px; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 25px; -fx-background-radius: 25px;");

        Button editButton = new Button("Edit CV");
        editButton.setLayoutX(340); // Adjust X position as needed
        editButton.setLayoutY( yOffset); // Adjust Y position as needed
        editButton.setOnAction(event -> handleEdit(cv)); // Attach event handler for edit action
        editButton.setStyle("-fx-background-color: #ea8d22; -fx-padding: 10px; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 25px; -fx-background-radius: 25px;");

        // Create Delete button
        Button deleteButton = new Button("Delete CV");
        deleteButton.setLayoutX(470); // Adjust X position as needed
        deleteButton.setLayoutY( yOffset); // Adjust Y position as needed
        deleteButton.setOnAction(event -> handleDelete(cv,event)); // Attach event handler for delete action
        deleteButton.setStyle("-fx-background-color: #f30b0b; -fx-padding: 10px; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 25px; -fx-background-radius: 25px;");

        // Add buttons to the card
        card.getChildren().addAll(editButton, deleteButton,resumeButton);

        return card;
    }



    private void redirectToAddSkill(ActionEvent event, int id) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Skill/AddSkill.fxml"));
            Parent root = loader.load();

            // Retrieve the controller instance
            AddSkill controller = loader.getController();

            // Pass the skill ID to the EditSkillController
            controller.setCvIdforaddskill(id,event);
            SkillService skillService = new SkillService();
            // controller.setSKILLService(skillService);
            // Get the current scene
            Scene scene = ((Node) event.getSource()).getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteSkill(Skill skill) {

        SkillService skillService = new SkillService() ;
        skillService.supprimer(skill);


    }

    private void redirectToEditSkill(MouseEvent event, int skillId,int idcvtoedit) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Skill/EditSkill.fxml"));
            Parent root = loader.load();

            // Retrieve the controller instance
            EditSkill controller = loader.getController();

            // Pass the skill ID to the EditSkillController
            controller.setSkillId(skillId,idcvtoedit);
            SkillService skillService = new SkillService();
           // controller.setSKILLService(skillService);
            // Get the current scene
            Scene scene = ((Node) event.getSource()).getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void handleCustomizedCV(CV cv, ActionEvent event) {
        Stage customizedCVStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/CV/customizedCV.fxml"));
        try {
            HBox root1 = loader.load();
            ResumeController controller = loader.getController();

            // Set the root and customized CV data
            controller.setRoot(root1);
            Scene scene = new Scene(root1);
            customizedCVStage.setScene(scene);
            customizedCVStage.show();
            customizedCVStage.setWidth(592); // Set width
            customizedCVStage.setHeight(800); // Set height
            customizedCVStage.setX(500); // Set X coordinate
            customizedCVStage.setY(10); // Set Y coordinate
            customizedCVStage.setTitle("Customized CV");
            controller.setCustomizedCV(cv);

            // Display the stage with the loaded root

        } catch (IOException e) {
            e.printStackTrace();
        }
    }




 /*   private void handleCustomizedCV(CV cv, ActionEvent event) {
        // Create a newstage
        Stage customizedCVStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/CV/customizedCV.fxml"));
        Parent root;
        try {
            // Load the FXML file and get the root element
            HBox root1 = loader.load();

            // Retrieve the controller instance
            ResumeController controller = loader.getController();
            System.out.println("\n customize=" + cv);

            // Pass the CV object and the root element to the controller
            controller.setCustomizedCV(cv);
            controller.setRoot(root1);
           // controller.generatePDF();

            // Show success alert
            showAlert(AlertType.INFORMATION, "Success", "PDF generated successfully.");*/

        /* if (root1 != null) {
            // Create a scene with the root element
            Scene scene = new Scene(root1);

            // Set the scene to the stage
            customizedCVStage.setScene(scene);

            // Set stage width and height
            customizedCVStage.setWidth(793); // Set your desired width
            customizedCVStage.setHeight(1122); // Set your desired height

            // Show the stage
            customizedCVStage.show();
        } else {
            // Root container is null, handle the error
            System.err.println("Root container is null in FXML file.");
            return;
        } */

      /*  } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }


    private void handleDelete(CV cv, ActionEvent event) {
        // Check if there are any skills associated with the CV
        List<Skill> skills = cvService.getSkillsForCV(cv.getId());
        if (!skills.isEmpty()) {
            // Show an alert indicating that the CV cannot be deleted
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Cannot Delete CV");
            alert.setHeaderText("CV Deletion Failed");
            alert.setContentText("The CV cannot be deleted because it has associated skills.");
            alert.showAndWait();
        } else {
            // No skills associated with the CV, proceed with deletion
            CvService cvService = new CvService();
            cvService.supprimer(cv);
            redirectallFreelancers(event);
        }
    }


    private void handleEdit(CV cv) {

            // Navigate to the EditOfferController
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CV/EditCV.fxml"));
            Parent root;
            try {
                root = loader.load();

                // Retrieve the controller instance
                EditCV controller = loader.getController();

                // Pass the offer and offerService to the EditOffer Controller
                controller.setEditedCV(cv);
                controller.setCVService(cvService);

                // Get the current scene
                Scene scene = cvScrollPane.getScene();
                scene.setRoot(root);
            } catch (IOException e) {
                e.printStackTrace();
            }


    }


    public void initialize2() {
        cvService = new CvService();
        CV cv = cvService.getCVById(cvidtoshow);
        if (cv != null) {
            VBox CVContainer = new VBox(10);
            AnchorPane card = createCVCard2(cv);
            CVContainer.getChildren().add(card); // Adjust spacing between cards

            cvScrollPane.setContent(CVContainer);
        }
    }

    private AnchorPane createCVCard2(CV cv) {
        AnchorPane card = new AnchorPane();
        card.getStyleClass().add("cv-card");

        // Get user image path using CvService
        String imagePath = cvService.getUserImageForCV(cvidtoshow);
        System.out.println("image222= "+imagePath);
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
            Image fallbackImage = new Image("/images/planet.png");
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
        Label DescriptionPrefixLabel = new Label("Experience: ");
        DescriptionPrefixLabel.setLayoutX(250);
        DescriptionPrefixLabel.setLayoutY(yOffset);
        DescriptionPrefixLabel.setStyle(" -fx-text-fill: #892193FF;");
        yOffset += 40;

        Label DescriptionLabel = new Label(cv.getDescription());
        DescriptionLabel.setLayoutX(250);
        DescriptionLabel.setLayoutY(yOffset);
        DescriptionLabel.setMaxWidth(400); // Set the maximum width for wrapping
        DescriptionLabel.setWrapText(true); // Enable wrapping

// Calculate the height based on the wrapped text
        double textHeight = DescriptionLabel.getLayoutBounds().getHeight();
        yOffset *= textHeight + 3; // Add 5 for spacing

// Add labels to the card




// Get the languages array from the CV object

        yOffset+=40;
// Create and configure the labels for languages
        Label LanguagesPrefixLabel = new Label("Languages: ");
        LanguagesPrefixLabel.setLayoutX(250);
        LanguagesPrefixLabel.setLayoutY(yOffset);
        LanguagesPrefixLabel.setStyle(" -fx-text-fill: #892193FF;");

        yOffset+=40;
// Add the labels to the card (outside the loop)
        card.getChildren().addAll(BIOPrefixLabel, BIOLabel, DescriptionPrefixLabel, DescriptionLabel, LanguagesPrefixLabel);
        String[] languages = cv.getLanguage();
        // Initial Y position for the first language label

        for (String language : languages) {
            // Remove the square brackets and double quotes from the language string
            String cleanLanguage = language.replace("[", "").replace("]", "").replace("\"", "");
            // Create a Label for each language with a point before it
            Label languageLabel = new Label("• " + cleanLanguage.trim());
            languageLabel.setLayoutX(270);
            languageLabel.setLayoutY(yOffset);
            yOffset += 20; // Increase Y position for the next language label
            card.getChildren().add(languageLabel);
        }

        yOffset += 40;
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
        int skillCount = 0;
        for (Skill skill : cvService.getSkillsForCV(cvidtoshow)) {
            Label skillLabel = new Label(skill.getName());
            skillLabel.setStyle("-fx-background-color: #6cb9b9; -fx-padding: 10px; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 20px; -fx-background-radius: 20px;");
            skillPane.getChildren().add(skillLabel);
            skillCount++;

            // Check if the maximum number of skills in a line is reached
            if (skillCount % 4 == 0) {
                // Add a new line separator
                skillPane.getChildren().add(new Label("\n"));
            }
        }


        yOffset += 40;

        Label nameuserLabel = new Label(cvService.getUserInfoForCV(cvidtoshow));
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

        yOffset += 40;
        Label CertificationPrefixLabel = new Label("Certification: ");
        CertificationPrefixLabel.setLayoutX(10);
        CertificationPrefixLabel.setLayoutY(yOffset);
        CertificationPrefixLabel.setStyle(" -fx-text-fill: #892193FF;");

        Label CertificationLabel = new Label(cv.getCertification());
        CertificationLabel.setLayoutX(150);
        CertificationLabel.setLayoutY(yOffset);

        yOffset += 40;
        Label ContactPrefixLabel = new Label("Contact: ");
        ContactPrefixLabel.setLayoutX(10);
        ContactPrefixLabel.setLayoutY(yOffset);
        ContactPrefixLabel.setStyle(" -fx-text-fill: #892193FF;");

        Label ContactLabel = new Label(cv.getContact());
        ContactLabel.setLayoutX(110);
        ContactLabel.setLayoutY(yOffset);

// Add the labels to the card

        card.getChildren().addAll(nameuserLabel,locationImage, LocationPrefixLabel,LocationLabel,CertificationPrefixLabel,CertificationLabel,ContactPrefixLabel,ContactLabel);

        /*Button editButton = new Button("Edit CV");
        editButton.setLayoutX(340); // Adjust X position as needed
        editButton.setLayoutY( yOffset); // Adjust Y position as needed
        // editButton.setOnAction(event -> handleEdit(cv)); // Attach event handler for edit action
        editButton.setStyle("-fx-background-color: #ea8d22; -fx-padding: 10px; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 25px; -fx-background-radius: 25px;");

        // Create Delete button
        Button deleteButton = new Button("Delete CV");
        deleteButton.setLayoutX(470); // Adjust X position as needed
        deleteButton.setLayoutY( /*yOffset*///); // Adjust Y position as needed
        // deleteButton.setOnAction(event -> handleDelete(cv)); // Attach event handler for delete action
        //deleteButton.setStyle("-fx-background-color: #f30b0b; -fx-padding: 10px; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 25px; -fx-background-radius: 25px;");

        // Add buttons to the card
       /* card.getChildren().addAll(editButton, deleteButton);*/

        return card;
    }

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
    @FXML
    private void redirectallFreelancers(ActionEvent event) {
        try {
            Window window = ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CV/showAllCVs.fxml"));
            Parent root = loader.load();

            // Pass the authenticated user to the controller
         //   showAllCVS ShowAllCVsController = loader.getController();

            // Show the UpdateUser view
            Stage stage = (Stage) window;

            // Set the new scene
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void navigateToAddCVPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CV/AddCv.fxml"));
            Parent root = loader.load();
            cvScrollPane.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void redirectToShowOWNCV(MouseEvent event, int id) {
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
    public void makeurcv(MouseEvent event) {
        // Check if the user has a CV
        boolean hasCV = cvService.checkIfUserHasCV(MainFx.getSession().getId()); // Implement this method to check if the user has a CV

        if (hasCV) {
            CV cv=cvService.getCVByUserId(MainFx.getSession().getId());
            // User has a CV, show the existing CV
            redirectToShowOWNCV(event,cv.getId()); // Implement this method to show the existing CV
        } else {
            // User doesn't have a CV, navigate to the page to add a new CV
            navigateToAddCVPage(); // Implement this method to navigate to the add CV page
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

    public void navigateAlloffersentreprise(MouseEvent event) {
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

    public void redirectFreelancersentreprise(MouseEvent event) {
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

    public void reclamation(MouseEvent event) {
        try {
            // Load the FXML file of the DisplayAllOffers view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/complaintentreprise.fxml"));
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

    public void alloffersuser(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/offer/Displayoffuser.fxml"));
            Parent root = loader.load();
            cvScrollPane.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void postoffer(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/offer/addOffer.fxml"));
            Parent root = loader.load();
            cvScrollPane.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
