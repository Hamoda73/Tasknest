package tasknest.services;

import tasknest.models.Application;
import tasknest.models.offers;
import tasknest.utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ApplicationService implements IService <Application>{

    private Connection connection = DataSource.getInstance().getConnection();


   /* public void ajouter(Application application) {
        String req = "INSERT INTO `application`(`offers_id`, `user_id`, `cv`) VALUES (?, ?, ?);";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, application.getOffers_id());
            pst.setInt(2, application.getUser_id());
            pst.setString(3, application.getCv());

            pst.executeUpdate();
            System.out.println("application added successfully !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }*/
   @Override
   public int ajouter(Application application) {
       String req = "INSERT INTO `application`(`offers_id`, `user_id`, `cv`) VALUES (?, ?, ?);";
       try {
           PreparedStatement pst = connection.prepareStatement(req, Statement.RETURN_GENERATED_KEYS);
           pst.setInt(1, application.getOffers_id());
           pst.setInt(2, application.getUser_id());
           pst.setString(3, application.getcvImagePath());

           int rowsAffected = pst.executeUpdate();
           if (rowsAffected > 0) {
               ResultSet rs = pst.getGeneratedKeys();
               if (rs.next()) {
                   int generatedId = rs.getInt(1);
                   System.out.println("Application added successfully with ID: " + generatedId);
               }
           } else {
               System.out.println("Failed to add application!");
           }
       } catch (SQLException e) {
           System.out.println("Error inserting application: " + e.getMessage());
       }
       return 0;
   }



    @Override
    public void supprimer(Application application) {
        String req = "DELETE FROM `application` WHERE `application`.`id` = ?;";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, application.getId());
            pst.executeUpdate();
            System.out.println("application deleted succussfully !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void modifier(Application application) {
        String req = "UPDATE `Application` SET `offers_id` = ?, `user_id` = ?,`cv` = ?  WHERE `application`.`id` = ?;";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, application.getOffers_id());
            pst.setInt(2, application.getUser_id());
            pst.setString(3, application.getcvImagePath());
            pst.setInt(4, application.getId());
            pst.executeUpdate();
            System.out.println("app updated successfully !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Application> afficher() {
        List<Application> offersList = new ArrayList();

        String req = "SELECT * FROM `application`";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                offersList.add(new Application(rs.getInt("id"),rs.getInt("offers_id"),rs.getInt("user_id"),rs.getString("cv")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return offersList;
    }

    public boolean applicationExists(int userId, int offerId) {
        String req = "SELECT COUNT(*) FROM application WHERE user_id = ? AND offers_id = ?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, userId);
            pst.setInt(2, offerId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public List<Application> getApplicationsByUserId(int userId) {
        List<Application> applications = new ArrayList<>();
        String query = "SELECT * FROM `application` WHERE `user_id` = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                applications.add(new Application(
                        resultSet.getInt("id"),
                        resultSet.getInt("offers_id"),
                        resultSet.getInt("user_id"),
                        resultSet.getString("cv")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving applications: " + e.getMessage());
        }
        return applications;
    }

    public offers getOfferById(int offerId) {
        offers off=new offers();
        String query = "SELECT * FROM `offers` WHERE `id` = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, offerId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                off = new offers(
                        resultSet.getInt("id"),
                        resultSet.getString("entreprise_name"),
                        resultSet.getString("domain"),
                        resultSet.getString("post"),
                        resultSet.getString("description"),
                        resultSet.getString("localisation"),
                        resultSet.getString("period"),
                        resultSet.getFloat("salary"),
                        resultSet.getInt("user_id")

                        );
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving offer: " + e.getMessage());
        }
        return off;
    }

}
