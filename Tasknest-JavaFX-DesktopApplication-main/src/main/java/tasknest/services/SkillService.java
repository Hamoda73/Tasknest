package tasknest.services;

import tasknest.models.Skill;
import tasknest.utils.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SkillService implements IService<Skill>{
    private Connection connection = DataSource.getInstance().getConnection();
    @Override
    public int ajouter(Skill skill) {
        String req = "INSERT INTO `skill`(`name`, `skill_value`, `cv_id`) VALUES (?, ?, ?);";
        try {
            PreparedStatement skillst = connection.prepareStatement(req);
            skillst.setString(1, skill.getName());
            skillst.setInt(2, skill.getSkill_value());
            skillst.setInt(3, skill.getCv_id());
            skillst.executeUpdate();
            System.out.println("Skill ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
       return 0;
    }
    @Override
    public void supprimer(Skill skill) {
        String req = "DELETE FROM `skill` WHERE `skill`.`id` = ?;";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, skill.getId());
            pst.executeUpdate();
            System.out.println("Skill supprimer avec succes !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    @Override
    public void modifier(Skill skill) {
        String req = "UPDATE `skill` SET `name` = ?, `skill_value` = ? WHERE `skill`.`id` = ?;";
        try {
            PreparedStatement skillst = connection.prepareStatement(req);
            skillst.setString(1, skill.getName());
            skillst.setInt(2, skill.getSkill_value());
            skillst.setInt(3, skill.getId());
            skillst.executeUpdate();
            System.out.println("Skill modifiée avec succes !");
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    @Override
    public List<Skill> afficher() {
        List<Skill> skills = new ArrayList<>();

        String req = "SELECT * FROM `skill`";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                skills.add(new Skill(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("skill_value"),
                        rs.getInt("cv_id")
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return skills;
    }



    public List<Skill> afficherskills() {
        List<Skill> skills = new ArrayList<>();
        Set<String> lowercaseNames = new HashSet<>();

        String req = "SELECT name FROM `skill`";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String name = rs.getString("name");
                String lowercaseName = name.toLowerCase();
                // Check if the lowercase name is already in the set
                if (!lowercaseNames.contains(lowercaseName)) {
                    // If not, add it to the set and the list
                    lowercaseNames.add(lowercaseName);
                    skills.add(new Skill(name));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return skills;
    }


    // Check if a skill name already exists for the given CV
    public boolean skillNameExistsForCV(int cvId, String skillName) throws SQLException {
        String query = "SELECT COUNT(*) FROM `skill` WHERE `cv_id` = ? AND `name` = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, cvId);
            statement.setString(2, skillName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        }
        return false;
    }

    public Skill getSkillByIdAndCVId(int skillId, int cvId) throws SQLException {
        Skill skill = null;
        try {
            String query = "SELECT * FROM `skill` WHERE `id` = ? AND `cv_id` = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, skillId);
            statement.setInt(2, cvId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // If a skill with the specified ID and CV ID is found, create a Skill object
                skill = new Skill(resultSet.getString("name"));
                skill.setId(resultSet.getInt("id"));
                skill.setSkill_value(resultSet.getInt("skill_value"));
                skill.setCv_id(resultSet.getInt("cv_id"));
            }
        } catch (SQLException e) {
            // Handle any SQL exceptions
            throw e;
        }
        return skill;
    }

}
