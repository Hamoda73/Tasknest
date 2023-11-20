import java.util.Comparator;
import java.util.Objects;

public class Employe implements Comparable<Employe>{
   private int identifiant;
    private String nom;
    private String prenom ;
    private String nom_de_departement;
    private int grade;
    public Employe(){}
    public Employe(int identifiant, String nom, String prenom, String nom_de_departement, int grade) {
        this.identifiant = identifiant;
        this.nom = nom;
        this.prenom = prenom;
        this.nom_de_departement = nom_de_departement;
        this.grade = grade;
    }

    public int getIdentifiant() {
        return identifiant;
    }

    public void setIdentifiant(int identifiant) {
        this.identifiant = identifiant;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom_de_departement() {
        return nom_de_departement;
    }

    public void setNom_de_departement(String nom_de_departement) {
        this.nom_de_departement = nom_de_departement;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }
    @Override
    public int compareTo(Employe e) {

        return this.identifiant - e.identifiant;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employe employe = (Employe) o;
        return identifiant == employe.identifiant && Objects.equals(nom, employe.nom) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifiant, nom);
    }

    @Override
    public String toString() {
        return "Employe{" +
                "identifiant=" + identifiant +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", nom_de_departement='" + nom_de_departement + '\'' +
                ", grade=" + grade +
                '}';
    }


}
