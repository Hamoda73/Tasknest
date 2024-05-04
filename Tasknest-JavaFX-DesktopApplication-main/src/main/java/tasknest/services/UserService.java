package tasknest.services;

import tasknest.models.users;
import tasknest.utils.DataSource;

import java.sql.*;
import java.util.Date;
import java.util.List;

public abstract class UserService implements IService<users>{

    private Connection connection = DataSource.getInstance().getConnection();

    @Override
    public abstract int ajouter(users users);

    @Override
    public abstract void supprimer(users users);

    @Override
    public abstract void modifier(users users);

    @Override
    public abstract List<users> afficher();

    public abstract void handleBlockUser(users user);


}
