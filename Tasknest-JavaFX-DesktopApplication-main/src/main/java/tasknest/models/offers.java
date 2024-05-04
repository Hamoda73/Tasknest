package tasknest.models;

import tasknest.services.userrService;

public class offers {

    private int id;
    private String entreprise_name;

    private String domain;

    private String post;

    private String description;

    private String localisation;

    private String period;

    private float salary;

    private int user_id;

    userrService userservice = new userrService();

    public offers(int id, String entreprise_name, String domain, String post, String description, String localisation, String period, float salary, int user_id) {
        if (id <= 0 || entreprise_name == null || entreprise_name.isEmpty() || domain == null || domain.isEmpty() ||
                post == null || post.isEmpty() || description == null || description.isEmpty() ||
                localisation == null || localisation.isEmpty() || period == null || period.isEmpty() || salary <= 0 || user_id <= 0) {
            throw new IllegalArgumentException("Invalid arguments for offers");
        }
        this.id = id;
        this.entreprise_name = entreprise_name;
        this.domain = domain;
        this.post = post;
        this.description = description;
        this.localisation = localisation;
        this.period = period;
        this.salary = salary;
        this.user_id = user_id;
    }

    public offers(String entreprise_name, String domain, String post, String description, String localisation, String period, float salary, int user_id) {
        if (entreprise_name == null || entreprise_name.isEmpty() || domain == null || domain.isEmpty() ||
                post == null || post.isEmpty() || description == null || description.isEmpty() ||
                localisation == null || localisation.isEmpty() || period == null || period.isEmpty() || salary <= 0 || user_id <= 0) {
            throw new IllegalArgumentException("Invalid arguments for offers");
        }
        this.entreprise_name = entreprise_name;
        this.domain = domain;
        this.post = post;
        this.description = description;
        this.localisation = localisation;
        this.period = period;
        this.salary = salary;
        this.user_id = user_id;
    }

    public offers(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid ID for offers");
        }
        this.id = id;
    }

    public offers() {

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEntreprise_name() {
        return entreprise_name;
    }

    public void setEntreprise_name(String entreprise_name) {
        this.entreprise_name = entreprise_name;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public float getSalary() {
        return salary;
    }

    public void setSalary(float salary) {
        this.salary = salary;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
    public users getUserbyidd(int user_id) {

        return userservice.getUserById(user_id);
    }

    @Override
    public String toString() {
        return "offers{" +
                "id=" + id +
                ", entreprise_name='" + entreprise_name + '\'' +
                ", domain='" + domain + '\'' +
                ", post='" + post + '\'' +
                ", description='" + description + '\'' +
                ", localisation='" + localisation + '\'' +
                ", period='" + period + '\'' +
                ", salary=" + salary +
                ", user_id=" + user_id +
                '}';
    }


}
