package dashboard.controllers;

import javafx.application.Platform;
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
import tasknest.models.Application;
import tasknest.models.offers;
import tasknest.services.ApplicationService;
import tasknest.services.OfferService;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
public class displayappsback {




    private offers off = new offers();
    OfferService offerService = new OfferService();
    ApplicationService applicationService = new ApplicationService();
    private Application App;
    int user_id=25; //freelancer eli aaml apply lekher nafsou fl apply ctrl l old apps mytfskhouch

    @FXML
    private ScrollPane appssScrollPane;





    public void setappsOFF(offers offer) {
        off.setId(offer.getId());
        System.out.println("huuuurrrr= "+offer);

        System.out.println("off idd= " +off.getId());
        System.out.println("off user idd= " +user_id);
       initialize();
    }



   public void initialize()  {
        ApplicationService appS= new ApplicationService();
       populateApps();
        System.out.println("helloooooooooooo");
    }

    public void populateApps() {
        System.out.println("off.getId(): "+off.getId());
        // Fetch offers posted by the static user ID
        List<Application> Offerapps = offerService.getApplicationsForOffer(off.getId());
        System.out.println("offerapps= "+ Offerapps );
        VBox offerAppsContainer = new VBox(10);

        for (Application app : Offerapps) {
            System.out.println("app user: "+ app);
            AnchorPane card = createOfferAppCard(app);

            offerAppsContainer.getChildren().add(card);
        }
        // offersContainer.getChildren().clear();
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


       String userEmail = App.getUserbyidd(App.getUser_id()).getEmail();
        Button DeleteappButton = new Button("Delete");
        DeleteappButton.setLayoutX(380);
        DeleteappButton.setLayoutY(200);

        DeleteappButton.getStyleClass().add("delete-button");
        DeleteappButton.setOnAction(event->  {

            applicationService.supprimer(App);

            String to = App.getUserbyidd(App.getUser_id()).getEmail();
            String subject = "Application Deleted Notification";
            String body = "Dear " + App.getUserbyidd(App.getUser_id()).getFname() + ",\n\n"
                    + "Your application  has been deleted.\n\n"
                    + "Regards,\n" ;
            sendDeleteNotificationEmail(userEmail);


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



        card.getChildren().addAll(imageView, FirstnamePrefixLabel, FirstnameLabel, LastnPrefixLabel, LastnLabel,
                PhonePrefixLabel, PhoneLabel, EmailPrefixLabel, EmailLabel,seeResumeButton,DeleteappButton,gifImageView
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

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/offer/displayofferback.fxml"));
            Parent root = loader.load();


            Scene scene = new Scene(root);


            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(scene);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





    private void sendDeleteNotificationEmail(String userEmail) {
        String subject = "Application Deleted Notification";
        String body = "Dear Applicant,\n\n"
                + "Your application has been deleted by the administrator.\n\n"
                + "Regards.";

        sendEmail(userEmail, subject, body);
    }


    private void sendEmail(String to, String subject, String body) {
        // Sender's email ID needs to be mentioned
        String from = "your_email@gmail.com"; // Replace with your email address

        // Assuming you are sending email from Gmail
        String host = "smtp.gmail.com";

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.smtp.port", "587");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.smtp.auth", "true");

        // Get the Session object.// and pass username and password
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("majus.erij1@gmail.com", "dplc kmtq lwbr ggnn");
            }
        });

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject(subject);

            // Now set the actual message
            message.setText(body);

            // Send message
            Transport.send(message);
            System.out.println("Email sent successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CV/showAllCVsBACK.fxml"));
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

    public void showalloffers(MouseEvent event) {
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
}

