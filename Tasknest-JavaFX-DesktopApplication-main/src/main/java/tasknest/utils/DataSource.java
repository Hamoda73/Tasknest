package tasknest.utils;
import java.sql.*;
public class DataSource {

    private static DataSource instance;
    private Connection connection;

    private final String USERNAME = "root";
    private  final String PASSWORD = "";
    private  final String URL = "jdbc:mysql://127.0.0.1:3306/hh";

    private DataSource(){
        try{
            connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
            System.out.println("Connecting to DB !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static DataSource getInstance() {
        if(instance == null)
            instance = new DataSource();
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
