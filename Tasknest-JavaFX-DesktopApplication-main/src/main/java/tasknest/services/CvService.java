package tasknest.services;

import tasknest.models.CV;
import tasknest.models.Skill;
import tasknest.utils.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
public class CvService implements IService<CV>{
    private Connection connection = DataSource.getInstance().getConnection();
    @Override
    public int ajouter(CV cv) {
        String req = "INSERT INTO `cv`(`bio`, `description`, `language`, `location`, `certification`, `contact`, `user_id`) VALUES (?, ?, ?, ?, ?, ?, ?);";
        int generatedId = -1; // Initialize generatedId to -1 indicating failure
        try {
            PreparedStatement cvst = connection.prepareStatement(req, Statement.RETURN_GENERATED_KEYS);
            cvst.setString(1, cv.getBio());
            cvst.setString(2, cv.getDescription());
        /* Convert the language array from your CV object into a JSON string using Jackson's ObjectMapper (class) from "Jackson library",
           and then use this JSON string to insert the language data into your database as a JSON object. */
            ObjectMapper objectMapper = new ObjectMapper();
            String languageJson = objectMapper.writeValueAsString(cv.getLanguage()); // Convert array to JSON string
            cvst.setString(3, languageJson); // Set as JSON string
            cvst.setString(4, cv.getLocation());
            cvst.setString(5, cv.getCertification());
            cvst.setString(6, cv.getContact());
            cvst.setInt(7, cv.getUser_id());
            cvst.executeUpdate();

            // Retrieve the generated ID
            ResultSet rs = cvst.getGeneratedKeys();
            if (rs.next()) {
               generatedId = rs.getInt(1); // Get the generated ID
            }

            System.out.println("CV ajouté avec succès !");
        } catch (SQLException | JsonProcessingException e) {
            System.out.println(e.getMessage());
        }
       return generatedId; // Return the generated ID
    }

