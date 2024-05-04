package tasknest.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.w3c.dom.Text;
import tasknest.models.complaint;
import tasknest.models.users;
import tasknest.services.UserService;
import tasknest.utils.DataSource;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class DisplayUsers {

    @FXML
    private VBox usersVBox; // VBox to hold the user labels

    @FXML
    private ScrollPane usersScrollPane; // ScrollPane to hold the VBox
    @FXML
    private Label homePageBtn;
    @FXML
    private ImageView profilePicture;
    @FXML
    private Button BlockBtn;
    @FXML
    private Label reclambutton;


    private final UserService userService = new UserService() {
        @Override
        public int ajouter(users users) {
return 0;
        }


        @Override
        public void supprimer(users user) {
            Connection connection;
            PreparedStatement preparedStatement = null;

            try {
                // Establish connection
                connection = DataSource.getInstance().getConnection();

                // SQL query to delete user
                String query = "DELETE FROM user WHERE id = ?";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, user.getId());

                // Execute query
                preparedStatement.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                // Close resources
            }
        }

        @Override
        public void modifier(users users) {

        }

        @Override
        public List<users> afficher() {
            List<users> userList = new ArrayList<>();

            Connection connection = null;
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;

            try {
                // Establish connection
                connection = DataSource.getInstance().getConnection();

                // SQL query to fetch users
                String query = "SELECT * FROM user WHERE roles != 'ROLE_ADMIN'";
                preparedStatement = connection.prepareStatement(query);

                // Execute query
                resultSet = preparedStatement.executeQuery();

                // Process result set
                while (resultSet.next()) {
                    users user = new users();
                    user.setId(resultSet.getInt("id"));
                    user.setFname(resultSet.getString("fname"));
                    user.setLname(resultSet.getString("lname"));
                    user.setEmail(resultSet.getString("email"));
                    user.setRoles(resultSet.getString("roles"));
                    user.setPhonenumber(resultSet.getInt("phonenumber"));
                    user.setBirthdate(resultSet.getDate("birthdate"));
                    user.setBlocked(resultSet.getBoolean("blocked"));

                    userList.add(user);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            return userList;
        }

     /*   @Override
        public List<complaint> afficher1() {
            return null;
        }
*/
        @Override
        public void handleBlockUser(users user) {
            Connection connection;
            PreparedStatement preparedStatement = null;

            try {
                // Establish connection
                connection = DataSource.getInstance().getConnection();

                // SQL query to delete user
                String query = "UPDATE user SET blocked =? WHERE id = ?";
                preparedStatement = connection.prepareStatement(query);
                user.setBlocked(!user.isBlocked());
                preparedStatement.setInt(1, user.isBlocked() ? 1 : 0);
                preparedStatement.setInt(2, user.getId());

                // Execute query
                preparedStatement.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


    };

    // Method to initialize the controller
    @FXML
    public void initialize() {
        // Fetch the list of users from your database
        List<users> userList = userService.afficher();

        // Populate the VBox with user labels
        populateUserLabels(userList);
    }

    @FXML
    void reclamnav(MouseEvent event) {

        try {
            // Load the FXML file
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/admin.fxml"));
            Parent root = fxmlLoader.load();

            // Get the reference to the current scene
            Scene scene = reclambutton.getScene();

            // Set the root of the scene to the content loaded from complaint.fxml
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    // Method to populate the VBox with user labels
    private void populateUserLabels(List<users> userList) {
        // Clear existing children
        usersVBox.getChildren().clear();

        // Add header labels
        HBox headerHBox = createHeaderHBox();
        usersVBox.getChildren().add(headerHBox);

        // Add user labels
        for (users user : userList) {
            HBox userHBox = createUserHBox(user);
            usersVBox.getChildren().add(userHBox);
        }
    }

    // Method to create header HBox
    private HBox createHeaderHBox() {
        // Create header labels
        Label idHeader = createHeaderLabel("ID", 47);
        Label fnameHeader = createHeaderLabel("Fname", 93);
        Label lnameHeader = createHeaderLabel("Lname", 109);
        Label emailHeader = createHeaderLabel("Email", 154);
        Label passwordHeader = createHeaderLabel("Password", 159);
        Label roleHeader = createHeaderLabel("Role", 121);
        Label phonenumberHeader = createHeaderLabel("Phone Num", 86);
        Label birthdateHeader = createHeaderLabel("Birthdate", 90);
        Label blockedHeader = createHeaderLabel("Blocked", 62);
        Label actionsHeader = createHeaderLabel("Actions", 174);

        // Create header HBox
        HBox headerHBox = new HBox();
        headerHBox.getChildren().addAll(idHeader, fnameHeader, lnameHeader, emailHeader, passwordHeader,
                roleHeader, phonenumberHeader, birthdateHeader, blockedHeader, actionsHeader);

        return headerHBox;
    }

    // Method to create header label
    private Label createHeaderLabel(String text, double width) {
        Label label = new Label(text);
        label.setPrefWidth(width);
        label.setStyle("-fx-font: 15.0 Calibri Bold Italic;");
        label.setAlignment(Pos.CENTER);

        return label;
    }

    // Method to create user HBox
    private HBox createUserHBox(users user) {
        // Create labels for user data
        Label idLabel = createUserLabel(String.valueOf(user.getId()), 47);
        Label fnameLabel = createUserLabel(user.getFname(), 93);
        Label lnameLabel = createUserLabel(user.getLname(), 109);
        Label emailLabel = createUserLabel(user.getEmail(), 154);
        Label passwordLabel = createUserLabel("********", 159);
        Label roleLabel = createUserLabel(user.getRoles(), 121);
        Label phonenumberLabel = createUserLabel(String.valueOf(user.getPhonenumber()), 86);
        Label birthdateLabel = createUserLabel(user.getBirthdate().toString(), 90);
        Label blockedLabel = createUserLabel(String.valueOf(user.isBlocked()), 62);

        // Create buttons for actions
        Button deleteButton = createActionButton("DELETE", 76, user);
        Button blockButton = createActionButton("BLOCK", 75, user);

        // Create user HBox
        HBox userHBox = new HBox();
        userHBox.getChildren().addAll(idLabel, fnameLabel, lnameLabel, emailLabel, passwordLabel,
                roleLabel, phonenumberLabel, birthdateLabel, blockedLabel, deleteButton, blockButton);

        return userHBox;
    }

    // Method to create user label
    private Label createUserLabel(String text, double width) {
        Label label = new Label(text);
        label.setPrefWidth(width);
        label.setStyle("-fx-font: 15.0 Calibri Bold Italic;");
        label.setAlignment(Pos.CENTER);

        return label;
    }

    // Method to create action button
    private Button createActionButton(String text, double width, users user) {
        Button button = new Button(text);
        button.setPrefWidth(width);
        button.setStyle("-fx-font: 15.0 Calibri Bold Italic;");
        if (text.equals("DELETE")) {
            button.setOnAction(event -> {
                userService.supprimer(user);
                populateUserLabels(userService.afficher());
            });
        }
        if (text.equals("BLOCK")) {
            button.setOnAction(event -> {
                userService.handleBlockUser(user);
                populateUserLabels(userService.afficher());
            });
        }
        // Implement action event for other buttons (e.g., block user)
        // else if (text.equals("BLOCK")) {
        //     button.setOnAction(event -> handleBlockAction(user));
        // }

        return button;
    }

    // Method to handle block action
    // private void handleBlockAction(users user) {
    //     // Implement block action logic
    // }

    @FXML
    void navigateToHome(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/home.fxml"));
            Parent root = loader.load();

            Scene scene = homePageBtn.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void navigateToProfile(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/profile.fxml"));
            Parent root = loader.load();

            Scene scene = profilePicture.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void allcvsback(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CV/showAllCVsBACK.fxml"));
            Parent root = loader.load();

            Scene scene = profilePicture.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void alloffersback(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/offer/displayofferback.fxml"));
            Parent root = loader.load();

            Scene scene = profilePicture.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
