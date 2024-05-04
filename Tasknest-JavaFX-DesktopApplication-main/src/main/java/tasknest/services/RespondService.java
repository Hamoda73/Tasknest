package tasknest.services;

import tasknest.models.complaint;
import tasknest.models.respond;
import tasknest.utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import tasknest.tests.MainFx;
public class RespondService implements IService<respond>{

    private Connection connection = DataSource.getInstance().getConnection();

    @Override
    public int ajouter(respond respond) {
        String req = "INSERT INTO `respond`(`complaint_id`, `message`) VALUES ('" + respond.getComplaint_id() + "', '" + respond.getMessage() + "');";
        try {
            Statement st = connection.createStatement();
            st.executeUpdate(req);
            System.out.println("Reponse ajouté avec succes !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public String getEmailOfComplaintSender(int complaintId) {
        String userEmail = null;
        String query = "SELECT u.email " +
                "FROM user u " +
                "JOIN complaint c ON u.id = c.user_id " +
                "WHERE c.id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, complaintId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                userEmail = resultSet.getString("email");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userEmail;
    }


    @Override
    public void supprimer(respond respond) {
        String req = "DELETE FROM `respond` WHERE `respond`.`id` = ?;";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, respond.getId());
            pst.executeUpdate();
            System.out.println("Reponse supprimé avec succes !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    @Override
    public void modifier(respond respond) {
        if (respond == null) {
            System.out.println("Error: respond object is null.");
            return;
        }

        String req = "UPDATE `respond` " +
                "SET `message` = ? " +
                "WHERE `id` = ?";

        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, respond.getMessage());
            pst.setInt(2, respond.getId());
            pst.executeUpdate();
            System.out.println("Response updated successfully!");
        } catch (SQLException e) {
            System.out.println("Error updating response: " + e.getMessage());
        }
    }








    @Override
    public List<respond> afficher() {
        System.out.println("hi");
        List<respond> respond = null;

        return respond;
    }
  /*  @Override
    public List<respond> afficher() {
        List<respond> responds = new ArrayList();

        String req = "SELECT * FROM `respond`";
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {
                responds.add(new respond(rs.getInt("id"), rs.getInt("complaint_id"), rs.getString("message")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return responds;
    }

   */

  /*@Override
    public List<complaint> afficher1() {
        List<complaint> complaints = new ArrayList<>();

        String req = "SELECT c.*, r.message AS response_message " +
                "FROM complaint c LEFT JOIN respond r ON c.id = r.complaint_id";
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {
                complaint c = new complaint(rs.getInt("id"), rs.getString("type"), rs.getString("message"));
                String responseMessage = rs.getString("response_message");
                c.setResponseMessage(responseMessage);
                complaints.add(c);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return complaints;
    }
*/

    public void sendEmail(String to, String subject, String body) {
        // Set mail server properties

        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587"); // Use port 587 for TLS
        properties.put("mail.smtp.auth", "true"); // Enable authentication
        properties.put("mail.smtp.starttls.enable", "true"); // Enable TLS

        // Authenticate sender
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("mohamedprojectqt@gmail.com", "fszdoigigmyjamjn");
            }
        };

        // Create session
        Session session = Session.getInstance(properties, authenticator);

        try {
            // Create email message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("mohamedprojectqt@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);

            // Send email
            Transport.send(message);

            System.out.println("Email sent successfully!");
        } catch (MessagingException e) {
            System.out.println("Failed to send email. Error: " + e.getMessage());
            e.printStackTrace();
        }
    }





}
