import org.postgresql.core.SqlCommand;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseTest {
    public static void main(String[] args){
        String url=System.getenv("DB_URL");
        String user=System.getenv("DB_USER");
        String password=System.getenv("DB_PASSWORD");

        try(Connection connection=DriverManager.getConnection(url,user,password)){
            System.out.println("Connected Successfully");
            System.out.println(connection.getMetaData().getDatabaseProductName());
        }catch(SQLException e){
            System.out.println("Connection Failed");
            e.printStackTrace();
        }
    }
}
