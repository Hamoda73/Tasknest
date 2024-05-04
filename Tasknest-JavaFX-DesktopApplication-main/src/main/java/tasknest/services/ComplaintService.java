package tasknest.services;

import tasknest.models.complaint;
import tasknest.models.respond;
import tasknest.utils.DataSource;
import tasknest.services.IService;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ComplaintService implements IService<complaint>{

    private Connection connection = DataSource.getInstance().getConnection();

    @Override
    public int ajouter(complaint complaint) {
        String req = "INSERT INTO `complaint`(`user_id`, `type`, `message`) VALUES ('" + complaint.getUser_id() + "', '" + complaint.getType() + "', '" + complaint.getMessage() + "');";
        try {
            Statement st = connection.createStatement();
            st.executeUpdate(req);
            System.out.println("Complaint ajouté avec succes !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public List<complaint> getComplaintsByUserId(int userId) {
        List<complaint> userComplaints = new ArrayList<>();
        String query = "SELECT c.id, c.type, c.message, r.message AS response_message " +
                "FROM complaint c LEFT JOIN respond r ON c.id = r.complaint_id " +
                "WHERE c.user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String type = resultSet.getString("type");
                String message = resultSet.getString("message");
                String responseMessage = resultSet.getString("response_message");
                // Create complaint object and add it to the list
                complaint c = new complaint(id, type, message);
                c.setResponseMessage(responseMessage);
                userComplaints.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userComplaints;
    }


    public List<complaint> getComplaintsByType(String type) {
        List<complaint> filteredComplaints = new ArrayList<>();
        String query = "SELECT c.id, c.type, c.message, r.message AS response_message " +
                "FROM complaint c LEFT JOIN respond r ON c.id = r.complaint_id " +
                "WHERE c.type = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, type);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String messageType = resultSet.getString("type");
                String message = resultSet.getString("message");
                String responseMessage = resultSet.getString("response_message");
                // Create complaint object and add it to the list
                complaint c = new complaint(id, messageType, message);
                c.setResponseMessage(responseMessage);
                filteredComplaints.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filteredComplaints;
    }




    public List<complaint> getComplaintsByUserIdAndText(int userId, String searchText) {
        List<complaint> userComplaints = new ArrayList<>();
        String query = "SELECT c.id, c.type, c.message, r.message AS response_message " +
                "FROM complaint c LEFT JOIN respond r ON c.id = r.complaint_id " +
                "WHERE c.user_id = ? AND c.message LIKE ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setString(2, "%" + searchText + "%");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String type = resultSet.getString("type");
                String message = resultSet.getString("message");
                String responseMessage = resultSet.getString("response_message");
                // Create complaint object and add it to the list
                complaint c = new complaint(id, type, message);
                c.setResponseMessage(responseMessage);
                userComplaints.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userComplaints;
    }



    @Override
    public void modifier(complaint complaint) {
        String req = "UPDATE `complaint` SET `type` = '" + complaint.getType() + "', `message` = '" + complaint.getMessage() + "' WHERE `complaint`.`id` = " + complaint.getId() + ";";
        try {
            Statement st = connection.createStatement();
            st.executeUpdate(req);
            System.out.println("Complaint modifié avec succes !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<complaint> afficher() {
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

    /*@Override
    public List<complaint> afficher1() {
        return null;
    }*/
    @Override
    public void supprimer(complaint complaint) {
        String req = "DELETE FROM `complaint` WHERE `complaint`.`id` = ?;";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, complaint.getId());
            pst.executeUpdate();
            System.out.println("Complaint supprimé avec succes !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
