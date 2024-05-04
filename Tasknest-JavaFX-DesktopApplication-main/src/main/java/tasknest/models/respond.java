package tasknest.models;

import java.util.Objects;

public class respond {
    private int id;
    private int complaint_id;
    private String message;

    public respond(int id, int complaint_id, String message) {
        this.id = id;
        this.complaint_id = complaint_id;
        this.message = message;
    }

    public respond(int complaint_id, String message) {
        this.complaint_id = complaint_id;
        this.message = message;
    }

    public respond() {

    }

    public respond(int id) {
        this.id=id;
    }




    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getComplaint_id() {
        return complaint_id;
    }

    public void setComplaint_id(int complaint_id) {
        this.complaint_id = complaint_id;
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
        respond respond = (respond) o;
        return id == respond.id && complaint_id == respond.complaint_id && Objects.equals(message, respond.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, complaint_id, message);
    }

    @Override
    public String toString() {
        return "respond{" +
                "id=" + id +
                ", complaint_id=" + complaint_id +
                ", message='" + message + '\'' +
                '}';
    }
}
