package tasknest.models;

import tasknest.services.userrService;

import java.sql.SQLException;

public class Application {
    private String cvImagePath;
    private int id;

    private int offers_id;

    private int user_id;


    userrService userservice = new userrService();

    public Application(int offers_id, int user_id, String cvImagePath) {
        if (offers_id <= 0 || user_id <= 0 || cvImagePath == null || cvImagePath.isEmpty() || cvImagePath == null || cvImagePath.isEmpty()) {
            throw new IllegalArgumentException("Invalid arguments for Application");
        }
        this.offers_id = offers_id;
        this.user_id = user_id;
        this.cvImagePath = cvImagePath;
    }

    public Application(int id, int offers_id, int user_id, String cv) {

        if (offers_id <= 0 || user_id <= 0 || cv == null || cv.isEmpty()) {
            throw new IllegalArgumentException("Invalid arguments for Application");
        }

        this.id = id;
        this.offers_id = offers_id;
        this.user_id = user_id;
        this.cvImagePath = cv;

    }


    public Application(int id) {
        this.id = id;
    }

    /*public Application(int userId, int offerId, String cvContent, String cvImagePath) {
    }*/
    public users getUserbyidd(int user_id) {

        return userservice.getUserById(user_id);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOffers_id() {
        return offers_id;
    }

    public void setOffers_id(int offers_id) {
        this.offers_id = offers_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getcvImagePath() {
        return cvImagePath;
    }

    public void setcvImagePath(String cv) {
        this.cvImagePath = cv;
    }

    @Override
    public String toString() {
        return "Application{" +
                "id=" + id +
                ", offers_id=" + offers_id +
                ", user_id=" + user_id +
                ", cv='" + cvImagePath + '\'' +
                '}';
    }




}

