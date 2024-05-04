package tasknest.services;
import tasknest.models.Application;
import tasknest.models.offers;
import tasknest.utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class OfferService implements IService<offers>{

    private Connection connection = DataSource.getInstance().getConnection();

    @Override
    public int ajouter(offers offer) {
        String req = "INSERT INTO `offers`(`entreprise_name`, `domain`,`post`, `description`,`localisation`, `period`,`salary`, `user_id`) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, offer.getEntreprise_name());
            pst.setString(2, offer.getDomain());
            pst.setString(3, offer.getPost());
            pst.setString(4, offer.getDescription());
            pst.setString(5, offer.getLocalisation());
            pst.setString(6, offer.getPeriod());
            pst.setFloat(7, offer.getSalary());
            pst.setInt(8, offer.getUser_id());
            pst.executeUpdate();
            System.out.println("offer added successfully !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
return 0;
    }

    @Override
    public void supprimer(offers offer) {
        String req = "DELETE FROM `offers` WHERE `offers`.`id` = ?;";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, offer.getId());
            pst.executeUpdate();
            System.out.println("Offer deleted succussfully !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /*@Override
    public void modifier(offers offer) {
        String req = "UPDATE `offers` SET `entreprise_name` = ?, `domain` = ?,`post` = ?,`description` = ?, `localisation` = ?,`period` = ?, `salary` = ? WHERE `offers`.`id` = ?;";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1,offer.getEntreprise_name());
            pst.setString(2, offer.getDomain());
            pst.setString(3, offer.getPost());
            pst.setString(4, offer.getDescription());
            pst.setString(5, offer.getLocalisation());
            pst.setString(  6, offer.getPeriod());
            pst.setFloat(7, offer.getSalary());
            pst.setInt(8,offer.getId());
            pst.executeUpdate();
            System.out.println("offer updated successfully!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

*/


    @Override
    public void modifier(offers offer) {
        String req = "UPDATE `offers` SET `entreprise_name` = ?, `domain` = ?,`post` = ?,`description` = ?, `localisation` = ?,`period` = ?, `salary` = ? WHERE `offers`.`id` = ?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, offer.getEntreprise_name());
            pst.setString(2, offer.getDomain());
            pst.setString(3, offer.getPost());
            pst.setString(4, offer.getDescription());
            pst.setString(5, offer.getLocalisation());
            pst.setString(6, offer.getPeriod());
            pst.setFloat(7, offer.getSalary());
            pst.setInt(8, offer.getId());
            pst.executeUpdate();
            System.out.println("Offer updated successfully!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<offers> afficher() {
        List<offers> offersList = new ArrayList();

        String req = "SELECT * FROM `offers`";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                offersList.add(new offers(rs.getInt("id"), rs.getString("entreprise_name"), rs.getString("domain"), rs.getString("post"), rs.getString("description"),rs.getString("localisation"),rs.getString("period"),rs.getFloat("salary"),rs.getInt("user_id")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return offersList;
    }

    private List<offers> allOffers = new ArrayList<>();


    public void addOffer(offers offer) {
        allOffers.add(offer);
    }

    // Get all offers
    public List<offers> getAllOffers() {

        return afficher();
    }

   /* private List<offers> offersList = new ArrayList<>();
    public List<offers> getOffersByUserId(int userId) {
        List<offers> userOffers = new ArrayList<>();
        for (offers offer : offersList) {
            if (offer.getUserId() == userId) {
                userOffers.add(offer);
            }
        }
        return userOffers;
    }*/

    public List<offers> afficheruseroffers(int userId) {
        List<offers> userOffers = new ArrayList<>();
        String req = "SELECT * FROM offers WHERE user_id = ?";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setInt(1, userId);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    offers offer = new offers();
                    offer.setId(rs.getInt("id"));
                    offer.setEntreprise_name(rs.getString("entreprise_name"));
                    offer.setDomain(rs.getString("domain"));
                    offer.setDescription(rs.getString("description"));
                    offer.setPost(rs.getString("post"));
                    offer.setPeriod(rs.getString("period"));
                    offer.setLocalisation(rs.getString("localisation"));
                    offer.setUser_id(rs.getInt("user_id"));
                    offer.setSalary(rs.getFloat("salary"));
                    userOffers.add(offer);
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return userOffers;
    }

    public List<Application> getApplicationsForOffer(int offerId) {
        List<Application> applications = new ArrayList<>();
        String query = "SELECT * FROM application a WHERE a.offers_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, offerId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {

                int applicationId = resultSet.getInt("id");
                int user_id = resultSet.getInt("user_id");
                int offer_id = resultSet.getInt("offers_id");
                String cv = resultSet.getString("cv");


                Application application = new Application(applicationId, offer_id, user_id, cv);


                applications.add(application);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return applications;
    }




}


