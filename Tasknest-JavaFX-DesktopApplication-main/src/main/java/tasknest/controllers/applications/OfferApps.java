package tasknest.controllers.applications;





import javax.mail.internet.MimeMessage;
import com.twilio.exception.ApiException;
import com.twilio.rest.proxy.v1.service.Session;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tasknest.models.Application;
import tasknest.models.offers;
import tasknest.services.ApplicationService;
import tasknest.services.OfferService;

import java.io.File;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URI;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;

import javax.mail.*;
import javax.mail.internet.*;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

import javafx.stage.Stage;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import tasknest.tests.MainFx;

public class OfferApps {

    private static final String ACCOUNT_SID = "ACad974bc2502471805035089fb98d9640";
    private static final String AUTH_TOKEN = "645cb256260e1df1aad66f4ce460f8c4";
    private static final String TWILIO_PHONE_NUMBER = "+19413402531";




    private Application selectedApp;
    private offers off = new offers();
    OfferService offerService = new OfferService();
    ApplicationService applicationService = new ApplicationService();
    private Application App;
    int user_id= MainFx.getSession().getId(); //freelancer eli aaml apply lekher nafsou fl apply ctrl l old apps mytfskhouch

    @FXML
    private ScrollPane appssScrollPane;

    public void setappsOFF(offers offer) {
        off.setId(offer.getId());
        off.setEntreprise_name(offer.getEntreprise_name());

        System.out.println("off idd= " + off.getId());
        System.out.println("off user idd= " + user_id);
        initialize();
    }


    public void initialize()  {
        ApplicationService appS= new ApplicationService();
        populateApps();
        System.out.println("helloooooooooooo");
       Twilio.init(ACCOUNT_SID, AUTH_TOKEN, TWILIO_PHONE_NUMBER);

    }


    public void populateApps() {
        System.out.println("off.getId(): " + off.getId());
        // Fetch applications for the offer
        List<Application> offerApps = offerService.getApplicationsForOffer(off.getId());
        System.out.println("offerapps= " + offerApps);
        VBox offerAppsContainer = new VBox(10);

        if (offerApps.isEmpty()) {
            AnchorPane noAppsPane = new AnchorPane();
            Label noAppsLabel = new Label("No applications yet");
            noAppsLabel.getStyleClass().add("no-apps-label");
            AnchorPane.setTopAnchor(noAppsLabel, 200.0);
            AnchorPane.setBottomAnchor(noAppsLabel, 200.0);
            AnchorPane.setLeftAnchor(noAppsLabel, 130.0);
            AnchorPane.setRightAnchor(noAppsLabel, 100.0);
            noAppsPane.getChildren().add(noAppsLabel);
            offerAppsContainer.getChildren().add(noAppsPane);
        } else {
            // Populate applications if available
            for (Application app : offerApps) {
                System.out.println("app user: " + app);
                AnchorPane card = createOfferAppCard(app);
                offerAppsContainer.getChildren().add(card);
            }
        }

        appssScrollPane.setContent(offerAppsContainer);
    }






    private AnchorPane createOfferAppCard(Application App) {
        AnchorPane card = new AnchorPane();
        card.getStyleClass().add("offerapp-card");

        ImageView imageView = new ImageView();
        String imageUrl = getClass().getResource("/images/appss.png").toExternalForm(); // Path to your image resource
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
        System.out.println("heyyy:  "+App.getUserbyidd(App.getUser_id()).getFname());
        Label FirstnameLabel = new Label(App.getUserbyidd(App.getUser_id()).getFname());
        FirstnameLabel.setLayoutX(420);
        FirstnameLabel.setLayoutY(10);

        Label LastnPrefixLabel = new Label("Last Name: ");
        LastnPrefixLabel.setLayoutX(300);
        LastnPrefixLabel.setLayoutY(50);
        LastnPrefixLabel.setStyle(" -fx-text-fill: #892193FF;");

        Label LastnLabel = new Label(App.getUserbyidd(App.getUser_id()).getLname());
        LastnLabel.setLayoutX(420);
        LastnLabel.setLayoutY(50);

        Label PhonePrefixLabel = new Label("Phone number: ");
        PhonePrefixLabel.setLayoutX(300);
        PhonePrefixLabel.setLayoutY(90);
        PhonePrefixLabel.setStyle(" -fx-text-fill: #892193FF;");

        Label PhoneLabel = new Label(String.valueOf(App.getUserbyidd(App.getUser_id()).getPhonenumber()));
        PhoneLabel.setLayoutX(460);
        PhoneLabel.setLayoutY(90);

        Label EmailPrefixLabel = new Label("Email: ");
        EmailPrefixLabel.setLayoutX(300);
        EmailPrefixLabel.setLayoutY(120);
        EmailPrefixLabel.setStyle(" -fx-text-fill: #892193FF;");

        Label EmailLabel = new Label(App.getUserbyidd(App.getUser_id()).getEmail());
        EmailLabel.setLayoutX(370);
        EmailLabel.setLayoutY(120);
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

        Image gifImage = new Image(getClass().getResourceAsStream("/images/bell.gif"));
        ImageView gifImageView = new ImageView(gifImage);


        Platform.runLater(() -> {
            gifImageView.setLayoutX(card.getWidth() - gifImage.getWidth());
            gifImageView.setLayoutY(15);
            gifImageView.setLayoutX(600);
            gifImageView.setFitWidth(100);
            gifImageView.setFitHeight(100);

        });
//get the fieldssss

        Label firstNameLabel = new Label("First Name: " + App.getUserbyidd(App.getUser_id()).getFname());
        Button SMSButton = new Button("Send SMS");
        SMSButton.setLayoutX(200);
        SMSButton.setLayoutY(200);
        SMSButton .getStyleClass().add("sms-button");
        SMSButton.setOnAction(event -> sendSMS(App));



        card.getChildren().addAll(imageView, FirstnamePrefixLabel, FirstnameLabel, LastnPrefixLabel, LastnLabel,
                PhonePrefixLabel, PhoneLabel, EmailPrefixLabel, EmailLabel,seeResumeButton,DeleteappButton,gifImageView,SMSButton
              );

        return card;
    }


    private void displayCVImage(String cvFilePath) {
        try {
            File file = new File(cvFilePath);
            if (file.exists() && Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(file);
            } else {

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

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/offer/DisplayAllOffersEntreprise.fxml"));
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
    private void navigateoffuser(MouseEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/offer/Displayoffuser.fxml"));
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
    private void navigatepostanoffer(MouseEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/offer/addOffer.fxml"));
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
    private void sendSMS(Application app) {
        String firstName = app.getUserbyidd(app.getUser_id()).getFname();
        String lastName = app.getUserbyidd(app.getUser_id()).getLname();
        String entrepriseName = off.getEntreprise_name();

        String messageBody = String.format("Dear %s %s, Our company %s wants to set an interview date with you. Please contact us for further details.", firstName, lastName, entrepriseName);

        try {
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
            Message message = Message.creator(
                            new PhoneNumber("+21694233067"),
                            new PhoneNumber(TWILIO_PHONE_NUMBER),
                            messageBody)
                    .create();

            System.out.println("SMS sent successfully!");
        } catch (ApiException e) {
            System.out.println("Failed to send SMS: " + e.getMessage());
        }
    }

    @FXML
    private void redirectFreelancers() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CV/showAllCVSEntreprise.fxml"));
            Parent root = loader.load();
            appssScrollPane.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
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




