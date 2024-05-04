package tasknest.controllers.applications;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;
import tasknest.controllers.CV.ShowCV;
import tasknest.controllers.CV.showAllCVS;
import tasknest.controllers.offer.editOffer;
import tasknest.models.Application;
import tasknest.models.CV;
import tasknest.models.offers;
import tasknest.services.ApplicationService;
import tasknest.services.CvService;
import tasknest.services.OfferService;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class seeMyApps {
    private int useridapp;
    private int offerID;
    @FXML
    ScrollPane MyappsScrollPane;
    ApplicationService applicationService = new ApplicationService();
    private CvService  CvService = new CvService();
    public void setUserApps(int user_Id,int offer_Id) {
        useridapp=user_Id;
        offerID=offer_Id;
        System.out.println("useridapp= "+useridapp);
        System.out.println("offerID= "+offerID);
        initialize();

    }
    public void setUserallApps(int user_Id) {
        useridapp=user_Id;
        System.out.println("useridapp= "+useridapp);
        System.out.println("offerID= "+offerID);
        initialize();

    }
    public void initialize() {
        ApplicationService applicationService = new ApplicationService();
        populateApps();



    }


    private void populateApps() {
        List<Application> userApps = applicationService.getApplicationsByUserId(useridapp);
        System.out.println("userApps= " + userApps);
        VBox appsContainer = new VBox(10);

        if (userApps.isEmpty()) {

            AnchorPane noAppsPane = new AnchorPane();
            Label noAppsLabel = new Label("You didn't Apply yet");
            noAppsLabel.getStyleClass().add("no-apps-label");
            AnchorPane.setTopAnchor(noAppsLabel, 200.0);
            AnchorPane.setBottomAnchor(noAppsLabel, 200.0);
            AnchorPane.setLeftAnchor(noAppsLabel, 120.0);
            AnchorPane.setRightAnchor(noAppsLabel, 100.0);
            noAppsPane.getChildren().add(noAppsLabel);
            appsContainer.getChildren().add(noAppsPane);
        } else {

            for (Application app : userApps) {
                AnchorPane card = createUserAppsCard(app);
                System.out.println("app= " + app);
                appsContainer.getChildren().add(card);
            }
        }

        MyappsScrollPane.setContent(appsContainer);
    }


    private AnchorPane createUserAppsCard(Application App) {
        AnchorPane card = new AnchorPane();
        card.getStyleClass().add("offerapp-card");

        ImageView imageView = new ImageView();
        String imageUrl = getClass().getResource("/images/appss.png").toExternalForm();
        Image image = new Image(imageUrl);

        imageView.setImage(image);
        imageView.getStyleClass().add("image-view");
        imageView.setFitWidth(300);
        imageView.setFitHeight(200);
        imageView.setLayoutX(0);
        imageView.setLayoutY(5);


        Label FirstnamePrefixLabel = new Label("First Name: ");
        FirstnamePrefixLabel.setLayoutX(300);
        FirstnamePrefixLabel.setLayoutY(10);
        FirstnamePrefixLabel.setStyle(" -fx-text-fill: #ea8d22;");
        System.out.println("heyyy:  "+App.getUserbyidd(useridapp).getFname());
        Label FirstnameLabel = new Label(App.getUserbyidd(useridapp).getFname());
        FirstnameLabel.setLayoutX(420);
        FirstnameLabel.setLayoutY(10);

        Label LastnPrefixLabel = new Label("Last Name: ");
        LastnPrefixLabel.setLayoutX(300);
        LastnPrefixLabel.setLayoutY(50);
        LastnPrefixLabel.setStyle(" -fx-text-fill: #892193FF;");

        Label LastnLabel = new Label(App.getUserbyidd(useridapp).getLname());
        LastnLabel.setLayoutX(420);
        LastnLabel.setLayoutY(50);

        Label EntreprisePrefixLabel = new Label("Entreprise: ");
        EntreprisePrefixLabel.setLayoutX(300);
        EntreprisePrefixLabel.setLayoutY(90);
        EntreprisePrefixLabel.setStyle(" -fx-text-fill: #892193FF;");

        Label EntrepriseLabel = new Label(applicationService.getOfferById(App.getOffers_id()).getEntreprise_name());
        EntrepriseLabel.setLayoutX(410);
        EntrepriseLabel.setLayoutY(90);

        Label PostPrefixLabel = new Label("Post: ");
        PostPrefixLabel.setLayoutX(300);
        PostPrefixLabel.setLayoutY(120);
        PostPrefixLabel.setStyle(" -fx-text-fill: #892193FF;");

        Label PostLabel = new Label(applicationService.getOfferById(App.getOffers_id()).getPost());
        PostLabel.setLayoutX(370);
        PostLabel.setLayoutY(120);
        Label CVPrefixLabel = new Label("CV: ");
        CVPrefixLabel.setLayoutX(300);
        CVPrefixLabel.setLayoutY(160);
        CVPrefixLabel.setStyle(" -fx-text-fill: #892193FF;");

        Label CVLabel = new Label(App.getcvImagePath());
        CVLabel.setLayoutX(420);
        CVLabel.setLayoutY(160);

        Button seeResumeButton = new Button("See Resume");
        seeResumeButton.setLayoutX(490);
        seeResumeButton.setLayoutY(200);
        seeResumeButton.getStyleClass().add("Resume-button");
        seeResumeButton.setOnAction(e -> displayCVImage(App.getcvImagePath()));



        Button DeleteappButton = new Button("Delete");
        DeleteappButton.setLayoutX(380);
        DeleteappButton.setLayoutY(200);

        DeleteappButton.getStyleClass().add("delete-button");
        DeleteappButton.setOnAction(event->  {
            applicationService.supprimer(App);

            initialize();
        });

        Button EditappButton = new Button("Edit");
        EditappButton.setLayoutX(270);
        EditappButton.setLayoutY(200);
        EditappButton.getStyleClass().add("Edit-buttonn");
        EditappButton.setOnAction(event->  {
            RedirecttoEditApp(App);

        });

        card.getChildren().addAll(imageView, FirstnamePrefixLabel, FirstnameLabel, LastnPrefixLabel, LastnLabel,
                EntreprisePrefixLabel, EntrepriseLabel, PostPrefixLabel, PostLabel,seeResumeButton,DeleteappButton,EditappButton
        );

        return card;

    }

    private void RedirecttoEditApp(Application App) {
        System.out.println("Application= "+App);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Applications/editApp.fxml"));
        Parent root;
        try {
            root = loader.load();
            System.out.println("ApplicationUser= "+App);
            // Retrieve the controller instance
            editApp controller = loader.getController();

            // Pass the offer and offerService to the EditOffer Controller
            controller.setEditedAPP(App);
            controller.setAppService(applicationService);

            // Get the current scene
            Scene scene = MyappsScrollPane.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayCVImage(String cvFilePath) {
        try {
            File file = new File(cvFilePath);
            if (file.exists() && Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(file);
            } else {
                // Show an alert if the file doesn't exist or if the desktop is not supported
                Alert alert = new Alert(Alert.AlertType.ERROR, "Could not open PDF file.", ButtonType.OK);
                alert.showAndWait();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }



    }


    @FXML
    private void navigateAlloffers(MouseEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/offer/DisplayAllOffers.fxml"));
            Parent root = loader.load();


            Scene scene = new Scene(root);


            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();


            stage.setScene(scene);


            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public void makeurcv(MouseEvent event) {
        // Check if the user has a CV
        boolean hasCV = CvService.checkIfUserHasCV(useridapp);

        if (hasCV) {
            CV cv=CvService.getCVByUserId(useridapp);
            // User has a CV, show the existing CV
            redirectToShowOWNCV(event,cv.getId());
        }
        else {
            // User doesn't have a CV, navigate to the page to add a new CV
            navigateToAddCVPage();

        }
    }

    @FXML
    private void navigateToAddCVPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CV/AddCv.fxml"));
            Parent root = loader.load();
            MyappsScrollPane.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
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
