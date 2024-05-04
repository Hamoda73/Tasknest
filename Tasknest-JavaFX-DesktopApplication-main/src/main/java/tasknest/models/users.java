package tasknest.models;

import java.util.Date;

public class users {
    private int id;
    private String email;
    private String fname;
    private String lname;
    private int phonenumber;
    private Date birthdate;
    private String roles;
    private String password;
    private String image;
    private boolean blocked;

    public users() {

    }

    public users(int id, String email, String fname, String lname, int phonenumber, Date birthdate, String roles, String password, String image, boolean blocked) {
        this.id = id;
        this.email = email;
        this.fname = fname;
        this.lname = lname;
        this.phonenumber = phonenumber;
        this.birthdate = birthdate;
        this.roles = roles;
        this.password = password;
        this.image = image;
        this.blocked = blocked;
    }

    public users(String email, String fname, String lname, int phonenumber, Date birthdate, String roles, String password, String image, boolean blocked) {
        this.email = email;
        this.fname = fname;
        this.lname = lname;
        this.phonenumber = phonenumber;
        this.birthdate = birthdate;
        this.roles = roles;
        this.password = password;
        this.image = image;
        this.blocked = blocked;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public int getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(int phonenumber) {
        this.phonenumber = phonenumber;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    // Convert userInfo to users
    public static users fromUserInfo(userInfo info) {
        return new users(
                info.id,
                info.email,
                info.fname,
                info.lname,
                info.phonenumber,
                info.birthdate,
                info.roles,
                info.password,
                info.image,
                info.blocked
        );
    }
}
