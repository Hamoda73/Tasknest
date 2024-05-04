package tasknest.tests;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import tasknest.models.users;

import java.io.IOException;
import java.util.Objects;

public class MainFx extends Application {

    public static Stage mainStage;
    private static MainFx instance;
    private static users session;
    @Override
    public void start(Stage primaryStage) throws Exception {
        mainStage = primaryStage;

        Parent root = FXMLLoader.load(getClass().getResource("/login.fxml"));
        Scene scene = new Scene(root);
        mainStage.setScene(scene);
        mainStage.setTitle("Sign up");
        mainStage.show();
        mainStage.setMaximized(true);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static MainFx getInstance() {
        if (instance == null) {
            instance = new MainFx();
        }
        return instance;
    }

    public static users getSession() {
        return session;
    }

    public static void setSession(users session) {
        MainFx.session = session;
    }

    public void login(users user) {
        MainFx.setSession(user);

        if (user.getRoles().equals("[\"ROLE_ADMIN\"]")) {
            loadBack();
        }
            //loadFront();
           else if (user.getRoles().equals("[\"ROLE_FREELANCER\"]")) {
                loadFrontFreelancer();
            }
            if (user.getRoles().equals("[\"ROLE_COMPANY\"]")) {
                loadFrontEntreprise();
            }


    }

    public void loadFront() {
        loadScene(
                "/profile.fxml",
                "",
                1100,
                700,
                false
        );
    }

    public void loadFrontFreelancer() {
        loadScene(
                "/offer/DisplayAllOffers.fxml",
                "",
                1100,
                700,
                false
        );
    }

    public void loadFrontEntreprise() {
        loadScene(
                "/offer/DisplayAllOffersEntreprise.fxml",
                "",
                1100,
                700,
                false
        );

    }

    public void loadBack() {
        loadScene(
                "/diaplayUsers.fxml",
                "",
                1100,
                700,
                false
        );
        mainStage.setMaximized(true);
    }
    public void logout() {
        session = null;

        System.out.println("Deconnexion ..");
        loadLogin();
    }

    public void loadLogin() {
        loadScene(
                "/login.fxml",
                "Connexion",
                1100,
                700,
                true
        );
    }

    public void loadForgotPasswordFirst() {
        loadScene(
                "/forgotPasswordFirst.fxml",
                "Connexion",
                1100,
                700,
                true
        );
    }

    public void loadForgotPasswordSecond() {
        loadScene(
                "/forgotPasswordSecond.fxml",
                "Connexion",
                1100,
                700,
                true
        );
    }

    public void loadForgotPasswordThird() {
        loadScene(
                "/forgotPasswordThird.fxml",
                "Connexion",
                1100,
                700,
                true
        );
    }

    private void loadScene(String fxmlLink, String title, int width, int height, boolean isAuthentification) {
        try {
            Stage primaryStage = this.mainStage; // Create a new stage

            Scene scene = new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlLink))));
            scene.setFill(Color.TRANSPARENT);

            primaryStage.setTitle(title);
            primaryStage.setWidth(width);
            primaryStage.setHeight(height);
            primaryStage.setMinWidth(width);
            primaryStage.setMinHeight(height);
            primaryStage.setScene(scene);
            primaryStage.setX((Screen.getPrimary().getBounds().getWidth() / 2) - (width / 2.0));
            primaryStage.setY((Screen.getPrimary().getBounds().getHeight() / 2) - (height / 2.0));

            primaryStage.setMaximized(true); // Set the stage to maximized

            primaryStage.show();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }



}
