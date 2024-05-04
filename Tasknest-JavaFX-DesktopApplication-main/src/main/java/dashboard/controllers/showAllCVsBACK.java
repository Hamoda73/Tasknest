package dashboard.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
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
import tasknest.controllers.CV.ShowCV;
import tasknest.models.CV;
import tasknest.models.Skill;
import tasknest.services.CvService;
import tasknest.services.SkillService;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import javafx.scene.chart.PieChart;
import java.util.HashMap;
import java.util.Map;
public class showAllCVsBACK {

    @FXML
    ScrollPane allCVSscroll;

    @FXML
    private Pagination pagination;
    private static final int ITEMS_PER_PAGE = 2;
    private ObservableList<CV> allCVsList = FXCollections.observableArrayList();

    @FXML
    private VBox cardsContainer;

    private tasknest.services.CvService CvService;

    public void initialize() {
        CvService = new CvService();
        allCVsList.addAll(CvService.afficher());
        Allcvs();
        setupPagination();

    }

    private void setupPagination() {
        int pageCount = (int) Math.ceil((double) allCVsList.size() / ITEMS_PER_PAGE);
        pagination = new Pagination(pageCount, 0);
        pagination.setPageFactory(this::createPage);
        allCVSscroll.setContent(pagination);
    }

    private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * ITEMS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ITEMS_PER_PAGE, allCVsList.size());
        List<CV> currentPageCVS = allCVsList.subList(fromIndex, toIndex);

        VBox pageContent = new VBox(10);
        for (CV cv : currentPageCVS) {
            AnchorPane card = createCVCard(cv);
            pageContent.getChildren().add(card);
        }
        return pageContent;
    }
 /*   private PieChart createSkillPieChart(List<Skill> skills) {
        // Create a new PieChart
        PieChart pieChart = new PieChart();

        // Calculate the total sum of skill values
        double totalSum = skills.stream().mapToDouble(Skill::getSkill_value).sum();

        // Create data for the chart
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Skill skill : skills) {
            // Calculate the percentage for each skill
            double percentage = (skill.getSkill_value() / totalSum) * 100.0;
            pieChartData.add(new PieChart.Data(skill.getName(), percentage));
        }

        // Set the data to the chart
        pieChart.setData(pieChartData);

        // Set preferred width and height to make the chart smaller
        pieChart.setPrefWidth(150); // Adjust width as needed
        pieChart.setPrefHeight(150); // Adjust height as needed

        return pieChart;
    }*/



    private VBox createSkillPieCharts(List<Skill> skills) {
        // Create a container to hold the PieCharts
        VBox container = new VBox();

        // Calculate the frequency of each skill
        Map<String, Integer> skillFrequency = calculateSkillFrequency(skills);

        // Total number of skills
        int totalSkills = skills.size();

        // Iterate through the map of skill frequencies
        for (Map.Entry<String, Integer> entry : skillFrequency.entrySet()) {
            String skillName = entry.getKey();
            int frequency = entry.getValue();

            // Calculate the percentage of the skill
            double percentage = (double) frequency / totalSkills * 100.0;

            // Create a new PieChart for the skill with its percentage
            PieChart pieChart = createSkillPieChart(skillName, percentage);

            // Add the PieChart to the container
            container.getChildren().add(pieChart);
        }

        return container;
    }

    private Map<String, Integer> calculateSkillFrequency(List<Skill> skills) {
        // Create a map to hold the frequency of each skill
        Map<String, Integer> skillFrequency = new HashMap<>();

        // Iterate through the list of skills
        for (Skill skill : skills) {
            String skillName = skill.getName();

            // Update the frequency count for the skill
            skillFrequency.put(skillName, skillFrequency.getOrDefault(skillName, 0) + 1);
        }

        return skillFrequency;
    }

    private PieChart createSkillPieChart(String skillName, double percentage) {
        // Create a new PieChart
        PieChart pieChart = new PieChart();

        // Create data for the chart
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        // Add a single data point representing the skill percentage
        pieChartData.add(new PieChart.Data(skillName + " (" + String.format("%.2f", percentage) + "%)", percentage));

        // Set the data to the chart
        pieChart.setData(pieChartData);

        // Set preferred width and height to make the chart smaller
        pieChart.setPrefWidth(150); // Adjust width as needed
        pieChart.setPrefHeight(150); // Adjust height as needed

        return pieChart;
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


// Add the labels to the card (outside the loop)
        card.getChildren().addAll(BIOPrefixLabel, BIOLabel);
        Label DescriptionPrefixLabel = new Label("Experience: ");
        DescriptionPrefixLabel.setLayoutX(250);
        DescriptionPrefixLabel.setLayoutY(yOffset);
        DescriptionPrefixLabel.setStyle(" -fx-text-fill: #892193FF;");
        yOffset += 40;

        Label DescriptionLabel = new Label(cv.getDescription());
        DescriptionLabel.setLayoutX(250);
        DescriptionLabel.setLayoutY(yOffset);
        DescriptionLabel.setMaxWidth(910); // Set the maximum width for wrapping
        DescriptionLabel.setWrapText(true); // Enable wrapping
        card.getChildren().addAll(DescriptionLabel, DescriptionPrefixLabel);
// Calculate the height based on the wrapped text
        double textHeight = DescriptionLabel.getLayoutBounds().getHeight();


        yOffset += textHeight +150 ; // Add 5 for spacing
       // card.getChildren().addAll(DescriptionLabel, DescriptionPrefixLabel);

// Create and configure the labels for languages
        Label LanguagesPrefixLabel = new Label("Languages: ");
        LanguagesPrefixLabel.setLayoutX(250);
        LanguagesPrefixLabel.setLayoutY(yOffset);
        LanguagesPrefixLabel.setStyle(" -fx-text-fill: #892193FF;");

        yOffset+=40;
// Add the labels to the card (outside the loop)
        card.getChildren().add(LanguagesPrefixLabel);
        String[] languages = cv.getLanguage();
        // Initial Y position for the first language label

        for (String language : languages) {
            // Remove the square brackets and double quotes from the language string
            String cleanLanguage = language.replace("[", "").replace("]", "").replace("\"", "");
            // Create a Label for each language with a point before it
            Label languageLabel = new Label("• " + cleanLanguage.trim());
            languageLabel.setLayoutX(270);
            languageLabel.setLayoutY(yOffset);
            yOffset += 30; // Increase Y position for the next language label
            card.getChildren().add(languageLabel);
        }


        yOffset += 40;
        Label CertificationPrefixLabel = new Label("Certification: ");
        CertificationPrefixLabel.setLayoutX(10);
        CertificationPrefixLabel.setLayoutY(yOffset);
        CertificationPrefixLabel.setStyle(" -fx-text-fill: #892193FF;");

        Label CertificationLabel = new Label(cv.getCertification());
        CertificationLabel.setLayoutX(150);
        CertificationLabel.setLayoutY(yOffset);
        card.getChildren().addAll(CertificationPrefixLabel,CertificationLabel);
        yOffset += 40;
        Label ContactPrefixLabel = new Label("Contact: ");
        ContactPrefixLabel.setLayoutX(10);
        ContactPrefixLabel.setLayoutY(yOffset);
        ContactPrefixLabel.setStyle("-fx-text-fill: #892193FF;");

        Label ContactLabel = new Label(cv.getContact());
        ContactLabel.setLayoutX(110);
        ContactLabel.setLayoutY(yOffset);
        ContactLabel.setStyle("-fx-text-fill: blue; -fx-underline: true;");
        card.getChildren().addAll(ContactPrefixLabel,ContactLabel);
// Set a mouse click event handler
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


        yOffset += 50;

        Label SkillsPrefixLabel = new Label("Skills: ");
        SkillsPrefixLabel.setLayoutX(10);
        SkillsPrefixLabel.setLayoutY(yOffset);
        SkillsPrefixLabel.setStyle("-fx-text-fill: #892193FF;");
        yOffset += 40;
// Create a FlowPane to hold the skill labels
        FlowPane skillPane = new FlowPane();
        skillPane.setLayoutX(30);
        skillPane.setLayoutY(yOffset);
        skillPane.setHgap(20); // Set horizontal gap between skill labels
        skillPane.setVgap(20); // Set vertical gap between skill labels
        skillPane.setPrefWrapLength(400); // Set preferred wrap length
        yOffset += 40;
// Add the skill pane to the card
        card.getChildren().addAll(SkillsPrefixLabel, skillPane);
        //PieChart pieChart = createSkillPieChart(CvService.getSkillsForCV(cv.getId()));

        // Set the layout coordinates for the pie chart
      //  pieChart.setLayoutX(450); // Adjust X position as needed
       // pieChart.setLayoutY(yOffset-70); // Adjust Y position as needed

        // Add the pie chart to the card
     //   card.getChildren().add(pieChart);
// Iterate through skills and add labels to the skill pane
        List<Skill> skills = CvService.getSkillsForCV(cv.getId());

        if (skills.isEmpty()) {
            // Add a message indicating no skills are available
            Label noSkillsLabel = new Label("No skills available");
            skillPane.getChildren().add(noSkillsLabel);

        } else {
            int skillCount = 0;
            for (Skill skill : CvService.getSkillsForCV(cv.getId())) {
                Label skillLabel = new Label(skill.getName());
                Label valueLabel = new Label(String.valueOf(skill.getSkill_value())+" %");
                System.out.println("val="+skill.getSkill_value());
                System.out.println("val="+skillLabel);
                ImageView crossIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/crosss.png")));
                crossIcon.setFitHeight(40); // Set the height of the cross icon
                crossIcon.setFitWidth(40); // Set the width of the cross icon

                // Create an HBox to hold the skill label and the cross icon
                HBox skillBox = new HBox(2,skillLabel,valueLabel, crossIcon);

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


                });


                skillLabel.setStyle("-fx-background-color: #6cb9b9; -fx-padding: 10px; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 20px; -fx-background-radius: 20px;");
                valueLabel.setStyle("-fx-background-color: #a781d2; -fx-padding: 10px; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 20px; -fx-background-radius: 50px;");


                // Add the skillBox to the skillPane
                skillPane.getChildren().add(skillBox);
                skillCount++;

                // Check if the maximum number of skills in a line is reached
                if (skillCount % 18 == 0) {
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


        Label LocationLabel = new Label(cv.getLocation());
        LocationLabel.setLayoutX(50);
        LocationLabel.setLayoutY(250);



// Add the labels to the card

        card.getChildren().addAll(nameuserLabel,locationImage,LocationLabel);

        // Create Delete button
        Button deleteButton = new Button("Delete CV");
        deleteButton.setLayoutX(1050); // Adjust X position as needed
        deleteButton.setLayoutY( yOffset); // Adjust Y position as needed
        deleteButton.setOnAction(event -> handleDelete(cv,event)); // Attach event handler for delete action
        deleteButton.setStyle("-fx-background-color: #f30b0b; -fx-padding: 10px; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 25px; -fx-background-radius: 25px;");

        // Add buttons to the card
        card.getChildren().addAll(deleteButton);

       /* Image gifImage = new Image(getClass().getResourceAsStream("/images/CVANIMATION.gif"));
        ImageView gifImageView = new ImageView(gifImage);

// Schedule the positioning of the ImageView to be executed after the layout pass
        Platform.runLater(() -> {
            gifImageView.setLayoutX(card.getWidth()-56);
            gifImageView.setLayoutY(30);
            gifImageView.setFitWidth(80);
            gifImageView.setFitHeight(80);
        });

// Add the GIF ImageView to the card
        card.getChildren().add(gifImageView);*/

        if (skills.isEmpty()) {
            // If there are no skills, add a message indicating so
            Label noSkillsLabel = new Label("No skills available");
            skillPane.getChildren().add(noSkillsLabel);
        }

        // If there are no CVs available, display a message indicating so
        if (CvService.afficher().isEmpty()) {
            Label noCVsLabel = new Label("No CVs available");
            card.getChildren().add(noCVsLabel);
        }
        return card;
    }

    private void handleDelete(CV cv, ActionEvent event) {

        // Check if there are any skills associated with the CV
        List<Skill> skills = CvService.getSkillsForCV(cv.getId());
        if (!skills.isEmpty()) {
            // Show an alert
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Cannot Delete CV");
            alert.setHeaderText("CV Deletion Failed");
            alert.setContentText("The CV cannot be deleted because it has associated skills.");
            alert.showAndWait();
        } else {
            // No skills -> DELETE
            CvService cvService = new CvService();
            cvService.supprimer(cv);
            redirectFreelancers(event);
        }
    }

    private void deleteSkill(Skill skill) {

        SkillService skillService = new SkillService() ;
        skillService.supprimer(skill);


    }

    public void redirectFreelancers(ActionEvent event) {
        try {
            Window window = ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CV/showAllCVsBACK.fxml"));
            Parent root = loader.load();

            // Pass the authenticated user to the controller
            dashboard.controllers.showAllCVsBACK showAllCVsBACKController = loader.getController();

            // Set the loggedInPatient in the UpdateUser controller

            // Show the UpdateUser view
            Stage stage = (Stage) window;

            // Set the new scene
            stage.setScene(new Scene(root));
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

    public void allcvsback(MouseEvent event) {
        try {
            // Load the FXML file of the DisplayAllOffers view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/showAllCVsBACK.fxml"));
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



    public void allofferssback(MouseEvent event) {
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
    public void showreclamation(MouseEvent event) {
        try {
            // Load the FXML file of the DisplayAllOffers view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin.fxml"));
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
















