package tasknest.controllers.offer;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.Window;
import tasknest.controllers.applications.Apply;
import tasknest.models.offers;
import tasknest.services.OfferService;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class DisplayAllOffersEntreprise implements Initializable {

    private ObservableList<offers> allOffersList = FXCollections.observableArrayList();
    @FXML
    ScrollPane offersScrollPane;
    @FXML
    private TextField searchField;
    private OfferService offerService;

    private FilteredList<offers> filteredOffersList;
    private SortedList<offers> sortedOffersList;
    private Comparator<offers> currentComparator;


    @FXML
    private Pagination pagination;

    @FXML
    private ChoiceBox<String> sortingChoiceBox;
    private static final int ITEMS_PER_PAGE = 3;

    @FXML
    private VBox cardsContainer;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        offerService = new OfferService();

        allOffersList.addAll(offerService.getAllOffers());

        filteredOffersList = new FilteredList<>(allOffersList);
        sortedOffersList = new SortedList<>(filteredOffersList);
        currentComparator = Comparator.comparingDouble(offers::getSalary).reversed(); // Initial sorting by salary

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterOffers(newValue);
        });

        sortedOffersList.comparatorProperty().addListener((observable, oldValue, newValue) -> {
            populateOffers();
        });

        setupPagination();

    }

    private void setupPagination() {
        int pageCount = (int) Math.ceil((double) filteredOffersList.size() / ITEMS_PER_PAGE);
        pagination = new Pagination(pageCount, 0);
        pagination.setPageFactory(this::createPage);
        offersScrollPane.setContent(pagination);
    }


    /*  private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * ITEMS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ITEMS_PER_PAGE, allOffersList.size());
        List<offers> currentPageOffers = allOffersList.subList(fromIndex, toIndex);

        VBox pageContent = new VBox(10);
        for (offers offer : currentPageOffers) {
            AnchorPane card = createOfferCard(offer);
            pageContent.getChildren().add(card);
        }
        return pageContent;
    }
*/
    private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * ITEMS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ITEMS_PER_PAGE, filteredOffersList.size());
        List<offers> currentPageOffers = filteredOffersList.subList(fromIndex, toIndex);

        VBox pageContent = new VBox(10);
        for (offers offer : currentPageOffers) {
            AnchorPane card = createOfferCard(offer);
            pageContent.getChildren().add(card);
        }
        return pageContent;
    }


  /*  private void filterOffers(String query) {
        Predicate<offers> containsQuery = offer ->
                offer.getEntreprise_name().toLowerCase().contains(query.toLowerCase()) ||
                        offer.getDomain().toLowerCase().contains(query.toLowerCase()) ||
                        offer.getPost().toLowerCase().contains(query.toLowerCase()) ||
                        offer.getDescription().toLowerCase().contains(query.toLowerCase()) ||
                        offer.getLocalisation().toLowerCase().contains(query.toLowerCase()) ||
                        offer.getPeriod().toLowerCase().contains(query.toLowerCase()) ||
                        String.valueOf(offer.getSalary()).toLowerCase().contains(query.toLowerCase());

        FilteredList<offers> filteredList = allOffersList.filtered(containsQuery);

        VBox offersContainer = new VBox(10);

        for (offers offer : filteredList) {
            AnchorPane card = createOfferCard(offer);
            offersContainer.getChildren().add(card);
        }

        offersScrollPane.setContent(offersContainer);
    }*/




    private void filterOffers(String query) {
        Predicate<offers> containsQuery = offer ->
                offer.getEntreprise_name().toLowerCase().contains(query.toLowerCase()) ||
                        offer.getDomain().toLowerCase().contains(query.toLowerCase()) ||
                        offer.getPost().toLowerCase().contains(query.toLowerCase()) ||
                        offer.getDescription().toLowerCase().contains(query.toLowerCase()) ||
                        offer.getLocalisation().toLowerCase().contains(query.toLowerCase()) ||
                        offer.getPeriod().toLowerCase().contains(query.toLowerCase()) ||
                        String.valueOf(offer.getSalary()).toLowerCase().contains(query.toLowerCase());

        FilteredList<offers> filteredList = allOffersList.filtered(containsQuery);

        VBox offersContainer = new VBox(10);

        for (offers offer : filteredList) {
            AnchorPane card = createOfferCard(offer);
            offersContainer.getChildren().add(card);
        }

        offersScrollPane.setContent(offersContainer);
    }






    private void populateOffers() {
        sortedOffersList.setComparator(currentComparator);

        VBox offersContainer = new VBox(10);

        for (offers offer : sortedOffersList) {
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
        //  descriptionLabel.setMaxWidth(300); // Set the maximum width for wrapping
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





      /*  Image gifImage = new Image(getClass().getResourceAsStream("/images/job.gif"));
        ImageView gifImageView = new ImageView(gifImage);


        Platform.runLater(() -> {
            gifImageView.setLayoutX(card.getWidth() - gifImage.getWidth());
            gifImageView.setLayoutY(15);
            gifImageView.setLayoutX(1000);
            gifImageView.setFitWidth(200);
            gifImageView.setFitHeight(200);



        });
*/



        List<tasknest.models.Application> applications = offerService.getApplicationsForOffer(offer.getId());

        // Calculate the number of applications
        int numApplications = applications.size();

        // Create a circle to display the number of applications
        Circle circle = new Circle(15);

        circle.setStrokeWidth(2);

        // Create a label to display the number of applications
        Label numAppsLabel = new Label(String.valueOf(numApplications));




        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(circle, numAppsLabel);
        stackPane.setLayoutX(1150);
        stackPane.setLayoutY(10);
        circle.setFill(Color.YELLOW);
        circle.setStrokeWidth(2);

        // Add the stack pane to the card
        card.getChildren().add(stackPane);


        card.getChildren().addAll(imageView,namePrefixLabel ,nameLabel, DomainPrefixLabel,DomainLabel,postPrefixLabel, postLabel,descriptionPrefixLabel, descriptionLabel, localisationPrefixLabel,localisationLabel,periodPrefixLabel, periodValueLabel,salaryPrefixLabel, salaryLabel/*,gifImageView*/);

        return card;
    }



    private void redirectToApply(MouseEvent event, int offerId) {
        try {

            Window window = ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Applications/Apply.fxml"));
            Parent root = loader.load();

            // pass the user and offer idss to the Apply controller
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




    @FXML
    private void navigateToDisplayAllOffers() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/offer/DisplayAllOffersEntreprise.fxml"));
            Parent root = loader.load();
            offersScrollPane.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sortBySalary() {
        currentComparator = Comparator.comparingDouble(offers::getSalary).reversed();
        sortedOffersList.setComparator(currentComparator);
    }

    private void sortByEntrepriseName() {
        currentComparator = Comparator.comparing(offers::getEntreprise_name);
        sortedOffersList.setComparator(currentComparator);
    }



    @FXML
    private void handleSortingSelection(ActionEvent event) {
        String selectedSort = sortingChoiceBox.getValue();
        if (selectedSort != null) {
            if (selectedSort.equals("salary")) {

                sortBySalary();
            } else if (selectedSort.equals("entreprise")) {

                sortByEntrepriseName();
            } else if (selectedSort.equals("applications")) {

                sortByNumApplications();
            }
        }
    }

    private void sortByNumApplications() {
        currentComparator = Comparator.comparingInt(offer -> offerService.getApplicationsForOffer(offer.getId()).size());
        sortedOffersList.setComparator(currentComparator);
    }

    private int getNumApplications(offers offer) {
        return offerService.getApplicationsForOffer(offer.getId()).size();
    }
    @FXML
    private void redirectFreelancers() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CV/showAllCVSEntreprise.fxml"));
            Parent root = loader.load();
            offersScrollPane.getScene().setRoot(root);
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
