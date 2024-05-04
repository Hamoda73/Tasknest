package tasknest.models;
import tasknest.services.CvService;

import java.lang.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CV {

    private int id;
    private String bio;
    private String description;
    private String[] language;
    private String location;
    private String certification;
    private String contact;
    private int user_id;




    public CV(String bio, String description, String[] language, String location, String certification, String contact, int user_id) {
        this.bio = bio;
        this.description = description;
        this.language = language;
        this.location = location;
        this.certification = certification;
        this.contact = contact;
        this.user_id = user_id;
    }

    public CV(int id, String bio, String description, String[] language, String location, String certification, String contact, int user_id) {
        this.id = id;
        this.bio = bio;
        this.description = description;
        this.language = language;
        this.location = location;
        this.certification = certification;
        this.contact = contact;
        this.user_id = user_id;
    }
    public CV(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
            this.bio = bio;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
            this.description = description;
    }

    public String[] getLanguage() {
        return language;
    }

    public void setLanguage(String[] language) {
        this.language = language;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
            this.location = location;
    }

    public String getCertification() {
        return certification;
    }

    public void setCertification(String certification) {
            this.certification = certification;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
            this.contact = contact;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
            this.user_id = user_id;
    }






    @Override
    public String toString() {
        return "CV{" +
                "id=" + id +
                ", bio='" + bio + '\'' +
                ", description='" + description + '\'' +
                ", language=" + Arrays.toString(language) +
                ", location='" + location + '\'' +
                ", certification='" + certification + '\'' +
                ", contact='" + contact + '\'' +
                ", user_id=" + user_id +
                '}'+
                "\n";
    }


    public void setArabic(boolean arabic) {
        // Assuming you have a field named 'languages' to store the languages
        if (arabic) {
            // Check if Arabic is selected, if so, add it to the languages array
            if (!Arrays.asList(language).contains("Arabic")) {
                // Convert the existing languages array to a list for easier manipulation
                List<String> languagesList = new ArrayList<>(Arrays.asList(language));
                // Add Arabic to the list
                languagesList.add("Arabic");
                // Convert the list back to an array
                this.language = languagesList.toArray(new String[0]);
            }
        } else {
            // If Arabic is not selected, remove it from the languages array
            if (Arrays.asList(language).contains("Arabic")) {
                // Convert the existing languages array to a list for easier manipulation
                List<String> languagesList = new ArrayList<>(Arrays.asList(language));
                // Remove Arabic from the list
                languagesList.remove("Arabic");
                // Convert the list back to an array
                this.language = languagesList.toArray(new String[0]);
            }
        }
    }

    public void setFrench(boolean french) {
        if (french) {
            if (!Arrays.asList(language).contains("French")) {
                List<String> languagesList = new ArrayList<>(Arrays.asList(language));
                languagesList.add("French");
                this.language = languagesList.toArray(new String[0]);
            }
        } else {
            if (Arrays.asList(language).contains("French")) {
                List<String> languagesList = new ArrayList<>(Arrays.asList(language));
                languagesList.remove("French");
                this.language = languagesList.toArray(new String[0]);
            }
        }
    }

    public void setEnglish(boolean english) {
        if (english) {
            if (!Arrays.asList(language).contains("English")) {
                List<String> languagesList = new ArrayList<>(Arrays.asList(language));
                languagesList.add("English");
                this.language = languagesList.toArray(new String[0]);
            }
        } else {
            if (Arrays.asList(language).contains("English")) {
                List<String> languagesList = new ArrayList<>(Arrays.asList(language));
                languagesList.remove("English");
                this.language = languagesList.toArray(new String[0]);
            }
        }
    }

    public void setGerman(boolean german) {
        if (german) {
            if (!Arrays.asList(language).contains("German")) {
                List<String> languagesList = new ArrayList<>(Arrays.asList(language));
                languagesList.add("German");
                this.language = languagesList.toArray(new String[0]);
            }
        } else {
            if (Arrays.asList(language).contains("German")) {
                List<String> languagesList = new ArrayList<>(Arrays.asList(language));
                languagesList.remove("German");
                this.language = languagesList.toArray(new String[0]);
            }
        }
    }


    public void addLanguage(String newLanguage) {
        // Convert the existing languages array to a list for easier manipulation
        List<String> languagesList = new ArrayList<>(Arrays.asList(language));
        // Add the new language to the list
        languagesList.add(newLanguage);
        // Convert the list back to an array
        this.language = languagesList.toArray(new String[0]);
    }

    public void clearLanguages() {
        // Set the languages array to an empty array
        this.language = new String[0];
    }


    public List<Skill> getSkills(int cvid) {
        CvService cvService = new CvService();
        return cvService.getSkillsForCV(cvid);
    }
}
