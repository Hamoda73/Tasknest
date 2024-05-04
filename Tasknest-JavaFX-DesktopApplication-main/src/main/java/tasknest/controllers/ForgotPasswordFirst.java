package tasknest.controllers;

import javafx.scene.control.Alert;
import tasknest.tests.MainFx;
import tasknest.services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import tasknest.utils.DataSource;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Random;
import java.util.ResourceBundle;

public class ForgotPasswordFirst implements Initializable{
    static String email;
    PreparedStatement preparedStatement;
    private Connection connection = DataSource.getInstance().getConnection();

    @FXML
    public TextField inputEmail;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccessAlert(String text) {
        Alert confirmation = new Alert(Alert.AlertType.INFORMATION);
        confirmation.setTitle("Success");
        confirmation.setHeaderText(null);
        confirmation.setContentText(text);
        confirmation.showAndWait();
    }
    public boolean checkExist(String email) {
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM `user` WHERE `email` = ?");

            preparedStatement.setString(1, email);

            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();

        } catch (SQLException exception) {
            System.out.println("Error (checkExist) : " + exception.getMessage());
        }
        return false;
    }
    @FXML
    public void backToLogin(ActionEvent actionEvent) {
        MainFx.getInstance().loadLogin();
    }

    @FXML
    public void next(ActionEvent actionEvent) {
        email = inputEmail.getText();

        if (email.isEmpty()) {
            showAlert("ERROR", "Empty Field!");
        } else {
            if (checkExist(email)) {
                ForgotPasswordSecond.resetCode = new Random().nextInt((9999 - 1000) + 1) + 1000;
                System.out.println("Reset code : " + ForgotPasswordSecond.resetCode);

                try {
                    sendMail(email);
                    showSuccessAlert("Un code de confirmation a été envoyé a votre email");
                } catch (Exception e) {
                    showAlert("ERROR", e.getMessage());
                    e.printStackTrace();
                }

                MainFx.getInstance().loadForgotPasswordSecond();
            } else {
                showAlert("ERROR", "Invalid Email!");
            }
        }
    }

    public static void sendMail(String recipient) throws Exception {
        System.out.println("Preparing to send email");
        Properties properties = new Properties();

        // Enable authentication
        properties.put("mail.smtp.auth", "true");
        // Set TLS encryption enabled
        properties.put("mail.smtp.starttls.enable", "true");
        // Set SMTP host
        properties.put("mail.smtp.host", "smtp.gmail.com");
        // Set smtp port
        properties.put("mail.smtp.port", "587");

        // Your gmail address
        String myAccountEmail = "elitecars.app@gmail.com";
        // Your gmail password
        String password = "ctztijcbxtzmyxav";

        // Create a session with account credentials
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(myAccountEmail, password);
            }
        });

        // Prepare email message
        Message message = prepareMessage(session, myAccountEmail, recipient);

        // Send mail
        if (message != null) {
            Transport.send(message);
            System.out.println("Mail sent successfully");
        } else {
            System.out.println("Error sending the mail");
        }
    }

    private static Message prepareMessage(Session session, String myAccountEmail, String recepient) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myAccountEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recepient));
            message.setSubject("Réinitialisation de mot de passe");
            String htmlCode = "<h1>Notification</h1> <br/> " +
                    "Your code is : " +
                    "<h2><b>" + ForgotPasswordSecond.resetCode + "</b></h2>";
            message.setContent(htmlCode, "text/html");
            return message;
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