    @Override
    public void supprimer(CV cv) {
        String req = "DELETE FROM `cv` WHERE `cv`.`id` = ?;";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, cv.getId());
            pst.executeUpdate();
            System.out.println("CV supprimer avec succes !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void modifier(CV cv) {
        String req = "UPDATE `cv` SET `bio` = ?, `description` = ?, `language` = ?, `location` = ?, `certification` = ?, `contact` = ? WHERE `cv`.`id` = ?;";
        try {
            PreparedStatement cvst = connection.prepareStatement(req);
            cvst.setString(1, cv.getBio());
            cvst.setString(2, cv.getDescription());
            /*convert the language array from your CV object into a JSON string using Jackson's ObjectMapper (class) from "Jackson library",
             and then use this JSON string to insert the language data into your database as a JSON object.*/
            ObjectMapper objectMapper = new ObjectMapper();
            String languageJson = objectMapper.writeValueAsString(cv.getLanguage());// Convert array to JSON string
            cvst.setString(3, languageJson); // Set as JSON string
            cvst.setString(4, cv.getLocation());
            cvst.setString(5, cv.getCertification());
            cvst.setString(6, cv.getContact());
            cvst.setInt(7, cv.getId());
            cvst.executeUpdate();
            System.out.println("CV modifiée avec succes !");
        }catch (SQLException | JsonProcessingException e) {
            System.out.println(e.getMessage());
        }
    }
    @Override
    public List<CV> afficher() {
        List<CV> cvs = new ArrayList<>();

        String req = "SELECT * FROM `cv`";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                // Split the 'language' string into an array using a delimiter (e.g., comma)
                String[] languages = rs.getString("language").split(",");
                // Create a new CV object with the split 'languages' array
                cvs.add(new CV(
                        rs.getInt("id"),
                        rs.getString("bio"),
                        rs.getString("description"),
                        languages, // Pass the 'languages' array
                        rs.getString("location"),
                        rs.getString("certification"),
                        rs.getString("contact"),
                        rs.getInt("user_id")
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return cvs;
    }


    public String fetchUserNameById(int userId) throws SQLException {
        String req = "SELECT lname FROM `user` WHERE `id` = ?";
        String lastName = null; // Initialize lastName variable
        try {
            PreparedStatement cvst = connection.prepareStatement(req);
            cvst.setInt(1, userId);
            ResultSet resultSet = cvst.executeQuery(); // Execute the query
            if (resultSet.next()) { // Check if there is a result
                lastName = resultSet.getString("lname"); // Get the last name from the result set
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return lastName; // Return the last name
    }

    public boolean userExistsInCVTable(int userId) throws SQLException {
        String query = "SELECT COUNT(*) AS count FROM CV WHERE user_id = ?";
        try (
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt("count");
                    return count > 0;
                }
            }
        }
        return false;
    }


    public CV getCVById(int cvId) {
        try {
            String query = "SELECT * FROM `cv` WHERE `id` = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, cvId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String[] languages = resultSet.getString("language").split(",");
                return new CV(
                        resultSet.getInt("id"),
                        resultSet.getString("bio"),
                        resultSet.getString("description"),
                        languages,
                        resultSet.getString("location"),
                        resultSet.getString("certification"),
                        resultSet.getString("contact"),
                        resultSet.getInt("user_id")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Skill> getSkillsForCV(int cvId) {
        List<Skill> skills = new ArrayList<>();
        try {
            String query = "SELECT * FROM `skill` WHERE `cv_id` = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, cvId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int skill_value = resultSet.getInt("skill_value");
                int cv_id = resultSet.getInt("cv_id");
                Skill skill = new Skill(name,skill_value,cv_id);
                skill.setId(id); // Set the id field of the Skill object
                skills.add(skill);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return skills;
    }


   public String getUserImageForCV(int cvId) {
       String sql = "SELECT u.image FROM `user` u JOIN `cv` ON u.`id` = cv.`user_id` WHERE cv.`id` = ?";
       try  {
           PreparedStatement stmt = connection.prepareStatement(sql);
           stmt.setInt(1, cvId);
           try (ResultSet rs = stmt.executeQuery()) {
               if (rs.next()) {
                   return rs.getString("image");
               }
           }
       } catch (SQLException e) {
           e.printStackTrace(); // Handle or log the exception as needed
       }
       return null; // Return null if no user is found for the given CV ID
   }
    public int getUserphonenumberForCV(int cvId) {
        String sql = "SELECT u.phonenumber FROM `user` u JOIN `cv` ON u.`id` = cv.`user_id` WHERE cv.`id` = ?";
        try  {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, cvId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("phonenumber");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception as needed
        }
        return 0; // Return null if no user is found for the given CV ID
    }

    public String getUserEMAILForCV(int cvId) {
        String sql = "SELECT u.email FROM `user` u JOIN `cv` ON u.`id` = cv.`user_id` WHERE cv.`id` = ?";
        try  {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, cvId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("email");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception as needed
        }
        return null; // Return null if no user is found for the given CV ID
    }

    public String getUserInfoForCV(int cvId) {
        String sql = "SELECT CONCAT(u.fname, ' ', u.lname) AS full_name FROM user u JOIN cv ON u.id = cv.user_id WHERE cv.id = ?";
        try  {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, cvId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String fullName = rs.getString("full_name");

                    return fullName; // Concatenate full name and image path using a delimiter
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception as needed
        }
        return null; // Return null if no user is found for the given CV ID
    }

private  int userId=50;

    public boolean checkIfUserHasCV(int userId) {
        String query = "SELECT COUNT(*) AS count FROM cv WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt("count");
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception as needed
        }
        return false;
    }

    public CV getCVByUserId(int userId) {
        String query = "SELECT * FROM cv WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String[] languages = resultSet.getString("language").split(",");
                    return new CV(
                            resultSet.getInt("id"),
                            resultSet.getString("bio"),
                            resultSet.getString("description"),
                            languages,
                            resultSet.getString("location"),
                            resultSet.getString("certification"),
                            resultSet.getString("contact"),
                            resultSet.getInt("user_id")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception as needed
        }
        return null; // Return null if no CV is found for the given user ID
    }


}
