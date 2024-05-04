package dashboard.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;
import tasknest.controllers.applications.Apply;
import tasknest.controllers.applications.OfferApps;
import tasknest.models.offers;
import tasknest.models.users;
import tasknest.services.OfferService;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
public class displayofferback implements Initializable {

    private ObservableList<offers> allOffersList = FXCollections.observableArrayList();
    @FXML
    ScrollPane offersScrollPane;

    private OfferService offerService;
    @FXML
   // private Pagination pagination;
    private static final int ITEMS_PER_PAGE = 3;
    @FXML
    private TextField searchField;

    @FXML
    private PieChart domainPieChart;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        offerService = new OfferService();
        allOffersList.addAll(offerService.getAllOffers());
        populateOffers(allOffersList);

        // Listen for changes in the search field and filter offers accordingly
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterOffers(newValue);
        });


        allOffersList.addListener((ListChangeListener.Change<? extends offers> change) -> updateDomainPieChart());



        updateDomainPieChart();
    }

    /*private void setupPagination() {
        List<offers> allOffers = offerService.getAllOffers();
        int pageCount = (int) Math.ceil((double) allOffers.size() / ITEMS_PER_PAGE);
        pagination.setPageCount(pageCount);
        pagination.setCurrentPageIndex(0);
        pagination.setPageFactory(this::createPage);
    }*/


   /* private Node createPage(int pageIndex) {
        List<offers> allOffers = offerService.getAllOffers();
        int fromIndex = pageIndex * ITEMS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ITEMS_PER_PAGE, allOffers.size());
        List<offers> currentPageOffers = allOffers.subList(fromIndex, toIndex);

        VBox pageContent = new VBox(10);
        for (offers offer : currentPageOffers) {
            AnchorPane card = createOfferCard(offer);
            pageContent.getChildren().add(card);
        }
        return pageContent;
    }/*

    */
    /*private void populateOffers() {
        List<offers> allOffers = offerService.getAllOffers();

        VBox offersContainer = new VBox(10);

        for (offers offer : allOffers) {
            AnchorPane card = createOfferCard(offer);
            offersContainer.getChildren().add(card);
        }

        offersScrollPane.setContent(offersContainer);
    }*/

   /* private void populateOffers() {
        List<offers> allOffers = offerService.getAllOffers();
        VBox offersContainer = new VBox(10);
        for (offers offer : allOffers) {
            AnchorPane card = createOfferCard(offer);
            offersContainer.getChildren().add(card);
        }
        offersScrollPane.setContent(offersContainer);
    }*/

    private void populateOffers(List<offers> offersList) {
        VBox offersContainer = new VBox(10);
        for (offers offer : offersList) {
            AnchorPane card = createOfferCard(offer);
            offersContainer.getChildren().add(card);
        }
        offersScrollPane.setContent(offersContainer);
    }




    private AnchorPane createOfferCard(offers offer) {

        AnchorPane card = new AnchorPane();
        card.getStyleClass().add("offer-card");

        ImageView imageView = new ImageView();
        String imageUrl = getClass().getResource("/images/taswira.png").toExternalForm();
        Image image = new Image(imageUrl);

        imageView.setImage(image);
        imageView.getStyleClass().add("image-view");

        /*Label nameLabel = new Label("Company  : " + offer.getEntreprise_name());
        nameLabel.setLayoutX(300);
        nameLabel.setLayoutY(10);*/

        Label namePrefixLabel = new Label("Company: ");
        namePrefixLabel.setLayoutX(300);
        namePrefixLabel.setLayoutY(10);
        namePrefixLabel.setStyle(" -fx-text-fill: #ea8d22;");

        Label nameLabel = new Label(offer.getEntreprise_name());
        nameLabel.setLayoutX(420);
        nameLabel.setLayoutY(10);


        Label DomainPrefixLabel = new Label("Domain: ");
        DomainPrefixLabel.setLayoutX(300);
        DomainPrefixLabel.setLayoutY(50);
        DomainPrefixLabel.setStyle(" -fx-text-fill: #892193FF;");

        Label DomainLabel = new Label(offer.getDomain());
        DomainLabel.setLayoutX(420);
        DomainLabel.setLayoutY(50);


        //newwwwwwwwwww
        // Inside your createOfferCard method:

        Label postPrefixLabel = new Label("Post: ");
        postPrefixLabel.setLayoutX(300);
        postPrefixLabel.setLayoutY(90);
        postPrefixLabel.setStyle(" -fx-text-fill: #892193FF;");

        Label postLabel = new Label(offer.getPost());
        postLabel.setLayoutX(420);
        postLabel.setLayoutY(90);

        Label descriptionPrefixLabel = new Label("Description: ");
        descriptionPrefixLabel.setLayoutX(300);
        descriptionPrefixLabel.setLayoutY(120);
        descriptionPrefixLabel.setStyle(" -fx-text-fill: #892193FF;");


        Label descriptionLabel = new Label(offer.getDescription());
        descriptionLabel.setLayoutX(420);
        descriptionLabel.setLayoutY(120);
       // descriptionLabel.setMaxWidth(300); // Set the maximum width for wrapping
        //descriptionLabel.setWrapText(true);



        Label localisationPrefixLabel = new Label("Location: ");
        localisationPrefixLabel.setLayoutX(300);
        localisationPrefixLabel.setLayoutY(160);
        localisationPrefixLabel.setStyle(" -fx-text-fill: #892193FF;");

        Label localisationLabel = new Label(offer.getLocalisation());
        localisationLabel.setLayoutX(420);
        localisationLabel.setLayoutY(160);







        Label periodPrefixLabel = new Label("Period: ");
        periodPrefixLabel.setLayoutX(300);
        periodPrefixLabel.setLayoutY(200);
        periodPrefixLabel.setStyle(" -fx-text-fill: #892193FF;");

        Label periodValueLabel = new Label(offer.getPeriod());
        periodValueLabel.setLayoutX(420);
        periodValueLabel.setLayoutY(200);
// Add both labels to your layout

        Label salaryPrefixLabel = new Label("Salary: ");
        salaryPrefixLabel.setLayoutX(300);
        salaryPrefixLabel.setLayoutY(240);
        salaryPrefixLabel.setStyle("-fx-text-fill: #892193FF;");

// Convert float salary to string
        String salaryText = String.valueOf(offer.getSalary());
        Label salaryLabel = new Label(salaryText);
        salaryLabel.setLayoutX(420);
        salaryLabel.setLayoutY(240);




        Button appsButton = new Button("See Applications");
        appsButton.setLayoutX(600);
        appsButton.setLayoutY(280);
        appsButton.getStyleClass().add("apply-button");
        appsButton.setOnAction(event->  {
            afficherApps(offer);

        });



        Button deleteButton = new Button("Delete");
        deleteButton.setLayoutX(460);
        deleteButton.setLayoutY(280);
      deleteButton.getStyleClass().add("delete-button");
        deleteButton.setOnAction(event->  {

            offerService.supprimer(offer);
         /*   String to = offer.getUser_id().getEmail();

            String subject = "Offer Deleted Notification";
            String body = "Dear " + offer.getUser_id().getFname() + ",\n\n"
                    + "Your  offer has been deleted.\n\n"
                    + "Regards.";

            sendDeleteNotificationEmail(to);*/

            initialize( null, null);
        });



        /*Image gifImage = new Image(getClass().getResourceAsStream("/images/letter.gif"));
        ImageView gifImageView = new ImageView(gifImage);


        Platform.runLater(() -> {
            gifImageView.setLayoutX(card.getWidth() - gifImage.getWidth());
            gifImageView.setLayoutY(5);
            gifImageView.setLayoutX(700);
            gifImageView.setFitWidth(70);
            gifImageView.setFitHeight(70);



        });*/





        card.getChildren().addAll(imageView,namePrefixLabel ,nameLabel, DomainPrefixLabel,DomainLabel,postPrefixLabel, postLabel,descriptionPrefixLabel, descriptionLabel, localisationPrefixLabel,localisationLabel,periodPrefixLabel, periodValueLabel,salaryPrefixLabel, salaryLabel, appsButton/*,gifImageView*/,deleteButton);

        return card;
    }


    private void filterOffers(String query) {
        // Create a predicate to filter offers based on the search query
        Predicate<offers> containsQuery = offer ->
                offer.getEntreprise_name().toLowerCase().contains(query.toLowerCase()) ||
                        offer.getDomain().toLowerCase().contains(query.toLowerCase()) ||
                        offer.getPost().toLowerCase().contains(query.toLowerCase()) ||
                        offer.getDescription().toLowerCase().contains(query.toLowerCase()) ||
                        offer.getLocalisation().toLowerCase().contains(query.toLowerCase()) ||
                        offer.getPeriod().toLowerCase().contains(query.toLowerCase()) ||
                        String.valueOf(offer.getSalary()).toLowerCase().contains(query.toLowerCase());

        // Filter the offers list based on the predicate
        FilteredList<offers> filteredList = allOffersList.filtered(containsQuery);

        // Update the display with the filtered offers
        populateOffers(filteredList);
    }



   /* private void afficherApps(offers offer) {
        System.out.println("heyyy= "+ offer);
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Applications/OfferAppsback.fxml"));
            Parent root = loader.load();
            OfferApps controller = loader.getController();

            System.out.println("heyyy= "+ offer);
            controller.setappsOFF(offer);
            offersScrollPane.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
*/


    private void afficherApps(offers offer) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Applications/offerAppsback.fxml"));
            Parent root = loader.load();
            displayappsback applyController = loader.getController();
           applyController.setappsOFF(offer);
            System.out.println("huuuu= "+offer);
            offersScrollPane.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void redirectToApply(MouseEvent event, int offerId) {
        try {

            Window window = ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Applications/Apply.fxml"));
            Parent root = loader.load();

            // Pass the user and offer IDs to the Apply controller
            Apply applyController = loader.getController();
            applyController.setUserAndOfferIds(offerId);

            // Show the Apply view
            Stage stage = (Stage) window;
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void navigateToAddOffer() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/offer/addOffer.fxml"));
            Parent root = loader.load();
            offersScrollPane.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


   @FXML
    private void navigateToDisplayuseroff() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/offer/Displayoffuser.fxml"));
            Parent root = loader.load();
            offersScrollPane.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateDomainPieChart() {
        // Calculate domain counts from the list of offers
        Map<String, Integer> domainCounts = new HashMap<>();

        for (offers offer : allOffersList) {
            String domain = offer.getDomain();
            domainCounts.put(domain, domainCounts.getOrDefault(domain, 0) + 1);
        }

        // Create an observable list of PieChart.Data
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        for (Map.Entry<String, Integer> entry : domainCounts.entrySet()) {
            pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }

        // Set the data for the pie chart
        domainPieChart.setData(pieChartData);
        domainPieChart.setPrefSize(500, 500);
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
}

