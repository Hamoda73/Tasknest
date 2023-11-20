import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;

public class SocieteArrayList  implements IGestion<Employe>{

    private List<Employe> Employes = new ArrayList<>();

    public SocieteArrayList() {
    }
    @Override
    public void ajouterEmploye(Employe emp){
        Employes.add(emp);
    }
    @Override
    public boolean rechercherEmploye(String nom){
        for (Employe employe : Employes) {
            if (employe.getNom().equals(nom)) {
                return true;
            }
        }
        return false;
}
    @Override
    public boolean rechercherEmploye(Employe emp){
        return Employes.contains(emp);
    }
    @Override
    public void supprimerEmploye(Employe emp){
        Employes.remove(emp);
    }
    @Override
    public void displayEmploye(){
        for (Employe employe : Employes) {
            System.out.println(employe);
        }
    }
    @Override
     public void trierEmployeParId(){
        Collections.sort(Employes);
     }

    @Override
    public void trierEmployeParNomDepartementEtGrade() {
        Comparator<Employe> nomDepartementGradeComparator = new Comparator <Employe>() {

            @Override
            public int compare(Employe e1, Employe e2) {
                int compareNom = e1.getNom().compareTo(e2.getNom());
                if (compareNom != 0) {
                    return compareNom;
                }

                int compareDepartement = e1.getNom_de_departement().compareTo(e2.getNom_de_departement());
                if (compareDepartement != 0) {
                    return compareDepartement;
                }
                return e1.getGrade() - e2.getGrade();
            }
        };        Collections.sort(Employes, nomDepartementGradeComparator);
    }
}
