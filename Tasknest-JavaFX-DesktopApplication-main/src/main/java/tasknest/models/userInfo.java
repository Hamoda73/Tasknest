package tasknest.models;

import java.util.Date;

public class userInfo {

    public static int id;
    public static String email;
    public static String fname;
    public static String lname;
    public static int phonenumber;
    public static Date birthdate;
    public static String roles;
    public static String password;
    public static String image;
    public static boolean blocked;

    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        userInfo.id = id;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        userInfo.email = email;
    }

    public static String getFname() {
        return fname;
    }

    public static void setFname(String fname) {
        userInfo.fname = fname;
    }

    public static String getLname() {
        return lname;
    }

    public static void setLname(String lname) {
        userInfo.lname = lname;
    }

    public static int getPhonenumber() {
        return phonenumber;
    }

    public static void setPhonenumber(int phonenumber) {
        userInfo.phonenumber = phonenumber;
    }

    public static Date getBirthdate() {
        return birthdate;
    }

    public static void setBirthdate(Date birthdate) {
        userInfo.birthdate = birthdate;
    }

    public static String getRoles() {
        return roles;
    }

    public static void setRoles(String roles) {
        userInfo.roles = roles;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        userInfo.password = password;
    }

    public static String getImage() {
        return image;
    }

    public static void setImage(String image) {
        userInfo.image = image;
    }

    public static boolean isBlocked() {
        return blocked;
    }

    public static void setBlocked(boolean blocked) {
        userInfo.blocked = blocked;
    }

    public static userInfo getCurrentUser() {
        // Implement your logic to retrieve the currently logged-in user's information here
        // For now, returning a new instance of userInfo as a placeholder
        return new userInfo();
    }
}
