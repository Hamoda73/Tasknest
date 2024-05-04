package tasknest.services;

import java.util.List;

public interface IService<T> {

    public int ajouter(T t);
    public void supprimer(T t);
    public void modifier(T t);
    public List<T> afficher();

}

