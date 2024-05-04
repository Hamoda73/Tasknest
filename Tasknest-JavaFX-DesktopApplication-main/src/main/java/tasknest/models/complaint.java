package tasknest.models;
import java.util.Objects;
import java.lang.*;

public class complaint {

    private int id;
    private String type;
    private String message;
    private respond response;
    private int user_id;
    private String responseMessage;

    public complaint(int id, String type, String message) {
        this.id = id;
        this.type = type;
        this.message = message;
        //this.response = new respond(); // Initialize the response object
    }

    public complaint(String type, String message) {
        this.type = type;
        this.message = message;
    }

    public complaint() {
    }
/*
    public complaint(int id, String type, String message, String response) {
    }*/

    public void setResponse(respond response) {
        this.response = response;
        if (response != null) {
            this.responseMessage = response.getMessage();
        }
    }

    public respond getResponse() {
        return response;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        complaint complaint = (complaint) o;
        return id == complaint.id && Objects.equals(type, complaint.type) && Objects.equals(message, complaint.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, message);
    }

    @Override
    public String toString() {
        return "complaint{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", message='" + message + '\'' +
                '}';
    }


}
