package tasknest.models;

public class Skill {
    private int id;
    private String name;
    private int skill_value;
    private int cv_id;

    public Skill(String name, int skill_value, int cv_id) {
        this.name = name;
        this.skill_value = skill_value;
        this.cv_id = cv_id;
    }

    public Skill(int id, String name, int skill_value, int cv_id) {
        this.id = id;
        this.name = name;
        this.skill_value = skill_value;
        this.cv_id = cv_id;
    }

    public Skill(String name) {
        this.name = name;
    }

    public Skill(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
   this.name = name;
    }

    public int getSkill_value() {
        return skill_value;
    }

    public void setSkill_value(int skill_value) {
         this.skill_value = skill_value;
    }

    public int getCv_id() {
        return cv_id;
    }

    public void setCv_id(int cv_id) {
         this.cv_id = cv_id;
    }

    @Override
    public String toString() {
        return "Skill{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", skill_value='" + skill_value + '\'' +
                ", cv_id=" + cv_id +
                '}'+
                "\n";
    }
}
